__author__ = "Zwickelstorfer Felix"

from datetime import datetime


def isPrim(x):
    """checks if a file is pirme"""
    for i in primes():
        if x % i == 0:
            return False
        if i * i > x:
            return True


def primes():
    """returns a generator for all primes and stores it in a cash"""
    yield from primes.numbers
    x = primes.numbers[- 1]
    while True:
        x += 2
        if isPrim(x):
            primes.numbers.append(x)
            yield x


primes.numbers = [2, 3, 5, 7, 11]

# Tests
t1 = datetime.now()
tPrime = primes()
for i in range(200_000):
    tPrime.__next__()
t2 = datetime.now()

for i in range(200_000):
    tPrime.__next__()
t3 = datetime.now()

erg = []
for _, prime in zip(range(100), primes()):
    erg.append(prime)
erg1 = []

x = primes()
last = next(x)
while last <= 100_000:
    erg1.append(last)
    last = next(x)

erg2 = primes()
erg3 = 0

# for prime in range(200_000):
#     erg3 = next(erg2)

print(erg1)
print(erg3)

print("Time till 200_000", t2 - t1)
print("Time till 400_000", t3 - t1)