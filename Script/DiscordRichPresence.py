#!/usr/bin/env python

import os

import sys


def assert_compatibility(required_version=0):
    """
    Determine whether the current python version matches required_version, and if not throws an exception.
    """
    if sys.version_info[0] < required_version:
        raise Exception("Python " + str(required_version) + " or a more recent version is required."
                                                            " (Using: " + str(sys.version_info[0]) + ")")


assert_compatibility(3)
is_windows = sys.platform.startswith('win')
is_linux = sys.platform.startswith('linux')
is_macos = sys.platform.startswith('darwin')
process_version = "v1.5.5"
process_hwnd = None
is_process_running = False
current_path = os.path.dirname(os.path.realpath(__file__))
help_url = "https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Install-Guide-for-World-of-Warcraft"


def load_config(path=current_path, default_path='/defaults.json', user_path='/config.json'):
    """
    Load the config and default_settings data from the specified paths, if applicable.
    """
    default_settings = open(path + default_path)
    user_settings = default_settings
    config = json.load(user_settings)
    try:
        user_settings = open(path + user_path)
        config.update(json.load(user_settings))
        print("Applying user-defined settings...")
    except FileNotFoundError:
        print("No user-defined settings found, using defaults...")
        return config
    else:
        return config


def setup_logging(config=None, debug_mode=False):
    """
    Configure logging based on config data and debug mode status.
    """
    log_path = current_path + '/logs'
    log_ext = '.log'
    log_modes = config["log_mode"]

    log_prefix = log_path + '/output'
    single_log_path = log_prefix + log_ext
    log_format = "%(asctime)s [%(levelname)s] %(message)s"
    log_date_style = "%m/%d/%Y %I:%M:%S %p"
    log_format_style = "%Y-%m-%d_%H-%M-%S"
    start_timestamp = datetime.now().strftime(log_format_style)

    # Adjust log level depending on mode
    log_level = logging.INFO
    if debug_mode:
        log_level = logging.DEBUG
    # Setup Main Logger and Formatting
    root_logger = logging.getLogger("DRPLogger")
    root_logger.setLevel(log_level)
    log_formatter = logging.Formatter(log_format, log_date_style)
    color_formatter = log_formatter
    # Setup handlers depending on config options
    if "full" in log_modes or "v1" in log_modes or "console" in log_modes:
        console_handler = logging.StreamHandler(stream=sys.stdout)
        if ("full" in log_modes or "color" in log_modes) and "no-color" not in log_modes and "plain" not in log_modes:
            # Import Optional Colored Formatter, if able
            try:
                from colored_log import ColoredFormatter

                color_formatter = ColoredFormatter(log_format, log_date_style)
                console_handler.setFormatter(color_formatter)
            except (ModuleNotFoundError, TypeError):
                pass
        else:
            console_handler.setFormatter(log_formatter)
        console_handler.setLevel(log_level)
        root_logger.addHandler(console_handler)

    if "full" in log_modes or "multiple_files" in log_modes or "files" in log_modes:
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
    elif "v1" in log_modes or "single_file" in log_modes or "file" in log_modes:
        file_handler = logging.FileHandler(single_log_path, mode='w')
        file_handler.setFormatter(log_formatter)
        file_handler.setLevel(log_level)
        root_logger.addHandler(file_handler)

    return root_logger


def callback(hwnd, extra):
    global process_hwnd
    global is_process_running
    if (win32gui.GetWindowText(hwnd) == config["process_name"] and
            win32gui.GetClassName(hwnd).startswith('GxWindowClass')):
        process_hwnd = hwnd
        is_process_running = True


def main(debug_mode=False):
    """
    Main entrypoint (Should only be run when used as a script, and not when imported).
    """
    # Global Definitions
    global process_hwnd
    global is_process_running
    # Primary Variables
    event_key = "$RPCEvent$"
    event_length = 11
    array_split_key = "=="
    array_separator_key = "|"
    process_hwnd = None
    is_process_running = False

    # RPC Data
    rpc_obj = None
    last_decoded = [None] * event_length
    last_activity = {}

    # Initial Welcome Messages
    root_logger.info("========== DiscordRichPresence Service - " + process_version + " ==========")
    root_logger.info("System Info: \"" + sys.version + "\"")
    root_logger.info("Started DiscordRichPresence Service for \"" + config["process_name"] + "\"")
    root_logger.info("Note: Please keep this script open while logging and sending Rich Presence updates.")
    root_logger.info("==========================================================")

    while True:
        process_hwnd = None
        is_process_running = False
        if is_windows:
            win32gui.EnumWindows(callback, None)
        else:
            is_process_running = is_running(config["process_name"])

        if debug_mode:
            # if in DEBUG mode, squares are read, the image with the dot matrix is
            # shown and then the script quits.
            if is_process_running:
                root_logger.debug('DEBUG: Reading squares. Please check result image for verification...')
                read_squares(process_hwnd, event_length, event_key, array_separator_key, debug_mode)
            else:
                root_logger.debug("DEBUG: Unable to locate target process \"" + config["process_name"] + "\".")
            input("Press Enter to continue...")
            break
        elif is_process_running:
            lines = read_squares(process_hwnd, event_length, event_key, array_separator_key, debug_mode)

            if not lines:
                time.sleep(config["scan_rate"])
                continue

            has_id_changed = lines[0] != last_decoded[0]

            if has_id_changed or lines != last_decoded:
                if not rpc_obj or has_id_changed:
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
                            root_logger.error("Unable to connect to Discord (%s)."
                                              'Discarding event and relaunching in %s '
                                              'sec.' % (str(exc), config["refresh_rate"]))
                            time.sleep(config["refresh_rate"])
                            last_decoded = [None] * event_length
                            last_activity = {}
                            rpc_obj = None
                            break
                        else:
                            break

                timer_data = {}
                asset_data = {}
                button_info = []
                activity = {}
                # Asset Data Sync
                if not null_or_empty(lines[1]):
                    asset_data["large_image"] = lines[1]
                    if not null_or_empty(lines[2]):
                        asset_data["large_text"] = lines[2]
                if not null_or_empty(lines[3]):
                    asset_data["small_image"] = lines[3]
                    if not null_or_empty(lines[4]):
                        asset_data["small_text"] = lines[4]
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
                    timer_data["start"] = lines[7]
                    if not null_or_empty(lines[8]):
                        timer_data["end"] = lines[8]
                # Buttons Data Sync
                if not null_or_empty(lines[9]):
                    button_data = parse_button_data(lines[9], array_split_key)
                    if len(button_data) == 2:
                        button_label = button_data[0] if (len(button_data[0]) <= 32) else button_data[1]
                        button_url = button_data[1] if (button_data[1] != button_label) else button_data[0]
                        button_info.append({"label": button_label, "url": button_url})
                if not null_or_empty(lines[10]):
                    button_data = parse_button_data(lines[10], array_split_key)
                    if len(button_data) == 2:
                        button_label = button_data[0] if not (is_valid_url(button_data[0])) else button_data[1]
                        button_url = button_data[1] if (button_data[1] != button_label) else button_data[0]
                        button_info.append({"label": button_label, "url": button_url})
                # Activity Data Sync
                if not null_or_empty(lines[5]):
                    activity["details"] = lines[5]
                if not null_or_empty(lines[6]):
                    activity["state"] = lines[6]

                activity["assets"] = asset_data
                activity["timestamps"] = timer_data
                activity["buttons"] = button_info

                if activity != last_activity:
                    root_logger.info("Setting new activity: %s" % activity)

                    try:
                        rpc_obj.update(
                            state=activity.get("state") or None,
                            details=activity.get("details") or None,
                            start=timer_data.get("start") or None,
                            end=timer_data.get("end") or None,
                            large_image=asset_data.get("large_image") or None,
                            large_text=asset_data.get("large_text") or None,
                            small_image=asset_data.get("small_image") or None,
                            small_text=asset_data.get("small_text") or None,
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
        elif not is_process_running and rpc_obj:
            root_logger.info('Target process is no longer active, disconnecting...')
            rpc_obj.close()
            rpc_obj = None
            # clear these so it gets re-read and resubmitted upon reconnection
            last_decoded = [None] * event_length
            last_activity = {}
        time.sleep(config["scan_rate"])


def decode_read_data(read: list, encoding='utf-8'):
    """
    Interprets bytes under the specified encoding, to produce a decoded string.
    """
    decoded = ""
    try:
        decoded = bytes(read).decode(encoding).rstrip('\0')
    except UnicodeDecodeError:
        return decoded

    return decoded


def get_decoded_chunks(decoded: str, event_key='', array_separator_key='') -> list:
    """
    Interprets a decoded event and splits it into various pieces.
    """
    return decoded.replace(event_key, '').split(array_separator_key)


def verify_read_data(decoded: str, event_length=0, event_key='', array_separator_key=''):
    """
    Interprets whether the specified decoded event fits the required spec of the correct length and correct endings.
    """
    parts = get_decoded_chunks(decoded, event_key, array_separator_key)
    return (len(parts) == event_length and decoded.endswith(event_key) and decoded.startswith(
        event_key) and decoded != event_key)


def is_valid_url(url, qualifying=('scheme', 'netloc')):
    """
    Interprets whether the specified url is valid based on the qualifiers.
    """
    tokens = urlparse(url)
    return all([getattr(tokens, qualifying_attr)
                for qualifying_attr in qualifying])


def null_or_empty(data):
    """
    Interprets whether or not the specified object is null or empty.
    """
    return not data or "" == data


def get_or_default(data, default=""):
    """
    Returns the object specified if not null or empty, otherwise uses the specified fallback.
    """
    return data if not (null_or_empty(data)) else default


def parse_button_data(line_data, array_split_key='') -> list:
    """
    Parses and splits valid button data into individual pieces within a list.
    """
    button_data = str(line_data).split(array_split_key, 1)
    button_data[0] = get_or_default(button_data[0])
    return button_data


def is_running(process_name):
    """
    Check if there is any running process that contains the given process_name (Requires psutil).
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
    """
    Apply the given offsets to the left, top, right, and bottom orients.
    """
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
    """
    Takes a screenshot of the specified window, cropped to the specified offsets.
    """
    try:
        # Change the line below depending on whether you want the whole window
        # or just the client area.
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


def read_squares(hwnd=None, event_length=0, event_key='', array_separator_key='', debug_mode=False):
    """
    Interpret a set of pixels, using the offsets and sizing from the config (Also perform sanity checks, if applicable).
    """
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
            if verify_read_data(current_decoded, event_length, event_key, array_separator_key):
                break
            else:
                waiting_for_null = True

    parts = get_decoded_chunks(current_decoded, event_key, array_separator_key)

    if debug_mode:
        im.show()
        return

    # sanity check
    if not (verify_read_data(current_decoded, event_length, event_key, array_separator_key)):
        return

    return parts


# Import Modules and perform Initial Setup
try:
    if is_windows:
        from ctypes import windll
        import win32gui
        import win32ui
        import pywintypes
    else:
        import psutil
    from PIL import Image, ImageGrab
    from pypresence import Presence
    # Universal Modules
    import json
    import logging
    # Import Sub-Package Data
    from datetime import datetime
    from logging.handlers import TimedRotatingFileHandler
    from urllib.parse import urlparse
    import time
except ModuleNotFoundError as err:
    print(
        "A module is missing (%s), preventing script execution! (Please review %s for Install Requirements)"
        % (err.name, help_url)
    )
    input("Press Enter to continue...")
    exit(1)
else:
    if __name__ == '__main__':
        # Main Entrypoint Execution
        config = load_config()
        root_logger = setup_logging(config, config["debug"])
        main(config["debug"])
