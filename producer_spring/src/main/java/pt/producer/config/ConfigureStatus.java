package pt.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.producer.model.Status;

@Configuration
public class ConfigureStatus {

    private boolean containerized;

    @Bean
    Status createStatus() {
        Status status = new Status();
        status.set_environment(containerized);
        return status;
    }
}
