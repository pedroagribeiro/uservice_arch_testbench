import json

# Ratio id: 0 => 5 workers; 10 olts
# Ratio id: 1 => 5 workers; 5 olts 
# Ratio id: 2 => 10 workers; 5 olts

ratio_0_start = 0
ratio_1_start = 20
ratio_2_start = 40

def results_file_parser(file_path):

    # Data object creation
    avg_time_total_algorithm_1 = [[], [], []]
    avg_time_total_algorithm_2 = [[], [], []]
    avg_time_total_algorithm_3 = [[], [], []]
    avg_time_total_algorithm_4 = [[], [], []]

    avg_time_broker_queue_algorithm_1 = [[], [], []]
    avg_time_broker_queue_algorithm_2 = [[], [], []]
    avg_time_broker_queue_algorithm_3 = [[], [], []]
    avg_time_broker_queue_algorithm_4 = [[], [], []]

    avg_time_worker_queue_algorithm_1 = [[], [], []]
    avg_time_worker_queue_algorithm_2 = [[], [], []]
    avg_time_worker_queue_algorithm_3 = [[], [], []]
    avg_time_worker_queue_algorithm_4 = [[], [], []]

    avg_time_olt_queue_algorithm_1 = [[], [], []]
    avg_time_olt_queue_algorithm_2 = [[], [], []]
    avg_time_olt_queue_algorithm_3 = [[], [], []]
    avg_time_olt_queue_algorithm_4 = [[], [], []]

    timedout_percentage_algorithm_1 = [[], [], []]
    timedout_percentage_algorithm_2 = [[], [], []]
    timedout_percentage_algorithm_3 = [[], [], []]
    timedout_percentage_algorithm_4 = [[], [], []]

    # Read JSON file
    file = open(file_path)
    runs_object = json.load(file)

    # Parse results
    for i in range(ratio_0_start, ratio_1_start, 4):
        for j in range(0, 4):
            run_object = runs_object[i + j] 
            if j == 0:
                avg_time_total_algorithm_1[0].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_1[0].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_1[0].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_1[0].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_1[0].append(run_object["timedout"])
            if j == 1:
                avg_time_total_algorithm_2[0].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_2[0].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_2[0].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_2[0].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_2[0].append(run_object["timedout"])
            if j == 2:
                avg_time_total_algorithm_3[0].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_3[0].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_3[0].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_3[0].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_3[0].append(run_object["timedout"])
            if j == 3:
                avg_time_total_algorithm_4[0].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_4[0].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_4[0].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_4[0].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_4[0].append(run_object["timedout"])

    for i in range(ratio_1_start, ratio_2_start, 4):
        for j in range(0, 4):
            run_object = runs_object[i + j] 
            if j == 0:
                avg_time_total_algorithm_1[1].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_1[1].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_1[1].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_1[1].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_1[1].append(run_object["timedout"])
            if j == 1:
                avg_time_total_algorithm_2[1].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_2[1].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_2[1].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_2[1].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_2[1].append(run_object["timedout"])
            if j == 2:
                avg_time_total_algorithm_3[1].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_3[1].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_3[1].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_3[1].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_3[1].append(run_object["timedout"])
            if j == 3:
                avg_time_total_algorithm_4[1].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_4[1].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_4[1].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_4[1].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_4[1].append(run_object["timedout"])

    for i in range(ratio_2_start, 60, 4):
        for j in range(0, 4):
            run_object = runs_object[i + j] 
            if j == 0:
                avg_time_total_algorithm_1[2].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_1[2].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_1[2].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_1[2].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_1[2].append(run_object["timedout"])
            if j == 1:
                avg_time_total_algorithm_2[2].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_2[2].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_2[2].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_2[2].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_2[2].append(run_object["timedout"])
            if j == 2:
                avg_time_total_algorithm_3[2].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_3[2].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_3[2].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_3[2].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_3[2].append(run_object["timedout"])
            if j == 3:
                avg_time_total_algorithm_4[2].append(run_object["avg_time_total"])
                avg_time_broker_queue_algorithm_4[2].append(run_object["avg_time_broker_queue"])
                avg_time_worker_queue_algorithm_4[2].append(run_object["avg_time_worker_queue"])
                avg_time_olt_queue_algorithm_4[2].append(run_object["avg_time_olt_queue"])
                timedout_percentage_algorithm_4[2].append(run_object["timedout"])

    results = {}

    results['avg_time_total_algorithm_1'] = avg_time_total_algorithm_1
    results['avg_time_total_algorithm_2'] = avg_time_total_algorithm_2
    results['avg_time_total_algorithm_3'] = avg_time_total_algorithm_3
    results['avg_time_total_algorithm_4'] = avg_time_total_algorithm_4

    results['avg_time_broker_queue_algorithm_1'] = avg_time_broker_queue_algorithm_1
    results['avg_time_broker_queue_algorithm_2'] = avg_time_broker_queue_algorithm_2
    results['avg_time_broker_queue_algorithm_3'] = avg_time_broker_queue_algorithm_3
    results['avg_time_broker_queue_algorithm_4'] = avg_time_broker_queue_algorithm_4

    results['avg_time_worker_queue_algorithm_1'] = avg_time_worker_queue_algorithm_1
    results['avg_time_worker_queue_algorithm_2'] = avg_time_worker_queue_algorithm_2
    results['avg_time_worker_queue_algorithm_3'] = avg_time_worker_queue_algorithm_3
    results['avg_time_worker_queue_algorithm_4'] = avg_time_worker_queue_algorithm_4

    results['avg_time_olt_queue_algorithm_1'] = avg_time_olt_queue_algorithm_1
    results['avg_time_olt_queue_algorithm_2'] = avg_time_olt_queue_algorithm_2
    results['avg_time_olt_queue_algorithm_3'] = avg_time_olt_queue_algorithm_3
    results['avg_time_olt_queue_algorithm_4'] = avg_time_olt_queue_algorithm_4

    results['timedout_percentage_algorithm_1'] = timedout_percentage_algorithm_1
    results['timedout_percentage_algorithm_2'] = timedout_percentage_algorithm_2
    results['timedout_percentage_algorithm_3'] = timedout_percentage_algorithm_3
    results['timedout_percentage_algorithm_4'] = timedout_percentage_algorithm_4

    return results
