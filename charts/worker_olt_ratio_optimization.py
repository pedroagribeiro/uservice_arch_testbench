import matplotlib.pyplot as plt

number_of_requests = [50, 100, 500, 1000, 2000]

# 5 workers; 10 olts => ratio = 0.5
# 5 workers; 10 olts => ratio = 1.0
# 10 workers; 5 olts => ratio = 2.0

# Seed 42
avg_time_total_seed_42_ratio_0_5 = [3451.0, 13077.0, 21670.0, 39786.0, 59386.0]
avg_time_total_seed_42_ratio_1_0 = [3834.0, 14299.0, 55295.0, 116800.0, 300992.0]
avg_time_total_seed_42_ratio_2_0 = [3697.0, 12006.0, 19863.0, 41622.0, 52385.0]
timedout_percentage_seed_42_ratio_0_5 = [0.0, 0.03, 0.036, 0.035, 0.051]
timedout_percentage_seed_42_ratio_1_0 = [0.0, 0.03, 0.064, 0.071, 0.102]
timedout_percentage_seed_42_ratio_2_0 = [0.0, 0.03, 0.058, 0.125, 0.087]

# Seed 7
avg_time_total_seed_7_ratio_0_5 = [3458.0, 12320.0, 20777.0, 32390.0, 50357.0]
avg_time_total_seed_7_ratio_1_0 = [4015.0, 15420.0, 56224.0, 133272.0, 290264.0]
avg_time_total_seed_7_ratio_2_0 = [3694.0, 11913.0, 19813.0, 30631.0, 72716.0]
timedout_percentage_seed_7_ratio_0_5 = [0.0, 0.03, 0.038, 0.044, 0.0515]
timedout_percentage_seed_7_ratio_1_0 = [0.0, 0.03, 0.084, 0.09, 0.0935]
timedout_percentage_seed_7_ratio_2_0 = [0.0, 0.03, 0.056, 0.071, 0.1445]

# Seed 34
avg_time_total_seed_34_ratio_0_5 = [4920.0, 11121.0, 28612.0, 32281.0, 27087.0]
avg_time_total_seed_34_ratio_1_0 = [8943.0, 19331.0, 64374.0, 127285.0, 266661.0]
avg_time_total_seed_34_ratio_2_0 = [5975.0, 15037.0, 22742.0, 39602.0, 44048.0]
timedout_percentage_seed_34_ratio_0_5 = [0.02, 0.04, 0.042, 0.04, 0.0415]
timedout_percentage_seed_34_ratio_1_0 = [0.04, 0.03, 0.098, 0.097, 0.085]
timedout_percentage_seed_34_ratio_2_0 = [0.02, 0.04, 0.066, 0.084, 0.0765]

def average_time_total_ratio_comparison():
    total_time_averages_array_0_5 = []
    total_time_average_array_1_0 = []
    total_time_average_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_0_5 = (avg_time_total_seed_42_ratio_0_5[i] + avg_time_total_seed_7_ratio_0_5[i] + avg_time_total_seed_34_ratio_0_5[i]) / 3
        avg_total_time_element_1_0 = (avg_time_total_seed_42_ratio_1_0[i] + avg_time_total_seed_7_ratio_1_0[i] + avg_time_total_seed_34_ratio_1_0[i]) / 3
        avg_total_time_element_2_0 = (avg_time_total_seed_42_ratio_2_0[i] + avg_time_total_seed_7_ratio_2_0[i] + avg_time_total_seed_34_ratio_2_0[i]) / 3
        total_time_averages_array_0_5.append(avg_total_time_element_0_5)
        total_time_average_array_1_0.append(avg_total_time_element_1_0)
        total_time_average_array_2_0.append(avg_total_time_element_2_0)
        # deviation calculus
        min_time = min([avg_total_time_element_0_5, avg_total_time_element_1_0, avg_total_time_element_2_0])
        deviation_0_5 = (avg_total_time_element_0_5 - min_time) / min_time
        deviation_1_0 = (avg_total_time_element_1_0 - min_time) / min_time
        deviation_2_0 = (avg_total_time_element_2_0 - min_time) / min_time
        percentual_deviation_0_5 += deviation_0_5
        percentual_deviation_1_0 += deviation_1_0
        percentual_deviation_2_0 += deviation_2_0
    plt.plot(number_of_requests, total_time_averages_array_0_5, label = "Ratio 0.5")
    plt.plot(number_of_requests, total_time_average_array_1_0, label = "Ratio 1.0")
    plt.plot(number_of_requests, total_time_average_array_2_0, label = "Ratio 2.0")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no ambiente de simulação (em ms)")
    plt.legend()
    plt.savefig("output/ratio_comparisons/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def timedout_percentage_ratio_comparison():
    timedout_percentage_averages_array_0_5 = []
    timedout_percentage_averages_array_1_0 = []
    timedout_percentage_averages_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_timedout_percentage_element_0_5 = (timedout_percentage_seed_42_ratio_0_5[i] + timedout_percentage_seed_7_ratio_0_5[i] + timedout_percentage_seed_34_ratio_0_5[i]) / 3
        avg_timedout_percentage_element_1_0 = (timedout_percentage_seed_42_ratio_1_0[i] + timedout_percentage_seed_7_ratio_1_0[i] + timedout_percentage_seed_34_ratio_1_0[i]) / 3
        avg_timedout_percentage_element_2_0 = (timedout_percentage_seed_42_ratio_2_0[i] + timedout_percentage_seed_7_ratio_2_0[i] + timedout_percentage_seed_34_ratio_2_0[i]) / 3
        timedout_percentage_averages_array_0_5.append(avg_timedout_percentage_element_0_5)
        timedout_percentage_averages_array_1_0.append(avg_timedout_percentage_element_1_0)
        timedout_percentage_averages_array_2_0.append(avg_timedout_percentage_element_2_0)
        # deviation calculus
        min_percentage = min([avg_timedout_percentage_element_0_5, avg_timedout_percentage_element_1_0, avg_timedout_percentage_element_2_0])
        deviation_0_5 = (avg_timedout_percentage_element_0_5 - min_percentage) / min_percentage
        deviation_1_0 = (avg_timedout_percentage_element_1_0 - min_percentage) / min_percentage
        deviation_2_0 = (avg_timedout_percentage_element_2_0 - min_percentage) / min_percentage
        percentual_deviation_0_5 += deviation_0_5
        percentual_deviation_1_0 += deviation_1_0
        percentual_deviation_2_0 += deviation_2_0
    plt.plot(number_of_requests, timedout_percentage_averages_array_0_5, label = "Ratio 0.5")
    plt.plot(number_of_requests, timedout_percentage_averages_array_1_0, label = "Ratio 1.0")
    plt.plot(number_of_requests, timedout_percentage_averages_array_2_0, label = "Ratio 2.0")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/ratio_comparisons/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

if __name__ == "__main__":
    tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison()
    toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison()
    percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
        print("Ratio 0.5 is optimal")
    if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
        print("Ratio 2.0 is optimal")