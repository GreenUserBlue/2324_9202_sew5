import functools


def _is_valid_operand(other):
    return (hasattr(other, "_denominator") and
            hasattr(other, "_numerator"))


def gcd(x, y):
    """
>>> gcd(100,75)
25
>>> gcd(100,7)
1
>>> gcd(-12,-16)
-4
"""
    z = x % y
    if z == 0:
        return y
    return gcd(y, z)


def as_integer_ratio(numerator):
    """
>>> as_integer_ratio(5.5)
Fraction(11, 2)
>>> as_integer_ratio(0.125)
Fraction(1, 8)
>>> as_integer_ratio(-0.125)
Fraction(-1, 8)
"""
    mul = 10 ** len(str(numerator).split('.')[1])
    return Fraction(int(numerator * mul), int(mul))
    # den = 1
    # while numerator != int(numerator):
    #     den *= 10
    #     numerator *= 10
    # return Fraction(int(numerator), int(den))


@functools.total_ordering
class Fraction:
    """ Klasse für Bruchzahlen
>>> fraction1 = Fraction(1,2)
>>> fraction1 # __repr__
Fraction(1, 2)
>>> print(fraction1) # __str__
1/2
>>> f2 = Fraction(1,4)
>>> print(f2)
1/4
>>> fraction1+1
Fraction(3, 2)
>>> fraction1+f2
Fraction(3, 4)
>>> print(3 + 1 / (7 + Fraction(1, 15)))
3 15/106
>>> print(Fraction(1 , 4) + Fraction(-1 , 8))
1/8
>>> print(Fraction(-2,-4))
1/2
>>> print(Fraction (2,1))
2
>>> print(Fraction (-2,1))
-2
>>> print(Fraction (-4,-2))
2
>>> frac=Fraction(10,5)
>>> frac.numerator
2
>>> frac.denominator
1
>>> print(Fraction(1.5))
1 1/2
>>> print(Fraction(1.5,2.5))
3/5
>>> print(Fraction(5,2)==2.5)
True
>>> print(Fraction(-5,-2)<2.5)
False
>>> print(Fraction(3,2)*2)
3
>>> print(2*Fraction(3,2))
3
>>> print(Fraction(5,3)/Fraction(2,7))
5 5/6
>>> f=Fraction(2,5)
>>> f.numerator
2
>>> f.denominator
5
"""

    def __init__(self, numerator=0, denominator=1):  # always: self as 1st parameter
        """Constructor for Method
        >>> Fraction()
        Fraction(0, 1)
        >>> Fraction(0.5,-1)
        Fraction(-1, 2)
        >>> Fraction(0,-2)
        Fraction(0, 1)
        """

        if abs(denominator) == 0:
            raise ArithmeticError
        if isinstance(numerator, float):
            numerator = as_integer_ratio(numerator)

        if isinstance(denominator, float):
            denominator = as_integer_ratio(denominator)

        if isinstance(numerator, Fraction) or isinstance(denominator, Fraction):
            frac = numerator / denominator
            numerator = frac._numerator
            denominator = frac._denominator

        if denominator < 0:
            denominator *= -1
            numerator *= -1

        self._numerator: int = numerator
        self._denominator: int = denominator
        self.reduce_fraction()

    def reduce_fraction(self):
        ggt = gcd(self._numerator, self._denominator)
        # if ggt > 1:
        self._numerator = int(self._numerator // ggt)
        self._denominator = int(self._denominator // ggt)

    def __repr__(self):
        return f'Fraction({self._numerator}, {self._denominator})'

    def __str__(self) -> str:
        if abs(self._numerator % self._denominator) == 0:
            return str(int(self._numerator / self._denominator))
        if abs(self._numerator) > abs(self._denominator):
            return (f'{int(self._numerator / self._denominator)} '
                    f'{int(abs(self._numerator) % self._denominator)}/{self._denominator}')
        return f'{self._numerator}/{self._denominator}'

    def __float__(self) -> float:
        return self._numerator / float(self._denominator)

    def __int__(self) -> int:
        return self._numerator // self._denominator

    def __add__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return self + Fraction(other)
        if isinstance(other, Fraction):
            return Fraction(self._numerator * other._denominator + other._numerator * self._denominator,
                            self._denominator * other._denominator)
        return NotImplemented

    def __radd__(self, other):
        return self + other

    def __rsub__(self, other):
        return (self - other) * -1

    def __sub__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return self - Fraction(other)
        if isinstance(other, Fraction):
            return self + Fraction(-other._numerator, other._denominator)
        return NotImplemented

    def __floordiv__(self, other):
        return self.__truediv__(other)

    def __truediv__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return self / Fraction(other)
        if isinstance(other, Fraction):
            return Fraction(self._numerator * other._denominator, self._denominator * other._numerator)
        return NotImplemented

    def __rfloordiv__(self, other):
        return self.__rtruediv__(other)

    def __rtruediv__(self, other):
        return Fraction(1) / (self / other)

    def __lt__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return self < Fraction(other)
        if isinstance(other, Fraction):
            return (self._numerator * other._denominator <
                    other._numerator * self._denominator)
        if isinstance(other, float):
            return float(self._numerator) / float(self._denominator) < other
        return NotImplemented

    def __eq__(self, other) -> bool:
        if isinstance(other, int) or isinstance(other, float):
            return self == Fraction(other)
        if isinstance(other, Fraction):
            fraction1 = Fraction(self._numerator, self._denominator)
            fraction2 = Fraction(other._numerator, other._denominator)
            return ((fraction1._numerator == fraction2._numerator and
                     fraction1._denominator == fraction2._denominator)
                    or (fraction1._numerator == 0 and fraction2._numerator == 0))
        return NotImplemented

    def __abs__(self):
        return Fraction(abs(self._numerator), abs(self._denominator))

    def __mul__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return self * Fraction(other)
        if isinstance(other, Fraction):
            return Fraction(self._numerator * other._numerator, self._denominator * other._denominator)
        return NotImplemented

    def __rmul__(self, other):
        return self * other

    @property
    def numerator(self):
        return self._numerator

    @property
    def denominator(self):
        return self._denominator


if __name__ == '__main__':
    print(Fraction(1/3))
    print(1/3)
    print(Fraction(3/10))
    f1 = Fraction(1, 2)
    print(f1 + 2)
    print(f1 + Fraction(2, 8))
    print(1 / Fraction(3, 5))
    print(3 + 1 / (7 + Fraction(1, 15)))
    print(Fraction(1, 4) + Fraction(-1, 8))
    print(Fraction(-2, -4) - Fraction(2, -4))
    print(Fraction(1_000_000_000_000_000_001, 1_000_000_000_000_000_000) < 1)
    print(Fraction(-5, 3))
#
