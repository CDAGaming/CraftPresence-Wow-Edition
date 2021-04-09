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
DEFAULT_COLOR = 37  # default white


def set_as_color(text, color):
    return '{0}{1}m{2}{3}' \
        .format(PREFIX, color, text, SUFFIX)


class ColoredFormatter(Formatter):

    def __init__(self, pattern=None, datefmt=None, style='%', validate=True):
        Formatter.__init__(self, pattern, datefmt, style, validate)

    def format(self, record):
        colored_record = copy(record)
        # Format Level Name
        levelname = colored_record.levelname
        seq = MAPPING.get(levelname, DEFAULT_COLOR)
        colored_levelname = set_as_color(levelname, seq)
        colored_record.levelname = colored_levelname
        # Format Time if applicable, to be yellow
        if self.usesTime():
            self.datefmt = set_as_color(self.datefmt, 33)
        return Formatter.format(self, colored_record)
