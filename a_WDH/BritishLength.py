class BritishLength:
    pass


class BritishLength:
    """
>>> a=britishlength(pounds=2)
>>> a
Britishlength(0, 2)
>>> print(a)
0 st 2 lb
>>> b=britishlength(pounds=15)
>>> b
Britishlength(1, 1)
>>> print(b)
1 st 1 lb
>>> c=britishlength(1,12)
>>> print(a+b)
1 st 3 lb
>>> print(a+b+c)
3 st 1 lb
"""

    @property
    def pounds(self) -> int:
        return ((self._pounds % 14) + 14) % 14

    @property
    def stones(self) -> int:
        return self._pounds // 14

    def __init__(self, stone=0, pounds=0):
        self._pounds = stone * 14 + pounds

    def __repr__(self) -> str:
        return f'Britishlength({self.stones}, {self.pounds})'

    def __str__(self) -> str:
        return f'{self.stones} st {self.pounds} lb'

    def __add__(self, other) -> BritishLength:
        if isinstance(other, BritishLength):
            return BritishLength(pounds=self._pounds + other._pounds)
        return NotImplemented
