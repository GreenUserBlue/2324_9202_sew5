import random

__author__ = "Zwickelstorfer Felix"

from math import ceil

firstPrimes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
               47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107,
               109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181,
               191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263,
               269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349,
               353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433,
               439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521,
               523, 541]


def is_prim_millerrabin(n: int, k: int) -> bool:
    """checks if a number is miller rabin prime"""
    if n == 2:
        return True
    if (n & 1) == 0:
        return False
    r, d = 0, n - 1
    while d % 2 == 0:
        r += 1
        d //= 2
    for _ in range(k):
        a = random.randrange(2, n - 1)
        x = pow(a, d, n)
        if x == n - 1 or x == 1:
            continue
        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                break
        else:
            return False
    return True


def isPrime(value: int) -> bool:
    """checks if a number is probably prime"""
    if value in firstPrimes:
        return True
    for i in firstPrimes:
        if value % i == 0:
            return False
    return is_prim_millerrabin(value, 20)


def generate_prime(key_size: int) -> int:
    """generates a single prime number with key_bize bitlength"""
    max = (2 << key_size) - 1
    val = random.randrange(max // 2, max, 2)
    while not isPrime(val):
        val += 2
    return val


def printBinary(nbr: int, bitPerLine: int) -> None:
    """prints numbers in binary
    >>> printBinary(191926127353949 - 1 - 4 - 8 - 16 - 64, 12)
    1 1 111 1
    111  1  1
    1 1  1  111

    """
    s = "{0:b}".format(nbr).replace("0", " ")
    for i in range(0, ceil(len(s) / bitPerLine)):
        print(s[i * bitPerLine:min((i + 1) * bitPerLine, len(s))])


if __name__ == '__main__':
    print(is_prim_millerrabin(1_234_567_891, 5))  # True
    print(is_prim_millerrabin(1_234_567_891 ** 2, 5))  # False
    # print(is_prim_millerrabin(220, 5))  # False
    # print(is_prim_millerrabin(29, 5))  # True
    # print(is_prim_millerrabin(17, 5))  # True
    # # print(generate_prime(512))
    # printBinary(191926127353949, 12)
