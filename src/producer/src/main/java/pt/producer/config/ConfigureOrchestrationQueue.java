package pt.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.producer.handlers.ReceiveOrchestrationHandler;

@Configuration
public class ConfigureOrchestrationQueue {

    public static final String QUEUE_NAME = "orchestration_queue";
    public static final String EXCHANGE_NAME = "";

    @Bean
    Queue createOrchestrationQueue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    TopicExchange orchestrationExchange() {
       return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding bindOrchestrationQueue(@Qualifier("createOrchestrationQueue") Queue q, @Qualifier("orchestrationExchange") TopicExchange exchange) {
        return BindingBuilder.bind(q).to(exchange).with(QUEUE_NAME);
    }

    @Bean
    SimpleMessageListenerContainer orchestrationContainer(ConnectionFactory connectionFactory, MessageListenerAdapter orchestrationListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(orchestrationListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter orchestrationListenerAdapter(ReceiveOrchestrationHandler handler) {
        return new MessageListenerAdapter(handler, "handleOrchestration");
    }

}
