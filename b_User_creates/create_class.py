import logging
import math
import random
import sys
import os
from openpyxl import load_workbook

__author__ = "Zwickelstorfer Felix"

# def start_program():
#     """runs the logic for the program"""
#     args = sys.argv
#     print(args)
#  # TODO change
#     logging.basicConfig(level=getattr(logging, args), format='[%(asctime)s] %(levelname)s %(message)s',
#                             datefmt='%Y-%m-%d %H:%M:%S')
#     logging.log(logging.INFO, "Logging turned on " + args.loglevel)
#     if args.keygen:
#         minSize = 17
#         if args.keygen < minSize:
#             logging.log(logging.ERROR, "Keygen bit size must be greater than " + str(minSize))
#         else:
#             logging.log(logging.INFO, "Trying to generate keys with " + str(args.keygen) + " bit")


def getSafeFilePaths(outputDir):

    return f"{outputDir}/creates.sh",f"{outputDir}/deletes.sh",f"{outputDir}/list.csv",f"{outputDir}/logfile.log"


def addCreateCommand(createFile, x):
    createFile.write(f'useradd -d "/home/klassen/k{str(x[0]).lower()}" -c "{x[0]} - {x[2]}" -m -g "klasse" -G cdrom,plugdev,sambashare -s /bin/bash k{str(x[0]).lower()}\n')
    createFile.write(f'echo "k{str(x[0]).lower()}:{str(x[0]).lower()}{get_password_random_char()}{x[1]}{get_password_random_char()}{x[2].lower()}{get_password_random_char()}" | chpasswd\n')
    pass


def get_password_random_char():
    passwdRndChar = "!%&(),._-=^#5"
    return passwdRndChar[math.floor(random.random() * len(passwdRndChar))]
def addDeleteCommand(deleteFile, x):
    # deleteFile.write(x)
    pass


def createClasses(path, outputDir):
    os.makedirs(outputDir, exist_ok=True)
    wb = load_workbook(path, read_only=True)
    ws = wb[wb.sheetnames[0]]
    createPath,deletePath,listingPath,logPath = getSafeFilePaths(outputDir)
    with open(createPath,"w") as createFile, open(deletePath,"w") as deleteFile:
        for row in ws.iter_rows(min_row=2):
            x = row[0].value,row[1].value,row[2].value
            print(x)
            addCreateCommand(createFile, x)
            addDeleteCommand(deleteFile,x)


#  ein Bash-Script2 mit allen notwendigen Schritten/Befehlen zum Erzeugen der Benutzer3
# – ein Bash-Script zum Löschen der angelegten Benutzer
# – eine Liste mit Usernamen und Passwörtern zum Verteilen an die unterrichtenden Lehrer
# – ein Logfile mit sinnvollen Angaben
# • dabei ist zu beachten
# – der Dateiname für die Eingabedatei soll auf der Kommandozeile angegeben werden (ohne vorangestelltes
# -f o.ä.).
# – mit den Optionen -v für verbose und -q für quite soll der Umfang des Loggings eingestellt werden können
# (Loglevel).
# ∗ damit sollten sich auch die Bash-Scripts ändern
# – die Benutzernamen sollen
# ∗ Klassen erhalten ein zusätzliches k als erstes Zeichen – der Name sollte nicht mit einer Ziffer beginnen
# ∗ sollen in Kleinbuchstaben angegeben werden
# ∗ etwaige Sonderzeichen (Umlaute) sollten ersetzt werden
# ∗ der Kommentar (gecos field) sollte auf einen sinnvollen Wert gesetzt werden
# – es gibt zwei zusätzliche Benutzer: lehrer und seminar

# Die Home-Verzeichnisse der Benutzer soll unter /home/klassen bzw. /home/lehrer/ angelegt werden,
# z.B. /home/klassen/k1ai
# ∗ diese Ordner existieren eventuell nicht
# – Die Linux-User müssen in folgenden existierenden System-Gruppen sein: cdrom,plugdev,sambashare

# – Wichtig: eventuell braucht man im Script ein Escape (“\”) beim Passwort, aber nicht im Textfile!
# – Man sollte auch
# ∗ den Benutzername (“Gecos”)
# ∗ eine sinnvolle Shell (/bin/bash)
# ∗ das Passwort (mittels chpasswd)
# setzen
# .
# – bei bereits existierenden Benutzern sollte eine Fehlermeldung erfolgen.
# – die Bash-Scripts sollten bei Problemen abbrechen
# – Logging
# ∗ Wir wollen einen RotatingFileHandler mit maximal 10.000 Bytes7 und maximal 5 alte Versionen
# (backup).
# ∗ Zusätzlich ist ein StreamHandler (=Ausgabe auf der Konsole) anzulegen.
# ∗ Tipp: je nach Kommandozeilen-Parametern kann man zB. logging.basicConfig(level=logging.DEBUG)
# oder logger.setLevel(logging.WARNING) aufrufen.
# ∗ Alternative: logging.config.fileConfig('logging.conf')
# ∗ es sollte für jede Art von Meldung mindestens einen Aufruf geben
# · Fehler “Datei nicht gefunden” statt Exception
# · Debug: alle Details zur Fehlersuche


if __name__ == "__main__":
    # start_program()
    createClasses("./res/Klassenraeume_2023.xlsx", "./outClasses")