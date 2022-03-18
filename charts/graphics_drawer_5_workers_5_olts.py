import matplotlib.pyplot as plt
from run_parser import results_file_parser

# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]
run_results_seed_42 = results_file_parser("results/random_seed_42/results_new_run.json")

def average_time_total_chart_random_42():
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_total_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_total_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_total_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_total_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_broker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_broker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_broker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_broker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_worker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_worker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_worker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_worker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_olt_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_olt_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_olt_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, run_results_seed_42['avg_time_olt_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_42():
    plt.plot(number_of_requests_random_42, run_results_seed_42['timedout_percentage_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, run_results_seed_42['timedout_percentage_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, run_results_seed_42['timedout_percentage_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, run_results_seed_42['timedout_percentage_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 2: 7
number_of_requests_random_7 = [50, 100, 500, 1000, 2000]
run_results_seed_7 = results_file_parser("results/random_seed_7/results_new_run.json")

def average_time_total_chart_random_7():
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_total_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_total_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_total_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_total_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_broker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_broker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_broker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_broker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_worker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_worker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_worker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_worker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_olt_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_olt_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_olt_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, run_results_seed_7['avg_time_olt_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_7():
    plt.plot(number_of_requests_random_7, run_results_seed_7['timedout_percentage_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, run_results_seed_7['timedout_percentage_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, run_results_seed_7['timedout_percentage_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, run_results_seed_7['timedout_percentage_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 3: 34
number_of_requests_random_34 = [50, 100, 500, 1000, 2000]
run_results_seed_34 = results_file_parser("results/random_seed_34/results_new_run.json")

def average_time_total_chart_random_34():
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_total_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_total_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_total_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_total_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_broker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_broker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_broker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_broker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_worker_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_worker_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_worker_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_worker_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_olt_queue_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_olt_queue_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_olt_queue_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, run_results_seed_34['avg_time_olt_queue_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_34():
    plt.plot(number_of_requests_random_34, run_results_seed_34['timedout_percentage_algorithm_1'][1], label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, run_results_seed_34['timedout_percentage_algorithm_2'][1], label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, run_results_seed_34['timedout_percentage_algorithm_3'][1], label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, run_results_seed_34['timedout_percentage_algorithm_4'][1], label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/timedout_percentage_chart.png")
    plt.close()


if __name__ == "__main__":
    # Seed 1: 42
    average_time_total_chart_random_42()
    average_time_broker_queue_chart_random_42()
    average_time_worker_queue_chart_random_42()
    average_time_olt_queue_chart_random_42()
    timedout_percentage_chart_random_42()
    # Seed 2: 7
    average_time_total_chart_random_7()
    average_time_broker_queue_chart_random_7()
    average_time_worker_queue_chart_random_7()
    average_time_olt_queue_chart_random_7()
    timedout_percentage_chart_random_7()
    # Seed 3: 34
    average_time_total_chart_random_34()
    average_time_broker_queue_chart_random_34()
    average_time_worker_queue_chart_random_34()
    average_time_olt_queue_chart_random_34()
    timedout_percentage_chart_random_34()
