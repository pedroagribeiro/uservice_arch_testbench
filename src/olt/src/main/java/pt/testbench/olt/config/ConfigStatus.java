package pt.testbench.olt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.olt.model.Status;

@Configuration
public class ConfigStatus {

    @Bean
    Status createStatus() {
        return new Status();
    }
}
