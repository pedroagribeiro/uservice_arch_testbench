package pt.testbench.worker.utils;

import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    private final static int olt_request_timeout = 20000;

    private static final long fast_message_duration = 2000;
    private static final long medium_message_duration = 19000;
    private static final long long_message_duration = 30000;

    private static final int ALL_CLEAR = 0;
    private static final int MIXED = 1;
    private static final int ALL_TIMEDOUT = 2;
    private static final Random random = new Random(34);


    public static List<OltRequest> generate_requests_sequence(Message m) {
        List<Long> batch_message_durations = new ArrayList<>();
        List<OltRequest> generated_requests = new ArrayList<>();
        int batch_type = random.nextInt(3);
        boolean has_red_request = false;
        switch(batch_type) {
            case ALL_CLEAR:
                batch_message_durations.add(fast_message_duration);
                batch_message_durations.add(fast_message_duration);
                batch_message_durations.add(fast_message_duration);
                batch_message_durations.add(fast_message_duration);
                break;
            case MIXED:
                batch_message_durations.add(fast_message_duration);
                batch_message_durations.add(fast_message_duration);
                batch_message_durations.add(medium_message_duration);
                batch_message_durations.add(medium_message_duration);
                break;
            case ALL_TIMEDOUT:
                batch_message_durations.add(long_message_duration);
                batch_message_durations.add(long_message_duration);
                batch_message_durations.add(long_message_duration);
                batch_message_durations.add(long_message_duration);
                has_red_request = true;
                break;
        }
        long minimum_theoretical_duration = 0;
        for(Long duration : batch_message_durations) {
            minimum_theoretical_duration += duration;
            OltRequest request = new OltRequest(m, duration, olt_request_timeout);
            generated_requests.add(request);

        }
        m.setMinimumTheoreticalDuration(minimum_theoretical_duration);
        m.setHasRedRequests(has_red_request);
        return generated_requests;
    }
}
