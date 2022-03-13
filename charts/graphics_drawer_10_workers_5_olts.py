from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_42 = [3697.0, 12006.0, 19863.0, 41622.0, 52385.0]
avg_time_total_algorithm_2_random_42 = [4218.0, 13575.0, 26186.0, 51713.0, 75639.0]
avg_time_total_algorithm_3_random_42 = [3714.0, 13513.0, 16697.0, 31830.0, 46877.0]
avg_time_total_algorithm_4_random_42 = [3790.0, 13417.0, 17863.0, 35887.0, 56107.0]

avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [28.0, 4914.0, 6496.0, 17794.0, 32024.0]
avg_time_broker_queue_algorithm_4_random_42 = [84.0, 4851.0, 7349.0, 22216.0, 41761.0]

avg_time_worker_queue_algorithm_1_random_42 = [1050.0, 7555.0, 14073.0, 33492.0, 45219.0]
avg_time_worker_queue_algorithm_2_random_42 = [782.0, 5714.0, 15705.0, 38604.0, 61859.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_42 = [1.0, 1034.0, 2369.0, 4684.0, 3679.0]
avg_time_olt_queue_algorithm_2_random_42 = [789.0, 4443.0, 7060.0, 9663.0, 10296.0]
avg_time_olt_queue_algorithm_3_random_42 = [1041.0, 5184.0, 6783.0, 10593.0, 11368.0]
avg_time_olt_queue_algorithm_4_random_42 = [1061.0, 5152.0, 7096.0, 10228.0, 10861.0]

timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.058, 0.125, 0.087]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.11, 0.138, 0.23, 0.2315]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.14, 0.144, 0.227, 0.2385]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.13, 0.15, 0.24, 0.2365]

def average_time_total_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_total_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/10_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_broker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/10_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_worker_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/10_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_42():
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, avg_time_olt_queue_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_42/10_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_42():
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_1_random_42, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_2_random_42, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_3_random_42, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_42, timedout_percentage_algorithm_4_random_42, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_42/10_workers_5_olts/timedout_percentage_chart.png")
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
    plt.savefig("output/random_seed_7/10_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_broker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/10_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_worker_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/10_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_7():
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, avg_time_olt_queue_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_7/10_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_7():
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_1_random_7, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_2_random_7, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_3_random_7, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_7, timedout_percentage_algorithm_4_random_7, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_7/10_workers_5_olts/timedout_percentage_chart.png")
    plt.close()

# Seed 3: 34

number_of_requests_random_34 = [50, 100, 500, 1000, 2000]

avg_time_total_algorithm_1_random_34 = [5975.0, 15037.0, 22742.0, 39602.0, 44048.0]
avg_time_total_algorithm_2_random_34 = [9133.0, 21996.0, 41220.0, 40751.0, 57800.0]
avg_time_total_algorithm_3_random_34 = [5936.0, 16666.0, 30338.0, 32802.0, 52128.0]
avg_time_total_algorithm_4_random_34 = [6305.0, 18755.0, 29774.0, 34499.0, 46871.0]

avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [3.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [908.0, 3945.0, 17660.0, 21667.0, 38035.0]
avg_time_broker_queue_algorithm_4_random_34 = [791.0, 5137.0, 17442.0, 23294.0, 32518.0]

avg_time_worker_queue_algorithm_1_random_34 = [2675.0, 11067.0, 16483.0, 32925.0, 37414.0]
avg_time_worker_queue_algorithm_2_random_34 = [3379.0, 8929.0, 28850.0, 30432.0, 45522.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_34 = [239.0, 702.0, 2857.0, 3193.0, 3160.0]
avg_time_olt_queue_algorithm_2_random_34 = [2705.0, 9799.0, 8970.0, 6836.0, 8804.0]
avg_time_olt_queue_algorithm_3_random_34 = [359.0, 9456.0, 9280.0, 7655.0, 10621.0]
avg_time_olt_queue_algorithm_4_random_34 = [2468.0, 10353.0, 8954.0, 7723.0, 10882.0]

timedout_percentage_algorithm_1_random_34 = [0.02, 0.04, 0.066, 0.084, 0.0765]
timedout_percentage_algorithm_2_random_34 = [0.08, 0.18, 0.2, 0.171, 0.2225]
timedout_percentage_algorithm_3_random_34 = [0.02, 0.16, 0.224, 0.198, 0.253]
timedout_percentage_algorithm_4_random_34 = [0.04, 0.18, 0.214, 0.199, 0.253]

def average_time_total_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_total_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/10_workers_5_olts/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_broker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/10_workers_5_olts/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_worker_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/10_workers_5_olts/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart_random_34():
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, avg_time_olt_queue_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/random_seed_34/10_workers_5_olts/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart_random_34():
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_1_random_34, label = "Algoritmo 1")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_2_random_34, label = "Algoritmo 2")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_3_random_34, label = "Algoritmo 3")
    plt.plot(number_of_requests_random_34, timedout_percentage_algorithm_4_random_34, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/random_seed_34/10_workers_5_olts/timedout_percentage_chart.png")
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
