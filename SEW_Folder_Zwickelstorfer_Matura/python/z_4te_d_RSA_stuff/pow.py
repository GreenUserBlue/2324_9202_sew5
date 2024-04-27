__author__ = "Zwickelstorfer Felix"

from time import time
from typing import Tuple


def pow_iter(a: int, b: int, n: int = 0) -> int:
    """ calculates the pow with mod or mod inverse
    >>> for x in [[10,20,30],[10,20,30],[30,50,70],[88,69,22]]:
    ...     y = pow_iter(x[0],x[1],x[2])
    ...     z = pow(x[0],x[1],x[2])
    ...     assert y == z
    >>> for x in [[37,81]]:
    ...     y = pow_iter(x[0],-1, x[1])
    ...     z = pow(x[0],-1,x[1])
    ...     assert y == z
    """
    if b > 0:
        cur = a
        res = 1
        while b > 0:
            if b & 1 != 0:
                res *= cur
                res %= n
            b >>= 1
            cur *= cur
            cur %= n
        return res
    if b == -1:
        return _mod_inverse(a, n)


def pow_rec(a: int, b: int, n: int = 0) -> int:
    """ calculates the pow with mod or mod inverse
    >>> for x in [[10,20,30],[10,20,30],[30,50,70],[88,69,22]]:
    ...     y = pow_rec(x[0],x[1],x[2])
    ...     z = pow(x[0],x[1],x[2])
    ...     assert y == z
    >>> for x in [[37,81]]:
    ...     y = pow_rec(x[0],-1, x[1])
    ...     z = pow(x[0],-1,x[1])
    ...     assert y == z
    """
    if b == 1:
        return a
    if b > 0:
        if b & 1 == 0:
            return (pow_rec(a, b // 2, n) ** 2) % n
        else:
            return (pow_rec(a, (b - 1) // 2, n) ** 2 * a) % n
    if b == -1:
        return _mod_inverse(a, n)


def _mod_inverse_sub_function(a: int, b: int) -> Tuple[int, int, int]:
    """calculates recursive the mod inverse"""
    if a == 0:
        return b, 0, 1
    else:
        g, y, x = _mod_inverse_sub_function(b % a, a)
        return g, x - (b // a) * y, y


def _mod_inverse(a: int, b: int) -> int:
    """calculates the mod inverse and checks if it there is even one possible"""
    fTup = _mod_inverse_sub_function(a, b)
    if fTup[0] != 1:
        raise Exception("Mod inverse not possible!")
    return fTup[1] % b


def doTestTime():
    print("For pow \"normal\":")
    t = time()
    for i in range(100_000_0):
        # pow(37, -1, 81)
        pow(2 << 17, 23, 81)
    print("Python Pow:", time() - t, "s")

    t = time()
    for i in range(100_000_0):
        # pow_rec(37, -1, 81)
        pow_rec(2 << 17, 23, 81)
    print("Rec Pow:", time() - t, "s")

    t = time()
    for i in range(100_000_0):
        # pow_iter(37, -1, 81)
        pow_iter(2 << 17, 23, 81)
    print("Iter Pow:", time() - t, "s")
    print()
    print("For mod inverse:")
    for i in range(100_000_0):
        pow(37, -1, 81)
    print("Python Pow:", time() - t, "s")

    t = time()
    for i in range(100_000_0):
        pow_rec(37, -1, 81)
    print("Rec Pow:", time() - t, "s")

    t = time()
    for i in range(100_000_0):
        pow_iter(37, -1, 81)
    print("Iter Pow:", time() - t, "s")


if __name__ == '__main__':
    # for x in [[30, 51, 70], [88, 69, 22], [10, 20, 30], [10, 20, 30]]:
    #     y = myPowIter(x[0], x[1], x[2])
    #     z = pow(x[0], x[1], x[2])
    #     print("1_wrong: ", y)
    #     print("correct: ", z)
    #     print()
    #     assert y == z
    print(_mod_inverse(37, 81))
    print(pow(37, -1, 81))
    doTestTime()
