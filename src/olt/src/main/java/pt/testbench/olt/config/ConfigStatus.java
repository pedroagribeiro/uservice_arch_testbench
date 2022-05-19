package pt.testbench.olt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import pt.testbench.olt.model.Status;

@Configuration
public class ConfigStatus {

    @Value("${olt.id}")
    private int olt_id;

    @Bean
    Status createStatus() {
        Status status = new Status(this.olt_id);
        Assert.notNull(status.getOltId(), "The olt identificator is null, the main process shall not proceed");
        Assert.notNull(status.getEnqueuedAtWorkerTimes(), "The enqueued at worker times map is null, the main process shall not proceed");
        return status;
    }
}
