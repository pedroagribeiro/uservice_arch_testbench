import json

# Ratio id: 0 => 5 workers; 10 olts
# Ratio id: 1 => 5 workers; 5 olts 
# Ratio id: 2 => 10 workers; 5 olts

def results_file_parser(file_path):

    results = {}
    # Read JSON file
    file = open(file_path)
    runs_object = json.load(file)

    # Parse results
    for i in range(0, 4):
        run_object = runs_object[i] 
        if i == 0:
            results['avg_time_total_algorithm_1'] = run_object['avg_time_total']
            results['avg_time_broker_queue_algorithm_1'] = run_object['avg_time_broker_queue']
            results['avg_time_worker_queue_algorithm_1'] = run_object['avg_time_worker_queue']
            results['avg_time_olt_queue_algorithm_1'] = run_object['avg_time_olt_queue']
            results['timedout_percentage_algorithm_1'] = run_object['timedout']
        if i == 1:
            results['avg_time_total_algorithm_2'] = run_object['avg_time_total']
            results['avg_time_broker_queue_algorithm_2'] = run_object['avg_time_broker_queue']
            results['avg_time_worker_queue_algorithm_2'] = run_object['avg_time_worker_queue']
            results['avg_time_olt_queue_algorithm_2'] = run_object['avg_time_olt_queue']
            results['timedout_percentage_algorithm_2'] = run_object['timedout']
        if i == 2:
            results['avg_time_total_algorithm_3'] = run_object['avg_time_total']
            results['avg_time_broker_queue_algorithm_3'] = run_object['avg_time_broker_queue']
            results['avg_time_worker_queue_algorithm_3'] = run_object['avg_time_worker_queue']
            results['avg_time_olt_queue_algorithm_3'] = run_object['avg_time_olt_queue']
            results['timedout_percentage_algorithm_3'] = run_object['timedout']
        if i == 3:
            results['avg_time_total_algorithm_4'] = run_object['avg_time_total']
            results['avg_time_broker_queue_algorithm_4'] = run_object['avg_time_broker_queue']
            results['avg_time_worker_queue_algorithm_4'] = run_object['avg_time_worker_queue']
            results['avg_time_olt_queue_algorithm_4'] = run_object['avg_time_olt_queue']
            results['timedout_percentage_algorithm_4'] = run_object['timedout']

    return results

def build_run_results(res_50_req, res_100_req, res_500_req, res_1000_req, res_2000_req):
    run_results = {
        "avg_time_total_algorithm_1": [],
        "avg_time_total_algorithm_2": [],
        "avg_time_total_algorithm_3": [],
        "avg_time_total_algorithm_4": [],
        "avg_time_broker_queue_algorithm_1": [],
        "avg_time_broker_queue_algorithm_2": [],
        "avg_time_broker_queue_algorithm_3": [],
        "avg_time_broker_queue_algorithm_4": [],
        "avg_time_worker_queue_algorithm_1": [],
        "avg_time_worker_queue_algorithm_2": [],
        "avg_time_worker_queue_algorithm_3": [],
        "avg_time_worker_queue_algorithm_4": [],
        "avg_time_olt_queue_algorithm_1": [],
        "avg_time_olt_queue_algorithm_2": [],
        "avg_time_olt_queue_algorithm_3": [],
        "avg_time_olt_queue_algorithm_4": [],
        "timedout_percentage_algorithm_1": [],
        "timedout_percentage_algorithm_2": [],
        "timedout_percentage_algorithm_3": [],
        "timedout_percentage_algorithm_4": []
    }
    # 50 requests
    run_results['avg_time_total_algorithm_1'].append(res_50_req['avg_time_total_algorithm_1'])
    run_results['avg_time_total_algorithm_2'].append(res_50_req['avg_time_total_algorithm_2'])
    run_results['avg_time_total_algorithm_3'].append(res_50_req['avg_time_total_algorithm_3'])
    run_results['avg_time_total_algorithm_4'].append(res_50_req['avg_time_total_algorithm_4'])
    run_results['avg_time_broker_queue_algorithm_1'].append(res_50_req['avg_time_broker_queue_algorithm_1'])
    run_results['avg_time_broker_queue_algorithm_2'].append(res_50_req['avg_time_broker_queue_algorithm_2'])
    run_results['avg_time_broker_queue_algorithm_3'].append(res_50_req['avg_time_broker_queue_algorithm_3'])
    run_results['avg_time_broker_queue_algorithm_4'].append(res_50_req['avg_time_broker_queue_algorithm_4'])
    run_results['avg_time_worker_queue_algorithm_1'].append(res_50_req['avg_time_worker_queue_algorithm_1'])
    run_results['avg_time_worker_queue_algorithm_2'].append(res_50_req['avg_time_worker_queue_algorithm_2'])
    run_results['avg_time_worker_queue_algorithm_3'].append(res_50_req['avg_time_worker_queue_algorithm_3'])
    run_results['avg_time_worker_queue_algorithm_4'].append(res_50_req['avg_time_worker_queue_algorithm_4'])
    run_results['avg_time_olt_queue_algorithm_1'].append(res_50_req['avg_time_olt_queue_algorithm_1'])
    run_results['avg_time_olt_queue_algorithm_2'].append(res_50_req['avg_time_olt_queue_algorithm_2'])
    run_results['avg_time_olt_queue_algorithm_3'].append(res_50_req['avg_time_olt_queue_algorithm_3'])
    run_results['avg_time_olt_queue_algorithm_4'].append(res_50_req['avg_time_olt_queue_algorithm_4'])
    run_results['timedout_percentage_algorithm_1'].append(res_50_req['timedout_percentage_algorithm_1'])
    run_results['timedout_percentage_algorithm_2'].append(res_50_req['timedout_percentage_algorithm_2'])
    run_results['timedout_percentage_algorithm_3'].append(res_50_req['timedout_percentage_algorithm_3'])
    run_results['timedout_percentage_algorithm_4'].append(res_50_req['timedout_percentage_algorithm_4'])
    # 100 requests
    run_results['avg_time_total_algorithm_1'].append(res_100_req['avg_time_total_algorithm_1'])
    run_results['avg_time_total_algorithm_2'].append(res_100_req['avg_time_total_algorithm_2'])
    run_results['avg_time_total_algorithm_3'].append(res_100_req['avg_time_total_algorithm_3'])
    run_results['avg_time_total_algorithm_4'].append(res_100_req['avg_time_total_algorithm_4'])
    run_results['avg_time_broker_queue_algorithm_1'].append(res_100_req['avg_time_broker_queue_algorithm_1'])
    run_results['avg_time_broker_queue_algorithm_2'].append(res_100_req['avg_time_broker_queue_algorithm_2'])
    run_results['avg_time_broker_queue_algorithm_3'].append(res_100_req['avg_time_broker_queue_algorithm_3'])
    run_results['avg_time_broker_queue_algorithm_4'].append(res_100_req['avg_time_broker_queue_algorithm_4'])
    run_results['avg_time_worker_queue_algorithm_1'].append(res_100_req['avg_time_worker_queue_algorithm_1'])
    run_results['avg_time_worker_queue_algorithm_2'].append(res_100_req['avg_time_worker_queue_algorithm_2'])
    run_results['avg_time_worker_queue_algorithm_3'].append(res_100_req['avg_time_worker_queue_algorithm_3'])
    run_results['avg_time_worker_queue_algorithm_4'].append(res_100_req['avg_time_worker_queue_algorithm_4'])
    run_results['avg_time_olt_queue_algorithm_1'].append(res_100_req['avg_time_olt_queue_algorithm_1'])
    run_results['avg_time_olt_queue_algorithm_2'].append(res_100_req['avg_time_olt_queue_algorithm_2'])
    run_results['avg_time_olt_queue_algorithm_3'].append(res_100_req['avg_time_olt_queue_algorithm_3'])
    run_results['avg_time_olt_queue_algorithm_4'].append(res_100_req['avg_time_olt_queue_algorithm_4'])
    run_results['timedout_percentage_algorithm_1'].append(res_100_req['timedout_percentage_algorithm_1'])
    run_results['timedout_percentage_algorithm_2'].append(res_100_req['timedout_percentage_algorithm_2'])
    run_results['timedout_percentage_algorithm_3'].append(res_100_req['timedout_percentage_algorithm_3'])
    run_results['timedout_percentage_algorithm_4'].append(res_100_req['timedout_percentage_algorithm_4'])
    # 500 requests
    run_results['avg_time_total_algorithm_1'].append(res_500_req['avg_time_total_algorithm_1'])
    run_results['avg_time_total_algorithm_2'].append(res_500_req['avg_time_total_algorithm_2'])
    run_results['avg_time_total_algorithm_3'].append(res_500_req['avg_time_total_algorithm_3'])
    run_results['avg_time_total_algorithm_4'].append(res_500_req['avg_time_total_algorithm_4'])
    run_results['avg_time_broker_queue_algorithm_1'].append(res_500_req['avg_time_broker_queue_algorithm_1'])
    run_results['avg_time_broker_queue_algorithm_2'].append(res_500_req['avg_time_broker_queue_algorithm_2'])
    run_results['avg_time_broker_queue_algorithm_3'].append(res_500_req['avg_time_broker_queue_algorithm_3'])
    run_results['avg_time_broker_queue_algorithm_4'].append(res_500_req['avg_time_broker_queue_algorithm_4'])
    run_results['avg_time_worker_queue_algorithm_1'].append(res_500_req['avg_time_worker_queue_algorithm_1'])
    run_results['avg_time_worker_queue_algorithm_2'].append(res_500_req['avg_time_worker_queue_algorithm_2'])
    run_results['avg_time_worker_queue_algorithm_3'].append(res_500_req['avg_time_worker_queue_algorithm_3'])
    run_results['avg_time_worker_queue_algorithm_4'].append(res_500_req['avg_time_worker_queue_algorithm_4'])
    run_results['avg_time_olt_queue_algorithm_1'].append(res_500_req['avg_time_olt_queue_algorithm_1'])
    run_results['avg_time_olt_queue_algorithm_2'].append(res_500_req['avg_time_olt_queue_algorithm_2'])
    run_results['avg_time_olt_queue_algorithm_3'].append(res_500_req['avg_time_olt_queue_algorithm_3'])
    run_results['avg_time_olt_queue_algorithm_4'].append(res_500_req['avg_time_olt_queue_algorithm_4'])
    run_results['timedout_percentage_algorithm_1'].append(res_500_req['timedout_percentage_algorithm_1'])
    run_results['timedout_percentage_algorithm_2'].append(res_500_req['timedout_percentage_algorithm_2'])
    run_results['timedout_percentage_algorithm_3'].append(res_500_req['timedout_percentage_algorithm_3'])
    run_results['timedout_percentage_algorithm_4'].append(res_500_req['timedout_percentage_algorithm_4'])
    # 1000 requests
    run_results['avg_time_total_algorithm_1'].append(res_1000_req['avg_time_total_algorithm_1'])
    run_results['avg_time_total_algorithm_2'].append(res_1000_req['avg_time_total_algorithm_2'])
    run_results['avg_time_total_algorithm_3'].append(res_1000_req['avg_time_total_algorithm_3'])
    run_results['avg_time_total_algorithm_4'].append(res_1000_req['avg_time_total_algorithm_4'])
    run_results['avg_time_broker_queue_algorithm_1'].append(res_1000_req['avg_time_broker_queue_algorithm_1'])
    run_results['avg_time_broker_queue_algorithm_2'].append(res_1000_req['avg_time_broker_queue_algorithm_2'])
    run_results['avg_time_broker_queue_algorithm_3'].append(res_1000_req['avg_time_broker_queue_algorithm_3'])
    run_results['avg_time_broker_queue_algorithm_4'].append(res_1000_req['avg_time_broker_queue_algorithm_4'])
    run_results['avg_time_worker_queue_algorithm_1'].append(res_1000_req['avg_time_worker_queue_algorithm_1'])
    run_results['avg_time_worker_queue_algorithm_2'].append(res_1000_req['avg_time_worker_queue_algorithm_2'])
    run_results['avg_time_worker_queue_algorithm_3'].append(res_1000_req['avg_time_worker_queue_algorithm_3'])
    run_results['avg_time_worker_queue_algorithm_4'].append(res_1000_req['avg_time_worker_queue_algorithm_4'])
    run_results['avg_time_olt_queue_algorithm_1'].append(res_1000_req['avg_time_olt_queue_algorithm_1'])
    run_results['avg_time_olt_queue_algorithm_2'].append(res_1000_req['avg_time_olt_queue_algorithm_2'])
    run_results['avg_time_olt_queue_algorithm_3'].append(res_1000_req['avg_time_olt_queue_algorithm_3'])
    run_results['avg_time_olt_queue_algorithm_4'].append(res_1000_req['avg_time_olt_queue_algorithm_4'])
    run_results['timedout_percentage_algorithm_1'].append(res_1000_req['timedout_percentage_algorithm_1'])
    run_results['timedout_percentage_algorithm_2'].append(res_1000_req['timedout_percentage_algorithm_2'])
    run_results['timedout_percentage_algorithm_3'].append(res_1000_req['timedout_percentage_algorithm_3'])
    run_results['timedout_percentage_algorithm_4'].append(res_1000_req['timedout_percentage_algorithm_4'])
    # 2000 requests
    run_results['avg_time_total_algorithm_1'].append(res_2000_req['avg_time_total_algorithm_1'])
    run_results['avg_time_total_algorithm_2'].append(res_2000_req['avg_time_total_algorithm_2'])
    run_results['avg_time_total_algorithm_3'].append(res_2000_req['avg_time_total_algorithm_3'])
    run_results['avg_time_total_algorithm_4'].append(res_2000_req['avg_time_total_algorithm_4'])
    run_results['avg_time_broker_queue_algorithm_1'].append(res_2000_req['avg_time_broker_queue_algorithm_1'])
    run_results['avg_time_broker_queue_algorithm_2'].append(res_2000_req['avg_time_broker_queue_algorithm_2'])
    run_results['avg_time_broker_queue_algorithm_3'].append(res_2000_req['avg_time_broker_queue_algorithm_3'])
    run_results['avg_time_broker_queue_algorithm_4'].append(res_2000_req['avg_time_broker_queue_algorithm_4'])
    run_results['avg_time_worker_queue_algorithm_1'].append(res_2000_req['avg_time_worker_queue_algorithm_1'])
    run_results['avg_time_worker_queue_algorithm_2'].append(res_2000_req['avg_time_worker_queue_algorithm_2'])
    run_results['avg_time_worker_queue_algorithm_3'].append(res_2000_req['avg_time_worker_queue_algorithm_3'])
    run_results['avg_time_worker_queue_algorithm_4'].append(res_2000_req['avg_time_worker_queue_algorithm_4'])
    run_results['avg_time_olt_queue_algorithm_1'].append(res_2000_req['avg_time_olt_queue_algorithm_1'])
    run_results['avg_time_olt_queue_algorithm_2'].append(res_2000_req['avg_time_olt_queue_algorithm_2'])
    run_results['avg_time_olt_queue_algorithm_3'].append(res_2000_req['avg_time_olt_queue_algorithm_3'])
    run_results['avg_time_olt_queue_algorithm_4'].append(res_2000_req['avg_time_olt_queue_algorithm_4'])
    run_results['timedout_percentage_algorithm_1'].append(res_2000_req['timedout_percentage_algorithm_1'])
    run_results['timedout_percentage_algorithm_2'].append(res_2000_req['timedout_percentage_algorithm_2'])
    run_results['timedout_percentage_algorithm_3'].append(res_2000_req['timedout_percentage_algorithm_3'])
    run_results['timedout_percentage_algorithm_4'].append(res_2000_req['timedout_percentage_algorithm_4'])
    return run_results

def load_run_results(seed, workers, olts):
    run_results_50_requests = results_file_parser("results/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/results_50.json")
    run_results_100_requests = results_file_parser("results/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/results_100.json")
    run_results_500_requests = results_file_parser("results/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/results_500.json")
    run_results_1000_requests = results_file_parser("results/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/results_1000.json")
    run_results_2000_requests = results_file_parser("results/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/results_2000.json")
    full_run_results = build_run_results(run_results_50_requests, run_results_100_requests, run_results_500_requests, run_results_1000_requests, run_results_2000_requests)
    return full_run_results

