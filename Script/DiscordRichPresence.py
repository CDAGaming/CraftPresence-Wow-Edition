import json
import logging
import os
import sys
import time
from ctypes import windll

import win32gui
import win32ui
from PIL import Image
from pypresence import Presence

dir_path = os.path.dirname(os.path.realpath(__file__))
# Logging Data
log_path = dir_path + '/output.log'
log_format = "%(asctime)s [%(levelname)s] %(message)s"
log_date_style = "%m/%d/%Y %I:%M:%S %p"
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
# Setup handlers depending on config options
if log_mode == "full" or log_mode == "console":
    console_handler = logging.StreamHandler(stream=sys.stdout)
    console_handler.setFormatter(log_formatter)
    console_handler.setLevel(log_level)
    root_logger.addHandler(console_handler)
if log_mode == "full" or log_mode == "file":
    file_handler = logging.FileHandler(log_path, mode='w')
    file_handler.setFormatter(log_formatter)
    file_handler.setLevel(log_level)
    root_logger.addHandler(file_handler)

# these are internal use variables, don't touch them
process_version = "v1.0.5"
unknown_key = "Skip"
array_split_key = "=="
decoded = ''
process_hwnd = None
rpc_obj = None
last_first_line = None
last_second_line = None
last_third_line = None
last_fourth_line = None
last_fifth_line = None
last_sixth_line = None
last_seventh_line = None
last_eighth_line = None
last_ninth_line = None
last_tenth_line = None
last_eleventh_line = None

last_start_timestamp = None
last_end_timestamp = None

last_activity = {}


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
    return decoded.replace('$RPCEvent$', '').split('|')


def verify_read_data(decoded):
    parts = get_decoded_chunks(decoded)
    return (len(parts) == 11 and decoded.endswith('$RPCEvent$') and decoded.startswith(
        '$RPCEvent$') and decoded != "$RPCEvent$")


def parse_button_data(line_data):
    button_data = str(line_data).split(array_split_key, 1)
    button_data[0] = (button_data[0] if not ("" == button_data[1]) else "")
    return button_data


def take_screenshot(hwnd, window_type=0, left_offset=0, left_specific=0, top_offset=0, top_specific=0, right_offset=0,
                    right_specific=0, bottom_offset=0, bottom_specific=0):
    # Change the line below depending on whether you want the whole window
    # or just the client area.
    left, top, right, bottom = win32gui.GetClientRect(hwnd)
    # left, top, right, bottom = win32gui.GetWindowRect(hwnd)

    # Calculate offsets and final width and height
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
    try:
        im = take_screenshot(hwnd, 3, 0, 0, 0, 0, 0, 0, 0, config["pixel_size"])
    except win32ui.error:
        root_logger.debug('win32ui.error')
        return

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

    first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line, tenth_line, eleventh_line = parts

    return first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line, tenth_line, eleventh_line


root_logger.info("========== DiscordRichPresence Service - " + process_version + " ==========")
root_logger.info("Started DiscordRichPresence Service for \"" + config["process_name"] + "\"")
root_logger.info("Note: Please keep this script open while logging and sending Rich Presence updates.")
root_logger.info("==========================================================")

while True:
    process_hwnd = None
    win32gui.EnumWindows(callback, None)

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

        first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line, tenth_line, eleventh_line = lines
        hasIdChanged = first_line != last_first_line

        if hasIdChanged or second_line != last_second_line or third_line != last_third_line or fourth_line != last_fourth_line or fifth_line != last_fifth_line or sixth_line != last_sixth_line or seventh_line != last_seventh_line or eighth_line != last_eighth_line or ninth_line != last_ninth_line or tenth_line != last_tenth_line or eleventh_line != last_eleventh_line:
            # there has been an update, so send it to discord
            last_first_line = first_line
            last_second_line = second_line
            last_third_line = third_line
            last_fourth_line = fourth_line
            last_fifth_line = fifth_line
            last_sixth_line = sixth_line
            last_seventh_line = seventh_line
            last_eighth_line = eighth_line
            last_ninth_line = ninth_line
            last_tenth_line = tenth_line
            last_eleventh_line = eleventh_line

            if not rpc_obj or hasIdChanged:
                if rpc_obj:
                    root_logger.info("The client id has been changed, reconnecting with new ID %s..." % first_line)
                    rpc_obj.close()
                else:
                    root_logger.info("Not connected to Discord, connecting to ID %s..." % first_line)

                while True:
                    try:
                        rpc_obj = Presence(client_id=first_line)
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
            if not (unknown_key in second_line):
                assetsData["large_image"] = second_line
                if not (unknown_key in third_line):
                    assetsData["large_text"] = third_line
            if not (unknown_key in fourth_line):
                assetsData["small_image"] = fourth_line
                if not (unknown_key in fifth_line):
                    assetsData["small_text"] = fifth_line
            # Start Timer Data Setup
            if "generated" in eighth_line:
                eighth_line = round(time.time())
            elif "last" in eighth_line:
                eighth_line = last_start_timestamp or round(time.time())
            # End Timer Data Setup
            if "generated" in ninth_line:
                ninth_line = round(time.time())
            elif "last" in ninth_line:
                ninth_line = last_end_timestamp or round(time.time())
            # Timer Data Sync
            if not (unknown_key in str(eighth_line)):
                timerData["start"] = eighth_line
                last_start_timestamp = eighth_line
                if not (unknown_key in str(ninth_line)):
                    timerData["end"] = ninth_line
                    last_end_timestamp = ninth_line
            # Buttons Data Sync
            if not (unknown_key in str(tenth_line)):
                primary_button_data = parse_button_data(tenth_line)
                if primary_button_data[0]:
                    buttonsData.append({"label": primary_button_data[0], "url": primary_button_data[1]})
            if not (unknown_key in str(eleventh_line)):
                secondary_button_data = parse_button_data(eleventh_line)
                if secondary_button_data[0]:
                    buttonsData.append({"label": secondary_button_data[0], "url": secondary_button_data[1]})
            # Activity Data Sync
            if not (unknown_key in sixth_line):
                activity["details"] = sixth_line
            if not (unknown_key in seventh_line):
                activity["state"] = seventh_line

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
                except Exception as exc:
                    root_logger.error('Looks like the connection to Discord was broken (%s). '
                                      'I will try to connect again in %s sec.' % (str(exc), config["refresh_rate"]))
                    last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eighth_line, last_ninth_line, last_tenth_line, last_eleventh_line = None, None, None, None, None, None, None, None, None, None, None
                    last_start_timestamp, last_end_timestamp = None, None
                    last_activity = {}
                    rpc_obj = None
    elif not process_hwnd and rpc_obj:
        root_logger.info('Target process is no longer active, disconnecting')
        rpc_obj.close()
        rpc_obj = None
        # clear these so it gets re-read and resubmitted upon reconnection
        last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eighth_line, last_ninth_line, last_tenth_line, last_eleventh_line = None, None, None, None, None, None, None, None, None, None, None
        last_start_timestamp, last_end_timestamp = None, None
        last_activity = {}
    time.sleep(config["refresh_rate"])
