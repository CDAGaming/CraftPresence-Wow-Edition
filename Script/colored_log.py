#!/usr/bin/env python

from copy import copy
from logging import Formatter

MAPPING = {
    'DEBUG': 37,  # white
    'INFO': 36,  # cyan
    'WARNING': 33,  # yellow
    'ERROR': 31,  # red
    'CRITICAL': 41,  # white on red bg
}

PREFIX = '\033['
SUFFIX = '\033[0m'


class ColoredFormatter(Formatter):

    def __init__(self, pattern=None, datefmt=None, style='%', validate=True):
        Formatter.__init__(self, pattern, datefmt, style, validate)

    def format(self, record):
        colored_record = copy(record)
        levelname = colored_record.levelname
        seq = MAPPING.get(levelname, 37)  # default white
        colored_levelname = '{0}{1}m{2}{3}' \
            .format(PREFIX, seq, levelname, SUFFIX)
        colored_record.levelname = colored_levelname
        return Formatter.format(self, colored_record)
