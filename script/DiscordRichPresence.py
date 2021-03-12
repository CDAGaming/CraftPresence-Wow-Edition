import json
import os
import time
from ctypes import windll

import win32gui
import win32ui
from PIL import Image

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
        # print('win32ui.error')
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


print("========== DiscordRichPresence Service - v1.0.0 ==========")
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
    elif process_hwnd:
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
                              'probably not running. I will try again in %s '
                              'sec.' % (str(exc), config["refresh_rate"]))
                        time.sleep(config["refresh_rate"])
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
