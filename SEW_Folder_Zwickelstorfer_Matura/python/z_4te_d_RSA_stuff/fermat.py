from collections import Counter

__author__ = "Zwickelstorfer Felix"

from typing import List, Tuple, Union


def showStatistic():
    """shows the statistic"""
    myList = (2, 3, 4, 5, 7, 11, 9, 15, 997, 6601, 8911)
    for i in myList:
        x = getSingleStatisticPercent(i)
        print(str(i) + ": ", "len:", len(x), x)


def getSingleStatisticPercent(nbr: int) -> List[Tuple[int, float]]:
    """searches for the statistic for a single value"""
    count = Counter()
    for i in range(1, nbr):
        count[pow(i, nbr - 1, nbr)] += 1
    return [(i, count[i] / (nbr - 1) * 100) for i in count]


if __name__ == '__main__':
    showStatistic()
