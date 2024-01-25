from collections import defaultdict, Counter
from itertools import groupby
from subprocess import Popen, PIPE
import matplotlib.pyplot as plt
import numpy as np

git_log = ["git", "log", "--pretty=format:%ad", "--date=format-local:%a-%H-%M"]
process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
out, err = process.communicate()

time_window = 0.5  # 1/4 hour

points = list()
for i in out.splitlines():
    cur = i.split("-")
    points.append((cur[0].lower(), (np.floor((int(cur[1]) + int(cur[2]) / 60) / time_window) * time_window)))

weekdays = ["mon", "tue", "wed", "thu", "fri", "sat", "sun"]

# Group the data by day and window
grouped_data = Counter()

for day, timestamp in points:
    grouped_data[(day, timestamp)] += 1

min_size = 50
additional_size = 15

print(grouped_data)
max_val = max(grouped_data.values())

data = {"x": [], "y": [], "sizes": []}
for day, time in grouped_data:
    data["x"].append(time)
    data["y"].append(weekdays.index(day))
    data["sizes"].append(min_size +  additional_size* grouped_data[(day, time)])
plt.figure(figsize=(10, 8))

print(data)
plt.ylabel('Weekday')
plt.scatter(data['x'], data['y'], s=data['sizes'], alpha=0.5)
plt.yticks(range(len(weekdays)), labels=weekdays)
plt.xticks(range(0, 25, 4))

plt.xlabel('Time')
plt.title(f'Max Mustermann: {len(points)} commits')
# plt.gca().invert_yaxis()  # Invert y-axis to have 'sun' at the top
# plt.grid(True)
plt.xlabel('Weekday')
# plt.title('Weekday Statistics with Circle Sizes Based on Commit Counts')

plt.savefig("plot1_zwickelstorfer2.png", dpi=72)
# plt.show()
