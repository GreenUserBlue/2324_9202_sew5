import argparse
import os
from logging.handlers import RotatingFileHandler

import matplotlib.axis
import matplotlib.pyplot as plt
import logging


# matplotlib.use("QtAgg") 'TKAgg','GTKAgg','Qt4Agg','WXAgg', alle Möglichkeiten

# Parameter for console:
# -s 800 600 -f myFile.png -r 8 -t green -n blue -a -S 14 -b orange

def setup_logger(logLevel: str = "INFO") -> None:
    """ creates the logger """
    global logger
    logging.basicConfig(level=getattr(logging, logLevel), format='[%(asctime)s] %(levelname)s %(message)s',
                        datefmt='%Y-%m-%d %H:%M:%S')
    logger = logging.getLogger('my_logger')
    if not os.path.exists("./log/"):
        os.makedirs("./log/")
    file_handler = RotatingFileHandler("./log/sierpinski.log", maxBytes=10000, backupCount=5)
    file_handler.setFormatter(logging.Formatter('[%(asctime)s] %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S'))
    # stream_handler = logging.StreamHandler()
    # stream_formatter = logging.Formatter('%(levelname)s - %(message)s')
    # stream_handler.setFormatter(stream_formatter)
    logger.addHandler(file_handler)
    # logger.addHandler(stream_handler)
    logger.info("Logging turned on " + str(logLevel))


def get_arg_parse() -> argparse.Namespace:
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


def check_smallest_size(points: list[tuple[float, float]], min_length: float) -> bool:
    """checks if any of the lengths is too short, and does this with the Pythagoras"""
    min_length_squared = min_length * min_length
    for p in range(len(points)):
        x = points[(p + 1) % len(points)][0] - points[p][0]
        y = points[(p + 1) % len(points)][1] - points[p][1]
        pixel_x, pixel_y = data_to_pixel_coords_within_plot((x, y))
        if pixel_x * pixel_x + pixel_y * pixel_y < min_length_squared:
            return False
    return True


def data_to_pixel_coords_within_plot(point):
    # print(fig.transFigure.transform_point((0, 0))) just generally maybe useful
    # print(fig.transFigure.transform_point((1, 1)))
    fig, ax = plt.gcf(), plt.gca()
    bbox = ax.get_window_extent().transformed(fig.dpi_scale_trans.inverted())
    width, height = bbox.width, bbox.height
    width *= fig.dpi
    height *= fig.dpi
    y_coords = ax.get_ylim()
    x_coords = ax.get_xlim()
    return (point[0] - x_coords[0]) * width / x_coords[1], (point[1] - y_coords[0]) * height / y_coords[1]


def pixel_to_data_coords_within_plot(point):
    fig, ax = plt.gcf(), plt.gca()
    bbox = ax.get_window_extent().transformed(fig.dpi_scale_trans.inverted())
    width, height = bbox.width, bbox.height
    width *= fig.dpi
    height *= fig.dpi
    y_coords = ax.get_ylim()
    x_coords = ax.get_xlim()
    return (point[0]) / width * x_coords[1] + x_coords[0], (point[1]) / height * y_coords[1] + y_coords[0]


def mid_points(points: list[tuple[float, float]]) -> list[tuple[float, float]]:
    new_points = []
    for p in range(len(points)):
        x = points[(p + 1) % len(points)][0] + points[p][0]
        y = points[(p + 1) % len(points)][1] + points[p][1]
        new_points.append((x / 2, y / 2))
    return new_points


def draw_triangles(args: argparse.Namespace, points: list[tuple[float, float]], counter: int = 0) -> None:
    if args.recursionDepth:
        if args.recursionDepth <= counter:
            return
    elif not check_smallest_size(points, args.minLength):
        return
    global logger
    plt.plot([points[0][0], points[1][0], points[2][0], points[0][0]],
             [points[0][1], points[1][1], points[2][1], points[0][1]], color=args.triangleColor)
    logger.debug("Drew points: " + str(points))
    middle_points = mid_points(points)
    draw_triangles(args, [points[0], middle_points[0], middle_points[2]], counter + 1)
    draw_triangles(args, [points[1], middle_points[0], middle_points[1]], counter + 1)
    draw_triangles(args, [points[2], middle_points[1], middle_points[2]], counter + 1)


def set_axis(hide: bool, ax: matplotlib.axis, points: list[tuple[float, float]]) -> None:
    if hide:
        # plt.axis('off')
        ax.set_axis_off()  # both work, but this one specifies which subplot as well
        logger.debug("Finished hiding axis")
        pass
    else:
        ax.tick_params(colors=arg_namespace.nameColor)
        [t.set_color(arg_namespace.nameColor) for t in ax.xaxis.get_ticklabels()]
        [t.set_color(arg_namespace.nameColor) for t in ax.yaxis.get_ticklabels()]
        [ax.spines[t].set_color(arg_namespace.nameColor) for t in ax.spines]
        logger.debug("Finished updating axis descriptions")

    # print([i[0] for i in points])
    ax.set_xlim([min([i[0] for i in points]), max([i[0] for i in points])])
    yRange = [min([i[1] for i in points]), max([i[1] for i in points])]
    ax.set_ylim(yRange)
    logger.debug("Finished setting the range of the Graph to the possible maximum")


def show_plot(args: argparse.Namespace) -> None:
    # plt.figure(figsize=(args.size[0] / 100, args.size[1] / 100))
    # fig, ax = plt.gcf(), plt.gca()
    fig, ax = plt.subplots(figsize=(args.size[0] / 100, args.size[1] / 100))  # pixel to matplotlib format
    points = [args.point1, args.point2, args.point3]
    draw_triangles(args, points)
    logger.info("Finished drawing Triangle")
    fig.set_facecolor(args.backgroundColor)
    ax.set_facecolor(args.backgroundColor)

    plt.title("Felix Zwickelstorfer", color=args.nameColor, ha="right", va="bottom",
              fontsize=args.nameSize, x=1)  # at the top right
    # plt.title("Felix Zwickelstorfer", color=args.nameColor, ha="right", va="top", fontsize=args.nameSize, x=1, y=0, pad=-20)  # at the bottom right
    set_axis(args.hideAxes, ax, points)

    ax.set_aspect('equal')
    fig.tight_layout()
    plt.savefig(args.file)
    logger.info("Saved the image")


if __name__ == "__main__":
    arg_namespace = get_arg_parse()
    try:
        if arg_namespace.verbose:
            setup_logger("DEBUG")
        elif arg_namespace.quiet:
            setup_logger("WARNING")
        else:
            setup_logger()
        show_plot(arg_namespace)
    except Exception as e:
        global logger
        logger.error(e)
