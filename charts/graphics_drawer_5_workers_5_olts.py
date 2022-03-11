from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_42 = [14149.0, 65094.0, 248557.0, 499626.0, 984517.0]
avg_time_total_algorithm_2_random_42 = [18498.0, 48789.0, 233915.0, 485337.0, 990788.0]
avg_time_total_algorithm_3_random_42 = [14707.0, 47254.0, 497120.0, 556622.0, 1065379.0]
avg_time_total_algorithm_4_random_42 = [14728.0, 46161.0, 236146.0, 521052.0, 1140963.0]

avg_time_broker_queue_algorithm_1_random_42 = [7.0, 7.0, 5.0, 5.0, 4.0]
avg_time_broker_queue_algorithm_2_random_42 = [7.0, 7.0, 5.0, 4.0, 4.0]
avg_time_broker_queue_algorithm_3_random_42 = [10738.0, 41544.0, 497120.0, 550147.0, 1059004.0]
avg_time_broker_queue_algorithm_4_random_42 = [10752.0, 40643.0, 230106.0, 513865.0, 1134370.0]

avg_time_worker_queue_algorithm_1_random_42 = [10452.0, 57635.0, 240176.0, 492509.0, 977886.0]
avg_time_worker_queue_algorithm_2_random_42 = [14366.0, 42590.0, 227496.0, 478119.0, 983877.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_42 = [1027.0, 3639.0, 3778.0, 3652.0, 3125.0]
avg_time_olt_queue_algorithm_2_random_42 = [1471.0, 2888.0, 2814.0, 3758.0, 3411.0]
avg_time_olt_queue_algorithm_3_random_42 = [103.0, 345.0, 400.0, 488.0, 577.0]
avg_time_olt_queue_algorithm_4_random_42 = [4.0, 199.0, 227.0, 3738.0, 477.0]

timedout_percentage_algorithm_1_random_42 = [0.0, 0.06, 0.076, 0.095, 0.0815]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.064, 0.093, 0.091]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.02, 0.018, 0.016, 0.0195]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.02, 0.016, 0.092, 0.019]

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
    # average_time_total_chart_random_42()
    # average_time_broker_queue_chart_random_42()
    # average_time_worker_queue_chart_random_42()
    # average_time_olt_queue_chart_random_42()
    # timedout_percentage_chart_random_42()
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
