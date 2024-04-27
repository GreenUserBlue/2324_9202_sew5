__author__ = "Zwickelstorfer Felix"

import functools
from math import floor
from typing import Generator


class BritishLength:
    pass


@functools.total_ordering
class BritishLength:
    """ represents the englisch Metricsystem
>>> BritishLength.limit
12
"""
    limit = 12

    def __init__(self, feet=1, inches=0):
        """
>>> BritishLength(3,17)
4 ft 5 in (1.3462 m)
>>> BritishLength(4,-8)
3 ft 4 in (1.0160 m)
>>> BritishLength(-6,5)
-5 ft -7 in (-1.7018 m)
>>> BritishLength(2,28)
4 ft 4 in (1.3208 m)
>>> BritishLength()
1 ft (0.3048 m)
>>> BritishLength(0)
0 in (0 m)
        """
        self._inches = inches + feet * BritishLength.limit

    def __str__(self) -> str:
        return (str(self.feet) + ' ft ' if self.feet != 0 else "") + (
            str(self.inches) + " in " if self.inches != 0 or self._inches == 0 else "") + "(" + (
                   f'{self.meters:.4f}' if self._inches != 0 else "0") + " m)"

    @property
    def feet(self) -> int:
        return floor(self._inches / BritishLength.limit) + (
            1 if self._inches <= -BritishLength.limit and self._inches % BritishLength.limit != 0 else 0)

    @property
    def inches(self) -> int:
        return self._inches % BritishLength.limit - (
            BritishLength.limit if self._inches < 0 and self._inches % BritishLength.limit != 0 else 0)

    @property
    def meters(self) -> float:
        return round(self._inches * 0.0254, 4)

    @inches.setter
    def inches(self, inches):
        self._inches = inches

    def __add__(self, other) -> BritishLength:
        """
>>> print(BritishLength(7, 8) + BritishLength(3, 9))
11 ft 5 in (3.4798 m)
        """
        if isinstance(other, BritishLength):
            return BritishLength(0, self._inches + other._inches)
        else:
            return NotImplemented

    def __sub__(self, other) -> BritishLength:
        if isinstance(other, BritishLength):
            return BritishLength(0, self._inches - other._inches)
        else:
            return NotImplemented

    def __mul__(self, other) -> BritishLength:
        """
        >>> print(BritishLength(2, 1) * 15)
        31 ft 3 in (9.5250 m)
        >>> print(BritishLength(2,6) * -2)
        -5 ft (-1.5240 m)
        """
        if isinstance(other, int):
            return BritishLength(0, self._inches * other)
        else:
            return NotImplemented

    def __truediv__(self, other) -> BritishLength:
        """ rundet ab bei ungerade ergebnissen
        >>> print(BritishLength(15, 1) / 15)
        1 ft (0.3048 m)
        >>> print(BritishLength(15, 0) / 15)
        1 ft (0.3048 m)
        >>> print(BritishLength(-15, -1) / 15)
        -1 ft -1 in (-0.3302 m)
        """
        if isinstance(other, int):
            return BritishLength(0, self._inches // other)
        else:
            return NotImplemented

    def __divmod__(self, other) -> BritishLength:
        return self.__truediv__(other)

    def __eq__(self, other) -> bool:
        """
>>> print((BritishLength(7, 8) + BritishLength(3, 9)) == BritishLength(11, 5))
True
        """
        if isinstance(other, BritishLength):
            return self._inches == other._inches
        else:
            return NotImplemented

    def __lt__(self, other) -> bool:
        """
>>> print((BritishLength(7, 8) + BritishLength(3, 9)) < BritishLength(12, 5))
True
        """
        if isinstance(other, BritishLength):
            return self._inches < other._inches
        else:
            return NotImplemented

    def __repr__(self):
        return str(self)


def fibo() -> Generator[BritishLength, None, None]:
    """
>>> x = fibo()
>>> print([x.__next__() for i in range(10)])
[1 ft 1 in (0.3302 m), 1 ft 1 in (0.3302 m), 2 ft 2 in (0.6604 m), 3 ft 3 in (0.9906 m), 5 ft 5 in (1.6510 m), 8 ft 8 in (2.6416 m), 14 ft 1 in (4.2926 m), 22 ft 9 in (6.9342 m), 36 ft 10 in (11.2268 m), 59 ft 7 in (18.1610 m)]
    """
    last = 0
    cur = 13
    yield BritishLength(0, cur)
    while True:
        newVal = cur + last
        last = cur
        cur = newVal
        yield BritishLength(0, newVal)


if __name__ == '__main__':
    print('PyCharm')
    print(BritishLength(3, 17))
    print(BritishLength(4, -8))
    print(BritishLength(-6, 5))
    print(BritishLength(2, -28))
    print()
    print(BritishLength(7, 8) + BritishLength(3, 9))
    print((BritishLength(7, 8) + BritishLength(3, 9)) == BritishLength(11, 5))
    print((BritishLength(7, 8) + BritishLength(3, 9)) < BritishLength(12, 5))
    print()
    print(BritishLength(2, 1))
    print(BritishLength(2, 1) * 15)
    print()
    x = fibo()
    print([x.__next__() for i in range(10)])

    print("Hallo")
    a = BritishLength()
    print(a)
    a.inches = 122
    print(a)
