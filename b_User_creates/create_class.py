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
    return f"{outputDir}/creates.sh", f"{outputDir}/deletes.sh", f"{outputDir}/list.csv", f"{outputDir}/logfile.log"


def getUserNameSpecialChars(username):
    uName = str(username).lower()
    if uName[0].isnumeric():
        uName = f"k{uName}"
    return uName


def addCreateCommand(createFile, x, addedUsers):
    uName = getUserNameSpecialChars(x[0])
    if uName in addedUsers:
        raise Exception(f"User ${uName} already exists!")
    addedUsers.append(uName)
    createFile.write(
        f'useradd -d "/home/klassen/{uName}" -c "{x[0]} - {x[2]}" -m -g "klasse" -G cdrom,plugdev,sambashare -s /bin/bash {uName}\n')
    createFile.write(
        f'echo "{uName}:{str(x[0]).lower()}{get_password_random_char()}{x[1]}{get_password_random_char()}{x[2].lower()}{get_password_random_char()}" | chpasswd\n')


def get_password_random_char():
    passwdRndChar = "!%&(),._-=^#5"
    return passwdRndChar[math.floor(random.random() * len(passwdRndChar))]


def addDeleteCommand(deleteFile, x):
    uName = getUserNameSpecialChars(x[0])
    deleteFile.write("userdel -r "+str(uName)+"\n")
    pass


def addCommands(createFile, deleteFile, x, addedUsers):
    addCreateCommand(createFile, x, addedUsers)
    addDeleteCommand(deleteFile, x)


def createClasses(path, outputDir):
    os.makedirs(outputDir, exist_ok=True)
    wb = load_workbook(path, read_only=True)
    ws = wb[wb.sheetnames[0]]
    createPath, deletePath, listingPath, logPath = getSafeFilePaths(outputDir)
    with open(createPath, "w") as createFile, open(deletePath, "w") as deleteFile:
        createFile.write("#! /bin/sh\ngroupadd klasse\n")
        deleteFile.write("#! /bin/sh\n")

        addedUsers = []
        for row in ws.iter_rows(min_row=2):
            x = row[0].value, row[1].value, row[2].value
            if x[0] is None:
                break
            addCommands(createFile, deleteFile, x, addedUsers)

        addCommands(createFile, deleteFile, ("lehrer", "0", "JUE"), addedUsers)  # Jüngling
        addCommands(createFile, deleteFile, ("seminar", "0", "JUE"), addedUsers)  # Jüngling


# - ein Bash-Script2 mit allen notwendigen Schritten/Befehlen zum Erzeugen der Benutzer3
# – ein Bash-Script zum Löschen der angelegten Benutzer
# – eine Liste mit Usernamen und Passwörtern zum Verteilen an die unterrichtenden Lehrer
# – ein Logfile mit sinnvollen Angaben


# – der Dateiname für die Eingabedatei soll auf der Kommandozeile angegeben werden (ohne vorangestelltes
# -f o.ä.).
# – mit den Optionen -v für verbose und -q für quite soll der Umfang des Loggings eingestellt werden können
# (Loglevel).
# ∗ damit sollten sich auch die Bash-Scripts ändern

# Die Home-Verzeichnisse der Benutzer soll unter /home/klassen bzw. /home/lehrer/ angelegt werden,

# – Wichtig: eventuell braucht man im Script ein Escape (“\”) beim Passwort, aber nicht im Textfile!

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
