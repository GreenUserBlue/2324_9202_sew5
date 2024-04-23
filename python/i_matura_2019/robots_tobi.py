import argparse
import logging
import sys
from logging.handlers import RotatingFileHandler
from time import sleep

logger = logging.getLogger("sierpinski_logger")

all_paths = []

def print_room(room: [str]):
    for row in room:
        print(row.strip())


def read_file(filename: str):
    """
    reads a room file and returns the contents in array-format
    """
    logger.debug(f"reading file {filename}...")

    with open(filename, "r") as f:
        return f.readlines()


def get_awab_index(room: [[str]]):
    """
    returns the index of all AWABs in the room
    """
    logger.debug(f"searching room for AWABs...")
    awabs = []

    for x, row in enumerate(room):
        y = str.find(row, "A")
        if y != -1:
            logger.debug(f"found AWAB at {x}/{y}")
            awabs.append((x, y))

    return awabs


def search(room, pos, path=None):
    """
    searches a room for the best for an AWAB
    """
    if path is None:
        path = []

    x = pos[0]
    y = pos[1]
    cur_field = room[x][y]

    if cur_field == "R":
        # print_room(path)
        # print(path)
        logger.debug(f"found station at {pos} with path {path}")
        all_paths.append(path.copy())
        return
    elif cur_field != " " and len(path) != 0:
        return

    path.append(pos)

    if cur_field != "A":
        room[x] = room[x][:y] + 'x' + room[x][y + 1:]

    search(room, (x + 1, y), path)  # down
    search(room, (x - 1, y), path)  # up
    search(room, (x, y + 1), path)  # right
    search(room, (x, y - 1), path)  # left

    room[x] = room[x][:y] + " " + room[x][y + 1:]
    path.pop()

    return

def create_logger():
    """
    creates the variables necessary for logging
    :return:
    """
    rot_file_handler = RotatingFileHandler('wegsuche.log', maxBytes=10_000, backupCount=5)
    stream_handler = logging.StreamHandler(sys.stdout)
    logger.addHandler(rot_file_handler)
    logger.addHandler(stream_handler)


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    create_logger()

    argparser = argparse.ArgumentParser()
    argparser.add_argument("filename", help="name of the input file")

    group = argparser.add_mutually_exclusive_group()
    group.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    group.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = argparser.parse_args()

    if args.verbosity:
        logger.setLevel(logging.DEBUG)

    room = read_file(args.filename)

    for pos in get_awab_index(room):
        search(room, pos)
        break
    print(all_paths)


if __name__ == '__main__':
    parse_args()