import matplotlib.pyplot as plt

# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

# avg_time_total_algorithm_1_random_42 = [3834.0, 14299.0, 55295.0, 116800.0, 300992.0]
# avg_time_total_algorithm_2_random_42 = [5644.0, 22598.0, 68332.0, 178580.0, 320601.0]
# avg_time_total_algorithm_3_random_42 = [3714.0, 13516.0, 16704.0, 33141.0, 41546.0]
# avg_time_total_algorithm_4_random_42 = [4015.0, 13027.0, 17219.0, 39115.0, 52048.0]
avg_time_total_algorithm_1_random_42 = [3838.0, 14062.0, 53571.0, 124913.0, 310665.0]
avg_time_total_algorithm_2_random_42 = [7321.0, 21159.0, 67033.0, 150473.0, 358387.0]
avg_time_total_algorithm_3_random_42 = [4324.0, 19001.0, 66594.0, 173378.0, 306528.0]
avg_time_total_algorithm_4_random_42 = [5270.0, 17887.0, 71171.0, 162653.0, 314520.0]

# avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_42 = [2.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_42 = [28.0, 4915.0, 6498.0, 19137.0, 27447.0]
# avg_time_broker_queue_algorithm_4_random_42 = [231.0, 4870.0, 6526.0, 25587.0, 37598.0]
avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [669.0, 12606.0, 59156.0, 165841.0, 298950.0]
avg_time_broker_queue_algorithm_4_random_42 = [1551.0, 11640.0, 63873.0, 154562.0, 306639.0]

# avg_time_worker_queue_algorithm_1_random_42 = [1104.0, 10111.0, 49507.0, 110369.0, 293323.0]
# avg_time_worker_queue_algorithm_2_random_42 = [2293.0, 16428.0, 60987.0, 170120.0, 312967.0]
# avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_42 = [1106.0, 9589.0, 47522.0, 117988.0, 302695.0]
avg_time_worker_queue_algorithm_2_random_42 = [4057.0, 14932.0, 60039.0, 143377.0, 350382.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_42 = [84.0, 771.0, 2367.0, 2985.0, 4182.0]
# avg_time_olt_queue_algorithm_2_random_42 = [703.0, 2753.0, 3926.0, 5015.0, 4147.0]
# avg_time_olt_queue_algorithm_3_random_42 = [1041.0, 5186.0, 6788.0, 10561.0, 10614.0]
# avg_time_olt_queue_algorithm_4_random_42 = [1139.0, 4743.0, 7275.0, 10125.0, 10695.0]
avg_time_olt_queue_algorithm_1_random_42 = [86.0, 1057.0, 2629.0, 3478.0, 4484.0]
avg_time_olt_queue_algorithm_2_random_42 = [618.0, 2811.0, 3574.0, 3650.0, 4518.0]
avg_time_olt_queue_algorithm_3_random_42 = [1011.0, 2981.0, 4020.0, 4454.0, 4093.0]
avg_time_olt_queue_algorithm_4_random_42 = [1074.0, 2833.0, 3881.0, 4647.0, 4397.0]

# timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.064, 0.071, 0.102]
# timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.086, 0.113, 0.096]
# timedout_percentage_algorithm_3_random_42 = [0.0, 0.14, 0.144, 0.229, 0.239]
# timedout_percentage_algorithm_4_random_42 = [0.0, 0.13, 0.156, 0.24, 0.232]
timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.062, 0.086, 0.1075]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.086, 0.085, 0.1225]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.07, 0.092, 0.11, 0.107]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.07, 0.102, 0.113, 0.109]

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

avg_time_total_algorithm_1_random_7 = [4015.0, 15420.0, 56224.0, 133272.0, 290264.0]
avg_time_total_algorithm_2_random_7 = [5318.0, 22574.0, 76550.0, 156212.0, 310249.0]
avg_time_total_algorithm_3_random_7 = [3715.0, 13517.0, 16808.0, 32052.0, 46211.0]
avg_time_total_algorithm_4_random_7 = [3931.0, 13205.0, 17090.0, 45973.0, 47125.0]

avg_time_broker_queue_algorithm_1_random_7 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_7 = [2.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_7 = [28.0, 4917.0, 6576.0, 17905.0, 32560.0]
avg_time_broker_queue_algorithm_4_random_7 = [135.0, 4359.0, 6518.0, 32418.0, 34058.0]

avg_time_worker_queue_algorithm_1_random_7 = [1268.0, 11172.0, 49453.0, 126372.0, 283083.0]
avg_time_worker_queue_algorithm_2_random_7 = [2073.0, 16208.0, 69078.0, 148257.0, 302017.0]
avg_time_worker_queue_algorithm_3_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_7 = [101.0, 831.0, 3352.0, 3454.0, 4054.0]
avg_time_olt_queue_algorithm_2_random_7 = [599.0, 2950.0, 4054.0, 4510.0, 4744.0]
avg_time_olt_queue_algorithm_3_random_7 = [1042.0, 5186.0, 6814.0, 10703.0, 10165.0]
avg_time_olt_queue_algorithm_4_random_7 = [1152.0, 5432.0, 7155.0, 10111.0, 9582.0]

timedout_percentage_algorithm_1_random_7 = [0.0, 0.03, 0.084, 0.09, 0.0935]
timedout_percentage_algorithm_2_random_7 = [0.0, 0.07, 0.092, 0.108, 0.1155]
timedout_percentage_algorithm_3_random_7 = [0.0, 0.14, 0.144, 0.231, 0.2305]
timedout_percentage_algorithm_4_random_7 = [0.0, 0.15, 0.152, 0.244, 0.213]

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

# avg_time_total_algorithm_1_random_34 = [8943.0, 19331.0, 64374.0, 127285.0, 266661.0]
# avg_time_total_algorithm_2_random_34 = [9599.0, 29328.0, 96757.0, 148901.0, 295588.0]
# avg_time_total_algorithm_3_random_34 = [10156.0, 28603.0, 84829.0, 150557.0, 274801.0]
# avg_time_total_algorithm_4_random_34 = [7804.0, 28768.0, 64262.0, 141516.0, 270607.0]
avg_time_total_algorithm_1_random_34 = [8907.0, 20321.0, 88059.0, 128813.0, 243105.0]
avg_time_total_algorithm_2_random_34 = [11911.0, 36520.0, 97426.0, 143386.0, 291058.0]
avg_time_total_algorithm_3_random_34 = [7689.0, 28554.0, 84630.0, 136431.0, 276136.0]
avg_time_total_algorithm_4_random_34 = [8878.0, 27681.0, 74263.0, 127845.0, 276625.0]

# avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_34 = [2.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_34 = [5349.0, 19629.0, 76947.0, 142932.0, 267111.0]
# avg_time_broker_queue_algorithm_4_random_34 = [3727.0, 18992.0, 57608.0, 134074.0, 263131.0]
avg_time_broker_queue_algorithm_1_random_34 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [3241.0, 19569.0, 76476.0, 128937.0, 268763.0]
avg_time_broker_queue_algorithm_4_random_34 = [4334.0, 19656.0, 67038.0, 120507.0, 269165.0]

# avg_time_worker_queue_algorithm_1_random_34 = [5123.0, 15625.0, 57482.0, 120167.0, 259901.0]
# avg_time_worker_queue_algorithm_2_random_34 = [5616.0, 19792.0, 88995.0, 141596.0, 287837.0]
# avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_34 = [5102.0, 16668.0, 80452.0, 122160.0, 236288.0]
avg_time_worker_queue_algorithm_2_random_34 = [7235.0, 25908.0, 90136.0, 136291.0, 283501.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_34 = [764.0, 438.0, 3491.0, 3635.0, 3286.0]
# avg_time_olt_queue_algorithm_2_random_34 = [934.0, 6268.0, 4361.0, 3823.0, 4278.0]
# avg_time_olt_queue_algorithm_3_random_34 = [2.0, 5709.0, 4484.0, 4145.0, 4219.0]
# avg_time_olt_queue_algorithm_4_random_34 = [1032.0, 6511.0, 3255.0, 3961.0, 4004.0]
avg_time_olt_queue_algorithm_1_random_34 = [759.0, 386.0, 4207.0, 3170.0, 3343.0]
avg_time_olt_queue_algorithm_2_random_34 = [1629.0, 7345.0, 3890.0, 3613.0, 4083.0]
avg_time_olt_queue_algorithm_3_random_34 = [1404.0, 5721.0, 4486.0, 4013.0, 3991.0]
avg_time_olt_queue_algorithm_4_random_34 = [1501.0, 4761.0, 3827.0, 3858.0, 3989.0]

# timedout_percentage_algorithm_1_random_34 = [0.04, 0.03, 0.098, 0.097, 0.085]
# timedout_percentage_algorithm_2_random_34 = [0.02, 0.14, 0.102, 0.109, 0.108]
# timedout_percentage_algorithm_3_random_34 = [0.00, 0.14, 0.12, 0.116, 0.1]
# timedout_percentage_algorithm_4_random_34 = [0.02, 0.14, 0.08, 0.092, 0.105]
timedout_percentage_algorithm_1_random_34 = [0.04, 0.03, 0.106, 0.08, 0.0885]
timedout_percentage_algorithm_2_random_34 = [0.06, 0.17, 0.092, 0.081, 0.1085]
timedout_percentage_algorithm_3_random_34 = [0.02, 0.14, 0.118, 0.106, 0.1]
timedout_percentage_algorithm_4_random_34 = [0.02, 0.13, 0.1, 0.094, 0.1045]

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
