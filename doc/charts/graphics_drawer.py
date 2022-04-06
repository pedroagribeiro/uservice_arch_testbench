from json import load
import matplotlib.pyplot as plt
from run_parser import load_run_results

def average_time_total_chart(x_array, y_array, seed, workers, olts):
    plt.plot(x_array, y_array['avg_time_total_algorithm_1'], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_total_algorithm_2'], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_total_algorithm_3'], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_total_algorithm_4'], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart(x_array, y_array, seed, workers, olts):
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_1'], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_2'], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_3'], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_4'], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart(x_array, y_array, seed, workers, olts):
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_1'], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_2'], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_3'], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_4'], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart(x_array, y_array, seed, workers, olts):
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_1'], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_2'], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_3'], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_4'], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart(x_array, y_array, seed, workers, olts):
    plt.plot(x_array, y_array['timedout_percentage_algorithm_1'], label = "Algoritmo 1")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_2'], label = "Algoritmo 2")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_3'], label = "Algoritmo 3")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_4'], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/timedout_percentage_chart.png")
    plt.close()

def avg_time_total_by_ratio(x_array, y_array, seed):
    # This methods uses the 2000 request runs
    algorithm_1 = []
    algorithm_2 = []
    algorithm_3 = []
    algorithm_4 = []
    for i in range(0, len(y_array)):
        algorithm_1.append(y_array[i]['avg_time_total_algorithm_1'][4])
        algorithm_2.append(y_array[i]['avg_time_total_algorithm_2'][4])
        algorithm_3.append(y_array[i]['avg_time_total_algorithm_3'][4])
        algorithm_4.append(y_array[i]['avg_time_total_algorithm_4'][4])
    plt.plot(x_array, algorithm_1, label = "Algoritmo 1")
    plt.plot(x_array, algorithm_2, label = "Algoritmo 2")
    plt.plot(x_array, algorithm_3, label = "Algoritmo 3")
    plt.plot(x_array, algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Ratio entre workers e olts")
    plt.ylabel("Tempo médio que cada pedido passa no ambiente de simulação (em ms)")
    plt.legend()
    plt.savefig("output/ratio_comparisons/random_seed_" + str(seed) + "/total_average_time_by_ratio.png")
    plt.close()

def timedout_percentage_by_ratio(x_array, y_array, seed):
    # This methods uses the 2000 request runs
    algorithm_1 = []
    algorithm_2 = []
    algorithm_3 = []
    algorithm_4 = []
    for i in range(0, len(y_array)):
        algorithm_1.append(y_array[i]['timedout_percentage_algorithm_1'][4])
        algorithm_2.append(y_array[i]['timedout_percentage_algorithm_2'][4])
        algorithm_3.append(y_array[i]['timedout_percentage_algorithm_3'][4])
        algorithm_4.append(y_array[i]['timedout_percentage_algorithm_4'][4])
    plt.plot(x_array, algorithm_1, label = "Algoritmo 1")
    plt.plot(x_array, algorithm_2, label = "Algoritmo 2")
    plt.plot(x_array, algorithm_3, label = "Algoritmo 3")
    plt.plot(x_array, algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Ratio entre workers e olts")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/ratio_comparisons/random_seed_" + str(seed) + "/timedout_percentage_by_ratio.png")
    plt.close()

number_of_requests = [50, 100, 500, 1000, 2000]

if __name__ == "__main__":
    # 1 workers; 5 olts => Ratio 0.2
        # -- Seed 42
        # -- Seed 7 
        # -- Seed 34 
    run_results_34_1_workers_5_olts = load_run_results(34, 1, 5)
    average_time_total_chart(number_of_requests, run_results_34_1_workers_5_olts, 34, 1, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_1_workers_5_olts, 34, 1, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_1_workers_5_olts, 34, 1, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_1_workers_5_olts, 34, 1, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_1_workers_5_olts, 34, 1, 5)
    # 2 workers; 5 olts => Ratio 0.4
        # -- Seed 42
        # -- Seed 7 
        # -- Seed 34 
    run_results_34_2_workers_5_olts = load_run_results(34, 2, 5)
    average_time_total_chart(number_of_requests, run_results_34_2_workers_5_olts, 34, 2, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_2_workers_5_olts, 34, 2, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_2_workers_5_olts, 34, 2, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_2_workers_5_olts, 34, 2, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_2_workers_5_olts, 34, 2, 5)
    # 3 workers; 5 olts => Ratio 0.6
        # -- Seed 42
        # -- Seed 7
        # -- Seed 34
    run_results_34_3_workers_5_olts = load_run_results(34, 3, 5)
    average_time_total_chart(number_of_requests, run_results_34_3_workers_5_olts, 34, 3, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_3_workers_5_olts, 34, 3, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_3_workers_5_olts, 34, 3, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_3_workers_5_olts, 34, 3, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_3_workers_5_olts, 34, 3, 5)
    # 4 workers; 5 olts => Ratio 0.8
        # -- Seed 42
        # -- Seed 7
        # -- Seed 34
    run_results_34_4_workers_5_olts = load_run_results(34, 4, 5)
    average_time_total_chart(number_of_requests, run_results_34_4_workers_5_olts, 34, 4, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_4_workers_5_olts, 34, 4, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_4_workers_5_olts, 34, 4, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_4_workers_5_olts, 34, 4, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_4_workers_5_olts, 34, 4, 5)
   # 5 workers; 5 olts => Ratio 1.0
        # -- Seed 42
    run_results_42_5_workers_5_olts = load_run_results(42, 5, 5)
    average_time_total_chart(number_of_requests, run_results_42_5_workers_5_olts, 42, 5, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_42_5_workers_5_olts, 42, 5, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_42_5_workers_5_olts, 42, 5, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_42_5_workers_5_olts, 42, 5, 5)
    timedout_percentage_chart(number_of_requests, run_results_42_5_workers_5_olts, 42, 5, 5)
        # -- Seed 7
    run_results_7_5_workers_5_olts = load_run_results(7, 5, 5)
    average_time_total_chart(number_of_requests, run_results_7_5_workers_5_olts, 7, 5, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_7_5_workers_5_olts, 7, 5, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_7_5_workers_5_olts, 7, 5, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_7_5_workers_5_olts, 7, 5, 5)
    timedout_percentage_chart(number_of_requests, run_results_7_5_workers_5_olts, 7, 5, 5)
        # -- Seed 34
    run_results_34_5_workers_5_olts = load_run_results(34, 5, 5)
    average_time_total_chart(number_of_requests, run_results_34_5_workers_5_olts, 34, 5, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_5_workers_5_olts, 34, 5, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_5_workers_5_olts, 34, 5, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_5_workers_5_olts, 34, 5, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_5_workers_5_olts, 34, 5, 5)
    # 6 workers; 5 olts => Ratio 1.2
        # -- Seed 34
    run_results_34_6_workers_5_olts = load_run_results(34, 6, 5)
    average_time_total_chart(number_of_requests, run_results_34_6_workers_5_olts, 34, 6, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_6_workers_5_olts, 34, 6, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_6_workers_5_olts, 34, 6, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_6_workers_5_olts, 34, 6, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_6_workers_5_olts, 34, 6, 5)
    # 7 workers: 5 olts => Ratio 1.4
    run_results_34_7_workers_5_olts = load_run_results(34, 7, 5)
    average_time_total_chart(number_of_requests, run_results_34_7_workers_5_olts, 34, 7, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_7_workers_5_olts, 34, 7, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_7_workers_5_olts, 34, 7, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_7_workers_5_olts, 34, 7, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_7_workers_5_olts, 34, 7, 5)
    # 8 workers; 5 olts => Ratio 1.6
    run_results_34_8_workers_5_olts = load_run_results(34, 8, 5)
    average_time_total_chart(number_of_requests, run_results_34_8_workers_5_olts, 34, 8, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_8_workers_5_olts, 34, 8, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_8_workers_5_olts, 34, 8, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_8_workers_5_olts, 34, 8, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_8_workers_5_olts, 34, 8, 5)
    # 9 workers; 5 olts => Ratio 1.8
    run_results_34_9_workers_5_olts = load_run_results(34, 9, 5)
    average_time_total_chart(number_of_requests, run_results_34_9_workers_5_olts, 34, 9, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_9_workers_5_olts, 34, 9, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_9_workers_5_olts, 34, 9, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_9_workers_5_olts, 34, 9, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_9_workers_5_olts, 34, 9, 5)
    # 10 workers; 5 olts => Ratio 2.0
        # -- Seed 42
    run_results_42_10_workers_5_olts = load_run_results(42, 10, 5)
    average_time_total_chart(number_of_requests, run_results_42_10_workers_5_olts, 42, 10, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_42_10_workers_5_olts, 42, 10, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_42_10_workers_5_olts, 42, 10, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_42_10_workers_5_olts, 42, 10, 5)
    timedout_percentage_chart(number_of_requests, run_results_42_10_workers_5_olts, 42, 10, 5)
        # -- Seed 7
    run_results_7_10_workers_5_olts = load_run_results(7, 10, 5)
    average_time_total_chart(number_of_requests, run_results_7_10_workers_5_olts, 7, 10, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_7_10_workers_5_olts, 7, 10, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_7_10_workers_5_olts, 7, 10, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_7_10_workers_5_olts, 7, 10, 5)
    timedout_percentage_chart(number_of_requests, run_results_7_10_workers_5_olts, 7, 10, 5)
        # -- Seed 34
    run_results_34_10_workers_5_olts = load_run_results(34, 10, 5)
    average_time_total_chart(number_of_requests, run_results_34_10_workers_5_olts, 34, 10, 5)
    average_time_broker_queue_chart(number_of_requests, run_results_34_10_workers_5_olts, 34, 10, 5)
    average_time_worker_queue_chart(number_of_requests, run_results_34_10_workers_5_olts, 34, 10, 5)
    average_time_olt_queue_chart(number_of_requests, run_results_34_10_workers_5_olts, 34, 10, 5)
    timedout_percentage_chart(number_of_requests, run_results_34_10_workers_5_olts, 34, 10, 5)

    ratios = [1.0, 2.0]

    # Ratio comparisons

        # -- Seed 42
    # avg_time_total_by_ratio(ratios, [run_results_42_5_workers_5_olts, run_results_42_10_workers_5_olts], 42) 
    # timedout_percentage_by_ratio(ratios, [run_results_42_5_workers_5_olts, run_results_42_10_workers_5_olts], 42)

        # -- Seed 7
    # avg_time_total_by_ratio(ratios, [run_results_7_5_workers_5_olts, run_results_7_10_workers_5_olts], 7) 
    # timedout_percentage_by_ratio(ratios, [run_results_7_5_workers_5_olts, run_results_7_10_workers_5_olts], 7)

        # -- Seed 34
    own_ratios = [0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0]
    avg_time_total_by_ratio(own_ratios, [run_results_34_2_workers_5_olts, run_results_34_3_workers_5_olts, run_results_34_4_workers_5_olts, run_results_34_5_workers_5_olts, run_results_34_6_workers_5_olts, run_results_34_7_workers_5_olts, run_results_34_8_workers_5_olts, run_results_34_9_workers_5_olts, run_results_34_10_workers_5_olts], 34) 
    timedout_percentage_by_ratio(own_ratios, [run_results_34_2_workers_5_olts, run_results_34_3_workers_5_olts, run_results_34_4_workers_5_olts, run_results_34_5_workers_5_olts, run_results_34_6_workers_5_olts, run_results_34_7_workers_5_olts, run_results_34_8_workers_5_olts, run_results_34_9_workers_5_olts, run_results_34_10_workers_5_olts], 34)



