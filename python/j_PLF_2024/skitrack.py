import argparse

__author__ = "Zwickelstorfer Felix"

import logging

import numpy
import regex as re

# Tests:
# python skitrack.py --out ski_filtered.csv --tal 999 -s 1000 ski.gpx
# python skitrack.py --verbose --marker --out ski1.png --tal 1500 ski.gpx
# python skitrack.py --quiet --out ski2.png --tal 1500 ski.gpx
#

def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("infile", help="Input-Datei (z.B. ski.gpx oder ski.csv)")
    parser.add_argument("-o", "--out", help="Zu generierende Datei, z.B. ski_new.csv oder ski.png", required=True)
    parser.add_argument("-m", "--marker", help="Sollen der erste und letzte Punkt markiert werden?", required=False,
                        action="store_true")
    parser.add_argument("-t", "--tal", help="Seehöhe niedrigster Punkt, der noch ausgewertet werden soll",
                        required=False)
    parser.add_argument("-s", "--spitze", help="Seehöhe höchster Punkt, der noch ausgewertet werden soll",
                        required=False)
    group = parser.add_mutually_exclusive_group()
    group.add_argument("-v", "--verbose", help="Zeit alle Details an (siehe Angabe)", required=False,
                       action="store_true")
    group.add_argument("-q", "--quiet", help="Keine Textausgabe", required=False,
                       action="store_true")
    return parser.parse_args()


def read_csv(path: str) -> list[tuple[str, tuple[float, float], float]]:
    output = []
    with open(path) as f:
        line = f.readline()
        while line:
            s = line.split(";")
            if len(s) == 4:
                output.append((s[0], (float(s[1]), float(s[2])), float(s[3])))
            line = f.readline()
    return output


def read_gpx(path: str) -> list[tuple[str, tuple[float, float], float]]:
    output = []
    with open(path) as f:
        m1 = re.findall(
            pattern=r'<trkpt\W*lat="(.*?)"\W*lon="(.*?)"\W*>.*?<ele>(.*?)</ele>.*?<time>(.*?)</time>.*?</trkpt>',
            string=f.read(),
            flags=re.DOTALL  # dot matches newlines as well
        )
        for s in m1:
            output.append((s[3].strip(), (float(s[0].strip()), float(s[1].strip())), float(s[2].strip())))
    return output


def write_csv(data, path):
    with open(path, "w") as f:
        for line in data:
            f.write(line[0] + ";" + str(line[0][0]) + ";" + str(line[0][1]) + ";" + str(line[2]) + "\n")


def write_png(data, out, needsMarker):
    import matplotlib.pyplot as plt
    plt.figure(figsize=(10, 8))
    plt.scatter(x=[i[1][1] for i in data], y=[i[1][0] for i in data], color="blue", alpha=0.5)
    if needsMarker:
        print(data[0][1])
        plt.annotate(text="Start", xy=(data[0][1][1], data[0][1][0]), fontsize=11, xytext=(-60, -25),
                     textcoords="offset pixels",
                     arrowprops=dict(arrowstyle="simple", color='green', connectionstyle="arc3,rad=0.3"))
        plt.annotate(text="Ende", xy=(data[-1][1][1], data[-1][1][0]), fontsize=11, xytext=(25, 10),
                     textcoords="offset pixels",
                     arrowprops=dict(arrowstyle="simple", color='green', connectionstyle="arc3,rad=0.3"))
    plt.savefig(out)
    # plt.show()


def main(args: argparse.Namespace) -> None:
    logger = logging.getLogger("skitrack")
    if len(args.infile) < 3:
        logger.error("Cannot recognise input file since the extension is wrong or to short")
        return
    extension_in = args.infile[-3:]
    if extension_in == "csv":
        data = read_csv(args.infile)
    elif extension_in == "gpx":
        data = read_gpx(args.infile)
    else:
        logger.error(f"Wrong input extension: '{extension_in}', only csv and gpx are allowed")
        return

    # filtering height
    data = [i for i in data if
            (args.tal is None or i[2] >= float(args.tal)) and (args.spitze is None or i[2] <= float(args.spitze))]

    if len(args.out) < 3:
        logger.error("Cannot recognise ouput file since the extension is wrong or to short")
        return
    extension_out = args.out[-3:]
    if extension_out == "csv":
        write_csv(data, args.out)
    elif extension_out == "png":
        write_png(data, args.out, args.marker)
    else:
        logger.error(f"Wrong output extension: '{extension_in}', only csv and png are allowed")
        return

    if not args.quiet:
        print("Niedrigster Punkt:", numpy.min([i[2] for i in data]))
        print("Höchster Punkt:", numpy.max([i[2] for i in data]))
        print("Anzahl der Wegpunkte:", len(data))
        if args.verbose:
            print("Startpunkt:", data[0])
            print("Endpunkt:", data[-1])
            print("Output-Datei:", args.out)


def setup_logger(args):
    logger = logging.getLogger("skitrack")
    if args.verbose:
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.CRITICAL)
    else:
        logger.setLevel(logging.INFO)
    logging.basicConfig(format="%(message)s")


if __name__ == "__main__":
    arguments = get_args()
    setup_logger(arguments)
    main(arguments)
