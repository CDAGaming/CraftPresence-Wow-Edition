package com.gitlab.cdagaming.craftpresence.handler;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringHandler {
    private static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");

    public static boolean isNullOrEmpty(final String entry) {
        return entry == null || entry.isEmpty();
    }

    public static String formatIP(final String input) {
        if (input.contains(":")) {
            final String[] formatted = input.split(":");
            return formatted[0];
        } else {
            return input;
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
            if (formattedKey.contains("-")) {
                formattedKey = formattedKey.replaceAll("-", "_");
            }
            if (BRACKET_PATTERN.matcher(formattedKey).find()) {
                formattedKey = BRACKET_PATTERN.matcher(formattedKey).replaceAll("");
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

    public static String[] removeFromArray(final String[] originalArray, final String toRemove) {
        return Arrays.stream(originalArray).filter(obj -> !obj.equals(toRemove)).toArray(String[]::new);
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
        if (!matched && alternativeMSG != null) {
            return alternativeMSG;
        } else {
            return formattedKey;
        }
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
                        formatted[indexNumber] = searchKey + splitCharacter + newMessage;
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
            return WordUtils.capitalizeFully(formattedKey);
        }
    }

    public static boolean elementExists(final String[] data, final int index) {
        boolean result = false;
        try {
            if (data.length >= index & data[index] != null) {
                result = true;
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            result = false;
        }
        return result;
    }

    public static String stripColors(final String input) {
        return isNullOrEmpty(input) ? input : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
