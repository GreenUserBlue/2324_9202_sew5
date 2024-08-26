# def meth(a: list[str]):
#     a.append("asdfasdf")
#
#
# b=["asdf"]
#
# meth(b)
#
# print(b)
import argparse

import numpy as np
import matplotlib.pyplot as plt

# plt.figure(figsize=(10, 8))
# fig, ax = plt.gcf(), plt.gca()
# points, = ax.plot(range(10), 'ro')


# print(data_to_pixel_coords_within_plot((0, 0), ax))
# print(pixel_to_data_coords_within_plot(data_to_pixel_coords_within_plot((5,5),ax),ax))

# We have to be sure to save the figure with it's current DPI
# (savfig overrides the DPI of the figure, by default)
# fig.savefig('test.png', dpi=fig.dpi)
# plt.show()
# from openpyxl.reader import excel as e


# print([(i, j) for j in range(3) for i in range(3)  if i != 1 or j != 1])
# def read_a_b(filename: str) -> str:
#     lines = []
#     wb = e.load_workbook(filename, read_only=False)
#     ws1 = wb[wb.sheetnames[0]]  # Hier kann man probably andere Zahlen angeben für andere Sheets in der gleichen Datei
#     print()
#
#     ws2 = wb[wb.sheetnames[1]]
#     # ws3 = wb[wb.sheetnames[2]]
#     # for row in ws1.iter_rows(min_row=1):
#     #     if row[0].value is not  None:
#     #         lines.append((row[0].value, row[1].value))
#     # print(lines)
#
#     # print(ws2[0][1])
#     for row in ws2.iter_rows(min_row=1):
#         for i in row:
#             print(row, "    ", i, "     ", i.value)
#
#     print(ws1['A1'])
#     ws2['B3'] = "Hallo welt"
#     ws2['B4'].value = "=CONCAT(b3,\", hallo universum\")"
#     print()
#     # if row[0].value == None:
#     #     break
#     # lines.append((row[0].value, row[1].value))
#     # for row in ws3.iter_rows(min_row=1):
#     #     if row[0].value == None:
#     #         break
#     #     lines.append((row[0].value, row[1].value))
#     wb.save("newCrops.xlsx")
#     return lines


# print(read_a_b("crops.xlsx"))


# itp_test.py -h   - automatisch
# itp_test.py --help

# itp_test.py "abcd.txt"
def get_arg_parse() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="date.py by Felix Zwickelstorfer / HTL Rennweg")
    parser.add_argument("file", help="name des files")
    parser.add_argument("--size", "-s", type=float, nargs=2)
    parser.add_argument("--point1", "-p1", type=float, nargs=2, default=[2.0, 8.0])
    parser.add_argument("--nameSize", "-S", type=float, default=12.0)
    group = parser.add_mutually_exclusive_group()

    group.add_argument("--verbose", "-v", action="store_true")
    group.add_argument("--quiet", "-q")

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--minLength", "-L", type=float)
    group.add_argument("--recursionDepth", "-r", type=int)
    return parser.parse_args()


if __name__ == "__main__":
    args = get_arg_parse()
    print(args.file)
    # print(args.size)
    print(args.verbose)
