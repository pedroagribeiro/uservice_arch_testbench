package pt.testbench.broker_spring.consumers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.testbench.broker_spring.model.Message;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private final Gson converter = new Gson();

    public void handleMessage(String body) {
        Message message = converter.fromJson(body, Message.class);
        log.info("Received: " + converter.toJson(message));
    }
}
