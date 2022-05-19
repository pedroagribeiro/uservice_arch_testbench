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
import pt.testbench.worker.handlers.ReceiveMessageHandler;

@Configuration
public class ConfigureWorkerMessageQueue {

    @Value("${worker.id}")
    private int worker_id;

    private final String EXCHANGE_NAME = "";

    @Bean
    Queue createMessageQueue() {
        String queue_name = "worker-" + this.worker_id + "-message-queue";
        return new Queue(queue_name, true, false, false);
    }

    @Bean
    TopicExchange messageExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding bindMessageQueue(@Qualifier("createMessageQueue") Queue q, @Qualifier("messageExchange") TopicExchange exchange) {
        String queue_name = "worker-" + this.worker_id + "-message-queue";
        return BindingBuilder.bind(q).to(exchange).with(queue_name);
    }

    @Bean
    SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        String queue_name = "worker-" + this.worker_id + "-message-queue";
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
