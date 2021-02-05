import rpc
import time
import json
import os

from PIL import Image, ImageGrab
import win32gui

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
last_eigth_line = None
last_ninth_line = None

def callback(hwnd, extra):
    global process_hwnd
    if (win32gui.GetWindowText(hwnd) == config["process_name"] and
            win32gui.GetClassName(hwnd).startswith('GxWindowClass')):
        process_hwnd = hwnd


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
    parts = decoded.replace('$RPCEvent$', '').split('|')

    if config["debug"]:
        im.show()
        return

    # sanity check
    if (len(parts) != 9 or
            not decoded.endswith('$RPCEvent$') or
            not decoded.startswith('$RPCEvent$')):
        return

    first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eigth_line, ninth_line = parts

    return first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eigth_line, ninth_line


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
            time.sleep(1)
            continue

        first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eigth_line, ninth_line = lines
        hasIdChanged = first_line != last_first_line

        if hasIdChanged or second_line != last_second_line or third_line != last_third_line or fourth_line != last_fourth_line or fifth_line != last_fifth_line or sixth_line != last_sixth_line or seventh_line != last_seventh_line or eigth_line != last_eigth_line or ninth_line != last_ninth_line:
            # there has been an update, so send it to discord
            last_first_line = first_line
            last_second_line = second_line
            last_third_line = third_line
            last_fourth_line = fourth_line
            last_fifth_line = fifth_line
            last_sixth_line = sixth_line
            last_seventh_line = seventh_line
            last_eigth_line = eigth_line
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
                        print("I couldn't connect to Discord (%s). It's "
                            'probably not running. I will try again in 5 '
                            'sec.' % str(exc))
                        time.sleep(5)
                        pass
                    else:
                        break
                print('Connected to Discord.')

            timerData = {}
            assetsData = {}
            activity = {}
            # Asset Data Sync
            if not("Skip" in second_line):
                assetsData["large_image"] = second_line
                if not("Skip" in third_line):
                    assetsData["large_text"] = third_line
            if not("Skip" in fourth_line):
                assetsData["small_image"] = fourth_line
                if not("Skip" in fifth_line):
                    assetsData["small_text"] = fifth_line
            # Timer Data Setup
            if("generated" in eigth_line):
                eigth_line = round(time.time())
            if("generated" in ninth_line):
                ninth_line = round(time.time())
            # Timer Data Sync
            if not("Skip" in str(eigth_line)):
                timerData["start"] = eigth_line
                if not("Skip" in str(ninth_line)):
                    timerData["end"] = ninth_line
            # Activity Data Sync
            if not("Skip" in sixth_line):
                activity["details"] = sixth_line
            if not("Skip" in seventh_line):
                activity["state"] = seventh_line

            activity["assets"] = assetsData
            activity["timestamps"] = timerData
            #print('Setting new activity {\n\tClient Id: %s,\n\tLargeImageKey: %s,\n\tLargeImageText: %s,\n\tSmallImageKey: %s,\n\tSmallImageText: %s,\n\tDetails: %s,\n\tGameState: %s,\n\tStartTime: %s,\n\tEndTime: %s\n}' % (first_line, second_line, third_line, fourth_line, fifth_line, sixth_line, seventh_line, eigth_line, ninth_line))
            print("Setting new activity: %s" % activity)

            try:
                rpc_obj.set_activity(activity)
            except Exception as exc:
                print('Looks like the connection to Discord was broken (%s). '
                    'I will try to connect again in 5 sec.' % str(exc))
                last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eigth_line, last_ninth_line = None, None, None, None, None, None, None, None, None
                rpc_obj = None
    elif not process_hwnd and rpc_obj:
        print('Target process is no longer active, disconnecting')
        rpc_obj.close()
        rpc_obj = None
        # clear these so it gets reread and resubmitted upon reconnection
        last_first_line, last_second_line, last_third_line, last_fourth_line, last_fifth_line, last_sixth_line, last_seventh_line, last_eigth_line, last_ninth_line = None, None, None, None, None, None, None, None, None
    time.sleep(5)
