import json
import os
import time

import win32gui
from PIL import Image, ImageGrab

import rpc

dir_path = os.path.dirname(os.path.realpath(__file__))

f = open(dir_path + '/config.json')
config = json.load(f)

# these are internal use variables, don't touch them
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
    return (len(parts) == 9 and decoded.endswith('$RPCEvent$') and decoded.startswith(
        '$RPCEvent$') and decoded != "$RPCEvent$")


def read_squares(hwnd):
    x, y, x1, y1 = win32gui.GetClientRect(hwnd)
    x, y = win32gui.ClientToScreen(hwnd, (x, y))
    x1, y1 = win32gui.ClientToScreen(hwnd, (x1 - x, y1 - y))
    new_rect = (x, y, x1, (y + config["pixel_size"]))
    waiting_for_null = False
    try:
        im = ImageGrab.grab(new_rect)
    except Image.DecompressionBombError:
        print('DecompressionBombError')
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

        if config["debug"]:
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

    if config["debug"]:
        im.show()
        return

    # sanity check
    if not (verify_read_data(current_decoded)):
        return

    first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line = parts

    return first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line


print("========== DiscordRichPresence Service - v0.0.8 ==========")
print("Started DiscordRichPresence Service for \"" + config["process_name"] + "\"")
print("Note: Please keep this script open while logging and sending Rich Presence updates.")
print("==========================================================")

while True:
    process_hwnd = None
    win32gui.EnumWindows(callback, None)

    if config["debug"]:
        # if in DEBUG mode, squares are read, the image with the dot matrix is
        # shown and then the script quits.
        if process_hwnd:
            print('DEBUG: reading squares. Is everything alright?')
            read_squares(process_hwnd)
        else:
            print("Launching in DEBUG mode but I couldn't find target process.")
        break
    elif win32gui.GetForegroundWindow() == process_hwnd:
        lines = read_squares(process_hwnd)

        if not lines:
            time.sleep(config["scan_rate"])
            continue

        first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eighth_line, ninth_line = lines
        hasIdChanged = first_line != last_first_line

        if hasIdChanged or second_line != last_second_line or third_line != last_third_line or fourth_line != last_fourth_line or fifth_line != last_fifth_line or sixth_line != last_sixth_line or seventh_line != last_seventh_line or eighth_line != last_eighth_line or ninth_line != last_ninth_line:
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

            if not rpc_obj or hasIdChanged:
                if rpc_obj:
                    print('The client id has been changed, reconnecting with new id...')
                    rpc_obj.close()
                else:
                    print('Not connected to Discord, connecting...')

                while True:
                    try:
                        rpc_obj = (rpc.DiscordIpcClient
                                   .for_platform(first_line))
                    except Exception as exc:
                        print("Unable to connect to Discord (%s). It's "
                              'probably not running. I will try again in 5 '
                              'sec.' % str(exc))
                        time.sleep(config["refresh_rate"])
                        pass
                    else:
                        break
                print('Connected to Discord.')

            timerData = {}
            assetsData = {}
            activity = {}
            # Asset Data Sync
            if not ("Skip" in second_line):
                assetsData["large_image"] = second_line
                if not ("Skip" in third_line):
                    assetsData["large_text"] = third_line
            if not ("Skip" in fourth_line):
                assetsData["small_image"] = fourth_line
                if not ("Skip" in fifth_line):
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
            if not ("Skip" in str(eighth_line)):
                timerData["start"] = eighth_line
                last_start_timestamp = eighth_line
                if not ("Skip" in str(ninth_line)):
                    timerData["end"] = ninth_line
                    last_end_timestamp = ninth_line
            # Activity Data Sync
            if not ("Skip" in sixth_line):
                activity["details"] = sixth_line
            if not ("Skip" in seventh_line):
                activity["state"] = seventh_line

            activity["assets"] = assetsData
            activity["timestamps"] = timerData
            if activity != last_activity:
                print("Setting new activity: %s" % activity)

                try:
                    rpc_obj.set_activity(activity)
                    last_activity = activity
                except Exception as exc:
                    print('Looks like the connection to Discord was broken (%s). '
                          'I will try to connect again in %s sec.' % (str(exc), config["refresh_rate"]))
                    last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eighth_line, last_ninth_line = None, None, None, None, None, None, None, None, None
                    last_start_timestamp, last_end_timestamp = None, None
                    last_activity = {}
                    rpc_obj = None
    elif not process_hwnd and rpc_obj:
        print('Target process is no longer active, disconnecting')
        rpc_obj.close()
        rpc_obj = None
        # clear these so it gets re-read and resubmitted upon reconnection
        last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eighth_line, last_ninth_line = None, None, None, None, None, None, None, None, None
        last_start_timestamp, last_end_timestamp = None, None
        last_activity = {}
    time.sleep(config["refresh_rate"])
