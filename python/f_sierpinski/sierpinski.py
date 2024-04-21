import argparse

def getArgParse():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", "-f", required=True)
    parser.add_argument("--size", "-s", type=float, nargs=2, required=True)
    parser.add_argument("--point1", "-p1", type=float, nargs=2, default=[2.0, 8.0])
    parser.add_argument("--point2", "-p2", type=float, nargs=2, default=[2.0, 2.0])
    parser.add_argument("--point3", "-p3", type=float, nargs=2, default=[6.0, 4.0])
    parser.add_argument("--nameSize", "-S", type=float, default=12.0)
    group = parser.add_mutually_exclusive_group()

    group.add_argument("--verbose", "-v", action="store_true")
    group.add_argument("--quiet", "-q", action="store_true")

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--minLength", "-L", type=float)
    group.add_argument("--recursionDepth", "-r", type=int)

    group.add_argument("--triangleColor", "-t", default="black")
    group.add_argument("--backgroundColor", "-b", default="white")
    group.add_argument("--nameColor", "-n", default="black")
    group.add_argument("--hideAxes", "-a", action="store_true")
    return parser.parse_args()


if __name__ == "__main__":
    args = getArgParse()
    print(args)
