from unicodedata import numeric
import matplotlib
import matplotlib.pyplot as plt
import numpy as np

number_of_requests = [50, 100, 500, 1000]

avg_time_total_algorithm_1 = [14114.0, 47756.0, 210780.0, 504711.0]
avg_time_total_algorithm_2 = [12970.0, 56012.0, 232305.0, 495288.0]

avg_time_broker_queue_algorithm_1 = [1.0, 1.0, 1.0, 1.0]
avg_time_broker_queue_algorithm_2 = [1.0, 1.0, 1.0, 1.0]

avg_time_worker_queue_algorithm_1 = [10441.0, 41303.0, 204305.0, 497295.0]
avg_time_worker_queue_algorithm_2 = [9053.0, 48946.0, 225147.0, 488544.0]

avg_time_olt_queue_algorithm_1 = [1017.0, 3034.0, 3052.0, 3966.0]
avg_time_olt_queue_algorithm_2 = [1266.0, 3647.0, 3735.0, 3295.0]

timedout_percentage_algorithm_1 = [0.0, 0.09, 0.066, 0.106]
timedout_percentage_algorithm_2 = [0.0, 0.09, 0.086, 0.087]

def average_time_total_chart():
    plt.plot(number_of_requests, avg_time_total_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_total_algorithm_2, label = "Algoritmo 2")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam no sistema (em ms)")
    plt.legend()
    plt.savefig("output/average_time_total_chart.png")
    plt.close()

def average_time_broker_queue_chart():
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_broker_queue_algorithm_2, label = "Algoritmo 2")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do broker (em ms)")
    plt.legend()
    plt.savefig("output/average_time_broker_queue_chart.png")
    plt.close()

def average_time_worker_queue_chart():
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_worker_queue_algorithm_2, label = "Algoritmo 2")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue do worker (em ms)")
    plt.legend()
    plt.savefig("output/average_time_worker_queue_chart.png")
    plt.close()

def average_time_olt_queue_chart():
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, avg_time_olt_queue_algorithm_2, label = "Algoritmo 2")
    plt.xlabel("Número de pedidos")
    plt.ylabel("Tempo total que os pedidos passam na queue da OLT (em ms)")
    plt.legend()
    plt.savefig("output/average_time_olt_queue_chart.png")
    plt.close()

def timedout_percentage_chart():
    plt.plot(number_of_requests, timedout_percentage_algorithm_1, label = "Algoritmo 1")
    plt.plot(number_of_requests, timedout_percentage_algorithm_2, label = "Algoritmo 2")
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
