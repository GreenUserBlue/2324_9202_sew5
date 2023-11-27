import re

m1 = re.search(r"(abc)d(g)?", "abcdef")
if m1:
    print("works")
    print(m1.group(1))
    print(m1.group(2) )
else:
    print("doesn't")
