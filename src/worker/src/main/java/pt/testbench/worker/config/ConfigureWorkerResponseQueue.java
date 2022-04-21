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
import pt.testbench.worker.handlers.ReceiveResponseHandler;

@Configuration
public class ConfigureWorkerResponseQueue {

   @Value("${worker.id}")
   private int worker_id;

   @Bean
   Queue createResponseQueue() {
       String queue_name = "olt-" + worker_id + "-response-queue";
       return new Queue(queue_name, true, false, false);
   }

   @Bean
   Binding bindResponseQueue(@Qualifier("createResponseQueue") Queue q, TopicExchange exchange) {
      String queue_name = "olt-" + worker_id + "-response-queue";
      return BindingBuilder.bind(q).to(exchange).with(queue_name);
   }

   @Bean
   SimpleMessageListenerContainer responseContainer(ConnectionFactory connectionFactory, MessageListenerAdapter responseListenerAdapter) {
       String queue_name = "olt-" + worker_id + "-response-queue";
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(queue_name);
       container.setMessageListener(responseListenerAdapter);
       return container;
   }

   @Bean
   MessageListenerAdapter responseListenerAdapter(ReceiveResponseHandler handler) {
       return new MessageListenerAdapter(handler, "handleResponse");
   }

}
