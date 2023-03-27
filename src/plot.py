from matplotlib import pyplot as plt

lines = []
dates = []
hare_pops = []
lynx_pops = []

for line in open("/Users/esachen/IdeaProjects/Simulation v2.0/myData.csv"):
    line_parts = line.split('\n')
    lines.append(line_parts[0])

dates = lines[0].split(",")
dates.remove('')
dates2 = []
for i in range(0, len(dates)):
    dates2.append(int(dates[i]))

hare_pops = lines[1].split(",")
hare_pops.remove('')
hare_pops2 = []
for i in range(0, len(hare_pops)):
    hare_pops2.append(int(hare_pops[i]))

lynx_pops = lines[2].split(",")
lynx_pops.remove('')
lynx_pops2 = []
for i in range(0, len(lynx_pops)):
    lynx_pops2.append(int(lynx_pops[i]))

fig, ax = plt.subplots()
ax.plot(dates2, hare_pops2, '-b', marker="o")
ax.set_xlabel("Years")
ax.set_ylabel("Hare", color="blue", fontsize=10)
ax2 = ax.twinx()
ax2.plot(dates2, lynx_pops2, '-r', marker="o")
ax2.set_ylabel("Lynx", color="red", fontsize=10)

plt.title("Population Over Time")
plt.show()