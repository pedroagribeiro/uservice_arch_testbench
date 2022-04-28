package pt.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.producer.model.Status;

@Configuration
public class ConfigureStatus {

    @Bean
    Status currentStatus() {
        return new Status(0);
    }

}
