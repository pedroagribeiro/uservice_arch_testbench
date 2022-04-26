package pt.testbench.worker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.worker.handlers.ReceiveOrchestrationHandler;

@Configuration
public class ConfigureWorkerOrchestrationQueue {

    @Value("${worker.id}")
    private int worker_id;
    private final String EXCHANGE_NAME = "";

    @Bean
    Queue createOrchestrationQueue() {
        String queue_name = "worker-" + worker_id + "-orchestration-queue";
        return new Queue(queue_name, true, false, false);
    }

    @Bean
    TopicExchange orchestrationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding bindOrchestrationQueue(@Qualifier("createOrchestrationQueue") Queue q, @Qualifier("orchestrationExchange") TopicExchange exchange) {
        String queue_name = "worker-" + worker_id + "-orchestration-queue";
        return BindingBuilder.bind(q).to(exchange).with(queue_name);
    }

    @Bean
    SimpleMessageListenerContainer orchestrationContainer(ConnectionFactory connectionFactory, MessageListenerAdapter orchestrationListenerAdapter) {
        String queue_name = "worker-" + worker_id + "-orchestration-queue";
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue_name);
        container.setMessageListener(orchestrationListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter orchestrationListenerAdapter(ReceiveOrchestrationHandler handler) {
        return new MessageListenerAdapter(handler, "handleOrchestration");
    }

}
