package pt.testbench.olt.config;

import org.springframework.beans.factory.annotation.Value;
import pt.testbench.olt.handlers.ReceiveMessageHandler;
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

@Configuration
public class ConfigOltMessageQueue {

    @Value("${olt.id}")
    private int olt_id;

    @Bean
    Queue createMessageQueue() {
        String queue_name = "olt-" + olt_id + "-message-queue";
        return new Queue(queue_name, true, false, false);
    }

    @Bean
    Binding bindMessageQueue(@Qualifier("createMessageQueue") Queue q, TopicExchange exchange) {
        String queue_name = "olt-" + olt_id + "-message-queue";
        return BindingBuilder.bind(q).to(exchange).with(queue_name);
    }

    @Bean
    SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        String queue_name = "olt-" + olt_id + "-message-queue";
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue_name);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(ReceiveMessageHandler handler) {
        return new MessageListenerAdapter(handler, "handleMessage");
    }
}
