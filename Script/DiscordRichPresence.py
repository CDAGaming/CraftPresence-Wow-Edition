#!/usr/bin/env python

# Import Packaged Modules
import json
import logging
import os
# Import Sub-Package Data
from datetime import datetime
from logging.handlers import TimedRotatingFileHandler
from urllib.parse import urlparse

# Import Extra Modules
import pywintypes
import sys
import time

# Internal Data (DNT)
is_windows = sys.platform.startswith('win')
is_linux = sys.platform.startswith('linux')
is_macos = sys.platform.startswith('darwin')
min_attributes = ('scheme', 'netloc')
process_version = "v1.3.3"
event_key = "$RPCEvent$"
event_length = 11
array_split_key = "=="
array_separator_key = "|"
decoded = ''
process_hwnd = None
dir_path = os.path.dirname(os.path.realpath(__file__))
help_url = "https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Install-Guide-for-World-of-Warcraft"
# Logging Data
log_path = dir_path + '/logs'
log_ext = '.log'

log_prefix = log_path + '/output'
single_log_path = log_prefix + log_ext
log_format = "%(asctime)s [%(levelname)s] %(message)s"
log_date_style = "%m/%d/%Y %I:%M:%S %p"
log_format_style = "%Y-%m-%d_%H-%M-%S"
start_timestamp = datetime.now().strftime(log_format_style)
# Config Data
f = open(dir_path + '/config.json')
config = json.load(f)
debug_mode = config["debug"]
log_mode = config["log_mode"]

# Adjust log level depending on mode
log_level = logging.INFO
if debug_mode:
    log_level = logging.DEBUG
# Setup Main Logger and Formatting
root_logger = logging.getLogger("DRPLogger")
root_logger.setLevel(log_level)
log_formatter = logging.Formatter(log_format, log_date_style)
color_formatter = log_formatter
# Import Optional Colored Formatter, if able
try:
    from colored_log import ColoredFormatter

    color_formatter = ColoredFormatter(log_format, log_date_style)
except ModuleNotFoundError as err:
    pass
# Setup handlers depending on config options
if log_mode == "full" or log_mode == "v1" or log_mode == "console":
    console_handler = logging.StreamHandler(stream=sys.stdout)
    console_handler.setFormatter(color_formatter)
    console_handler.setLevel(log_level)
    root_logger.addHandler(console_handler)
if log_mode == "full" or log_mode == "multiple_files" or log_mode == "files":
    if not os.path.exists(log_path):
        os.makedirs(log_path)
    staged_handler = TimedRotatingFileHandler(single_log_path, when="midnight", interval=1)
    staged_handler.suffix = start_timestamp
    staged_handler.namer = lambda name: name.replace(log_ext, "") + log_ext
    should_roll_over = os.path.isfile(single_log_path)
    if should_roll_over:  # log already exists, roll over!
        staged_handler.doRollover()
    staged_handler.setFormatter(log_formatter)
    staged_handler.setLevel(log_level)
    root_logger.addHandler(staged_handler)
if log_mode == "v1" or log_mode == "single_file" or log_mode == "file":
    file_handler = logging.FileHandler(single_log_path, mode='w')
    file_handler.setFormatter(log_formatter)
    file_handler.setLevel(log_level)
    root_logger.addHandler(file_handler)

# Initial Welcome Messages
root_logger.info("========== DiscordRichPresence Service - " + process_version + " ==========")
root_logger.info("Started DiscordRichPresence Service for \"" + config["process_name"] + "\"")
root_logger.info("Note: Please keep this script open while logging and sending Rich Presence updates.")
root_logger.info("==========================================================")

# RPC Data
rpc_obj = None
last_decoded = [None] * event_length
last_activity = {}

# Import Third-Party Modules
try:
    if is_windows:
        from ctypes import windll
        import win32gui
        import win32ui
    else:
        import psutil
    from PIL import Image, ImageGrab
    from pypresence import Presence
except ModuleNotFoundError as err:
    root_logger.error(
        "A module is missing (%s), preventing script execution! (Please review %s for Install Requirements)",
        err.name, help_url
    )
    input("Press Enter to continue...")
    exit(1)


def callback(hwnd, extra):
    global process_hwnd
    if (win32gui.GetWindowText(hwnd) == config["process_name"] and
            win32gui.GetClassName(hwnd).startswith('GxWindowClass')):
        process_hwnd = hwnd


def decode_read_data(read):
    decoded = ""
    try:
        decoded = bytes(read).decode('utf-8').rstrip('\0')
    except UnicodeDecodeError:
        return decoded

    return decoded


def get_decoded_chunks(decoded):
    return decoded.replace(event_key, '').split(array_separator_key)


def verify_read_data(decoded):
    parts = get_decoded_chunks(decoded)
    return (len(parts) == event_length and decoded.endswith(event_key) and decoded.startswith(
        event_key) and decoded != event_key)


def is_valid_url(url, qualifying=min_attributes):
    tokens = urlparse(url)
    return all([getattr(tokens, qualifying_attr)
                for qualifying_attr in qualifying])


def null_or_empty(data):
    return not data or "" == data


def get_or_default(data, default=""):
    return data if not (null_or_empty(data)) else default


def parse_button_data(line_data):
    button_data = str(line_data).split(array_split_key, 1)
    button_data[0] = get_or_default(button_data[0])
    return button_data


def is_running(process_name):
    """
    Check if there is any running process that contains the given name processName.
    """
    # Iterate over the all the running process
    for proc in psutil.process_iter():
        try:
            # Check if process name contains the given name string.
            if process_name.lower() in proc.name().lower():
                return True
        except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
            pass
    return False


def interpret_offsets(left=0, top=0, right=0, bottom=0, left_offset=0, left_specific=0, top_offset=0, top_specific=0,
                      right_offset=0,
                      right_specific=0, bottom_offset=0, bottom_specific=0):
    if left_specific > 0:
        left = left_specific
    if right_specific > 0:
        right = right_specific
    if top_specific > 0:
        top = top_specific
    if bottom_specific > 0:
        bottom = bottom_specific

    left = left + left_offset
    right = right + right_offset
    top = top + top_offset
    bottom = bottom + bottom_offset

    return left, top, right, bottom


def take_screenshot(hwnd, window_type=0, left_offset=0, left_specific=0, top_offset=0, top_specific=0, right_offset=0,
                    right_specific=0, bottom_offset=0, bottom_specific=0):
    # Change the line below depending on whether you want the whole window
    # or just the client area.
    try:
        left, top, right, bottom = win32gui.GetClientRect(hwnd)
        # left, top, right, bottom = win32gui.GetWindowRect(hwnd)
    except pywintypes.error:
        # Fallback to specific offsets if GetClientRect fails
        # Note: This is just a failsafe to prevent it from crashing
        left, top, right, bottom = left_specific, top_specific, right_specific, bottom_specific

    # Calculate offsets and final width and height
    left, top, right, bottom = interpret_offsets(left, top, right, bottom, left_offset, left_specific, top_offset,
                                                 top_specific,
                                                 right_offset, right_specific, bottom_offset, bottom_specific)

    width = right - left
    height = bottom - top

    hwnd_dc = win32gui.GetWindowDC(hwnd)
    mfc_dc = win32ui.CreateDCFromHandle(hwnd_dc)
    save_dc = mfc_dc.CreateCompatibleDC()

    save_bitmap = win32ui.CreateBitmap()
    save_bitmap.CreateCompatibleBitmap(mfc_dc, width, height)

    save_dc.SelectObject(save_bitmap)

    windll.user32.PrintWindow(hwnd, save_dc.GetSafeHdc(), window_type)

    bmpinfo = save_bitmap.GetInfo()
    bmpstr = save_bitmap.GetBitmapBits(True)

    im = Image.frombuffer(
        'RGB',
        (bmpinfo['bmWidth'], bmpinfo['bmHeight']),
        bmpstr, 'raw', 'BGRX', 0, 1)

    win32gui.DeleteObject(save_bitmap.GetHandle())
    save_dc.DeleteDC()
    mfc_dc.DeleteDC()
    win32gui.ReleaseDC(hwnd, hwnd_dc)

    return im


def read_squares(hwnd):
    waiting_for_null = False
    im = None
    if is_windows:
        try:
            im = take_screenshot(
                hwnd, config["window_type"],
                config["left_offset"], config["left_specific"],
                config["top_offset"], config["top_specific"],
                config["right_offset"], config["right_specific"],
                config["bottom_offset"], config["pixel_size"]
            )
        except win32ui.error:
            root_logger.debug('win32ui.error', exc_info=True)
            return
        except ValueError:
            root_logger.error('Unable to retrieve enough Image Data, try resizing your window perhaps?')
            return
    else:
        im = ImageGrab.grab()
        im_width, im_height = im.size
        left, top, right, bottom = interpret_offsets(
            0, 0, im_width, im_height,
            config["left_offset"], config["left_specific"],
            config["top_offset"], config["top_specific"],
            config["right_offset"], config["right_specific"],
            config["bottom_offset"], config["pixel_size"]
        )
        im = im.crop((left, top, right, bottom))

    read = []
    current_decoded = ""
    for square_idx in range(im.width):
        x = int(square_idx * config["pixel_size"] / 2)
        y = int(config["pixel_size"] / 2)
        try:
            r, g, b = im.getpixel((x, y))
        except IndexError:
            break

        if debug_mode:
            im.putpixel((x, y), (255, 255, 255))

        if r == g == b == 0:
            waiting_for_null = False
        elif not waiting_for_null:
            read.append(r)
            read.append(g)
            read.append(b)

            current_decoded = decode_read_data(read)
            if verify_read_data(current_decoded):
                break
            else:
                waiting_for_null = True

    parts = get_decoded_chunks(current_decoded)

    if debug_mode:
        im.show()
        return

    # sanity check
    if not (verify_read_data(current_decoded)):
        return

    return parts


while True:
    process_hwnd = None
    if is_windows:
        win32gui.EnumWindows(callback, None)
    else:
        process_hwnd = is_running(config["process_name"])

    if debug_mode:
        # if in DEBUG mode, squares are read, the image with the dot matrix is
        # shown and then the script quits.
        if process_hwnd:
            root_logger.debug('DEBUG: reading squares. Is everything alright?')
            read_squares(process_hwnd)
        else:
            root_logger.debug("Launching in DEBUG mode but I couldn't find target process.")
        break
    elif process_hwnd:
        lines = read_squares(process_hwnd)

        if not lines:
            time.sleep(config["scan_rate"])
            continue

        hasIdChanged = lines[0] != last_decoded[0]

        if hasIdChanged or lines != last_decoded:
            if not rpc_obj or hasIdChanged:
                if rpc_obj:
                    root_logger.info("The client id has been changed, reconnecting with new ID %s..." % lines[0])
                    rpc_obj.close()
                else:
                    root_logger.info("Not connected to Discord, connecting to ID %s..." % lines[0])

                while True:
                    try:
                        rpc_obj = Presence(client_id=lines[0])
                        rpc_obj.connect()
                    except Exception as exc:
                        root_logger.error("Unable to connect to Discord (%s). It's "
                                          'probably not running. I will try again in %s '
                                          'sec.' % (str(exc), config["refresh_rate"]))
                        time.sleep(config["refresh_rate"])
                    else:
                        break

            timerData = {}
            assetsData = {}
            buttonsData = []
            activity = {}
            # Asset Data Sync
            if not null_or_empty(lines[1]):
                assetsData["large_image"] = lines[1]
                if not null_or_empty(lines[2]):
                    assetsData["large_text"] = lines[2]
            if not null_or_empty(lines[3]):
                assetsData["small_image"] = lines[3]
                if not null_or_empty(lines[4]):
                    assetsData["small_text"] = lines[4]
            # Start Timer Data Setup
            if "generated" in lines[7]:
                lines[7] = round(time.time())
            elif "last" in lines[7]:
                lines[7] = last_decoded[7] or round(time.time())
            # End Timer Data Setup
            if "generated" in lines[8]:
                lines[8] = round(time.time())
            elif "last" in lines[8]:
                lines[8] = last_decoded[8] or round(time.time())
            # Timer Data Sync
            if not null_or_empty(lines[7]):
                timerData["start"] = lines[7]
                last_start_timestamp = lines[7]
                if not null_or_empty(lines[8]):
                    timerData["end"] = lines[8]
                    last_end_timestamp = lines[8]
            # Buttons Data Sync
            if not null_or_empty(lines[9]):
                button_data = parse_button_data(lines[9])
                if len(button_data) == 2:
                    button_label = button_data[0] if (len(button_data[0]) <= 32) else button_data[1]
                    button_url = button_data[1] if (button_data[1] != button_label) else button_data[0]
                    buttonsData.append({"label": button_label, "url": button_url})
            if not null_or_empty(lines[10]):
                button_data = parse_button_data(lines[10])
                if len(button_data) == 2:
                    button_label = button_data[0] if not (is_valid_url(button_data[0])) else button_data[1]
                    button_url = button_data[1] if (button_data[1] != button_label) else button_data[0]
                    buttonsData.append({"label": button_label, "url": button_url})
            # Activity Data Sync
            if not null_or_empty(lines[5]):
                activity["details"] = lines[5]
            if not null_or_empty(lines[6]):
                activity["state"] = lines[6]

            activity["assets"] = assetsData
            activity["timestamps"] = timerData
            activity["buttons"] = buttonsData

            if activity != last_activity:
                root_logger.info("Setting new activity: %s" % activity)

                try:
                    rpc_obj.update(
                        state=activity.get("state") or None,
                        details=activity.get("details") or None,
                        start=timerData.get("start") or None,
                        end=timerData.get("end") or None,
                        large_image=assetsData.get("large_image") or None,
                        large_text=assetsData.get("large_text") or None,
                        small_image=assetsData.get("small_image") or None,
                        small_text=assetsData.get("small_text") or None,
                        buttons=activity.get("buttons") or None
                    )
                    last_activity = activity
                    last_decoded = lines
                except Exception as exc:
                    root_logger.error('Looks like the connection to Discord was broken (%s). '
                                      'I will try to connect again in %s sec.' % (str(exc), config["refresh_rate"]))
                    last_decoded = [None] * event_length
                    last_activity = {}
                    rpc_obj = None
    elif not process_hwnd and rpc_obj:
        root_logger.info('Target process is no longer active, disconnecting')
        rpc_obj.close()
        rpc_obj = None
        # clear these so it gets re-read and resubmitted upon reconnection
        last_decoded = [None] * event_length
        last_activity = {}
    time.sleep(config["refresh_rate"])
