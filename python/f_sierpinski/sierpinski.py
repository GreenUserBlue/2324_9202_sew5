import argparse
import matplotlib.pyplot as plt


# matplotlib.use("QtAgg") 'TKAgg','GTKAgg','Qt4Agg','WXAgg', alle Möglichkeiten

# Parameter for console:
# -s 800 600 -f myfile.png -r 8 -t green -n blue -a -S 14 -b orange

def getArgParse():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", "-f", required=True)
    parser.add_argument("--size", "-s", type=float, nargs=2, required=True)
    parser.add_argument("--point1", "-p1", type=float, nargs=2, default=[2.0, 8.0])
    parser.add_argument("--point2", "-p2", type=float, nargs=2, default=[2.0, 2.0])
    parser.add_argument("--point3", "-p3", type=float, nargs=2, default=[8.0, 5.0])
    parser.add_argument("--nameSize", "-S", type=float, default=12.0)
    group = parser.add_mutually_exclusive_group()

    group.add_argument("--verbose", "-v", action="store_true")
    group.add_argument("--quiet", "-q", action="store_true")

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--minLength", "-L", type=float)
    group.add_argument("--recursionDepth", "-r", type=int)

    parser.add_argument("--triangleColor", "-t", default="black")
    parser.add_argument("--backgroundColor", "-b", default="white")
    parser.add_argument("--nameColor", "-n", default="black")
    parser.add_argument("--hideAxes", "-a", action="store_true")
    return parser.parse_args()


def check_smallest_size(points, minLength):
    minLength = minLength * minLength
    for p in range(len(points)):
        x = points[(p + 1) % len(points)][0] - points[p][0]
        y = points[(p + 1) % len(points)][1] - points[p][1]
        if x * x + y * y < minLength: return False
    return True


def mid_points(points):
    new_points = []
    for p in range(len(points)):
        x = points[(p + 1) % len(points)][0] + points[p][0]
        y = points[(p + 1) % len(points)][1] + points[p][1]
        new_points.append((x / 2, y / 2))
    return new_points


def draw_triangles(args, points, counter=0):
    if args.recursionDepth:
        if args.recursionDepth <= counter:
            return
    elif not check_smallest_size(points, args.minLength):
        return
    # print(points)
    plt.plot([points[0][0], points[1][0], points[2][0], points[0][0]],
             [points[0][1], points[1][1], points[2][1], points[0][1]], color=args.triangleColor)
    # plt.plot([-2, 2, 6], [28, 2, 4], color=args.triangleColor)
    mids = mid_points(points)
    draw_triangles(args, [points[0], mids[0], mids[2]], counter + 1)
    draw_triangles(args, [points[1], mids[0], mids[1]], counter + 1)
    draw_triangles(args, [points[2], mids[1], mids[2]], counter + 1)


def showPlot(args):
    # plt.figure(figsize=(args.size[0] / 100, args.size[1] / 100))
    # fig, ax = plt.gcf(), plt.gca()
    fig, ax = plt.subplots(figsize=(args.size[0] / 100, args.size[1] / 100))  # pixel to matplotlib format
    points = [args.point1, args.point2, args.point3]
    draw_triangles(args, points)
    fig.set_facecolor(args.backgroundColor)
    ax.set_facecolor(args.backgroundColor)

    plt.title("Felix Zwickelstorfer", color=args.nameColor, horizontalalignment="right", verticalalignment="bottom", fontsize=args.nameSize, x=1)  # at the top right
    # plt.title("Felix Zwickelstorfer", color=args.nameColor, horizontalalignment="right", verticalalignment="top", fontsize=args.nameSize, x=1, y=0, pad=-20)  # at the bottom right
    if args.hideAxes:
        # plt.axis('off')
        ax.set_axis_off()  # both work, but this one specifies which subplot as well
        pass
    else:
        ax.tick_params(colors=args.nameColor)
        [t.set_color(args.nameColor) for t in ax.xaxis.get_ticklabels()]
        [t.set_color(args.nameColor) for t in ax.yaxis.get_ticklabels()]
        [ax.spines[t].set_color(args.nameColor) for t in ax.spines]
    # print([i[0] for i in points])
    ax.set_xlim([min([i[0] for i in points]), max([i[0] for i in points])])
    yRange = [min([i[1] for i in points]), max([i[1] for i in points])]
    ax.set_ylim(yRange)

    ax.set_aspect('equal')
    fig.tight_layout()
    # plt.show()
    plt.savefig(args.file)


if __name__ == "__main__":
    args = getArgParse()
    showPlot(args)
