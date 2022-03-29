package pt.testbench.worker_spring.config;

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
import pt.testbench.worker_spring.handlers.ReceiveResponseHandler;

@Configuration
public class ConfigureWorkerResponseQueue {

   public static final String QUEUE_NAME = "response_queue";

   @Bean
   Queue createResponseQueue() {
       return new Queue(QUEUE_NAME, true, false, false);
   }

   @Bean
   Binding bindResponseQueue(@Qualifier("createResponseQueue") Queue q, TopicExchange exchange) {
      return BindingBuilder.bind(q).to(exchange).with(QUEUE_NAME);
   }

   @Bean
   SimpleMessageListenerContainer responseContainer(ConnectionFactory connectionFactory, MessageListenerAdapter responseListenerAdapter) {
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(QUEUE_NAME);
       container.setMessageListener(responseListenerAdapter);
       return container;
   }

   @Bean
   MessageListenerAdapter responseListenerAdapter(ReceiveResponseHandler handler) {
       return new MessageListenerAdapter(handler, "handleResponse");
   }
}
