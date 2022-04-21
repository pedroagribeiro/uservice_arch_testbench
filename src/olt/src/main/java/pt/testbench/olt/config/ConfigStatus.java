package pt.testbench.olt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.olt.model.Status;

@Configuration
public class ConfigStatus {

    @Value("${olt.id}")
    private int olt_id;

    @Bean
    Status createStatus() {
        return new Status(olt_id);
    }
}
