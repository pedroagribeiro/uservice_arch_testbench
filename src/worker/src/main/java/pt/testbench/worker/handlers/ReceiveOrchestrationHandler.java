package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Orchestration;
import pt.testbench.worker.model.Status;
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.utils.SequenceGenerator;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private static final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    private String broker_host = "broker";
    private String base_olt_host = "olt-";
    private String base_worker_host = "worker-";

    private Random random = new Random(34);

    @Autowired private MessageRepository messagesRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;

    private Message fetch_message_from_broker() {
        Message m = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/message", HttpMethod.GET, entity, String.class);
            String message_json = (String) response.getBody();
            m = converter.fromJson(message_json, Message.class);
            log.info("Fetched: " + converter.toJson(m) + " from the broker");
        } catch(HttpClientErrorException.NotFound e) {
            log.info("Could not fetch message from the broker, it is empty!");
        } catch(HttpServerErrorException.ServiceUnavailable e) {
            // log.info("The broker cannot provide message at the moment");
        }
        return m;
    }

    private void inform_oracle_of_handling(String olt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}&worker={worker}", HttpMethod.POST, entity, String.class, olt, status.getWorkerId());
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling!");
            }
        }
    }

    private void inform_oracle_of_handling_end(String olt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}", HttpMethod.DELETE, entity, String.class, olt);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling end, something went wrong");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling ending!");
            }
        }
    }


    private void perform_olt_request(OltRequest m, String olt) {
        String olt_host = base_olt_host + olt;
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        m.setLeftWorker(new Date().getTime());
        this.oltRequestsRepository.save(m);
        HttpEntity<OltRequest> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + olt_host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The message could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The message was sent to the olt!");
            }
        }
    }

    private int ask_oracle_if_anyone_is_using_olt(String olt) {
        int broker_port = 8081;
        int worker = -1;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":" + broker_port + "/management?olt={olt}", HttpMethod.GET, entity, String.class, olt);
            String worker_json = (String) response.getBody();
            worker = Integer.parseInt(worker_json);
        } catch(HttpClientErrorException.NotFound e) {
            log.info("There's no worker processing request for such olt");
        }
        return worker;
    }



    private Callable<Boolean> request_satisfied(String request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleOrchestration(String body) {
        Orchestration orchestration = converter.fromJson(body, Orchestration.class);
        log.info("Received orchestration: " + converter.toJson(orchestration));
        status.setArchitecture(orchestration.getAlgorithm());
        status.setSequence(orchestration.getSequence());
        log.info("Running logic " + status.getArchitecture() + " ...");
        status.setIsOnGoingRun(true);
        status.setWorkers(orchestration.getWorkers());
        if(orchestration.getSequence() == 2) {
            int additional_yellow_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            status.setTargetYellowMessages(status.getTargetYellowMessages() + additional_yellow_messages);
        }
        if(orchestration.getSequence() == 3) {
            int additional_yellow_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            int additional_red_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            status.setTargetYellowMessages(status.getTargetYellowMessages() + additional_yellow_messages);
            status.setTargetRedMessages(status.getTargetRedMessages() + additional_red_messages);
        }
        boolean auto_consume = status.isOnGoingRun() && (status.getArchitecture() == 1 || status.getArchitecture() == 2);
        if(auto_consume) {
            while(status.isOnGoingRun()) {
                Message m = fetch_message_from_broker();
                if(m != null) {
                    long instant = new Date().getTime();
                    m.setWorker(status.getWorkerId());
                    if(status.getArchitecture() == 2) {
                        int worker = ask_oracle_if_anyone_is_using_olt(m.getOlt());
                        log.info("Oracle search result: " + worker);
                        while(worker != -1) {
                            try {
                                Thread.sleep(1000);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            worker = ask_oracle_if_anyone_is_using_olt(m.getOlt());
                        }
                    }
                    log.info("Message that I got: " + converter.toJson(m));
                    List<OltRequest> generated_olt_requests = null;
                    if(orchestration.getSequence() == 1) {
                        generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, -1, 0);
                    }
                    if(orchestration.getSequence() == 2) {
                        int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
                        int random_input = Math.min(left_yellow_messages, 4);
                        int accessory_messages = random.nextInt(random_input);
                        status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
                        generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
                    }
                    if(orchestration.getSequence() == 3) {
                        int acessory_type = random.nextInt(2);
                        if(acessory_type == 0) {
                           int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
                           int random_input = Math.min(left_yellow_messages, 4);
                           int accessory_messages = random.nextInt(random_input);
                           status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
                           generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
                        } else {
                           int left_red_messages = status.getTargetRedMessages() - status.getGeneratedRedMessages();
                           int random_input = Math.min(left_red_messages, 4);
                           int accessory_messages = random.nextInt(random_input);
                           status.setGeneratedRedMessages(status.getGeneratedRedMessages() + accessory_messages);
                           generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 1, accessory_messages);
                        }
                    }
                    m = this.messagesRepository.save(m);
                    List<OltRequest> sorted_olt_requests = new ArrayList<>();
                    assert generated_olt_requests != null;
                    for(OltRequest request : generated_olt_requests) {
                        request.setOriginMessage(m);
                        request = this.oltRequestsRepository.save(request);
                        sorted_olt_requests.add(request);
                    }
                    m.setStartedProcessing(new Date().getTime());
                    m = this.messagesRepository.save(m);
                    for(OltRequest request : sorted_olt_requests) {
                        request.setOriginMessage(m);
                    }
                    inform_oracle_of_handling(m.getOlt());
                    int timedout_requests = 0;
                    boolean provision_timedout = false;
                    for(int i = 0; i < sorted_olt_requests.size(); i++) {
                        if(!provision_timedout) {
                            OltRequest request = sorted_olt_requests.get(i);
                            status.setCurrentActiveRequest(request.getId());
                            status.getRequestSatisfied().put(request.getId(), false);
                            request.setNotProcessed(false);
                            perform_olt_request(request, m.getOlt());
                            try {
                                Awaitility.await().atMost(request.getTimeout(), TimeUnit.MILLISECONDS).until(request_satisfied(request.getId()));
                            } catch (Exception e) {
                                provision_timedout = true;
                                inform_oracle_of_handling_end(m.getOlt());
                                log.warn("Timeout: The request " + request.getId() + " timedout");
                                m.setCompletedProcessing(new Date().getTime());
                                m.setSuccessful(false);
                                m = this.messagesRepository.save(m);
                                timedout_requests++;
                            }
                            if(i == 3) {
                                inform_oracle_of_handling_end(m.getOlt());
                            }
                        }
                    }
                    if(timedout_requests == 0) {
                        m.setCompletedProcessing(new Date().getTime());
                        m.setSuccessful(true);
                        this.messagesRepository.save(m);
                    }
                } else {
                    try { 
                        Thread.sleep(500);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
