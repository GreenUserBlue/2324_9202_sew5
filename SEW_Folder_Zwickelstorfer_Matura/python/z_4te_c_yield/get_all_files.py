import os


def get_all_files(pathname):
    for file in os.listdir(pathname):
        x = os.path.join(pathname, file)
        if os.path.isdir(x):
            yield from get_all_files(x)
        else:
            yield x


for i in get_all_files(".\..\\"):
    print(i)
