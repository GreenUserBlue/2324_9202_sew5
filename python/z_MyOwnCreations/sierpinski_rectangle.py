import matplotlib.pyplot as plt

colors=["Red","green","blue"]


def drawing(points, counter):
    if counter == 5:
        return
    x = points[0]
    y = points[1]
    plt.fill(x, y, color="green")
    # plt.fill(x, y, color=colors[counter%len(colors)])
    minX = min(x)
    minY = min(y)
    maxY = max(y)
    maxX = max(x)
    hl = (maxX - minX) / 2  # half_length --> zu faul zum schreiben
    counter = counter + 1
    # for i in range()
    drawing(([minX, minX, minX - hl, minX - hl], [minY, minY - hl, minY - hl, minY]), counter)
    drawing(([maxX, maxX, maxX + hl, maxX + hl], [minY, minY - hl, minY - hl, minY]), counter)
    drawing(([maxX, maxX, maxX + hl, maxX + hl], [maxY, maxY + hl, maxY + hl, maxY]), counter)
    drawing(([minX, minX, minX - hl, minX - hl], [maxY, maxY + hl, maxY + hl, maxY]), counter)


def controller(points):
    plt.figure(figsize=(5, 5))
    drawing(points, 0)
    plt.show()


if __name__ == "__main__":
    # args = get_parse()
    # setup_logging(args.verbose, args.quiet)
    x = [1100, 1300, 1300, 1100]
    y = [1100, 1100, 1300, 1300]
    controller((x, y))
