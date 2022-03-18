import matplotlib.pyplot as plt
from run_parser import results_file_parser

number_of_requests = [50, 100, 500, 1000, 2000]

run_results_42 = results_file_parser("results/random_seed_42/results_new_run.json") 
run_results_7 = results_file_parser("results/random_seed_7/results_new_run.json")
run_results_34 = results_file_parser("results/random_seed_34/results_new_run.json")

# 5 workers; 10 olts => ratio = 0.5
# 5 workers; 10 olts => ratio = 1.0
# 10 workers; 5 olts => ratio = 2.0

### ALGORITHM 1

# Seed 42

avg_time_total_seed_42_ratio_0_5_algorithm_1 = run_results_42['avg_time_total_algorithm_1'][0] 
avg_time_total_seed_42_ratio_1_0_algorithm_1 = run_results_42['avg_time_total_algorithm_1'][1]
avg_time_total_seed_42_ratio_2_0_algorithm_1 = run_results_42['avg_time_total_algorithm_1'][2] 
timedout_percentage_seed_42_ratio_0_5_algorithm_1 = run_results_42['timedout_percentage_algorithm_1'][0] 
timedout_percentage_seed_42_ratio_1_0_algorithm_1 = run_results_42['timedout_percentage_algorithm_1'][1] 
timedout_percentage_seed_42_ratio_2_0_algorithm_1 = run_results_42['timedout_percentage_algorithm_1'][2]

# Seed 7
avg_time_total_seed_7_ratio_0_5_algorithm_1 = run_results_7['avg_time_total_algorithm_1'][0] 
avg_time_total_seed_7_ratio_1_0_algorithm_1 = run_results_7['avg_time_total_algorithm_1'][1] 
avg_time_total_seed_7_ratio_2_0_algorithm_1 = run_results_7['avg_time_total_algorithm_1'][2] 
timedout_percentage_seed_7_ratio_0_5_algorithm_1 = run_results_7['timedout_percentage_algorithm_1'][0] 
timedout_percentage_seed_7_ratio_1_0_algorithm_1 = run_results_7['timedout_percentage_algorithm_1'][1] 
timedout_percentage_seed_7_ratio_2_0_algorithm_1 = run_results_7['timedout_percentage_algorithm_1'][2] 

# Seed 34
avg_time_total_seed_34_ratio_0_5_algorithm_1 = run_results_34['avg_time_total_algorithm_1'][0]
avg_time_total_seed_34_ratio_1_0_algorithm_1 = run_results_34['avg_time_total_algorithm_1'][1]
avg_time_total_seed_34_ratio_2_0_algorithm_1 = run_results_34['avg_time_total_algorithm_1'][2]  
timedout_percentage_seed_34_ratio_0_5_algorithm_1 = run_results_34['timedout_percentage_algorithm_1'][0] 
timedout_percentage_seed_34_ratio_1_0_algorithm_1 = run_results_34['timedout_percentage_algorithm_1'][1]
timedout_percentage_seed_34_ratio_2_0_algorithm_1 = run_results_34['timedout_percentage_algorithm_1'][2]

### ALGORITHM 2

# Seed 42

avg_time_total_seed_42_ratio_0_5_algorithm_2 = run_results_42['avg_time_total_algorithm_2'][0] 
avg_time_total_seed_42_ratio_1_0_algorithm_2 = run_results_42['avg_time_total_algorithm_2'][1]
avg_time_total_seed_42_ratio_2_0_algorithm_2 = run_results_42['avg_time_total_algorithm_2'][2] 
timedout_percentage_seed_42_ratio_0_5_algorithm_2 = run_results_42['timedout_percentage_algorithm_2'][0] 
timedout_percentage_seed_42_ratio_1_0_algorithm_2 = run_results_42['timedout_percentage_algorithm_2'][1] 
timedout_percentage_seed_42_ratio_2_0_algorithm_2 = run_results_42['timedout_percentage_algorithm_2'][2]

# Seed 7
avg_time_total_seed_7_ratio_0_5_algorithm_2 = run_results_7['avg_time_total_algorithm_2'][0] 
avg_time_total_seed_7_ratio_1_0_algorithm_2 = run_results_7['avg_time_total_algorithm_2'][1] 
avg_time_total_seed_7_ratio_2_0_algorithm_2 = run_results_7['avg_time_total_algorithm_2'][2] 
timedout_percentage_seed_7_ratio_0_5_algorithm_2 = run_results_7['timedout_percentage_algorithm_2'][0] 
timedout_percentage_seed_7_ratio_1_0_algorithm_2 = run_results_7['timedout_percentage_algorithm_2'][1] 
timedout_percentage_seed_7_ratio_2_0_algorithm_2 = run_results_7['timedout_percentage_algorithm_2'][2] 

# Seed 34
avg_time_total_seed_34_ratio_0_5_algorithm_2 = run_results_34['avg_time_total_algorithm_2'][0]
avg_time_total_seed_34_ratio_1_0_algorithm_2 = run_results_34['avg_time_total_algorithm_2'][1]
avg_time_total_seed_34_ratio_2_0_algorithm_2 = run_results_34['avg_time_total_algorithm_2'][2]  
timedout_percentage_seed_34_ratio_0_5_algorithm_2 = run_results_34['timedout_percentage_algorithm_2'][0] 
timedout_percentage_seed_34_ratio_1_0_algorithm_2 = run_results_34['timedout_percentage_algorithm_2'][1]
timedout_percentage_seed_34_ratio_2_0_algorithm_2 = run_results_34['timedout_percentage_algorithm_2'][2]

### ALGORITHM 3

# Seed 42

avg_time_total_seed_42_ratio_0_5_algorithm_3 = run_results_42['avg_time_total_algorithm_3'][0] 
avg_time_total_seed_42_ratio_1_0_algorithm_3 = run_results_42['avg_time_total_algorithm_3'][1]
avg_time_total_seed_42_ratio_2_0_algorithm_3 = run_results_42['avg_time_total_algorithm_3'][2] 
timedout_percentage_seed_42_ratio_0_5_algorithm_3 = run_results_42['timedout_percentage_algorithm_3'][0] 
timedout_percentage_seed_42_ratio_1_0_algorithm_3 = run_results_42['timedout_percentage_algorithm_3'][1] 
timedout_percentage_seed_42_ratio_2_0_algorithm_3 = run_results_42['timedout_percentage_algorithm_3'][2]

# Seed 7
avg_time_total_seed_7_ratio_0_5_algorithm_3 = run_results_7['avg_time_total_algorithm_3'][0] 
avg_time_total_seed_7_ratio_1_0_algorithm_3 = run_results_7['avg_time_total_algorithm_3'][1] 
avg_time_total_seed_7_ratio_2_0_algorithm_3 = run_results_7['avg_time_total_algorithm_3'][2] 
timedout_percentage_seed_7_ratio_0_5_algorithm_3 = run_results_7['timedout_percentage_algorithm_3'][0] 
timedout_percentage_seed_7_ratio_1_0_algorithm_3 = run_results_7['timedout_percentage_algorithm_3'][1] 
timedout_percentage_seed_7_ratio_2_0_algorithm_3 = run_results_7['timedout_percentage_algorithm_3'][2] 

# Seed 34
avg_time_total_seed_34_ratio_0_5_algorithm_3 = run_results_34['avg_time_total_algorithm_3'][0]
avg_time_total_seed_34_ratio_1_0_algorithm_3 = run_results_34['avg_time_total_algorithm_3'][1]
avg_time_total_seed_34_ratio_2_0_algorithm_3 = run_results_34['avg_time_total_algorithm_3'][2]  
timedout_percentage_seed_34_ratio_0_5_algorithm_3 = run_results_34['timedout_percentage_algorithm_3'][0] 
timedout_percentage_seed_34_ratio_1_0_algorithm_3 = run_results_34['timedout_percentage_algorithm_3'][1]
timedout_percentage_seed_34_ratio_2_0_algorithm_3 = run_results_34['timedout_percentage_algorithm_3'][2]

### ALGORITHM 4

# Seed 42

avg_time_total_seed_42_ratio_0_5_algorithm_4 = run_results_42['avg_time_total_algorithm_4'][0] 
avg_time_total_seed_42_ratio_1_0_algorithm_4 = run_results_42['avg_time_total_algorithm_4'][1]
avg_time_total_seed_42_ratio_2_0_algorithm_4 = run_results_42['avg_time_total_algorithm_4'][2] 
timedout_percentage_seed_42_ratio_0_5_algorithm_4 = run_results_42['timedout_percentage_algorithm_4'][0] 
timedout_percentage_seed_42_ratio_1_0_algorithm_4 = run_results_42['timedout_percentage_algorithm_4'][1] 
timedout_percentage_seed_42_ratio_2_0_algorithm_4 = run_results_42['timedout_percentage_algorithm_4'][2]

# Seed 7
avg_time_total_seed_7_ratio_0_5_algorithm_4 = run_results_7['avg_time_total_algorithm_4'][0] 
avg_time_total_seed_7_ratio_1_0_algorithm_4 = run_results_7['avg_time_total_algorithm_4'][1] 
avg_time_total_seed_7_ratio_2_0_algorithm_4 = run_results_7['avg_time_total_algorithm_4'][2] 
timedout_percentage_seed_7_ratio_0_5_algorithm_4 = run_results_7['timedout_percentage_algorithm_4'][0] 
timedout_percentage_seed_7_ratio_1_0_algorithm_4 = run_results_7['timedout_percentage_algorithm_4'][1] 
timedout_percentage_seed_7_ratio_2_0_algorithm_4 = run_results_7['timedout_percentage_algorithm_4'][2] 

# Seed 34
avg_time_total_seed_34_ratio_0_5_algorithm_4 = run_results_34['avg_time_total_algorithm_4'][0]
avg_time_total_seed_34_ratio_1_0_algorithm_4 = run_results_34['avg_time_total_algorithm_4'][1]
avg_time_total_seed_34_ratio_2_0_algorithm_4 = run_results_34['avg_time_total_algorithm_4'][2]  
timedout_percentage_seed_34_ratio_0_5_algorithm_4 = run_results_34['timedout_percentage_algorithm_4'][0] 
timedout_percentage_seed_34_ratio_1_0_algorithm_4 = run_results_34['timedout_percentage_algorithm_4'][1]
timedout_percentage_seed_34_ratio_2_0_algorithm_4 = run_results_34['timedout_percentage_algorithm_4'][2]




def average_time_total_ratio_comparison_algorithm_1():
    total_time_averages_array_0_5 = []
    total_time_average_array_1_0 = []
    total_time_average_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_0_5 = (avg_time_total_seed_42_ratio_0_5_algorithm_1[i] + avg_time_total_seed_7_ratio_0_5_algorithm_1[i] + avg_time_total_seed_34_ratio_0_5_algorithm_1[i]) / 3
        avg_total_time_element_1_0 = (avg_time_total_seed_42_ratio_1_0_algorithm_1[i] + avg_time_total_seed_7_ratio_1_0_algorithm_1[i] + avg_time_total_seed_34_ratio_1_0_algorithm_1[i]) / 3
        avg_total_time_element_2_0 = (avg_time_total_seed_42_ratio_2_0_algorithm_1[i] + avg_time_total_seed_7_ratio_2_0_algorithm_1[i] + avg_time_total_seed_34_ratio_2_0_algorithm_1[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_1/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def timedout_percentage_ratio_comparison_algorithm_1():
    timedout_percentage_averages_array_0_5 = []
    timedout_percentage_averages_array_1_0 = []
    timedout_percentage_averages_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_timedout_percentage_element_0_5 = (timedout_percentage_seed_42_ratio_0_5_algorithm_1[i] + timedout_percentage_seed_7_ratio_0_5_algorithm_1[i] + timedout_percentage_seed_34_ratio_0_5_algorithm_1[i]) / 3
        avg_timedout_percentage_element_1_0 = (timedout_percentage_seed_42_ratio_1_0_algorithm_1[i] + timedout_percentage_seed_7_ratio_1_0_algorithm_1[i] + timedout_percentage_seed_34_ratio_1_0_algorithm_1[i]) / 3
        avg_timedout_percentage_element_2_0 = (timedout_percentage_seed_42_ratio_2_0_algorithm_1[i] + timedout_percentage_seed_7_ratio_2_0_algorithm_1[i] + timedout_percentage_seed_34_ratio_2_0_algorithm_1[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_1/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def average_time_total_ratio_comparison_algorithm_2():
    total_time_averages_array_0_5 = []
    total_time_average_array_1_0 = []
    total_time_average_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_0_5 = (avg_time_total_seed_42_ratio_0_5_algorithm_2[i] + avg_time_total_seed_7_ratio_0_5_algorithm_2[i] + avg_time_total_seed_34_ratio_0_5_algorithm_2[i]) / 3
        avg_total_time_element_1_0 = (avg_time_total_seed_42_ratio_1_0_algorithm_2[i] + avg_time_total_seed_7_ratio_1_0_algorithm_2[i] + avg_time_total_seed_34_ratio_1_0_algorithm_2[i]) / 3
        avg_total_time_element_2_0 = (avg_time_total_seed_42_ratio_2_0_algorithm_2[i] + avg_time_total_seed_7_ratio_2_0_algorithm_2[i] + avg_time_total_seed_34_ratio_2_0_algorithm_2[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_2/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def timedout_percentage_ratio_comparison_algorithm_2():
    timedout_percentage_averages_array_0_5 = []
    timedout_percentage_averages_array_1_0 = []
    timedout_percentage_averages_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_timedout_percentage_element_0_5 = (timedout_percentage_seed_42_ratio_0_5_algorithm_2[i] + timedout_percentage_seed_7_ratio_0_5_algorithm_2[i] + timedout_percentage_seed_34_ratio_0_5_algorithm_2[i]) / 3
        avg_timedout_percentage_element_1_0 = (timedout_percentage_seed_42_ratio_1_0_algorithm_2[i] + timedout_percentage_seed_7_ratio_1_0_algorithm_2[i] + timedout_percentage_seed_34_ratio_1_0_algorithm_2[i]) / 3
        avg_timedout_percentage_element_2_0 = (timedout_percentage_seed_42_ratio_2_0_algorithm_2[i] + timedout_percentage_seed_7_ratio_2_0_algorithm_2[i] + timedout_percentage_seed_34_ratio_2_0_algorithm_2[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_2/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def average_time_total_ratio_comparison_algorithm_3():
    total_time_averages_array_0_5 = []
    total_time_average_array_1_0 = []
    total_time_average_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_0_5 = (avg_time_total_seed_42_ratio_0_5_algorithm_3[i] + avg_time_total_seed_7_ratio_0_5_algorithm_3[i] + avg_time_total_seed_34_ratio_0_5_algorithm_3[i]) / 3
        avg_total_time_element_1_0 = (avg_time_total_seed_42_ratio_1_0_algorithm_3[i] + avg_time_total_seed_7_ratio_1_0_algorithm_3[i] + avg_time_total_seed_34_ratio_1_0_algorithm_3[i]) / 3
        avg_total_time_element_2_0 = (avg_time_total_seed_42_ratio_2_0_algorithm_3[i] + avg_time_total_seed_7_ratio_2_0_algorithm_3[i] + avg_time_total_seed_34_ratio_2_0_algorithm_3[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_3/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def timedout_percentage_ratio_comparison_algorithm_3():
    timedout_percentage_averages_array_0_5 = []
    timedout_percentage_averages_array_1_0 = []
    timedout_percentage_averages_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_timedout_percentage_element_0_5 = (timedout_percentage_seed_42_ratio_0_5_algorithm_3[i] + timedout_percentage_seed_7_ratio_0_5_algorithm_3[i] + timedout_percentage_seed_34_ratio_0_5_algorithm_3[i]) / 3
        avg_timedout_percentage_element_1_0 = (timedout_percentage_seed_42_ratio_1_0_algorithm_3[i] + timedout_percentage_seed_7_ratio_1_0_algorithm_3[i] + timedout_percentage_seed_34_ratio_1_0_algorithm_3[i]) / 3
        avg_timedout_percentage_element_2_0 = (timedout_percentage_seed_42_ratio_2_0_algorithm_3[i] + timedout_percentage_seed_7_ratio_2_0_algorithm_3[i] + timedout_percentage_seed_34_ratio_2_0_algorithm_3[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_3/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def average_time_total_ratio_comparison_algorithm_4():
    total_time_averages_array_0_5 = []
    total_time_average_array_1_0 = []
    total_time_average_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_0_5 = (avg_time_total_seed_42_ratio_0_5_algorithm_4[i] + avg_time_total_seed_7_ratio_0_5_algorithm_4[i] + avg_time_total_seed_34_ratio_0_5_algorithm_4[i]) / 3
        avg_total_time_element_1_0 = (avg_time_total_seed_42_ratio_1_0_algorithm_4[i] + avg_time_total_seed_7_ratio_1_0_algorithm_4[i] + avg_time_total_seed_34_ratio_1_0_algorithm_4[i]) / 3
        avg_total_time_element_2_0 = (avg_time_total_seed_42_ratio_2_0_algorithm_4[i] + avg_time_total_seed_7_ratio_2_0_algorithm_4[i] + avg_time_total_seed_34_ratio_2_0_algorithm_4[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_4/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

def timedout_percentage_ratio_comparison_algorithm_4():
    timedout_percentage_averages_array_0_5 = []
    timedout_percentage_averages_array_1_0 = []
    timedout_percentage_averages_array_2_0 = []
    percentual_deviation_0_5 = 0
    percentual_deviation_1_0 = 0
    percentual_deviation_2_0 = 0
    for i in range(0, len(number_of_requests)):
        avg_timedout_percentage_element_0_5 = (timedout_percentage_seed_42_ratio_0_5_algorithm_4[i] + timedout_percentage_seed_7_ratio_0_5_algorithm_4[i] + timedout_percentage_seed_34_ratio_0_5_algorithm_4[i]) / 3
        avg_timedout_percentage_element_1_0 = (timedout_percentage_seed_42_ratio_1_0_algorithm_4[i] + timedout_percentage_seed_7_ratio_1_0_algorithm_4[i] + timedout_percentage_seed_34_ratio_1_0_algorithm_4[i]) / 3
        avg_timedout_percentage_element_2_0 = (timedout_percentage_seed_42_ratio_2_0_algorithm_4[i] + timedout_percentage_seed_7_ratio_2_0_algorithm_4[i] + timedout_percentage_seed_34_ratio_2_0_algorithm_4[i]) / 3
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
    plt.savefig("output/ratio_comparisons/algorithm_4/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_0_5, percentual_deviation_1_0, percentual_deviation_2_0

if __name__ == "__main__":
    # Algorithm 1
    tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_1()
    toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_1()
    percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    print("For ALGORITHM 1:")
    print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
        print("Ratio 0.5 is optimal")
    if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
        print("Ratio 2.0 is optimal")
    print('\n')

    # Algorithm 2
    tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_2()
    toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_2()
    percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    print("For ALGORITHM 2:")
    print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
        print("Ratio 0.5 is optimal")
    if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
        print("Ratio 2.0 is optimal")
    print('\n')

    # Algorithm 1
    tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_3()
    toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_3()
    percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    print("For ALGORITHM 3:")
    print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
        print("Ratio 0.5 is optimal")
    if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
        print("Ratio 2.0 is optimal")
    print('\n')

    # Algorithm 4
    tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_4()
    toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_4()
    percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    print("For ALGORITHM 4:")
    print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
        print("Ratio 0.5 is optimal")
    if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
        print("Ratio 2.0 is optimal")
    print('\n')