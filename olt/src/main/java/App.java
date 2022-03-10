import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    @Parameter(names = { "-id" }, description = "Identifier of the OLT")
    private static int id; 

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment is fully containerized")
    private static boolean containerized;

    // This should be set to how many worker containers there are in the deployment
    private static final int WORKER_CONTAINERS = 5;

    private String olt_queue_host;
    private int olt_queue_port;
    
    private Connection olt_queue_connection;
    private List<Connection> workers_queues_connections; 
    private Channel olt_queue_request_channel;
    private List<Channel> workers_queues_response_channels;

    private static Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public static void main(String[] args) throws Exception {
        App application = new App();
        JCommander commands = JCommander.newBuilder().addObject(application).build();
        try {
            commands.parse(args);
        } catch(Exception e) {
            commands.usage();
            System.exit(-1);
        }
        application.run();
    }

    public void run() {
        establish_environment_variables();
        establish_connection_with_olt_queue();
        establish_olts_queue_channels();
        establish_connection_with_workers();
        establish_workers_queues_channels();
        start_message_comsumption();
    }

    public void establish_environment_variables() {
        if(containerized) {
            this.olt_queue_host = "olt-queue" + id;
            this.olt_queue_port = 5672;
        } else {
            this.olt_queue_host = "localhost";
            this.olt_queue_port = 5676 + id;
        }
    }

    private void establish_connection_with_olt_queue() {
        log.info("STATUS: Connecting to the OLT queue");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.olt_queue_host);
        factory.setPort(this.olt_queue_port); 
        while(this.olt_queue_connection == null) {
            try {
                this.olt_queue_connection = factory.newConnection();
                log.info("SUCCESS: Connected to the OLT queue");
            } catch(IOException | TimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void establish_olts_queue_channels() {
        log.info("STATUS: Setting up communication channels on OLT queue connection");
        try {
            this.olt_queue_request_channel = this.olt_queue_connection.createChannel();
            this.olt_queue_request_channel.queueDeclare("requests", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: An error ocurred while trying to create the \"requests\" channel on the olt queue");
        }
        log.info("SUCCESS: Created the communication channels on the OLT queue connection");
    }

    private void establish_connection_with_workers() {
        log.info("STATUS: Connecting to the workers queues");
        if(this.workers_queues_connections == null) {
            this.workers_queues_connections = new ArrayList<>();
        }
        for(int i = 0; i < App.WORKER_CONTAINERS; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("worker-queue" + i);
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5672 + i);
            }
            Connection connection = null;
            while(connection == null) {
                try {
                    connection = factory.newConnection();
                    this.workers_queues_connections.add(connection);
                } catch(IOException | TimeoutException e) {
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        log.info("SUCCESS: Connected to the workers");
    }

    private void establish_workers_queues_channels() {
        log.info("STATUS: Creating the \"response\" channels on the workers queues");
        if(this.workers_queues_response_channels == null) {
            this.workers_queues_response_channels = new ArrayList<>();
        }
        for(int i = 0; i < this.workers_queues_connections.size(); i++) {
            try {
                Channel channel = this.workers_queues_connections.get(i).createChannel();
                channel.queueDeclare("responses", false, false, false, null);
                this.workers_queues_response_channels.add(channel);
            } catch(IOException e) {
                log.info("FAILURE: An error ocurred while creating the \"responses\" channel on worker " + i);
            }
        }
    }

    private void start_message_comsumption() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = App.converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_olt(new Date().getTime());
            process_message(m);
        };
        try {
            this.olt_queue_request_channel.basicConsume("requests", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("FAILURE: An error ocurred when trying to consume messages from the \"requests\" channel of the OLT queue");
        }
    }

    private void process_message(Message m) {
        log.info("STATUS: Received '" + converter.toJson(m) + "'"); 
        Response res = new Response(200, new Date().getTime(), -1, m);
        int origin_worker = m.get_worker();
        try {
            Thread.sleep(m.get_processing_time());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        res.set_ended_handling(new Date().getTime());
        res.set_origin_message(m);
        try {
            this.workers_queues_response_channels.get(origin_worker).basicPublish("", "responses", null, converter.toJson(res).getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.info("FAILURE: An error ocurred when trying to publish to the \"responses\" channel of the OLT queue");
        }
    }

}
