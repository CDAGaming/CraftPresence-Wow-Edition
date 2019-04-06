package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StringHandler {
    private static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");
    private static final String MC_CHAR_MAPPINGS = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
    private static int[] MC_CHAR_WIDTH = new int[256];
    private static byte[] MC_GLYPH_WIDTH = new byte[65536];

    public static void init() {
        MC_CHAR_WIDTH = (int[]) lookupObject(FontRenderer.class, CraftPresence.instance.fontRenderer, "charWidth");
        MC_GLYPH_WIDTH = (byte[]) lookupObject(FontRenderer.class, CraftPresence.instance.fontRenderer, "glyphWidth");
    }

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

    public static String wrapFormattedStringToWidth(String stringInput, int wrapWidth) {
        int stringSizeToWidth = sizeStringToWidth(stringInput, wrapWidth);

        if (stringInput.length() <= stringSizeToWidth) {
            return stringInput;
        } else {
            String s = stringInput.substring(0, stringSizeToWidth);
            char currentCharacter = stringInput.charAt(stringSizeToWidth);
            boolean flag = Character.isSpaceChar(currentCharacter) || currentCharacter == '\n';
            String s1 = getFormatFromString(s) + stringInput.substring(stringSizeToWidth + (flag ? 1 : 0));
            return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    public static String getFormatFromString(String text) {
        StringBuilder s = new StringBuilder();
        int i = -1;
        int j = text.length();

        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = text.charAt(i + 1);

                if (STRIP_COLOR_PATTERN.matcher(String.valueOf(c0)).find()) {
                    s = new StringBuilder("\u00a7" + c0);
                }
            }
        }

        return s.toString();
    }

    public static int getStringWidth(String stringEntry) {
        if (isNullOrEmpty(stringEntry)) {
            return 0;
        } else {
            int strLength = 0;
            for (char strChar : stringEntry.toCharArray()) {
                strLength += getCharWidth(strChar, Constants.TRANSLATOR.isUnicode);
            }
            return strLength;
        }
    }

    public static int getCharWidth(char characterInput, boolean usingUnicode) {
        if (Character.isSpaceChar(characterInput) || characterInput == 160) {
            return 4;
        } else if (characterInput == 167) {
            return -1;
        } else {
            int index = MC_CHAR_MAPPINGS.indexOf(characterInput);
            if (characterInput > 0 && index != -1 && !usingUnicode) {
                return MC_CHAR_WIDTH[index];
            } else if (MC_GLYPH_WIDTH[characterInput] != 0) {
                int glyphIndex = MC_GLYPH_WIDTH[characterInput] & 255;
                int shiftedIndex = glyphIndex >>> 4;
                int remappedIndex = glyphIndex & 15;
                ++remappedIndex;
                return (remappedIndex - shiftedIndex) / 2 + 1;
            } else {
                return 0;
            }
        }
    }

    public static int sizeStringToWidth(String stringEntry, int wrapWidth) {
        int stringLength = stringEntry.length();
        int charWidth = 0;
        int currentLine = 0;
        int currentIndex = -1;

        for (boolean flag = false; currentLine < stringLength; ++currentLine) {
            char c0 = stringEntry.charAt(currentLine);

            switch (c0) {
                case '\n':
                    --currentLine;
                    break;
                case ' ':
                    currentIndex = currentLine;
                default:
                    charWidth += getCharWidth(c0, Constants.TRANSLATOR.isUnicode);
                    if (flag) {
                        ++charWidth;
                    }

                    break;
                case '\u00a7':
                    if (currentLine < stringLength - 1) {
                        ++currentLine;

                        char c1 = stringEntry.charAt(currentLine);

                        String stringOfCharacter = String.valueOf(c1);

                        flag = stringOfCharacter.equalsIgnoreCase("l") && !(stringOfCharacter.equalsIgnoreCase("r") || STRIP_COLOR_PATTERN.matcher(stringOfCharacter).find());
                    }
            }

            if (c0 == '\n') {
                ++currentLine;
                currentIndex = currentLine;
                break;
            }

            if (charWidth > wrapWidth) {
                break;
            }
        }


        return currentLine != stringLength && currentIndex != -1 && currentIndex < currentLine ? currentIndex : currentLine;
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

    public static void executeMethod(Class classToAccess, Object instance, String[] methodNames, Object[] args, Class<?>... argumentTypes) {
        for (String methodName : methodNames) {
            try {
                Method lookupMethod = classToAccess.getDeclaredMethod(methodName, argumentTypes);
                if (lookupMethod != null) {
                    lookupMethod.setAccessible(true);
                    lookupMethod.invoke(instance, args);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static int generateHash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static String stripColors(final String input) {
        return isNullOrEmpty(input) ? input : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
