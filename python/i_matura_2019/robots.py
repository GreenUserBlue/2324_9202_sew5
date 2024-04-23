import argparse
import logging


def search():
    pass


def get_surrounding_pos(pos: tuple[int, int]):
    return [(pos[0] + 1, pos[1]), (pos[0] - 1, pos[1]), (pos[0], pos[1] + 1), (pos[0], pos[1] - 1)]


def get_data(data: list[str]):
    robots, rechargers, emtpy_spaces, targets = set(), set(), set(), set()

    for i in range(len(data)):
        for j in range(len(data[i])):
            match data[i][j]:
                case "A":
                    robots.add((i, j))
                case " ":
                    emtpy_spaces.add((i, j))
                case ".":
                    emtpy_spaces.add((i, j))
                case "R":
                    rechargers.add((i, j))
    for i in rechargers:
        for j in get_surrounding_pos(i):
            if j in emtpy_spaces or j in robots:
                targets.add(j)

    return robots, targets, emtpy_spaces, (len(data), len(data[0]))


# ------------------------------------------
# ------------------------------------------ The follwing solution is deapth-first search, which is, even though it is optimised, too slow for room_3.txt
# ------------------------------------------

# def solve_single_robot(r, open_targets, emtpy_spaces, path: list = None, best_path_len=-1):
#     if path is None:
#         path = []
#     if r in open_targets:
#         return path
#     if len(path) > best_path_len >= 0:
#         return -1
#     bestPath = -1
#     emtpy_spaces.discard(r)  # removes it, but doesn't throw an error if it doesn't exist
#     possible_spaces =
#     direct_targets = [i for i in possible_spaces if i in open_targets]
#     if len(direct_targets) > 0:
#         possible_spaces = direct_targets
#     for i in possible_spaces:
#         path.append(i)
#         new_path = solve_single_robot(i, open_targets, emtpy_spaces.copy(), path.copy(), best_path_len)
#         if new_path != -1:
#             if bestPath == -1 or best_path_len > len(new_path):
#                 bestPath = new_path
#                 best_path_len = len(bestPath)
#         path.pop()
#     return bestPath

# def solve_robot(r, open_targets, emtpy_spaces):
#     path = solve_single_robot(r, open_targets, emtpy_spaces)
#     if path == -1:
#         return []
#     return path

def solve_single_robot(r, open_targets, emtpy_spaces):
    logger = logging.getLogger("wegsuche")
    if r in open_targets:
        logger.debug("found solution after 0 states, len: 1")
        logger.info("solution: " + str(r) + " -> " + str([r]))
        return []
    paths = []
    next_paths = [[r]]
    counter = 1
    while paths != next_paths:
        logger.debug("front: 1")
        paths = next_paths
        next_paths = []
        for path in paths:
            next_gen = [i for i in get_surrounding_pos(path[-1]) if i in emtpy_spaces]
            direct_targets = [i for i in next_gen if i in open_targets]
            if len(direct_targets) > 0:
                path.append(direct_targets[0])
                logger.debug("found solution after " + str(counter) + " states, len: " + str(len(path)))
                logger.info("solution: "+str(r)+" -> "+str(path))
                return path
            for i in next_gen:
                new_path = path.copy()
                new_path.append(i)
                next_paths.append(new_path)
                emtpy_spaces.discard(i)
        counter += 1
    logger.critical("solution: no solution was found")
    return []


def compute_file(path: str):
    logger = logging.getLogger("wegsuche")
    logger.info("Reading " + str(path))
    with open(path) as f:
        robots, open_targets, emtpy_spaces, size = get_data(f.readlines())
        logger.info("goals: " + str(open_targets))
        for r in robots:
            logger.info("solve for " + str(r))
            path = solve_single_robot(r, open_targets.copy(), emtpy_spaces.copy())
            if len(path) > 0:
                emtpy_spaces.remove(path[-1])
                open_targets.remove(path[-1])
                emtpy_spaces.add(r)
            else:
                open_targets.discard(r)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("filename", help="name of the input file")
    group = parser.add_mutually_exclusive_group(required=False)
    group.add_argument("--verbose", "-v", help="sets verbose logging", action="store_true")
    group.add_argument("--quiet", "-q", help="sets quiet logging", action="store_true")
    return parser.parse_args()


def setup_logger(args):
    log_level = logging.INFO
    if args.verbose:
        log_level = logging.DEBUG
    elif args.quiet:
        log_level = logging.CRITICAL
    logging.basicConfig(level=log_level, datefmt='%Y-%m-%d %H:%M:%S',
                        # format='[%(asctime)s] %(levelname)s %(message)s',
                        format='%(levelname)s:%(name)s:%(message)s',
                        filename="paths.log")
    logger = logging.getLogger("wegsuche")


if __name__ == "__main__":
    args = parse_args()
    setup_logger(args)
    try:
        compute_file(args.filename)
    except Exception as e:
        logger = logging.getLogger("wegsuche")
        logger.error(e)