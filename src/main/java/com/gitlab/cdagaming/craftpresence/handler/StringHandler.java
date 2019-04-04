package com.gitlab.cdagaming.craftpresence.handler;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Field;
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
        if (!isNullOrEmpty(input)) {
            final String[] formatted = input.split(":", 2);
            return !returnPort ? (elementExists(formatted, 0) ? formatted[0] : "127.0.0.1") : (elementExists(formatted, 1) ? formatted[1] : "25565");
        } else {
            return !returnPort ? "127.0.0.1" : "25565";
        }
    }

    public static String formatPackIcon(final String original) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey)) {
            return formattedKey;
        } else {
            if (formattedKey.contains("\\s")) {
                formattedKey = formattedKey.replaceAll("\\s+", "");
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
        List<String> formatted = Lists.newLinkedList(Arrays.asList(originalArray));
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
            searchKey = searchKey.replaceAll("\\s+", "");
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
            if (formattedKey.contains(" ")) {
                formattedKey = formattedKey.replaceAll("\\s+", " ");
            }
            if (BRACKET_PATTERN.matcher(formattedKey).find()) {
                formattedKey = BRACKET_PATTERN.matcher(formattedKey).replaceAll("");
            }
            if (STRIP_COLOR_PATTERN.matcher(formattedKey).find()) {
                formattedKey = STRIP_COLOR_PATTERN.matcher(formattedKey).replaceAll("");
            }

            return removeRepeatWords(capitalizeWord(formattedKey)).trim();
        }
    }

    public static String removeRepeatWords(final String original) {
        if (isNullOrEmpty(original)) {
            return original;
        } else {
            String lastWord = "";
            StringBuilder finalString = new StringBuilder();
            String[] wordList = original.split(" ");

            for (String word : wordList) {
                if (isNullOrEmpty(lastWord) || !word.equals(lastWord)) {
                    finalString.append(word).append(" ");
                    lastWord = word;
                }
            }

            return finalString.toString().trim();
        }
    }

    public static String formatDimensionName(final String dimName, final boolean formatToID) {
        StringBuilder formattedKey = new StringBuilder(dimName);
        if (isNullOrEmpty(formattedKey.toString())) {
            return formattedKey.toString();
        } else {
            if (formattedKey.toString().contains("WorldProvider")) {
                formattedKey = new StringBuilder(formattedKey.toString().replace("WorldProvider", ""));
            }

            if (formattedKey.toString().contains(" ")) {
                formattedKey = new StringBuilder(formattedKey.toString().replaceAll("\\s+", " "));
            }

            if (formattedKey.toString().equalsIgnoreCase("surface")) {
                return "overworld";
            } else if (formattedKey.toString().equalsIgnoreCase("hell") || formattedKey.toString().equalsIgnoreCase("nether")) {
                return "the_nether";
            } else if (formattedKey.toString().equalsIgnoreCase("end")) {
                return "the_end";
            } else {
                if (formatToID) {
                    return formattedKey.toString().trim().toLowerCase().replace(" ", "_");
                } else {
                    return formatWord(formattedKey.toString());
                }
            }
        }
    }

    public static String capitalizeWord(String str) {
        StringBuilder s = new StringBuilder();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (ch == ' ' && str.charAt(i) != ' ')
                s.append(Character.toUpperCase(str.charAt(i)));
            else
                s.append(str.charAt(i));
            ch = str.charAt(i);
        }

        // Return the string with trimming
        return s.toString().trim();
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
            return Lists.newArrayList();
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
        } catch (Exception ex) {
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

    public static int generateHash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static String stripColors(final String input) {
        return isNullOrEmpty(input) ? input : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
