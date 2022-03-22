package pt.testbench.broker_spring.consumers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.testbench.broker_spring.model.Orchestration;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private final Gson converter = new Gson();

    public void handleOrchestration(String body) {
       Orchestration orchestration = converter.fromJson(body, Orchestration.class);
       log.info("Received orchestration: " + converter.toJson(orchestration));
    }
}
