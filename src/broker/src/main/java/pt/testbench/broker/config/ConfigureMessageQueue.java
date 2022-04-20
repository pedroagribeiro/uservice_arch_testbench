package pt.testbench.broker.config;

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
import pt.testbench.broker.handlers.ReceiveMessageHandler;

@Configuration
public class ConfigureMessageQueue {

    public static final String QUEUE_NAME = "message_queue";

    @Bean
    Queue createMessageQueue() {
       return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    Binding bindMessageQueue(@Qualifier("createMessageQueue") Queue q, TopicExchange exchange) {
        return BindingBuilder.bind(q).to(exchange).with(QUEUE_NAME);
    }

    @Bean
    SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(ReceiveMessageHandler handler) {
        return new MessageListenerAdapter(handler, "handleMessage");
    }

}
