import argparse
import hashlib
import logging
import os
import shutil
from math import sqrt, floor
from pathlib import Path
import random
from typing import Generator, Tuple, List
import miller_rabin
from sympy import gcd

__author__ = "Zwickelstorfer Felix"


def get_path_private_key() -> str:
    """ the path to the private key
    """
    return str(os.path.expanduser('~')) + r'\.zwick_ssh\id_rsa_zwick'


def get_path_public_key() -> str:
    """ the path to the public key
    """
    return get_path_private_key() + ".pub"


def get_args() -> argparse.Namespace:
    """ the arguments for the program
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("-l", "--loglevel", default="WARNING",
                        choices=['DEBUG', 'INFO', 'WARNING', 'ERROR', 'CRITICAL'],
                        help="Set the logging level")
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("-k", "--keygen", nargs='?', help="generate new keys with the given length",
                       type=int,
                       const=1024)
    group.add_argument("-e", "--encrypt", help="path from file to encrypt", type=str, nargs=2)
    group.add_argument("-d", "--decrypt", help="path from file to decrypt", type=str, nargs=2)
    parser.add_argument("-s", "--sign", help="Creates or checks with a signature file", required=False, const=True,
                        nargs="?")
    return parser.parse_args()


def generate_keys(key_size: int) -> Tuple[Tuple[int, int, int], Tuple[int, int, int]]:
    """ generates a private and a public key
    >>> abc = generate_keys(512)
    >>> e, d, n=abc[0][0], abc[1][0], abc[0][1]
    >>> for x in [10,20,30,40,50,0, 1<<500]:
    ...     c = pow(x, e, abc[0][1])
    ...     y = pow(c, d, abc[1][1])
    ...     assert x == y
    """
    p = miller_rabin.generate_prime(key_size // 2)
    q = miller_rabin.generate_prime(key_size // 2)
    while q == p:
        q = miller_rabin.generate_prime(key_size // 2)
    n = p * q
    tempSum = (p - 1) * (q - 1)
    e = 4
    while gcd(e, tempSum) != 1 and not miller_rabin.isPrime(e):
        e = random.randrange(1 << (key_size - 1), n)
        e |= 1
    d = pow(e, -1, tempSum)
    return (e, n, key_size), (d, n, key_size)


def generate_and_save_new_keys(key_size=512) -> None:
    """ generates and saves keys"""
    path = os.path.expanduser('~') + r'\.zwick_ssh'
    if os.path.exists(path) and os.path.isdir(path):
        logging.log(logging.WARNING, "Removing all old keys!")
        shutil.rmtree(path)
    Path(path).mkdir(parents=True, exist_ok=True)
    keys = generate_keys(key_size)
    with open(get_path_private_key(), "w") as f:
        f.write(str(keys[0][0]) + "\n" + str(keys[0][1]) + "\n" + str(keys[0][2]))
    with open(get_path_public_key(), "w") as f:
        f.write(str(keys[1][0]) + "\n" + str(keys[1][1]) + "\n" + str(keys[1][2]))
    logging.log(logging.INFO, "Keys generated successfully.")


# noinspection PyTypeChecker
def get_key_data_from_file(path: str) -> Tuple[int, int, int]:
    """reads the file where a key are stored"""
    try:
        with open(str(path), "r") as f:
            return [int(i) for i in f.read().split("\n")]
    except Exception:
        logging.log(logging.ERROR, "No keys were found!")


def file2ints(path: str, byte_length=2) -> Generator[int, None, None]:
    """reads blocks from a file"""
    try:
        with(open(path, "rb")) as f:
            a = f.read(byte_length)
            while a != b'':
                yield int.from_bytes(a, byteorder="big")
                a = f.read(byte_length)
    except Exception:
        logging.log(logging.ERROR, "Source File can't be accessed!")


def generate_and_check_files(paths: Tuple[str, str], keys: Tuple[int, int, int]) -> bool:
    """checks if the src file and the key exist and generates the dest file if necessary"""
    if keys is None:
        return False
    if not Path(paths[0]).exists():
        logging.log(logging.ERROR, "Source File wasn't found!")
        return False
    if paths[0] == paths[1]:
        logging.log(logging.WARNING, "Replacing the old file with the new file.")
    if not Path(paths[1]).parent.exists():
        logging.log(logging.DEBUG, "Generated Path for Saving File!")
        Path(paths[1]).parent.mkdir(parents=True, exist_ok=True)
    if not Path(paths[1]).exists():
        logging.log(logging.DEBUG, "Generated File for saving!")
        f = open(paths[1], "w")
        f.close()
    return True


def en_de_crypt_and_save(paths: Tuple[str, str], keys: Tuple[int, int, int], bytes_add: Tuple[int, int]) -> None:
    """de or encrypts from one file and saves the result into another file"""
    if generate_and_check_files(paths, keys):
        with open(paths[1], "wb") as f:
            reader = file2ints(paths[0], keys[2] // 8 + bytes_add[0])
            first_block = en_de_crypt_value_to_write(next(reader), keys, bytes_add)
            last_block = b''
            if bytes_add[0] == 0:
                overhead = os.path.getsize(paths[0]) % (keys[2] // 8 + bytes_add[0])
                f.write(en_de_crypt_value_to_write(overhead, keys, bytes_add))
                last_block = first_block
            for i in reader:
                new_block = en_de_crypt_value_to_write(i, keys, bytes_add)
                f.write(last_block)
                last_block = new_block
            if bytes_add[0] == 1:
                f.write(last_block[-int.from_bytes(first_block, byteorder="big"):])
                logging.log(logging.INFO, "Decryption successful")
            else:
                f.write(last_block)
                logging.log(logging.INFO, "Encryption successful")


def get_possible_primes(N: int) -> List[Tuple[int, int]]:
    """checks for possible solutions with N
    >>> for i in range(20, 51, 10):
    ...     assert len([b for b in get_possible_primes(generate_keys(i)[0][1])]) == 1
    """
    x = floor(sqrt(N)) | 1
    while x.bit_length() < N.bit_length() and x.bit_length() == (N // x).bit_length():
        if (N / x).is_integer() and miller_rabin.isPrime(x) and miller_rabin.isPrime(N // x):
            yield x, N // x
        x += 2


def get_hash_from_file(pathOld: str) -> str:
    """generates a hex hash value from a file"""
    h = hashlib.sha1()
    with open(pathOld, "rb") as f:
        chunk = 0
        while chunk != b'':
            chunk = f.read(1024)
            h.update(chunk)
    return h.hexdigest()


def encrypt_file(paths: Tuple[str, str], sign: bool) -> None:
    """encrypts a file"""
    keys = get_key_data_from_file(get_path_private_key())
    if sign:
        if Path.exists(Path(paths[0])):
            logging.log(logging.DEBUG, "Hash File is being created")
            with open(paths[1] + ".myHashTmp", "w") as nF:
                nF.write(get_hash_from_file(paths[0]))
            logging.log(logging.DEBUG, "Hash File is being encrypted")
            en_de_crypt_and_save((paths[1] + ".myHashTmp", paths[1] + ".myHash"), keys, (0, 1))
            os.remove(paths[1] + ".myHashTmp")
    en_de_crypt_and_save(paths, keys, (0, 1))


def en_de_crypt_value_to_write(value: int, keys: Tuple[int, int, int], bytes_add: Tuple[int, int]) -> bytes:
    """en or decrypts a value"""
    new_value = pow(value, keys[0], keys[1])
    new_value_bytes = new_value.to_bytes(length=keys[2] // 8 + bytes_add[1], byteorder="big")
    return new_value_bytes


def decrypt_file(paths: Tuple[str, str], sign: bool) -> None:
    """decrypts a file"""
    try:
        keys = get_key_data_from_file(get_path_public_key())
        en_de_crypt_and_save(paths, keys, (1, 0))
        if sign:
            if Path.exists(Path(paths[0])):
                en_de_crypt_and_save((paths[0] + ".myHash", paths[0] + ".myHashTmp"), keys, (1, 0))
                logging.log(logging.DEBUG, "Hash File is being checked")
                with open(paths[0] + ".myHashTmp") as f:
                    if f.read() == get_hash_from_file(paths[1]):
                        logging.log(logging.INFO, "Signing was correct")
                    else:
                        logging.error("The hash sum of the files were not the same.")
                os.remove(paths[0] + ".myHashTmp")
    except OverflowError:
        logging.error("Failed to convert message. Probably someone has altered a file")
        if sign and os.path.exists(paths[0] + ".myHashTmp"):
            os.remove(paths[0] + ".myHashTmp")


def start_program():
    """runs the logic for the program"""
    args = get_args()
    if args.loglevel:
        logging.basicConfig(level=getattr(logging, args.loglevel), format='[%(asctime)s] %(levelname)s %(message)s',
                            datefmt='%Y-%m-%d %H:%M:%S')
        logging.log(logging.INFO, "Logging turned on " + args.loglevel)
    if args.keygen:
        minSize = 17
        if args.keygen < minSize:
            logging.log(logging.ERROR, "Keygen bit size must be greater than " + str(minSize))
        else:
            logging.log(logging.INFO, "Trying to generate keys with " + str(args.keygen) + " bit")
            generate_and_save_new_keys(args.keygen)
    if args.encrypt:
        encrypt_file(args.encrypt, args.sign)
    if args.decrypt:
        decrypt_file(args.decrypt, args.sign)


if __name__ == "__main__":
    # start_program()
    generate_and_save_new_keys(1000)
    encrypt_file(("rsa.py", "rsa_enc.py"), False)
    decrypt_file(("rsa_enc.py", "rsa_dec.py"), False)
    # a = generate_keys(18)
    # print(a)
    # print([b for b in get_possible_primes(a[0][1])])
    # print([b for b in get_possible_primes(a[0][1])])
    # for i in range(20, 41, 10):
    #     x = [b for b in getPossiblePrimes(generate_keys(i)[0][1])]
    #     print(x, len(x))
    # ((203729, 802673, 18), (59969, 802673, 18))
    # [(941, 853)]
# x = [b for b in get_possible_primes(119143)]
# print(x, len(x))
