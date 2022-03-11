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
