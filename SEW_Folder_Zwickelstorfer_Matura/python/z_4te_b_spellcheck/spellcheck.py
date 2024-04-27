__author__ = "Zwickelstorfer Felix"


def split_word(word):
    return [(word[:i], word[i:]) for i in range(len(word) + 1)]


def edit1(word):
    possibleAdds = "abcdefghijklmnopqrstuvwxyzäöüß"
    splitedWords = split_word(word)
    a = set(j[0] + i + j[1] for j in splitedWords for i in possibleAdds)
    b = set(word[:i] + word[i + 1:i + 2] + word[i:i + 1] + word[i + 2:] for i in range(len(word)))
    c = set(word[:j] + i + word[j + 1:] for j in range(len(word)) for i in possibleAdds)
    d = set(j[0] + j[1][1:] for j in splitedWords)
    return a | b | c | d



def edit1_good(word, allWords):
    return edit1(word) & set(x.lower() for x in allWords)


def read_all_words(filename):
    return set(line.rstrip().lower() for line in open(filename))


def edit2_good(word, allWords):
    return set(j for i in edit1(word) for j in edit1_good(i, allWords))


def correct(word, wordlist):
    """
    >>> woerter = read_all_words("../res/german.dic")
>>> correct("Aalsuppe", woerter)
{'aalsuppe'}
>>> correct("Alsuppe", woerter)
{'aalsuppe'}
>>> sorted(correct("Alsupe", woerter))
['aalsuppe', 'absude', 'anhupe', 'elspe', 'lupe']
    """
    word2 = word.lower()
    return {word2} & wordlist or edit1_good(word2, wordlist) or edit2_good(word2, wordlist) or {word}


if __name__ == "__main__":
    allWords = read_all_words("../res/german.dic")
    print(split_word("abc"))
    print(len(edit1("abc")))
    print(edit1_good("abd", {"abc", "acb", "abcd", "abcde"}))
    print(edit2_good("abd", {"abc", "acb", "abcd", "abcde", "abcdef"}))
    print(correct("abd", {"abc", "acb", "abcd", "abcde", "abcdef"}))
    print(correct("Hello", allWords))
