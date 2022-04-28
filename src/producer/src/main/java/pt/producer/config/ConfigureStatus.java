package pt.producer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.producer.model.Message;
import pt.producer.model.Status;
import pt.producer.repository.MessageRepository;

@Configuration
public class ConfigureStatus {

    @Bean
    Status currentStatus() {
        return new Status(0);
    }

}
