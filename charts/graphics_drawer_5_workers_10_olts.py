import matplotlib.pyplot as plt

# Seed 1: 42
number_of_requests_random_42 = [50, 100, 500, 1000, 2000]

# avg_time_total_algorithm_1_random_42 = [3451.0, 13077.0, 21670.0, 39786.0, 59386.0]
# avg_time_total_algorithm_2_random_42 = [5347.0, 17734.0, 23704.0, 59578.0, 55404.0]
# avg_time_total_algorithm_3_random_42 = [3217.0, 8341.0, 8353.0, 8777.0, 9358.0]
# avg_time_total_algorithm_4_random_42 = [3181.0, 8852.0, 8552.0, 8720.0, 9515.0]
avg_time_total_algorithm_1_random_42 = [3518.0, 12944.0, 18710.0, 30321.0, 66337.0]
avg_time_total_algorithm_2_random_42 = [4348.0, 16481.0, 26384.0, 58109.0, 78710.0 ]
avg_time_total_algorithm_3_random_42 = [3664.0, 13436.0, 23517.0, 58726.0, 51719.0]
avg_time_total_algorithm_4_random_42 = [4042.0, 14541.0, 20192.0, 39950.0, 68492.0]

# avg_time_broker_queue_algorithm_1_random_42 = [3.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_42 = [2.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_42 = [51.0, 1835.0, 2190.0, 2154.0, 2879.0]
# avg_time_broker_queue_algorithm_4_random_42 = [2.0, 2031.0, 2512.0, 2326.0, 2903.0]
avg_time_broker_queue_algorithm_1_random_42 = [66.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_42 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_42 = [382.0, 8055.0, 18113.0, 52513.0, 46237.0]
avg_time_broker_queue_algorithm_4_random_42 = [920.0, 9112.0, 14724.0, 33693.0, 62609.0]

# avg_time_worker_queue_algorithm_1_random_42 = [769.0, 8895.0, 17262.0, 35419.0, 54330.0]
# avg_time_worker_queue_algorithm_2_random_42 = [2233.0, 12213.0, 18442.0, 53992.0, 49911.0]
# avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_42 = [793.0, 8666.0, 14345.0, 25561.0, 61001.0]
avg_time_worker_queue_algorithm_2_random_42 = [1315.0, 10905.0, 20881.0, 52235.0, 73197.0]
avg_time_worker_queue_algorithm_3_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_42 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_42 = [24.0, 764.0, 988.0, 920.0, 1569.0]
# avg_time_olt_queue_algorithm_2_random_42 = [465.0, 2104.0, 1835.0, 2320.0, 2006.0]
# avg_time_olt_queue_algorithm_3_random_42 = [2.0, 3091.0, 2744.0, 3178.0, 2994.0]
# avg_time_olt_queue_algorithm_4_random_42 = [534.0, 3405.0, 2620.0, 2950.0, 3126.0]
avg_time_olt_queue_algorithm_1_random_42 = [6.0, 859.0, 944.0, 1313.0, 1847.0]
avg_time_olt_queue_algorithm_2_random_42 = [384.0, 2158.0, 2083.0, 2428.0, 2027.0]
avg_time_olt_queue_algorithm_3_random_42 = [2.0, 1966.0, 1983.0, 2769.0, 1997.0]
avg_time_olt_queue_algorithm_4_random_42 = [476.0, 2015.0, 2050.0, 2544.0, 2399.0]

# timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.036, 0.035, 0.051]
# timedout_percentage_algorithm_2_random_42 = [0.0, 0.06, 0.054, 0.072, 0.0605]
# timedout_percentage_algorithm_3_random_42 = [0.0, 0.07, 0.086, 0.089, 0.0925]
# timedout_percentage_algorithm_4_random_42 = [0.0, 0.08, 0.072, 0.079, 0.0885]
timedout_percentage_algorithm_1_random_42 = [0.0, 0.03, 0.04, 0.045, 0.058]
timedout_percentage_algorithm_2_random_42 = [0.0, 0.05, 0.06, 0.078, 0.0635]
timedout_percentage_algorithm_3_random_42 = [0.0, 0.05, 0.066, 0.076, 0.0625]
timedout_percentage_algorithm_4_random_42 = [0.0, 0.06, 0.06, 0.077, 0.0645]

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

avg_time_total_algorithm_1_random_7 = [3458.0, 12320.0, 20777.0, 32390.0, 50357.0]
avg_time_total_algorithm_2_random_7 = [5483.0, 17305.0, 26693.0, 65590.0, 66343.0]
avg_time_total_algorithm_3_random_7 = [3285.0, 8346.0, 8357.0, 8783.0, 9334.0]
avg_time_total_algorithm_4_random_7 = [3201.0, 8836.0, 8504.0, 8771.0, 9859.0]

avg_time_broker_queue_algorithm_1_random_7 = [3.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_7 = [2.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_7 = [52.0, 1839.0, 2192.0, 2158.0, 2850.0]
avg_time_broker_queue_algorithm_4_random_7 = [29.0, 2059.0, 2231.0, 2265.0, 3396.0]

avg_time_worker_queue_algorithm_1_random_7 = [775.0, 8283.0, 16214.0, 27925.0, 45352.0]
avg_time_worker_queue_algorithm_2_random_7 = [2363.0, 12135.0, 21491.0, 59518.0, 60606.0]
avg_time_worker_queue_algorithm_3_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_7 = [0.0, 0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1_random_7 = [25.0, 619.0, 1142.0, 1557.0, 1517.0]
avg_time_olt_queue_algorithm_2_random_7 = [471.0, 1752.0, 1781.0, 2625.0, 2249.0]
avg_time_olt_queue_algorithm_3_random_7 = [2.0, 3092.0, 2745.0, 3179.0, 2998.0]
avg_time_olt_queue_algorithm_4_random_7 = [526.0, 3362.0, 2853.0, 3061.0, 2977.0]

timedout_percentage_algorithm_1_random_7 = [0.0, 0.03, 0.038, 0.044, 0.0515]
timedout_percentage_algorithm_2_random_7 = [0.0, 0.05, 0.056, 0.069, 0.0685]
timedout_percentage_algorithm_3_random_7 = [0.0, 0.07, 0.086, 0.089, 0.092]
timedout_percentage_algorithm_4_random_7 = [0.0, 0.09, 0.088, 0.082, 0.0905]

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

# avg_time_total_algorithm_1_random_34 = [4920.0, 11121.0, 28612.0, 32281.0, 27087.0]
# avg_time_total_algorithm_2_random_34 = [6511.0, 17183.0, 36284.0, 49837.0, 51604.0]
# avg_time_total_algorithm_3_random_34 = [5273.0, 12658.0, 32046.0, 35710.0, 48649.0]
# avg_time_total_algorithm_4_random_34 = [4665.0, 16358.0, 33352.0, 33123.0, 44321.0]
avg_time_total_algorithm_1_random_34 = [4842.0, 11898.0, 27418.0, 35507.0, 35484.0]
avg_time_total_algorithm_2_random_34 = [9180.0, 17762.0, 35592.0, 48251.0, 56148.0]
avg_time_total_algorithm_3_random_34 = [5301.0, 12594.0, 31996.0, 45017.0, 46133.0]
avg_time_total_algorithm_4_random_34 = [5193.0, 13005.0, 42876.0, 34774.0, 34972.0]

# avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_2_random_34 = [2.0, 2.0, 1.0, 1.0, 1.0]
# avg_time_broker_queue_algorithm_3_random_34 = [1498.0, 7829.0, 26589.0, 30297.0, 42948.0]
# avg_time_broker_queue_algorithm_4_random_34 = [1124.0, 11278.0, 27574.0, 27867.0, 38697.0]
avg_time_broker_queue_algorithm_1_random_34 = [3.0, 1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2_random_34 = [3.0, 2.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_3_random_34 = [1517.0, 7777.0, 26451.0, 39633.0, 40405.0]
avg_time_broker_queue_algorithm_4_random_34 = [1564.0, 8117.0, 36864.0, 29498.0, 29476.0]

# avg_time_worker_queue_algorithm_1_random_34 = [1780.0, 7163.0, 24215.0, 27698.0, 22537.0]
# avg_time_worker_queue_algorithm_2_random_34 = [2848.0, 12170.0, 30832.0, 44508.0, 45949.0]
# avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
# avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_1_random_34 = [1511.0, 7799.0, 22715.0, 30594.0, 30680.0]
avg_time_worker_queue_algorithm_2_random_34 = [5038.0, 12688.0, 30216.0, 43119.0, 50497.0]
avg_time_worker_queue_algorithm_3_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4_random_34 = [0.0, 0.0, 0.0, 0.0, 0.0]

# avg_time_olt_queue_algorithm_1_random_34 = [80.0, 691.0, 997.0, 1100.0, 1076.0]
# avg_time_olt_queue_algorithm_2_random_34 = [613.0, 1745.0, 2052.0, 1846.0, 2181.0]
# avg_time_olt_queue_algorithm_3_random_34 = [2.0, 1564.0, 2059.0, 1932.0, 2229.0]
# avg_time_olt_queue_algorithm_4_random_34 = [495.0, 1816.0, 2380.0, 1775.0, 2153.0]
avg_time_olt_queue_algorithm_1_random_34 = [274.0, 831.0, 1302.0, 1430.0, 1329.0]
avg_time_olt_queue_algorithm_2_random_34 = [1092.0, 1806.0, 1976.0, 1648.0, 2178.0]
avg_time_olt_queue_algorithm_3_random_34 = [200.0, 1552.0, 2056.0, 1904.0, 2257.0]
avg_time_olt_queue_algorithm_4_random_34 = [584.0, 1623.0, 2614.0, 1795.0, 2024.0]

# timedout_percentage_algorithm_1_random_34 = [0.02, 0.04, 0.042, 0.04, 0.0415]
# timedout_percentage_algorithm_2_random_34 = [0.02, 0.06, 0.06, 0.056, 0.0665]
# timedout_percentage_algorithm_3_random_34 = [0.02, 0.04, 0.06, 0.06, 0.0665]
# timedout_percentage_algorithm_4_random_34 = [0.02, 0.05, 0.074, 0.054, 0.065]
timedout_percentage_algorithm_1_random_34 = [0.02, 0.04, 0.052, 0.052, 0.044]
timedout_percentage_algorithm_2_random_34 = [0.04, 0.05, 0.052, 0.047, 0.0645]
timedout_percentage_algorithm_3_random_34 = [0.02, 0.04, 0.058, 0.064, 0.067]
timedout_percentage_algorithm_4_random_34 = [0.02, 0.04, 0.074, 0.059, 0.06]

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
