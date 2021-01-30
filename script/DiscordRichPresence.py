import rpc
import time
import json
import os

from PIL import Image, ImageGrab
import win32api
import win32con
import win32gui

dir_path = os.path.dirname(os.path.realpath(__file__))

f = open(dir_path + '/config.json')
config = json.load(f)

# these are internal use variables, don't touch them
decoded = ''
wow_hwnd = None
rpc_obj = None
last_first_line = None
last_second_line = None
last_third_line = None
last_fourth_line = None

def callback(hwnd, extra):
    global wow_hwnd
    if (win32gui.GetWindowText(hwnd) == 'World of Warcraft' and
            win32gui.GetClassName(hwnd).startswith('GxWindowClass')):
        wow_hwnd = hwnd


def read_squares(hwnd):
    rect = win32gui.GetWindowRect(hwnd)
    #height = (win32api.GetSystemMetrics(win32con.SM_CYCAPTION) +
    #        win32api.GetSystemMetrics(win32con.SM_CYBORDER) * 4 +
    #        win32api.GetSystemMetrics(win32con.SM_CYEDGE) * 2)
    new_rect = (rect[0], rect[1], rect[2], config["my_width"])
    try:
        im = ImageGrab.grab(new_rect)
    except Image.DecompressionBombError:
        print('DecompressionBombError')
        return

    read = []
    for square_idx in range(int(im.width / config["my_width"])):
        x = int(square_idx * config["my_width"] + config["my_width"] / 2)
        y = int(config["my_width"] / 2)
        r, g, b = im.getpixel((x, y))

        if config["debug"]:
            im.putpixel((x, y), (255, 255, 255))

        if r == g == b == 0:
            break

        read.append(r)
        read.append(g)
        read.append(b)

    try:
        decoded = bytes(read).decode('utf-8').rstrip('\0')
    except:
        if not config["debug"]:
            return
    parts = decoded.replace('$WorldOfWarcraftDRP$', '').split('|')

    if config["debug"]:
        im.show()
        return

    # sanity check
    if (len(parts) != 4 or
            not decoded.endswith('$WorldOfWarcraftDRP$') or
            not decoded.startswith('$WorldOfWarcraftDRP$')):
        return

    first_line, second_line, third_line, fourth_line = parts

    return first_line, second_line, third_line, fourth_line


while True:
    wow_hwnd = None
    win32gui.EnumWindows(callback, None)

    if config["debug"]:
        # if in DEBUG mode, squares are read, the image with the dot matrix is
        # shown and then the script quits.
        if wow_hwnd:
            print('DEBUG: reading squares. Is everything alright?')
            read_squares(wow_hwnd)
        else:
            print("Launching in DEBUG mode but I couldn't find WoW.")
        break
    elif win32gui.GetForegroundWindow() == wow_hwnd:
        lines = read_squares(wow_hwnd)

        if not lines:
            time.sleep(1)
            continue

        first_line, second_line, third_line, fourth_line = lines

        if first_line != last_first_line or second_line != last_second_line:
            # there has been an update, so send it to discord
            last_first_line = first_line
            last_second_line = second_line
            last_third_line = third_line
            last_fourth_line = fourth_line

            if not rpc_obj:
                print('Not connected to Discord, connecting...')
                while True:
                    try:
                        rpc_obj = (rpc.DiscordIpcClient
                                .for_platform(config["discord_client_id"]))
                    except Exception as exc:
                        print("I couldn't connect to Discord (%s). It's "
                            'probably not running. I will try again in 5 '
                            'sec.' % str(exc))
                        time.sleep(5)
                        pass
                    else:
                        break
                print('Connected to Discord.')

            print('Setting new activity: %s - %s - %s - %s' % (first_line, second_line, third_line, fourth_line))
            dungeonTimer = {}
            if("Dungeon" in second_line or "Raid" in second_line or "Battleground" in second_line):
                dungeonTimer = { 'start': round(time.time()) }
            activity = {
                'details': third_line,
                'state': first_line + ' - ' + second_line,
                'assets': {
                    'large_image': config["wow_icon"],
                    'large_text': fourth_line
                },
                'timestamps': dungeonTimer
            }

            try:
                rpc_obj.set_activity(activity)
            except Exception as exc:
                print('Looks like the connection to Discord was broken (%s). '
                    'I will try to connect again in 5 sec.' % str(exc))
                last_first_line, last_second_line, last_third_line, last_fourth_line = None, None, None, None
                rpc_obj = None
    elif not wow_hwnd and rpc_obj:
        print('WoW no longer exists, disconnecting')
        rpc_obj.close()
        rpc_obj = None
        # clear these so it gets reread and resubmitted upon reconnection
        last_first_line, last_second_line, last_third_line, last_fourth_line = None, None, None, None
    time.sleep(5)
