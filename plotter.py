import matplotlib.pyplot as plt
import time

statuses = {1: [], 2: [], 3: []}
timestamps = []

def plot_capacity():
    plt.figure()
    while True:
        plt.clf()
        for server_id, data in statuses.items():
            plt.plot(timestamps, data, label=f"Server {server_id}")
        plt.legend()
        plt.draw()
        plt.pause(0.1)

while True:
    timestamps.append(time.time())
    statuses[1].append(1000)
    statuses[2].append(950)
    statuses[3].append(900)
    time.sleep(5)
    plot_capacity()
