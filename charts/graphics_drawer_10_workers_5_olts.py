import matplotlib.pyplot as plt

# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

# avg_time_total_algorithm_1_random_42 = [3697.0, 12006.0, 19863.0, 41622.0, 52385.0]
# avg_time_total_algorithm_2_random_42 = [4218.0, 13575.0, 26186.0, 51713.0, 75639.0]
# avg_time_total_algorithm_3_random_42 = [3714.0, 13513.0, 16697.0, 31830.0, 46877.0]
# avg_time_total_algorithm_4_random_42 = [3790.0, 13417.0, 17863.0, 35887.0, 56107.0]
avg_time_total_algorithm_1_random_42 = [3700.0, 11901.0, 16889.0, 31579.0, 62130.0]
avg_time_total_algorithm_2_random_42 = [3711.0, 13685.0, 19802.0, 39012.0, 72449.0]
avg_time_total_algorithm_3_random_42 = [3715.0, 13511.0, 16694.0, 33506.0, 45959.0]
avg_time_total_algorithm_4_random_42 = [3795.0, 13148.0, 17856.0, 36008.0, 45187.0]

# avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_42 = [2.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_42 = [28.0, 4914.0, 6496.0, 17794.0, 32024.0]
# avg_time_broker_queue_algorithm_4_random_42 = [84.0, 4851.0, 7349.0, 22216.0, 41761.0]
avg_time_broker_queue_algorithm_1_random_42 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [28.0, 4914.0, 6496.0, 20022.0, 30759.0]
avg_time_broker_queue_algorithm_4_random_42 = [179.0, 4795.0, 6872.0, 23457.0, 30338.0]

# avg_time_worker_queue_algorithm_1_random_42 = [1050.0, 7555.0, 14073.0, 33492.0, 45219.0]
# avg_time_worker_queue_algorithm_2_random_42 = [782.0, 5714.0, 15705.0, 38604.0, 61859.0]
# avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_42 = [1049.0, 7655.0, 11342.0, 24807.0, 54054.0]
avg_time_worker_queue_algorithm_2_random_42 = [283.0, 6543.0, 11276.0, 27834.0, 58999.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_42 = [1.0, 1034.0, 2369.0, 4684.0, 3679.0]
# avg_time_olt_queue_algorithm_2_random_42 = [789.0, 4443.0, 7060.0, 9663.0, 10296.0]
# avg_time_olt_queue_algorithm_3_random_42 = [1041.0, 5184.0, 6783.0, 10593.0, 11368.0]
# avg_time_olt_queue_algorithm_4_random_42 = [1061.0, 5152.0, 7096.0, 10228.0, 10861.0]
avg_time_olt_queue_algorithm_1_random_42 = [1.0, 830.0, 2126.0, 3327.0, 4598.0]
avg_time_olt_queue_algorithm_2_random_42 = [781.0, 3726.0, 5106.0, 7732.0, 9964.0]
avg_time_olt_queue_algorithm_3_random_42 = [1007.0, 5182.0, 6780.0, 10040.0, 11715.0]
avg_time_olt_queue_algorithm_4_random_42 = [972.0, 4939.0, 7566.0, 9188.0, 11364.0]

# timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.058, 0.125, 0.087]
# timedout_percentage_algorithm_2_random_42 = [0.0, 0.11, 0.138, 0.23, 0.2315]
# timedout_percentage_algorithm_3_random_42 = [0.0, 0.14, 0.144, 0.227, 0.2385]
# timedout_percentage_algorithm_4_random_42 = [0.0, 0.13, 0.15, 0.24, 0.2365]
timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.046, 0.073, 0.119]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.08, 0.11, 0.179, 0.2125]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.14, 0.144, 0.223, 0.2545]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.13, 0.162, 0.229, 0.23]

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

avg_time_total_algorithm_1_random_7 = [3694.0, 11913.0, 19813.0, 30631.0, 72716.0]
avg_time_total_algorithm_2_random_7 = [4728.0, 14711.0, 27389.0, 41629.0, 72008.0]
avg_time_total_algorithm_3_random_7 = [3715.0, 13518.0, 16717.0, 32026.0, 45434.0]
avg_time_total_algorithm_4_random_7 = [3968.0, 13046.0, 17409.0, 38874.0, 60471.0]

avg_time_broker_queue_algorithm_1_random_7 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_7 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_7 = [28.0, 4917.0, 6513.0, 18581.0, 31029.0]
avg_time_broker_queue_algorithm_4_random_7 = [220.0, 4454.0, 6777.0, 25823.0, 44662.0]

avg_time_worker_queue_algorithm_1_random_7 = [1046.0, 7655.0, 14421.0, 23910.0, 63410.0]
avg_time_worker_queue_algorithm_2_random_7 = [802.0, 6351.0, 17623.0, 28918.0, 57571.0]
avg_time_worker_queue_algorithm_3_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_7 = [1.0, 832.0, 1970.0, 3273.0, 5817.0]
avg_time_olt_queue_algorithm_2_random_7 = [1279.0, 4943.0, 6344.0, 9263.0, 10949.0]
avg_time_olt_queue_algorithm_3_random_7 = [1042.0, 5185.0, 6785.0, 9999.0, 10921.0]
avg_time_olt_queue_algorithm_4_random_7 = [1103.0, 5176.0, 7212.0, 9605.0, 12324.0]

timedout_percentage_algorithm_1_random_7 = [0.0, 0.03, 0.056, 0.071, 0.1445]
timedout_percentage_algorithm_2_random_7 = [0.0, 0.15, 0.134, 0.195, 0.244]
timedout_percentage_algorithm_3_random_7 = [0.0, 0.14, 0.144, 0.228, 0.2405]
timedout_percentage_algorithm_4_random_7 = [0.0, 0.15, 0.158, 0.257, 0.2495]

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

# avg_time_total_algorithm_1_random_34 = [5975.0, 15037.0, 22742.0, 39602.0, 44048.0]
# avg_time_total_algorithm_2_random_34 = [9133.0, 21996.0, 41220.0, 40751.0, 57800.0]
# avg_time_total_algorithm_3_random_34 = [5936.0, 16666.0, 30338.0, 32802.0, 52128.0]
# avg_time_total_algorithm_4_random_34 = [6305.0, 18755.0, 29774.0, 34499.0, 46871.0]
avg_time_total_algorithm_1_random_34 = [5939.0, 15868.0, 24680.0, 32576.0, 46682.0]
avg_time_total_algorithm_2_random_34 = [7532.0, 17830.0, 42723.0, 45858.0, 64308.0]
avg_time_total_algorithm_3_random_34 = [5863.0, 16665.0, 26936.0, 35515.0, 51870.0]
avg_time_total_algorithm_4_random_34 = [6130.0, 16681.0, 25803.0, 37869.0, 51526.0]

# avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_34 = [3.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_34 = [908.0, 3945.0, 17660.0, 21667.0, 38035.0]
# avg_time_broker_queue_algorithm_4_random_34 = [791.0, 5137.0, 17442.0, 23294.0, 32518.0]
avg_time_broker_queue_algorithm_1_random_34 = [1.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [2.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [741.0, 3942.0, 14853.0, 23888.0, 37883.0]
avg_time_broker_queue_algorithm_4_random_34 = [660.0, 4507.0, 12404.0, 27008.0, 38074.0]

# avg_time_worker_queue_algorithm_1_random_34 = [2675.0, 11067.0, 16483.0, 32925.0, 37414.0]
# avg_time_worker_queue_algorithm_2_random_34 = [3379.0, 8929.0, 28850.0, 30432.0, 45522.0]
# avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_34 = [2649.0, 11471.0, 17546.0, 25677.0, 37284.0]
avg_time_worker_queue_algorithm_2_random_34 = [2124.0, 6807.0, 29354.0, 34899.0, 52091.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_34 = [239.0, 702.0, 2857.0, 3193.0, 3160.0]
# avg_time_olt_queue_algorithm_2_random_34 = [2705.0, 9799.0, 8970.0, 6836.0, 8804.0]
# avg_time_olt_queue_algorithm_3_random_34 = [359.0, 9456.0, 9280.0, 7655.0, 10621.0]
# avg_time_olt_queue_algorithm_4_random_34 = [2468.0, 10353.0, 8954.0, 7723.0, 10882.0]
avg_time_olt_queue_algorithm_1_random_34 = [239.0, 1131.0, 3734.0, 3416.0, 5924.0]
avg_time_olt_queue_algorithm_2_random_34 = [2360.0, 7757.0, 9969.0, 7476.0, 8743.0]
avg_time_olt_queue_algorithm_3_random_34 = [1888.0, 9459.0, 8685.0, 8146.0, 10515.0]
avg_time_olt_queue_algorithm_4_random_34 = [2425.0, 8910.0, 10000.0, 7380.0, 9981]

# timedout_percentage_algorithm_1_random_34 = [0.02, 0.04, 0.066, 0.084, 0.0765]
# timedout_percentage_algorithm_2_random_34 = [0.08, 0.18, 0.2, 0.171, 0.2225]
# timedout_percentage_algorithm_3_random_34 = [0.02, 0.16, 0.224, 0.198, 0.253]
# timedout_percentage_algorithm_4_random_34 = [0.04, 0.18, 0.214, 0.199, 0.253]
timedout_percentage_algorithm_1_random_34 = [0.02, 0.05, 0.106, 0.085, 0.157]
timedout_percentage_algorithm_2_random_34 = [0.04, 0.16, 0.196, 0.183, 0.2157]
timedout_percentage_algorithm_3_random_34 = [0.04, 0.16, 0.192, 0.205, 0.261]
timedout_percentage_algorithm_4_random_34 = [0.04, 0.16, 0.212, 0.187, 0.237]

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
