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
#         print("empty")
#         return []
#     return path

def solve_single_robot(r, open_targets, emtpy_spaces):
    if r in open_targets:
        return []
    paths = []
    next_paths = [[r]]

    while paths != next_paths:
        paths = next_paths
        next_paths = []
        for path in paths:
            next_gen = [i for i in get_surrounding_pos(path[-1]) if i in emtpy_spaces]
            direct_targets = [i for i in next_gen if i in open_targets]
            if len(direct_targets) > 0:
                path.append(direct_targets[0])
                return path
            for i in next_gen:
                new_path = path.copy()
                new_path.append(i)
                next_paths.append(new_path)
                emtpy_spaces.discard(i)
    return []



def compute_file(path: str):
    with open(path) as f:
        robots, open_targets, emtpy_spaces, size = get_data(f.readlines())
        for r in robots:
            path = solve_single_robot(r, open_targets.copy(), emtpy_spaces.copy())
            if len(path) > 0:
                emtpy_spaces.remove(path[-1])
                open_targets.remove(path[-1])
                emtpy_spaces.add(r)
            else:
                open_targets.discard(r)
            print(path)


compute_file("./room_3.txt")
