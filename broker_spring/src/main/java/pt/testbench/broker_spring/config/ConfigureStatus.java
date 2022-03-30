package pt.testbench.broker_spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.broker_spring.model.Status;

@Configuration
public class ConfigureStatus {

    @Bean
    Status setStatus() {
        return new Status();
    }
}
