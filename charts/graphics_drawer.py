from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np

number_of_requests = [50, 100, 500, 1000]

avg_time_total_algorithm_1 = [14149.0, 65094.0, 248557.0, 499626.0]
avg_time_total_algorithm_2 = [18498.0, 48789.0, 233915.0, 485337.0]
avg_time_total_algorithm_3 = [14707.0, 47254.0, 497120.0, 556622.0]
avg_time_total_algorithm_4 = [14728.0, 46161.0, 236146.0, 521052.0]

avg_time_broker_queue_algorithm_1 = [7.0, 7.0, 5.0, 5.0]
avg_time_broker_queue_algorithm_2 = [7.0, 7.0, 5.0, 4.0]
avg_time_broker_queue_algorithm_3 = [10738.0, 41544.0, 497120.0, 550147.0]
avg_time_broker_queue_algorithm_4 = [10752.0, 40643.0, 230106.0, 513865.0]

avg_time_worker_queue_algorithm_1 = [10452.0, 57635.0, 240176.0, 492509.0]
avg_time_worker_queue_algorithm_2 = [14366.0, 42590.0, 227496.0, 478119.0]
avg_time_worker_queue_algorithm_3 = [0.0, 0.0, 0.0, 0.0]
avg_time_worker_queue_algorithm_4 = [0.0, 0.0, 0.0, 0.0]

avg_time_olt_queue_algorithm_1 = [1027.0, 3639.0, 3778.0, 3652.0]
avg_time_olt_queue_algorithm_2 = [1471.0, 2888.0, 2814.0, 3758.0]
avg_time_olt_queue_algorithm_3 = [103.0, 345.0, 400.0, 488.0]
avg_time_olt_queue_algorithm_4 = [4.0, 199.0, 227.0, 3738.0]

timedout_percentage_algorithm_1 = [0.0, 0.06, 0.076, 0.095]
timedout_percentage_algorithm_2 = [0.0, 0.06, 0.064, 0.093]
timedout_percentage_algorithm_3 = [0.0, 0.02, 0.018, 0.016]
timedout_percentage_algorithm_4 = [0.0, 0.02, 0.016, 0.092]

def average_time_total_chart():
    plt.plot(number_of_requests, avg_time_total_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_total_algorithm_2, label = "Algoritmo 2")
    plt.plot(number_of_requests, avg_time_total_algorithm_3, label = "Algoritmo 3")
    plt.plot(number_of_requests, avg_time_total_algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart():
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_2, label = "Algoritmo 2")
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_3, label = "Algoritmo 3")
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart():
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_2, label = "Algoritmo 2")
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_3, label = "Algoritmo 3")
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart():
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_2, label = "Algoritmo 2")
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_3, label = "Algoritmo 3")
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart():
    plt.plot(number_of_requests, timedout_percentage_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, timedout_percentage_algorithm_2, label = "Algoritmo 2")
    plt.plot(number_of_requests, timedout_percentage_algorithm_3, label = "Algoritmo 3")
    plt.plot(number_of_requests, timedout_percentage_algorithm_4, label = "Algoritmo 4")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Percetagem de pedidos timedout")
    plt.legend()
    plt.savefig("output/timedout_percentage_chart.png")
    plt.close()

if __name__ == "__main__":
    average_time_total_chart()
    average_time_broker_queue_chart()
    average_time_worker_queue_chart()
    average_time_olt_queue_chart()
    timedout_percentage_chart()
