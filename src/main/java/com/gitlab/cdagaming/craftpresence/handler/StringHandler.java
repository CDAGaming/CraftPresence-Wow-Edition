package com.gitlab.cdagaming.craftpresence.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StringHandler {
    private static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");

    public static boolean isNullOrEmpty(final String entry) {
        return entry == null || entry.isEmpty() || entry.equalsIgnoreCase("null");
    }

    public static boolean isValidBoolean(final String entry) {
        return !isNullOrEmpty(entry) && (entry.equalsIgnoreCase("true") || entry.equalsIgnoreCase("false"));
    }

    public static boolean isValidInteger(final String entry) {
        if (!isNullOrEmpty(entry)) {
            try {
                Integer.parseInt(entry);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isValidLong(final String entry) {
        if (!isNullOrEmpty(entry)) {
            try {
                Long.parseLong(entry);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String formatIP(final String input, final boolean returnPort) {
        if (!isNullOrEmpty(input) && input.contains(":")) {
            final String[] formatted = input.split(":");
            return !returnPort ? formatted[0] : formatted[1];
        } else {
            return !returnPort ? getLocalIP() : "25565";
        }
    }

    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress().trim();
        } catch (Exception ex) {
            return "0.0.0.0";
        }
    }

    public static String formatPackIcon(final String original) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey)) {
            return formattedKey;
        } else {
            if (formattedKey.contains(" ")) {
                formattedKey = formattedKey.replaceAll(" ", "");
            }
            if (formattedKey.contains("'")) {
                formattedKey = formattedKey.replaceAll("'", "");
            }
            if (formattedKey.contains(".")) {
                formattedKey = formattedKey.replaceAll("\\.", "_");
            }
            if (BRACKET_PATTERN.matcher(formattedKey).find()) {
                formattedKey = BRACKET_PATTERN.matcher(formattedKey).replaceAll("");
            }
            if (STRIP_COLOR_PATTERN.matcher(formattedKey).find()) {
                formattedKey = STRIP_COLOR_PATTERN.matcher(formattedKey).replaceAll("");
            }
            return formattedKey.toLowerCase();
        }
    }

    public static String[] adjustArraySize(final String[] theArray, final int adjustBy, final int operationID) {
        int i = theArray.length;
        int n = i + adjustBy;

        if (operationID == 0) {
            n = i + adjustBy;
        }
        if (operationID == 1) {
            n = i - adjustBy;
        }

        String[] newArray = new String[n];
        System.arraycopy(theArray, 0, newArray, 0, theArray.length);
        return newArray;
    }

    public static String[] addToArray(final String[] array, final int index, final String message) {
        if (array.length <= index) {
            int extendNum = index - array.length;
            String[] newArray = adjustArraySize(array, extendNum + 1, 0);
            newArray[index] = message;
            return newArray;
        } else {
            array[index] = message;
            return array;
        }
    }

    public static String[] removeFromArray(final String[] originalArray, final String searchTerm, final int searchIndex, final String splitCharacter) {
        int indexNumber = 0;
        List<String> formatted = new ArrayList<>(Arrays.asList(originalArray));
        if (!isNullOrEmpty(formatted.toString())) {
            for (String part : formatted) {
                String[] splitPart = part.split(splitCharacter);
                if (!StringHandler.isNullOrEmpty(splitPart[searchIndex]) && splitPart[searchIndex].equalsIgnoreCase(searchTerm)) {
                    formatted.remove(indexNumber);
                    break;
                }
                indexNumber++;
            }
        }
        return formatted.toArray(new String[0]);
    }

    public static String getConfigPart(final String[] original, final String searchTerm, final int searchIndex, final int resultIndex, final String splitCharacter, final String alternativeMSG) {
        String formattedKey = "";
        boolean matched = false;
        for (String part : original) {
            String[] splitPart = part.split(splitCharacter);
            if (splitPart[searchIndex].equalsIgnoreCase(searchTerm) && elementExists(splitPart, resultIndex)) {
                formattedKey = splitPart[resultIndex];
                matched = true;
                break;
            }
        }
        return !matched && !isNullOrEmpty(alternativeMSG) ? alternativeMSG : formattedKey;
    }

    public static String[] setConfigPart(final String[] original, final String searchTerm, final int searchIndex, final int resultIndex, final String splitCharacter, final String newMessage) {
        int indexNumber = -1;
        boolean replacing = false;
        String[] formatted = original;
        String searchKey = searchTerm;

        if (searchKey.contains(" ")) {
            searchKey = searchKey.replaceAll(" ", "");
        }

        if (!isNullOrEmpty(Arrays.toString(formatted))) {
            for (String part : formatted) {
                indexNumber++;
                String[] splitPart = part.split(splitCharacter);
                if (splitPart[searchIndex].equalsIgnoreCase(searchKey)) {
                    replacing = true;
                    if (elementExists(splitPart, resultIndex)) {
                        final String formattedText = part.replace(splitPart[resultIndex], newMessage);
                        formatted[indexNumber] = formattedText;
                    } else {
                        formatted[indexNumber] = part + splitCharacter + newMessage;
                    }
                    break;
                }
            }
            if (!replacing) {
                formatted = addToArray(original, indexNumber + 1, searchKey + splitCharacter + newMessage);
            }
        }
        return formatted;
    }

    public static String formatWord(final String original) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey)) {
            return formattedKey;
        } else {
            if (formattedKey.contains("_")) {
                formattedKey = formattedKey.replaceAll("_", " ");
            }
            if (formattedKey.contains("-")) {
                formattedKey = formattedKey.replaceAll("-", " ");
            }
            if (BRACKET_PATTERN.matcher(formattedKey).find()) {
                formattedKey = BRACKET_PATTERN.matcher(formattedKey).replaceAll("");
            }
            if (STRIP_COLOR_PATTERN.matcher(formattedKey).find()) {
                formattedKey = STRIP_COLOR_PATTERN.matcher(formattedKey).replaceAll("");
            }
            return WordUtils.capitalizeFully(formattedKey);
        }
    }

    public static List<String> splitTextByNewLine(final String original) {
        if (!isNullOrEmpty(original)) {
            String formattedText = original;
            if (formattedText.contains("\n")) {
                formattedText = original.replace("\n", "&newline&");
            }
            if (formattedText.contains("\\n")) {
                formattedText = original.replace("\\n", "&newline&");
            }
            if (formattedText.contains("\\\\n+")) {
                formattedText = original.replace("\\\\n+", "&newline&");
            }
            return Arrays.asList(formattedText.split("&newline&"));
        } else {
            return null;
        }
    }

    public static void sendMessageToPlayer(final Entity sender, final String message) {
        if (sender instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) sender;
            final List<String> lines = splitTextByNewLine(message);
            if (lines != null && !lines.isEmpty()) {
                for (String line : lines) {
                    player.sendMessage(new TextComponentString(line));
                }
            }
        }
    }

    public static boolean elementExists(final String[] data, final int index) {
        return elementExists(Arrays.asList(data), index);
    }

    public static boolean elementExists(final List<String> data, final int index) {
        boolean result;
        try {
            result = data.size() >= index && !isNullOrEmpty(data.get(index));
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            result = false;
        }
        return result;
    }

    public static Object lookupObject(Class classToAccess, Object instance, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field lookupField = classToAccess.getDeclaredField(fieldName);
                if (lookupField != null) {
                    lookupField.setAccessible(true);
                    return lookupField.get(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static String stripColors(final String input) {
        return isNullOrEmpty(input) ? input : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
