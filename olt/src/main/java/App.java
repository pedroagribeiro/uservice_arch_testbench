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

    private static final int OLT_CONTAINERS = 3;

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
            this.olt_queue_host = "olt" + id + "-queue";
            this.olt_queue_port = 5672;
        } else {
            this.olt_queue_host = "localhost";
            this.olt_queue_port = 5676 + id;
        }
    }

    private void establish_connection_with_olt_queue() {
        log.info("🕋 Connecting to the \"OLT QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.olt_queue_host);
        factory.setPort(this.olt_queue_port); 
        while(this.olt_queue_connection == null) {
            try {
                this.olt_queue_connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"OLT QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"OLT QUEUE\"!");
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void establish_olts_queue_channels() {
        log.info("🕋 Setting up communication channels on \"OLT QUEUE\" connection...");
        try {
            this.olt_queue_request_channel = this.olt_queue_connection.createChannel();
            this.olt_queue_request_channel.queueDeclare("requests", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ An error ocurred while trying to create the \"requests\" channel on the \"OLT QUEUE\"!");
        }
        log.info("✅ Sucessfuly created the communication channels on the \"OLT QUEUE\" connection!");
    }

    private void establish_connection_with_workers() {
        log.info("🕋 Connecting to the \"WORKER's QUEUES\"...");
        if(this.workers_queues_connections == null) {
            this.workers_queues_connections = new ArrayList<>();
        }
        for(int i = 0; i < App.OLT_CONTAINERS; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("worker-queue" + i);
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5672 + i);
            }
            try {
                Connection connection = factory.newConnection();
                this.workers_queues_connections.add(connection);
            } catch(IOException | TimeoutException e) {
                log.info("❌ An error ocurred while trying to connect to \"WORKER " + i + "\"!");
            }
        }
        log.info("✅ Successfuly connect to the workers!");
    }

    private void establish_workers_queues_channels() {
        log.info("🕋 Creating the \"response\" channels on the \"WORKERS QUEUES\"...");
        if(this.workers_queues_response_channels == null) {
            this.workers_queues_response_channels = new ArrayList<>();
        }
        for(int i = 0; i < this.workers_queues_connections.size(); i++) {
            try {
                Channel channel = this.workers_queues_connections.get(i).createChannel();
                channel.queueDeclare("responses", false, false, false, null);
                this.workers_queues_response_channels.add(channel);
            } catch(IOException e) {
                log.info("❌ An error ocurred while creating the \"responses\" channel on \"WORKER " + i + "\"!");
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
            log.info("❌ An error ocurred when trying to consume messages from the \"requests\" channel of the \"OLT QUEUE\"!");
        }
    }

    private void process_message(Message m) {
        log.info("📥 Received '" + converter.toJson(m) + "'"); 
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
            log.info("❌ An error ocurred when trying to publish to the \"responses\" channel of the \"OLT QUEUE\"!");
        }
    }

}
