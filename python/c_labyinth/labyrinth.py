import argparse
import time


class LabyrinthSolver:
    def __init__(self, filename, x_start, y_start, should_print, print_time, delay):
        self.labyrinth = self.load_labyrinth(filename)
        self.x_start = x_start
        self.y_start = y_start
        self.should_print = should_print
        self.print_time = print_time
        self.delay = delay
        self.solutions = set()

    def load_labyrinth(self, filename):
        with open(filename, 'r') as file:
            labyrinth = [line.strip() for line in file]
        return labyrinth

    def search_all(self, zeile, spalte, lab):
        if zeile < 0 or spalte < 0 or zeile >= len(lab) or spalte >= len(lab[0]):
            return 0
        if lab[zeile][spalte] == 'A':
            self.add_solution(lab)
            return 1
        if lab[zeile][spalte] == '#' or lab[zeile][spalte] == 'X':
            return 0
        amount = 0
        old = lab[zeile][spalte]
        lab[zeile] = lab[zeile][:spalte] + 'X' + lab[zeile][spalte + 1:]

        self.print_solution(lab)

        amount += self.search_all(zeile + 1, spalte, lab)
        amount += self.search_all(zeile, spalte + 1, lab)
        amount += self.search_all(zeile - 1, spalte, lab)
        amount += self.search_all(zeile, spalte - 1, lab)
        lab[zeile] = lab[zeile][:spalte] + old + lab[zeile][spalte + 1:]
        return amount

    def solve_labyrinth(self):
        start_time = time.time()

        amount = self.search_all(self.x_start, self.y_start, self.labyrinth)
        print(amount)
        elapsed_time = (time.time() - start_time) * 1000
        # for i in self.solutions:
        #     print(str(i))
        # print(len(self.solutions))
        if self.print_time:
            print(f"Total calculation time: {elapsed_time:.2f} ms")

    def add_solution(self, solution):
        s = ""
        for i in solution:
            s += str(i) + '\n'
        self.solutions.add(s)

    def print_solution(self, solution):
        s = ""
        for i in solution:
            s += str(i) + '\n'

        print(s)

    def run(self):
        self.solve_labyrinth()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Calculate number of ways through a labyrinth")
    parser.add_argument("filename", help="file containing the labyrinth to solve")
    parser.add_argument("-x", "--xstart", type=int, default=5, help="x-coordinate to start")
    parser.add_argument("-y", "--ystart", type=int, default=5, help="y-coordinate to start")
    parser.add_argument("-p", "--print", action="store_true", help="print output of every solution")
    parser.add_argument("-t", "--time", action="store_true", help="print total calculation time (in milliseconds)")
    parser.add_argument("-d", "--delay", type=int, default=0, help="delay after printing a solution (in milliseconds)")

    args = parser.parse_args()

    solver = LabyrinthSolver(args.filename, args.xstart, args.ystart, args.print, args.time, args.delay)
    solver.run()
