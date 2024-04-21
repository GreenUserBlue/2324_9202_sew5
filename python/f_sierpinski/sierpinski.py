import argparse


# Dateiname für das abzuspeichernde Bild
# Bildgröße z.B. 800x600 (Länge/Breite)
# Koordinate des ersten Dreieckspunktes (Default = links oben)
# Koordinate des zweiten Dreieckspunktes (Default = links unten)
# Koordinate des dritten Dreieckspunktes (Default = rechts mittig)
# minimale Seitenlänge (in Pixel) oder Rekursionstiefe. Diese beiden Werte sollen sich als Argument gegenseitg
# ausschließen
# logging (verbose oder quiet)
# Farbe des Dreiecks
# Farbe des Hintergrundes
# Farbe des Textes
# Achsen ausblenden

def getArgParse():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", "-f")
    parser.add_argument("--size", "-s", type=float, nargs=2, required=True)
    parser.add_argument("--point1", "-p1", type=float, nargs=2, default=[800.0, 600.0])
    parser.add_argument("--point2", "-p2", type=float, nargs=2, default=[800.0, 600.0])
    parser.add_argument("--point3", "-p3", type=float, nargs=2, default=[800.0, 600.0])
    return parser.parse_args()


if __name__ == "__main__":
    args = getArgParse()
    print(args)
