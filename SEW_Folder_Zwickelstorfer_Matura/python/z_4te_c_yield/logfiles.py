import gzip
import os
from collections import Counter
from typing import Iterator, Tuple, Generator

__author__ = "Zwickelstorfer Felix"


def open_files(filenames: Iterator[str]) -> Generator[str, None, None]:
    """opens all files"""
    for file in filenames:
        if file.endswith('.gz'):
            yield gzip.open(file, 'r')
        else:
            yield open(file)


def get_all_files(path: str) -> Generator[str, None, None]:
    """searches for all files"""
    if os.path.isdir(path):
        for i in os.listdir(path):
            if os.path.isdir(i):
                yield from get_all_files(i)
            else:
                yield i
    else:
        if os.path.exists(path):
            yield path


def read_lines(files: Iterator[str]) -> Generator[str, None, None]:
    """reads all the lines for the files"""
    for f in files:
        for line in f:
            yield line


def get_pc_ip(lines: Iterator[str]) -> Generator[str, None, None]:
    """searches the Client IP in the lines"""
    for line in lines:
        yield line.strip().split()[0]


def get_bytes(lines: Iterator[str]) -> Generator[str, None, None]:
    """searches the bytes in the lines"""
    for line in lines:
        line = str(line).split("]", 1)[1].strip().split("\"")
        if len(line) > 2:
            line = line[2].strip().split(" ")
            if len(line) >= 2:
                line = line[1].strip()
                yield line


def get_visited_page(lines: Iterator[str]) -> Tuple[str, int]:
    """searches the visited page in the lines"""

    for line in lines:
        line = str(line).strip().split("\"")
        if len(line) > 2:
            line = line[1].strip().split(" ")
            if len(line) >= 2:
                yield line[1].strip()


def countMostCommon(lines: Iterator[str]) -> Tuple[str, int]:
    """counts the most common lines in the list"""
    res = Counter()
    for line in lines:
        res[line] += 1
    return res.most_common(1)[0]


def calcSum(lines: Iterator[str]) -> int:
    """calcs the sum of all the lines"""
    summ = 0
    for i in lines:
        summ += int(i)
    return summ


print(countMostCommon(get_pc_ip(read_lines(open_files(get_all_files("./../res/c_res/access.log"))))))
print(countMostCommon(get_visited_page(read_lines(open_files(get_all_files("./../res/c_res/access.log"))))))
print(calcSum(get_bytes(read_lines(open_files(get_all_files("./../res/c_res/access.log"))))))

print("")
print("With Zip File (2 times the original)")
print(countMostCommon(get_visited_page(read_lines(open_files(get_all_files("./../res/c_res/access.gz"))))))
