from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_42 = [3451.0, 13077.0, 21670.0, 39786.0, 59386.0]
avg_time_total_algorithm_2_random_42 = [5347.0, 17734.0, 23704.0, 59578.0, 55404.0]
avg_time_total_algorithm_3_random_42 = [3217.0, 8341.0, 8353.0, 8777.0, 9358.0]
avg_time_total_algorithm_4_random_42 = [3181.0, 8852.0, 8552.0, 8720.0, 9515.0]

avg_time_broker_queue_algorithm_1_random_42 = [3.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [51.0, 1835.0, 2190.0, 2154.0, 2879.0]
avg_time_broker_queue_algorithm_4_random_42 = [2.0, 2031.0, 2512.0, 2326.0, 2903.0]

avg_time_worker_queue_algorithm_1_random_42 = [769.0, 8895.0, 17262.0, 35419.0, 54330.0]
avg_time_worker_queue_algorithm_2_random_42 = [2233.0, 12213.0, 18442.0, 53992.0, 49911.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_42 = [24.0, 764.0, 988.0, 920.0, 1569.0]
avg_time_olt_queue_algorithm_2_random_42 = [465.0, 2104.0, 1835.0, 2320.0, 2006.0]
avg_time_olt_queue_algorithm_3_random_42 = [2.0, 3091.0, 2744.0, 3178.0, 2994.0]
avg_time_olt_queue_algorithm_4_random_42 = [534.0, 3405.0, 2620.0, 2950.0, 3126.0]

timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.036, 0.035, 0.051]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.054, 0.072, 0.0605]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.07, 0.086, 0.089, 0.0925]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.08, 0.072, 0.079, 0.0885]

def average_time_total_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_10_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_10_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_10_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_10_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_42():
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_42/5_workers_10_olts/timedout_percentage_chart.png")
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
    plt.savefig("output/random_seed_7/5_workers_10_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_10_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_10_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_10_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_7():
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_7/5_workers_10_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 3: 34

number_of_requests_random_34 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_34 = [4920.0, 11121.0, 28612.0, 32281.0, 27087.0]
avg_time_total_algorithm_2_random_34 = [6511.0, 17183.0, 36284.0, 49837.0, 51604.0]
avg_time_total_algorithm_3_random_34 = [5273.0, 12658.0, 32046.0, 35710.0, 48649.0]
avg_time_total_algorithm_4_random_34 = [4665.0, 16358.0, 33352.0, 33123.0, 44321.0]

avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [1498.0, 7829.0, 26589.0, 30297.0, 42948.0]
avg_time_broker_queue_algorithm_4_random_34 = [1124.0, 11278.0, 27574.0, 27867.0, 38697.0]

avg_time_worker_queue_algorithm_1_random_34 = [1780.0, 7163.0, 24215.0, 27698.0, 22537.0]
avg_time_worker_queue_algorithm_2_random_34 = [2848.0, 12170.0, 30832.0, 44508.0, 45949.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_34 = [80.0, 691.0, 997.0, 1100.0, 1076.0]
avg_time_olt_queue_algorithm_2_random_34 = [613.0, 1745.0, 2052.0, 1846.0, 2181.0]
avg_time_olt_queue_algorithm_3_random_34 = [2.0, 1564.0, 2059.0, 1932.0, 2229.0]
avg_time_olt_queue_algorithm_4_random_34 = [495.0, 1816.0, 2380.0, 1775.0, 2153.0]

timedout_percentage_algorithm_1_random_34 = [0.02, 0.04, 0.042, 0.04, 0.0415]
timedout_percentage_algorithm_2_random_34 = [0.02, 0.06, 0.06, 0.056, 0.0665]
timedout_percentage_algorithm_3_random_34 = [0.02, 0.04, 0.06, 0.06, 0.0665]
timedout_percentage_algorithm_4_random_34 = [0.02, 0.05, 0.074, 0.054, 0.065]

def average_time_total_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_10_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_10_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_10_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_10_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_34():
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_34/5_workers_10_olts/timedout_percentage_chart.png")
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
