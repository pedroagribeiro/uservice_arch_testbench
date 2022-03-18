import matplotlib.pyplot as plt
from run_parser import results_file_parser

def average_time_total_chart(x_array, y_array, seed, workers, olts, ratio_id):
    plt.plot(x_array, y_array['avg_time_total_algorithm_1'][ratio_id], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_total_algorithm_2'][ratio_id], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_total_algorithm_3'][ratio_id], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_total_algorithm_4'][ratio_id], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart(x_array, y_array, seed, workers, olts, ratio_id):
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_1'][ratio_id], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_2'][ratio_id], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_3'][ratio_id], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_broker_queue_algorithm_4'][ratio_id], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart(x_array, y_array, seed, workers, olts, ratio_id):
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_1'][ratio_id], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_2'][ratio_id], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_3'][ratio_id], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_worker_queue_algorithm_4'][ratio_id], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart(x_array, y_array, seed, workers, olts, ratio_id):
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_1'][ratio_id], label = "Algoritmo 1")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_2'][ratio_id], label = "Algoritmo 2")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_3'][ratio_id], label = "Algoritmo 3")
    plt.plot(x_array, y_array['avg_time_olt_queue_algorithm_4'][ratio_id], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart(x_array, y_array, seed, workers, olts, ratio_id):
    plt.plot(x_array, y_array['timedout_percentage_algorithm_1'][ratio_id], label = "Algoritmo 1")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_2'][ratio_id], label = "Algoritmo 2")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_3'][ratio_id], label = "Algoritmo 3")
    plt.plot(x_array, y_array['timedout_percentage_algorithm_4'][ratio_id], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_" + str(seed) + "/" + str(workers) + "_workers_" + str(olts) + "_olts/timedout_percentage_chart.png")
    plt.close()

def avg_time_total_by_ratio(x_array, y_array, seed):
    # This methods uses the 2000 request runs
    plt.plot(x_array, [y_array['avg_time_total_algorithm_1'][0][4], y_array['avg_time_total_algorithm_1'][1][4], y_array['avg_time_total_algorithm_1'][2][4]], label = "Algoritmo 1")
    plt.plot(x_array, [y_array['avg_time_total_algorithm_2'][0][4], y_array['avg_time_total_algorithm_2'][1][4], y_array['avg_time_total_algorithm_2'][2][4]], label = "Algoritmo 2")
    plt.plot(x_array, [y_array['avg_time_total_algorithm_3'][0][4], y_array['avg_time_total_algorithm_3'][1][4], y_array['avg_time_total_algorithm_3'][2][4]], label = "Algoritmo 3")
    plt.plot(x_array, [y_array['avg_time_total_algorithm_4'][0][4], y_array['avg_time_total_algorithm_4'][1][4], y_array['avg_time_total_algorithm_4'][2][4]], label = "Algoritmo 4")
    plt.xlabel("Ratio entre workers e olts")
    plt.ylabel("Tempo médio que cada pedido passa no ambiente de simulação (em ms)")
    plt.legend()
    plt.savefig("output/ratio_comparisons/random_seed_" + str(seed) + "/total_average_time_by_ratio.png")
    plt.close()

def timedout_percentage_by_ratio(x_array, y_array, seed):
    # This methods uses the 2000 request runs
    plt.plot(x_array, [y_array['timedout_percentage_algorithm_1'][0][4], y_array['timedout_percentage_algorithm_1'][1][4], y_array['timedout_percentage_algorithm_1'][2][4]], label = "Algoritmo 1")
    plt.plot(x_array, [y_array['timedout_percentage_algorithm_2'][0][4], y_array['timedout_percentage_algorithm_2'][1][4], y_array['timedout_percentage_algorithm_2'][2][4]], label = "Algoritmo 2")
    plt.plot(x_array, [y_array['timedout_percentage_algorithm_3'][0][4], y_array['timedout_percentage_algorithm_3'][1][4], y_array['timedout_percentage_algorithm_3'][2][4]], label = "Algoritmo 3")
    plt.plot(x_array, [y_array['timedout_percentage_algorithm_4'][0][4], y_array['timedout_percentage_algorithm_4'][1][4], y_array['timedout_percentage_algorithm_4'][2][4]], label = "Algoritmo 4")
    plt.xlabel("Ratio entre workers e olts")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/ratio_comparisons/random_seed_" + str(seed) + "/timedout_percentage_by_ratio.png")
    plt.close()

number_of_requests = [50, 100, 500, 1000, 2000]
run_results_42 = results_file_parser("results/random_seed_42/results_new_run.json")
run_results_7 = results_file_parser("results/random_seed_7/results_new_run.json")
run_results_34 = results_file_parser("results/random_seed_34/results_new_run.json")
ratios = [0.5, 1.0, 2.0]

if __name__ == "__main__":
    # 5 workers; 10 olts => Ratio 0.5
        # -- Seed 42
    average_time_total_chart(number_of_requests, run_results_42, 42, 5, 10, 0)
    average_time_broker_queue_chart(number_of_requests, run_results_42, 42, 5, 10, 0)
    average_time_worker_queue_chart(number_of_requests, run_results_42, 42, 5, 10, 0)
    average_time_olt_queue_chart(number_of_requests, run_results_42, 42, 5, 10, 0)
    timedout_percentage_chart(number_of_requests, run_results_42, 42, 5, 10, 0)
        # -- Seed 7
    average_time_total_chart(number_of_requests, run_results_7, 7, 5, 10, 0)
    average_time_broker_queue_chart(number_of_requests, run_results_7, 7, 5, 10, 0)
    average_time_worker_queue_chart(number_of_requests, run_results_7, 7, 5, 10, 0)
    average_time_olt_queue_chart(number_of_requests, run_results_7, 7, 5, 10, 0)
    timedout_percentage_chart(number_of_requests, run_results_7, 7, 5, 10, 0)
        # -- Seed 34
    average_time_total_chart(number_of_requests, run_results_34, 34, 5, 10, 0)
    average_time_broker_queue_chart(number_of_requests, run_results_34, 34, 5, 10, 0)
    average_time_worker_queue_chart(number_of_requests, run_results_34, 34, 5, 10, 0)
    average_time_olt_queue_chart(number_of_requests, run_results_34, 34, 5, 10, 0)
    timedout_percentage_chart(number_of_requests, run_results_34, 34, 5, 10, 0)
    # 5 workers; 5 olts => Ratio 1.0
        # -- Seed 42
    average_time_total_chart(number_of_requests, run_results_42, 42, 5, 5, 1)
    average_time_broker_queue_chart(number_of_requests, run_results_42, 42, 5, 5, 1)
    average_time_worker_queue_chart(number_of_requests, run_results_42, 42, 5, 5, 1)
    average_time_olt_queue_chart(number_of_requests, run_results_42, 42, 5, 5, 1)
    timedout_percentage_chart(number_of_requests, run_results_42, 42, 5, 5, 1)
        # -- Seed 7
    average_time_total_chart(number_of_requests, run_results_7, 7, 5, 5, 1)
    average_time_broker_queue_chart(number_of_requests, run_results_7, 7, 5, 5, 1)
    average_time_worker_queue_chart(number_of_requests, run_results_7, 7, 5, 5, 1)
    average_time_olt_queue_chart(number_of_requests, run_results_7, 7, 5, 5, 1)
    timedout_percentage_chart(number_of_requests, run_results_7, 7, 5, 5, 1)
        # -- Seed 34
    average_time_total_chart(number_of_requests, run_results_34, 34, 5, 5, 1)
    average_time_broker_queue_chart(number_of_requests, run_results_34, 34, 5, 5, 1)
    average_time_worker_queue_chart(number_of_requests, run_results_34, 34, 5, 5, 1)
    average_time_olt_queue_chart(number_of_requests, run_results_34, 34, 5, 5, 1)
    timedout_percentage_chart(number_of_requests, run_results_34, 34, 5, 5, 1)
    # 10 workers; 5 olts => Ratio 2.0
        # -- Seed 42
    average_time_total_chart(number_of_requests, run_results_42, 42, 10, 5, 2)
    average_time_broker_queue_chart(number_of_requests, run_results_42, 42, 10, 5, 2)
    average_time_worker_queue_chart(number_of_requests, run_results_42, 42, 10, 5, 2)
    average_time_olt_queue_chart(number_of_requests, run_results_42, 42, 10, 5, 2)
    timedout_percentage_chart(number_of_requests, run_results_42, 42, 10, 5, 2)
        # -- Seed 7
    average_time_total_chart(number_of_requests, run_results_7, 7, 10, 5, 2)
    average_time_broker_queue_chart(number_of_requests, run_results_7, 7, 10, 5, 2)
    average_time_worker_queue_chart(number_of_requests, run_results_7, 7, 10, 5, 2)
    average_time_olt_queue_chart(number_of_requests, run_results_7, 7, 10, 5, 2)
    timedout_percentage_chart(number_of_requests, run_results_7, 7, 10, 5, 2)
        # -- Seed 34
    average_time_total_chart(number_of_requests, run_results_34, 34, 10, 5, 2)
    average_time_broker_queue_chart(number_of_requests, run_results_34, 34, 10, 5, 2)
    average_time_worker_queue_chart(number_of_requests, run_results_34, 34, 10, 5, 2)
    average_time_olt_queue_chart(number_of_requests, run_results_34, 34, 10, 5, 2)
    timedout_percentage_chart(number_of_requests, run_results_34, 34, 10, 5, 2)

    # Ratio comparisons

        # -- Seed 42
    avg_time_total_by_ratio(ratios, run_results_42, 42) 
    timedout_percentage_by_ratio(ratios, run_results_42, 42)

        # -- Seed 7
    avg_time_total_by_ratio(ratios, run_results_7, 7) 
    timedout_percentage_by_ratio(ratios, run_results_7, 7)

        # -- Seed 34
    avg_time_total_by_ratio(ratios, run_results_34, 34) 
    timedout_percentage_by_ratio(ratios, run_results_34, 34)



