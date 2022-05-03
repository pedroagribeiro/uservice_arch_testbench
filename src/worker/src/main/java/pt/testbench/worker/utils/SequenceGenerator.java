package pt.testbench.worker.utils;

import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import java.util.ArrayList;
import java.util.List;

public class SequenceGenerator {

    /**
     * From each NEW_ONU message 4 new requests are generated.
     * 1st request - register ONU.
     * 2nd request - register services.
     * 3rd request - make ports available.
     * 4th request - conclude and install services.
     */

    /**
     * Fast messages: 2000 milliseconds
     * Medium messages: 19000 millissenconds
     * Long messages: 30000 milliseconds
     */

    private final static int olt_request_timeout = 3000;

    private static final long fast_message_duration = 50;
    private static final long medium_message_duration = 500;
    private static final long long_message_duration = 4000;

    public static List<OltRequest> generate_requests_sequence(Message m) {
        List<OltRequest> requests_to_return = new ArrayList<>();
        int green_messages = 4 - m.getYellowRequests() - m.getRedRequests();
        long minimum_theoretical_duration = 0;
        for(int i = 0; i < green_messages; i++) {
            minimum_theoretical_duration += fast_message_duration;
            OltRequest request = new OltRequest(m.getId() + "-" + i, fast_message_duration, olt_request_timeout);
            requests_to_return.add(request);
        }
        for(int i = 0; i < m.getYellowRequests(); i++) {
            minimum_theoretical_duration += medium_message_duration;
            int order_number = i + green_messages;
            OltRequest request = new OltRequest(m.getId() + "-" + order_number, medium_message_duration, olt_request_timeout);
            requests_to_return.add(request);
        }
        for(int i = 0; i < m.getRedRequests(); i++) {
            minimum_theoretical_duration += long_message_duration;
            int order_number = green_messages + m.getYellowRequests() + i;
            OltRequest request = new OltRequest(m.getId() + "-" + order_number, long_message_duration, olt_request_timeout);
            requests_to_return.add(request);
        }
        m.setMinimumTheoreticalDuration(minimum_theoretical_duration);
        return requests_to_return;
    }
}
