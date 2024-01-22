import argparse
import time


class LabyrinthSolver:
    """
    >>> solver = LabyrinthSolver("l2.txt", 5, 5, False, 0, 0)
    >>> solver.solve_labyrinth()
    nbr of solutions: 486
    """

    border_char = '#'
    visited_char = 'X'
    target_char = 'A'

    def __init__(self, filename, x_start, y_start, should_print, print_time, delay):
        self.labyrinth = self.load_labyrinth(filename)
        self.x_start = x_start
        self.y_start = y_start
        self.should_print = should_print
        self.print_time = print_time
        self.delay = delay
        self.solutions = set()

    def load_labyrinth(self, filename):
        """ reads the labyrinth from the file """
        with open(filename, 'r') as file:
            labyrinth = [line.strip() for line in file]
        return labyrinth

    def search_all(self, zeile, spalte, lab):
        """searches all solutions"""
        if zeile < 0 or spalte < 0 or zeile >= len(lab) or spalte >= len(lab[0]):
            return 0
        if lab[zeile][spalte] == LabyrinthSolver.target_char:
            # self.add_solution(lab)
            if self.should_print:
                self.print_solution(lab)
                time.sleep(self.delay / 1000)
            return 1
        if lab[zeile][spalte] == LabyrinthSolver.border_char or lab[zeile][spalte] == LabyrinthSolver.visited_char:
            return 0
        amount = 0
        old = lab[zeile][spalte]
        lab[zeile] = lab[zeile][:spalte] + LabyrinthSolver.visited_char + lab[zeile][spalte + 1:]

        amount += self.search_all(zeile + 1, spalte, lab)
        amount += self.search_all(zeile, spalte + 1, lab)
        amount += self.search_all(zeile - 1, spalte, lab)
        amount += self.search_all(zeile, spalte - 1, lab)
        lab[zeile] = lab[zeile][:spalte] + old + lab[zeile][spalte + 1:]
        return amount

    def solve_labyrinth(self):
        """solves the labyrinth and prints the outputs"""
        start_time = time.time()

        amount = self.search_all(self.x_start, self.y_start, self.labyrinth)
        elapsed_time = (time.time() - start_time) * 1000

        print(f"nbr of solutions: {amount}")

        # if self.should_print:
        #     for i in self.solutions:
        #         print(str(i))
        #         print()
        #
            # print(f"nbr of solutions: {amount}")

        if self.print_time:
            print(f"Total calculation time: {elapsed_time:.2f} ms")

    # def add_solution(self, solution):
    #     s = ""
    #     for i in solution:
    #         s += str(i) + '\n'
    #     self.solutions.add(s)

    def print_solution(self, solution):
        """prints the labyrinth"""
        s = ""
        for i in solution:
            s += str(i) + '\n'
        print(s)


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
    solver.solve_labyrinth()
