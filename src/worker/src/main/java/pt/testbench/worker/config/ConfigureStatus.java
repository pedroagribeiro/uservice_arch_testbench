package pt.testbench.worker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.testbench.worker.model.Status;

@Configuration
public class ConfigureStatus {

    @Value("${worker.id}")
    private int worker_id;

    @Bean
    Status createStatus() {
        return new Status(this.worker_id);
    }
}
