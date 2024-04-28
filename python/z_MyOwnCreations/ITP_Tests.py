# def meth(a: list[str]):
#     a.append("asdfasdf")
#
#
# b=["asdf"]
#
# meth(b)
#
# print(b)
import numpy as np
import matplotlib.pyplot as plt

plt.figure(figsize=(10, 8))
fig, ax = plt.gcf(), plt.gca()
points, = ax.plot(range(10), 'ro')



print(data_to_pixel_coords_within_plot((0, 0), ax))
print(pixel_to_data_coords_within_plot(data_to_pixel_coords_within_plot((5,5),ax),ax))

# We have to be sure to save the figure with it's current DPI
# (savfig overrides the DPI of the figure, by default)
# fig.savefig('test.png', dpi=fig.dpi)
plt.show()
