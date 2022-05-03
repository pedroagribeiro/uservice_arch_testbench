package pt.producer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import pt.producer.model.Message;
import pt.producer.model.PerOltProcessingTime;
import pt.producer.model.Result;

public class ResultCalculator {

    private static final long average_message_processing_time = 200;

    private static int calculate_minimum_theoretical_failed_provisions(List<Message> run_messages) {
        int minimum_theoretical_failed_provisions = 0;
        for(Message m : run_messages) {
            if(m.getRedRequests() > 0) minimum_theoretical_failed_provisions++;
        }
        return minimum_theoretical_failed_provisions;
    }

    private static int calculate_verified_failed_provisions(List<Message> run_messages) {
        int verified_failed_provisions = 0;
        for(Message m : run_messages) {
            if(!m.getSuccessful()) verified_failed_provisions++;
        }
        return verified_failed_provisions;
    }

    private static long calculate_verified_run_duration(Result r, long end_instant) {
        return end_instant - r.getStartInstant();
    }

    private static long calculate_theoretical_time_limit(Result r) {
        return (r.getRequests() / r.getOlts()) * ResultCalculator.average_message_processing_time; 
    }
   
    public static Result calculate_run_result(long end_instant, List<Message> run_messages, Result result) {
        result.setEndInstant(end_instant);
        result.setTheoreticalTotalTimeLimit(calculate_theoretical_time_limit(result));
        result.setTheoreticalTimeoutRequestsLimit(calculate_minimum_theoretical_failed_provisions(run_messages));
        result.setVerifiedTotalTime(calculate_verified_run_duration(result, end_instant));
        result.setVerifiedTimedoutRequests(calculate_verified_failed_provisions(run_messages));
        result.setStatus(Result.availableStatus[2]);
        return result;
    }

    public static List<PerOltProcessingTime> calculate_per_olt_metrics(List<Message> run_messages, Result result) {
        Map<String, List<Long>> provisioning_times_by_olt = new HashMap<>();
        for(Message m : run_messages) {
            if(m.getSuccessful()) {
                long provisioning_time = m.getCompletedProcessing() - m.getStartedProcessing();
                if (!provisioning_times_by_olt.containsKey(m.getOlt())) {
                    provisioning_times_by_olt.put(m.getOlt(), new ArrayList<>());
                }
                provisioning_times_by_olt.get(m.getOlt()).add(provisioning_time);
            }
        }
        List<PerOltProcessingTime> registries = new ArrayList<>();
        for(String olt : provisioning_times_by_olt.keySet()) {
            List<Long> processing_times = provisioning_times_by_olt.get(olt);
            long minimum_processing_time = Collections.min(processing_times);
            long maximum_processing_time = Collections.max(processing_times);
            OptionalDouble average_processing_time_aux = processing_times.stream().mapToDouble(a -> a).average();
            double average_processing_time = average_processing_time_aux.isPresent() ? average_processing_time_aux.getAsDouble() : 0;
            PerOltProcessingTime registry = new PerOltProcessingTime(result, olt, minimum_processing_time, maximum_processing_time, average_processing_time);
            registries.add(registry);
        }
        return registries;
    }
}
