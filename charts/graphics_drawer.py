from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000]

avg_time_total_algorithm_1_random_42 = [14149.0, 65094.0, 248557.0, 499626.0]
avg_time_total_algorithm_2_random_42 = [18498.0, 48789.0, 233915.0, 485337.0]
avg_time_total_algorithm_3_random_42 = [14707.0, 47254.0, 497120.0, 556622.0]
avg_time_total_algorithm_4_random_42 = [14728.0, 46161.0, 236146.0, 521052.0]

avg_time_broker_queue_algorithm_1_random_42 = [7.0, 7.0, 5.0, 5.0]
avg_time_broker_queue_algorithm_2_random_42 = [7.0, 7.0, 5.0, 4.0]
avg_time_broker_queue_algorithm_3_random_42 = [10738.0, 41544.0, 497120.0, 550147.0]
avg_time_broker_queue_algorithm_4_random_42 = [10752.0, 40643.0, 230106.0, 513865.0]

avg_time_worker_queue_algorithm_1_random_42 = [10452.0, 57635.0, 240176.0, 492509.0]
avg_time_worker_queue_algorithm_2_random_42 = [14366.0, 42590.0, 227496.0, 478119.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_42 = [1027.0, 3639.0, 3778.0, 3652.0]
avg_time_olt_queue_algorithm_2_random_42 = [1471.0, 2888.0, 2814.0, 3758.0]
avg_time_olt_queue_algorithm_3_random_42 = [103.0, 345.0, 400.0, 488.0]
avg_time_olt_queue_algorithm_4_random_42 = [4.0, 199.0, 227.0, 3738.0]

timedout_percentage_algorithm_1_random_42 = [0.0, 0.06, 0.076, 0.095]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.064, 0.093]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.02, 0.018, 0.016]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.02, 0.016, 0.092]

def average_time_total_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_42():
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_42/timedout_percentage_chart.png")
    plt.close()

# Seed 2: 7

number_of_requests_random_7 = [50, 100, 500]

avg_time_total_algorithm_1_random_7 = [21423.0, 54764.0, 215862.0]
avg_time_total_algorithm_2_random_7 = [30597.0, 63052.0, 252514.0]
avg_time_total_algorithm_3_random_7 = [24495.0, 65484.0, 266222.0]
avg_time_total_algorithm_4_random_7 = [22886.0, 69561.0, 260116.0]

avg_time_broker_queue_algorithm_1_random_7 = [6.0, 6.0, 5.0]
avg_time_broker_queue_algorithm_2_random_7 = [8.0, 6.0, 5.0]
avg_time_broker_queue_algorithm_3_random_7 = [19185.0, 58303.0, 259627.0]
avg_time_broker_queue_algorithm_4_random_7 = [17805.0, 62113.0, 253773.0]

avg_time_worker_queue_algorithm_1_random_7 = [17796.0, 47133.0, 209309.0]
avg_time_worker_queue_algorithm_2_random_7 = [25193.0, 54164.0, 245460.0]
avg_time_worker_queue_algorithm_3_random_7 = [0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_7 = [0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_7 = [480.0, 3864.0, 3026.0]
avg_time_olt_queue_algorithm_2_random_7 = [2262.0, 5127.0, 3529.0]
avg_time_olt_queue_algorithm_3_random_7 = [475.0, 1495.0, 364.0]
avg_time_olt_queue_algorithm_4_random_7 = [1949.0, 1189.0, 333.0]

timedout_percentage_algorithm_1_random_7 = [0.02, 0.11, 0.1]
timedout_percentage_algorithm_2_random_7 = [0.04, 0.13, 0.096]
timedout_percentage_algorithm_3_random_7 = [0.0, 0.04, 0.022]
timedout_percentage_algorithm_4_random_7 = [0.0, 0.04, 0.02]

def average_time_total_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_7():
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_7/timedout_percentage_chart.png")
    plt.close()

# Seed 3: 34

number_of_requests_random_34 = [50, 100, 500]

avg_time_total_algorithm_1_random_34 = [14595.0, 58628.0, 249483.0]
avg_time_total_algorithm_2_random_34 = [16367.0, 68849.0, 214793.0]
avg_time_total_algorithm_3_random_34 = [17349.0, 72169.0, 246215.0]
avg_time_total_algorithm_4_random_34 = [22635.0, 71911.0, 258507.0]

avg_time_broker_queue_algorithm_1_random_34 = [5.0, 5.0, 5.0]
avg_time_broker_queue_algorithm_2_random_34 = [7.0, 6.0, 5.0]
avg_time_broker_queue_algorithm_3_random_34 = [12561.0, 65679.0, 240209.0]
avg_time_broker_queue_algorithm_4_random_34 = [17503.0, 65537.0, 252059.0]

avg_time_worker_queue_algorithm_1_random_34 = [11257.0, 51868.0, 242520.0]
avg_time_worker_queue_algorithm_2_random_34 = [11919.0, 61709.0, 208655.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_34 = [274.0, 3475.0, 3543.0]
avg_time_olt_queue_algorithm_2_random_34 = [1387.0, 3861.0, 2724.0]
avg_time_olt_queue_algorithm_3_random_34 = [144.0, 828.0, 325.0]
avg_time_olt_queue_algorithm_4_random_34 = [4.0, 673.0, 702.0]

timedout_percentage_algorithm_1_random_34 = [0.02, 0.1, 0.1]
timedout_percentage_algorithm_2_random_34 = [0.04, 0.09, 0.072]
timedout_percentage_algorithm_3_random_34 = [0.02, 0.03, 0.01]
timedout_percentage_algorithm_4_random_34 = [0.02, 0.03, 0.02]

def average_time_total_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_34():
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_34/timedout_percentage_chart.png")
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
