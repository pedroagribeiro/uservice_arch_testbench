from lib2to3.pgen2.token import PERCENTEQUAL
import matplotlib.pyplot as plt
from run_parser import results_file_parser, load_run_results

number_of_requests = [50, 100, 500, 1000, 2000]

run_results_34_1_workers_5_olts = load_run_results(34, 1, 5) 
run_results_34_2_workers_5_olts = load_run_results(34, 2, 5)
run_results_34_3_workers_5_olts = load_run_results(34, 3, 5)
run_results_34_4_workers_5_olts = load_run_results(34, 4, 5)
run_results_34_5_workers_5_olts = load_run_results(34, 5, 5)
run_results_34_6_workers_5_olts = load_run_results(34, 6, 5)
run_results_34_10_workers_5_olts = load_run_results(34, 10, 5)

# 1 workers; 5 olts => ratio = 0.2
# 2 workers; 5 olts => ratio = 0.4
# 3 workers; 5 olts => ratio = 0.6
# 4 workers; 5 olts => ratio = 0.8
# 5 workers; 5 olts => ratio = 1.0
# 6 workers; 5 olts => ratio = 1.2 
# 10 workers; 5 olts => ratio = 2.0

### ALGORITHM 1

# Seed 34
avg_time_total_seed_34_1_workers_5_olts_algorithm_1 = run_results_34_1_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_2_workers_5_olts_algorithm_1 = run_results_34_2_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_3_workers_5_olts_algorithm_1 = run_results_34_3_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_4_workers_5_olts_algorithm_1 = run_results_34_4_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_5_workers_5_olts_algorithm_1 = run_results_34_5_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_6_workers_5_olts_algorithm_1 = run_results_34_6_workers_5_olts['avg_time_total_algorithm_1']
avg_time_total_seed_34_10_workers_5_olts_algorithm_1 = run_results_34_10_workers_5_olts['avg_time_total_algorithm_1']
timedout_percentage_seed_34_1_workers_5_olts_algorithm_1 = run_results_34_1_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_2_workers_5_olts_algorithm_1 = run_results_34_2_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_3_workers_5_olts_algorithm_1 = run_results_34_3_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_4_workers_5_olts_algorithm_1 = run_results_34_4_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_5_workers_5_olts_algorithm_1 = run_results_34_5_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_6_workers_5_olts_algorithm_1 = run_results_34_6_workers_5_olts['timedout_percentage_algorithm_1']
timedout_percentage_seed_34_10_workers_5_olts_algorithm_1 = run_results_34_10_workers_5_olts['timedout_percentage_algorithm_1']

def average_time_total_ratio_comparison_algorithm_1():
    total_time_average_1_workers_5_olts = []
    total_time_average_2_workers_5_olts = []
    total_time_average_3_workers_5_olts = []
    total_time_average_4_workers_5_olts = []
    total_time_average_5_workers_5_olts = []
    total_time_average_6_workers_5_olts = []
    total_time_average_10_workers_5_olts = []
    percentual_deviation_1_workers_5_olts = 0
    percentual_deviation_2_workers_5_olts = 0
    percentual_deviation_3_workers_5_olts = 0
    percentual_deviation_4_workers_5_olts = 0
    percentual_deviation_5_workers_5_olts = 0
    percentual_deviation_6_workers_5_olts = 0
    percentual_deviation_10_workers_5_olts = 0
    for i in range(0, len(number_of_requests)):
        avg_total_time_element_1_workers_5_olts = avg_time_total_seed_34_1_workers_5_olts_algorithm_1[i] 
        avg_total_time_element_2_workers_5_olts = avg_time_total_seed_34_2_workers_5_olts_algorithm_1[i]
        avg_total_time_element_3_workers_5_olts = avg_time_total_seed_34_3_workers_5_olts_algorithm_1[i]
        avg_total_time_element_4_workers_5_olts = avg_time_total_seed_34_4_workers_5_olts_algorithm_1[i]
        avg_total_time_element_5_workers_5_olts = avg_time_total_seed_34_5_workers_5_olts_algorithm_1[i]
        avg_total_time_element_6_workers_5_olts = avg_time_total_seed_34_6_workers_5_olts_algorithm_1[i] 
        avg_total_time_element_10_workers_5_olts = avg_time_total_seed_34_10_workers_5_olts_algorithm_1[i] 
        total_time_average_1_workers_5_olts.append(avg_total_time_element_1_workers_5_olts)
        total_time_average_2_workers_5_olts.append(avg_total_time_element_2_workers_5_olts)
        total_time_average_3_workers_5_olts.append(avg_total_time_element_3_workers_5_olts)
        total_time_average_4_workers_5_olts.append(avg_total_time_element_4_workers_5_olts)
        total_time_average_5_workers_5_olts.append(avg_total_time_element_5_workers_5_olts)
        total_time_average_6_workers_5_olts.append(avg_total_time_element_6_workers_5_olts)
        total_time_average_10_workers_5_olts.append(avg_total_time_element_10_workers_5_olts)
        # deviation calculus
        min_time = min([avg_total_time_element_1_workers_5_olts, avg_total_time_element_2_workers_5_olts, avg_total_time_element_3_workers_5_olts, avg_total_time_element_4_workers_5_olts, avg_total_time_element_5_workers_5_olts, avg_total_time_element_6_workers_5_olts])
        deviation_1_workers_5_olts = (avg_total_time_element_1_workers_5_olts - min_time) / min_time
        deviation_2_workers_5_olts = (avg_total_time_element_2_workers_5_olts - min_time) / min_time
        deviation_3_workers_5_olts = (avg_total_time_element_3_workers_5_olts - min_time) / min_time
        deviation_4_workers_5_olts = (avg_total_time_element_4_workers_5_olts - min_time) / min_time
        deviation_5_workers_5_olts = (avg_total_time_element_5_workers_5_olts - min_time) / min_time
        deviation_6_workers_5_olts = (avg_total_time_element_6_workers_5_olts - min_time) / min_time
        deviation_10_workers_5_olts = (avg_total_time_element_10_workers_5_olts - min_time) / min_time
        percentual_deviation_1_workers_5_olts += deviation_1_workers_5_olts
        percentual_deviation_2_workers_5_olts += deviation_2_workers_5_olts
        percentual_deviation_3_workers_5_olts += deviation_3_workers_5_olts
        percentual_deviation_4_workers_5_olts += deviation_4_workers_5_olts
        percentual_deviation_5_workers_5_olts += deviation_5_workers_5_olts
        percentual_deviation_6_workers_5_olts += deviation_6_workers_5_olts
        percentual_deviation_10_workers_5_olts += deviation_10_workers_5_olts
    plt.plot(number_of_requests, total_time_average_1_workers_5_olts, label = "Ratio 0.2")
    plt.plot(number_of_requests, total_time_average_2_workers_5_olts, label = "Ratio 0.4")
    plt.plot(number_of_requests, total_time_average_3_workers_5_olts, label = "Ratio 0.6")
    plt.plot(number_of_requests, total_time_average_4_workers_5_olts, label = "Ratio 0.8")
    plt.plot(number_of_requests, total_time_average_5_workers_5_olts, label = "Ratio 1.0")
    plt.plot(number_of_requests, total_time_average_6_workers_5_olts, label = "Ratio 1.2")
    plt.plot(number_of_requests, total_time_average_10_workers_5_olts, label = "Ratio 2.0")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no ambiente de simulação (em ms)")
    plt.legend()
    plt.savefig("output/ratio_comparisons/algorithm_1/total_average_time_by_ratio.png")
    plt.close()
    return percentual_deviation_1_workers_5_olts, percentual_deviation_2_workers_5_olts, percentual_deviation_3_workers_5_olts, percentual_deviation_4_workers_5_olts, percentual_deviation_5_workers_5_olts, percentual_deviation_6_workers_5_olts, percentual_deviation_10_workers_5_olts

def timedout_percentage_ratio_comparison_algorithm_1():
    timedout_percentage_averages_array_1_workers_5_olts = []
    timedout_percentage_averages_array_2_workers_5_olts = []
    timedout_percentage_averages_array_3_workers_5_olts = []
    timedout_percentage_averages_array_4_workers_5_olts = []
    timedout_percentage_averages_array_5_workers_5_olts = []
    timedout_percentage_averages_array_6_workers_5_olts = []
    timedout_percentage_averages_array_10_workers_5_olts = []
    percentual_deviation_1_workers_5_olts = 0
    percentual_deviation_2_workers_5_olts = 0
    percentual_deviation_3_workers_5_olts = 0
    percentual_deviation_4_workers_5_olts = 0
    percentual_deviation_5_workers_5_olts = 0
    percentual_deviation_6_workers_5_olts = 0
    percentual_deviation_10_workers_5_olts = 0
    for i in range(0, len(number_of_requests)):
        # change the following to the average when I got the run results
        avg_timedout_percentage_element_1_workers_5_olts = timedout_percentage_seed_34_1_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_2_workers_5_olts = timedout_percentage_seed_34_2_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_3_workers_5_olts = timedout_percentage_seed_34_3_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_4_workers_5_olts = timedout_percentage_seed_34_4_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_5_workers_5_olts = timedout_percentage_seed_34_5_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_6_workers_5_olts = timedout_percentage_seed_34_6_workers_5_olts_algorithm_1[i]
        avg_timedout_percentage_element_10_workers_5_olts = timedout_percentage_seed_34_10_workers_5_olts_algorithm_1[i]
        timedout_percentage_averages_array_1_workers_5_olts.append(avg_timedout_percentage_element_1_workers_5_olts)
        timedout_percentage_averages_array_2_workers_5_olts.append(avg_timedout_percentage_element_2_workers_5_olts)
        timedout_percentage_averages_array_3_workers_5_olts.append(avg_timedout_percentage_element_3_workers_5_olts)
        timedout_percentage_averages_array_4_workers_5_olts.append(avg_timedout_percentage_element_4_workers_5_olts)
        timedout_percentage_averages_array_5_workers_5_olts.append(avg_timedout_percentage_element_5_workers_5_olts)
        timedout_percentage_averages_array_6_workers_5_olts.append(avg_timedout_percentage_element_6_workers_5_olts)
        timedout_percentage_averages_array_10_workers_5_olts.append(avg_timedout_percentage_element_10_workers_5_olts)
        # deviation calculus
        min_percentage = min([avg_timedout_percentage_element_1_workers_5_olts, avg_timedout_percentage_element_2_workers_5_olts, avg_timedout_percentage_element_3_workers_5_olts, avg_timedout_percentage_element_4_workers_5_olts, avg_timedout_percentage_element_5_workers_5_olts, avg_timedout_percentage_element_6_workers_5_olts])
        if min_percentage != 0:
            deviation_1_workers_5_olts = (avg_timedout_percentage_element_1_workers_5_olts - min_percentage) / min_percentage
            deviation_2_workers_5_olts = (avg_timedout_percentage_element_2_workers_5_olts - min_percentage) / min_percentage
            deviation_3_workers_5_olts = (avg_timedout_percentage_element_3_workers_5_olts - min_percentage) / min_percentage
            deviation_4_workers_5_olts = (avg_timedout_percentage_element_4_workers_5_olts - min_percentage) / min_percentage
            deviation_5_workers_5_olts = (avg_timedout_percentage_element_5_workers_5_olts - min_percentage) / min_percentage
            deviation_6_workers_5_olts = (avg_timedout_percentage_element_6_workers_5_olts - min_percentage) / min_percentage
            deviation_10_workers_5_olts = (avg_timedout_percentage_element_10_workers_5_olts - min_percentage) / min_percentage
        else:
            deviation_1_workers_5_olts = (avg_timedout_percentage_element_1_workers_5_olts - min_percentage) / 1.0
            deviation_2_workers_5_olts = (avg_timedout_percentage_element_2_workers_5_olts - min_percentage) / 1.0
            deviation_3_workers_5_olts = (avg_timedout_percentage_element_3_workers_5_olts - min_percentage) / 1.0
            deviation_4_workers_5_olts = (avg_timedout_percentage_element_4_workers_5_olts - min_percentage) / 1.0
            deviation_5_workers_5_olts = (avg_timedout_percentage_element_5_workers_5_olts - min_percentage) / 1.0
            deviation_6_workers_5_olts = (avg_timedout_percentage_element_6_workers_5_olts - min_percentage) / 1.0
            deviation_10_workers_5_olts = (avg_timedout_percentage_element_10_workers_5_olts - min_percentage) / 1.0
        percentual_deviation_1_workers_5_olts += deviation_1_workers_5_olts
        percentual_deviation_2_workers_5_olts += deviation_2_workers_5_olts
        percentual_deviation_3_workers_5_olts += deviation_3_workers_5_olts
        percentual_deviation_4_workers_5_olts += deviation_4_workers_5_olts
        percentual_deviation_5_workers_5_olts += deviation_5_workers_5_olts
        percentual_deviation_6_workers_5_olts += deviation_6_workers_5_olts
        percentual_deviation_10_workers_5_olts += deviation_10_workers_5_olts
    plt.plot(number_of_requests, timedout_percentage_averages_array_1_workers_5_olts, label = "Ratio 0.2")
    plt.plot(number_of_requests, timedout_percentage_averages_array_2_workers_5_olts, label = "Ratio 0.4")
    plt.plot(number_of_requests, timedout_percentage_averages_array_3_workers_5_olts, label = "Ratio 0.6")
    plt.plot(number_of_requests, timedout_percentage_averages_array_4_workers_5_olts, label = "Ratio 0.8")
    plt.plot(number_of_requests, timedout_percentage_averages_array_5_workers_5_olts, label = "Ratio 1.0")
    plt.plot(number_of_requests, timedout_percentage_averages_array_6_workers_5_olts, label = "Ratio 1.2")
    plt.plot(number_of_requests, timedout_percentage_averages_array_10_workers_5_olts, label = "Ratio 2.0")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percentagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/ratio_comparisons/algorithm_1/timedout_percentage_by_ratio.png")
    plt.close()
    return percentual_deviation_1_workers_5_olts, percentual_deviation_2_workers_5_olts, percentual_deviation_3_workers_5_olts, percentual_deviation_4_workers_5_olts, percentual_deviation_5_workers_5_olts, percentual_deviation_6_workers_5_olts, percentual_deviation_10_workers_5_olts

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
    tpd_1_workers_5_olts, tpd_2_workers_5_olts, tpd_3_workers_5_olts, tpd_4_workers_5_olts, tpd_5_workers_5_olts, tpd_6_workers_5_olts, tpd_10_workers_5_olts = average_time_total_ratio_comparison_algorithm_1()
    toutpd_1_workers_5_olts, toutpd_2_workers_5_olts, toutpd_3_workers_5_olts, toutpd_4_workers_5_olts, toutpd_5_workers_5_olts, toutpd_6_workers_5_olts, toutpd_10_workers_5_olts = timedout_percentage_ratio_comparison_algorithm_1()
    percentage_deviation_1_workers_5_olts = tpd_1_workers_5_olts + toutpd_1_workers_5_olts
    percentage_deviation_2_workers_5_olts = tpd_2_workers_5_olts + toutpd_2_workers_5_olts
    percentage_deviation_3_workers_5_olts = tpd_3_workers_5_olts + toutpd_3_workers_5_olts
    percentage_deviation_4_workers_5_olts = tpd_4_workers_5_olts + toutpd_4_workers_5_olts
    percentage_deviation_5_workers_5_olts = tpd_5_workers_5_olts + toutpd_5_workers_5_olts
    percentage_deviation_6_workers_5_olts = tpd_6_workers_5_olts + toutpd_6_workers_5_olts
    percentage_deviation_10_workers_5_olts = tpd_10_workers_5_olts + toutpd_10_workers_5_olts
    print("For ALGORITHM 1:")
    print("Ratio 0.2 deviation from optimal is: " + str(percentage_deviation_1_workers_5_olts))
    print("Ratio 0.4 deviation from optimal is: " + str(percentage_deviation_2_workers_5_olts))
    print("Ratio 0.6 deviation from optimal is: " + str(percentage_deviation_3_workers_5_olts))
    print("Ratio 0.6 deviation from optimal is: " + str(percentage_deviation_3_workers_5_olts))
    print("Ratio 0.8 deviation from optimal is: " + str(percentage_deviation_4_workers_5_olts))
    print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_5_workers_5_olts))
    print("Ratio 1.2 deviation from optimal is: " + str(percentage_deviation_6_workers_5_olts))
    print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_10_workers_5_olts))
    if(percentage_deviation_1_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_1_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_1_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_1_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_1_workers_5_olts < percentage_deviation_6_workers_5_olts and percentage_deviation_1_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 0.2 is optimal")
    if(percentage_deviation_2_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_2_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_2_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_2_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_2_workers_5_olts < percentage_deviation_6_workers_5_olts and percentage_deviation_2_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 0.4 is optimal")
    if(percentage_deviation_3_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_3_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_3_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_3_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_3_workers_5_olts < percentage_deviation_6_workers_5_olts and percentage_deviation_3_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 0.6 is optimal")
    if(percentage_deviation_4_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_4_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_4_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_4_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_4_workers_5_olts < percentage_deviation_6_workers_5_olts and percentage_deviation_4_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 0.8 is optimal")
    if(percentage_deviation_5_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_5_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_5_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_5_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_5_workers_5_olts < percentage_deviation_6_workers_5_olts and percentage_deviation_5_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 1.0 is optimal")
    if(percentage_deviation_6_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_6_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_6_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_6_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_6_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_6_workers_5_olts < percentage_deviation_10_workers_5_olts):
        print("Ratio 1.2 is optimal")
    if(percentage_deviation_10_workers_5_olts < percentage_deviation_1_workers_5_olts and percentage_deviation_10_workers_5_olts < percentage_deviation_2_workers_5_olts and percentage_deviation_10_workers_5_olts < percentage_deviation_3_workers_5_olts and percentage_deviation_10_workers_5_olts < percentage_deviation_4_workers_5_olts and percentage_deviation_10_workers_5_olts < percentage_deviation_5_workers_5_olts and percentage_deviation_10_workers_5_olts < percentage_deviation_6_workers_5_olts):
        print("Ratio 2.0 is optimal")
    print('\n')

    # Algorithm 2
    # tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_2()
    # toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_2()
    # percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    # percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    # percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    # print("For ALGORITHM 2:")
    # print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    # print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    # print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    # if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
    #     print("Ratio 0.5 is optimal")
    # if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
    #     print("Ratio 1.0 is optimal")
    # if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
    #     print("Ratio 2.0 is optimal")
    # print('\n')

    # Algorithm 3
    # tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_3()
    # toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_3()
    # percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    # percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    # percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    # print("For ALGORITHM 3:")
    # print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    # print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    # print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    # if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
    #     print("Ratio 0.5 is optimal")
    # if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
    #     print("Ratio 1.0 is optimal")
    # if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
    #     print("Ratio 2.0 is optimal")
    # print('\n')

    # Algorithm 4
    # tpd_0_5, tpd_1_0, tpd_2_0 = average_time_total_ratio_comparison_algorithm_4()
    # toutpd_0_5, toutpd_1_0, toutpd_2_0 = timedout_percentage_ratio_comparison_algorithm_4()
    # percentage_deviation_0_5 = tpd_0_5 + toutpd_0_5
    # percentage_deviation_1_0 = tpd_1_0 + toutpd_1_0
    # percentage_deviation_2_0 = tpd_2_0 + toutpd_2_0
    # print("For ALGORITHM 4:")
    # print("Ratio 0.5 deviation from optimal is: " + str(percentage_deviation_0_5))
    # print("Ratio 1.0 deviation from optimal is: " + str(percentage_deviation_1_0))
    # print("Ratio 2.0 deviation from optimal is: " + str(percentage_deviation_2_0))
    # if(percentage_deviation_0_5 < percentage_deviation_1_0 and percentage_deviation_0_5 < percentage_deviation_2_0):
    #     print("Ratio 0.5 is optimal")
    # if(percentage_deviation_1_0 < percentage_deviation_0_5 and percentage_deviation_1_0 < percentage_deviation_2_0):
    #     print("Ratio 1.0 is optimal")
    # if(percentage_deviation_2_0 < percentage_deviation_0_5 and percentage_deviation_2_0 < percentage_deviation_1_0):
    #     print("Ratio 2.0 is optimal")
    # print('\n')