package pt.testbench.worker_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.worker_spring.model.Status;

@Configuration
public class ConfigureStatus {

    @Bean
    Status createStatus() {
        return new Status();
    }
}
