from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_42 = [3834.0, 14299.0, 55295.0, 116800.0, 300992.0]
avg_time_total_algorithm_2_random_42 = [5644.0, 22598.0, 68332.0, 178580.0, 320601.0]
avg_time_total_algorithm_3_random_42 = [3714.0, 13516.0, 16704.0, 33141.0, 41546.0]
avg_time_total_algorithm_4_random_42 = [4015.0, 13027.0, 17219.0, 39115.0, 52048.0]

avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [28.0, 4915.0, 6498.0, 19137.0, 27447.0]
avg_time_broker_queue_algorithm_4_random_42 = [231.0, 4870.0, 6526.0, 25587.0, 37598.0]

avg_time_worker_queue_algorithm_1_random_42 = [1104.0, 10111.0, 49507.0, 110369.0, 293323.0]
avg_time_worker_queue_algorithm_2_random_42 = [2293.0, 16428.0, 60987.0, 170120.0, 312967.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_42 = [84.0, 771.0, 2367.0, 2985.0, 4182.0]
avg_time_olt_queue_algorithm_2_random_42 = [703.0, 2753.0, 3926.0, 5015.0, 4147.0]
avg_time_olt_queue_algorithm_3_random_42 = [1041.0, 5186.0, 6788.0, 10561.0, 10614.0]
avg_time_olt_queue_algorithm_4_random_42 = [1139.0, 4743.0, 7275.0, 10125.0, 10695.0]

timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.064, 0.071, 0.102]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.086, 0.113, 0.096]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.14, 0.144, 0.229, 0.239]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.13, 0.156, 0.24, 0.232]


def average_time_total_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_42():
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_5_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 2: 7

number_of_requests_random_7 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_7 = [21423.0, 54764.0, 215862.0, 485321.0, 930501.0]
avg_time_total_algorithm_2_random_7 = [30597.0, 63052.0, 252514.0, 521362.0, 968164.0]
avg_time_total_algorithm_3_random_7 = [24495.0, 65484.0, 266222.0, 601500.0, 1082109.0]
avg_time_total_algorithm_4_random_7 = [22886.0, 69561.0, 260116.0, 505470.0, 1018536.0]

avg_time_broker_queue_algorithm_1_random_7 = [6.0, 6.0, 5.0, 5.0, 5.0]
avg_time_broker_queue_algorithm_2_random_7 = [8.0, 6.0, 5.0, 5.0, 5.0]
avg_time_broker_queue_algorithm_3_random_7 = [19185.0, 58303.0, 259627.0, 594600.0, 1075652.0]
avg_time_broker_queue_algorithm_4_random_7 = [17805.0, 62113.0, 253773.0, 498216.0, 1011602.0]

avg_time_worker_queue_algorithm_1_random_7 = [17796.0, 47133.0, 209309.0, 478333.0, 923829.0]
avg_time_worker_queue_algorithm_2_random_7 = [25193.0, 54164.0, 245460.0, 514109.0, 960966.0]
avg_time_worker_queue_algorithm_3_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_7 = [480.0, 3864.0, 3026.0, 3469.0, 3140.0]
avg_time_olt_queue_algorithm_2_random_7 = [2262.0, 5127.0, 3529.0, 3739.0, 3670.0]
avg_time_olt_queue_algorithm_3_random_7 = [475.0, 1495.0, 364.0, 663.0, 484.0]
avg_time_olt_queue_algorithm_4_random_7 = [1949.0, 1189.0, 333.0, 3750.0, 3417.0]

timedout_percentage_algorithm_1_random_7 = [0.02, 0.11, 0.1, 0.092, 0.0855]
timedout_percentage_algorithm_2_random_7 = [0.04, 0.13, 0.096, 0.092, 0.081]
timedout_percentage_algorithm_3_random_7 = [0.0, 0.04, 0.022, 0.024, 0.016]
timedout_percentage_algorithm_4_random_7 = [0.0, 0.04, 0.02, 0.101, 0.089]

def average_time_total_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_total_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_7():
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_5_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 3: 34

number_of_requests_random_34 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_34 = [8943.0, 19331.0, 64374.0, 127285.0, 266661.0]
avg_time_total_algorithm_2_random_34 = [9599.0, 29328.0, 96757.0, 148901.0, 295588.0]
avg_time_total_algorithm_3_random_34 = [10156.0, 28603.0, 84829.0, 150557.0, 274801.0]
avg_time_total_algorithm_4_random_34 = [7804.0, 28768.0, 64262.0, 141516.0, 270607.0]

avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [5349.0, 19629.0, 76947.0, 142932.0, 267111.0]
avg_time_broker_queue_algorithm_4_random_34 = [3727.0, 18992.0, 57608.0, 134074.0, 263131.0]

avg_time_worker_queue_algorithm_1_random_34 = [5123.0, 15625.0, 57482.0, 120167.0, 259901.0]
avg_time_worker_queue_algorithm_2_random_34 = [5616.0, 19792.0, 88995.0, 141596.0, 287837.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_34 = [764.0, 438.0, 3491.0, 3635.0, 3286.0]
avg_time_olt_queue_algorithm_2_random_34 = [934.0, 6268.0, 4361.0, 3823.0, 4278.0]
avg_time_olt_queue_algorithm_3_random_34 = [2.0, 5709.0, 4484.0, 4145.0, 4219.0]
avg_time_olt_queue_algorithm_4_random_34 = [1032.0, 6511.0, 3255.0, 3961.0, 4004.0]

timedout_percentage_algorithm_1_random_34 = [0.04, 0.03, 0.098, 0.097, 0.085]
timedout_percentage_algorithm_2_random_34 = [0.02, 0.14, 0.102, 0.109, 0.108]
timedout_percentage_algorithm_3_random_34 = [0.00, 0.14, 0.12, 0.116, 0.1]
timedout_percentage_algorithm_4_random_34 = [0.02, 0.14, 0.08, 0.092, 0.105]

def average_time_total_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_34():
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_4_random_34, label = "Algoritmo 4")
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
    # average_time_total_chart_random_7()
    # average_time_broker_queue_chart_random_7()
    # average_time_worker_queue_chart_random_7()
    # average_time_olt_queue_chart_random_7()
    # timedout_percentage_chart_random_7()
    # Seed 3: 34
    average_time_total_chart_random_34()
    average_time_broker_queue_chart_random_34()
    average_time_worker_queue_chart_random_34()
    average_time_olt_queue_chart_random_34()
    timedout_percentage_chart_random_34()
