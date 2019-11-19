package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");
    public static int[] MC_CHAR_WIDTH = new int[256];
    public static byte[] MC_GLYPH_WIDTH = new byte[65536];

    public static Color getColorFromHex(final String hexColor) {
        try {
            if (hexColor.length() == 7 && !StringUtils.isNullOrEmpty(hexColor.substring(1))) {
                int r = Integer.valueOf(hexColor.substring(1, 3), 16);
                int g = Integer.valueOf(hexColor.substring(3, 5), 16);
                int b = Integer.valueOf(hexColor.substring(5, 7), 16);

                return new Color(r, g, b);
            } else if (hexColor.length() == 6 && !hexColor.startsWith("#")) {
                int r = Integer.valueOf(hexColor.substring(0, 2), 16);
                int g = Integer.valueOf(hexColor.substring(2, 4), 16);
                int b = Integer.valueOf(hexColor.substring(4, 6), 16);

                return new Color(r, g, b);
            } else {
                return Color.white;
            }
        } catch (Exception ex) {
            return Color.white;
        }
    }

    public static String getHexFromColor(Color color) {
        return "0x" + toSafeHexValue(color.getAlpha()) + toSafeHexValue(color.getRed()) + toSafeHexValue(color.getGreen()) + toSafeHexValue(color.getBlue());
    }

    private static String toSafeHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    public static Tuple<String, List<String>> getMatches(final String regexValue, final String original) {
        List<String> matches = Lists.newArrayList();

        if (!isNullOrEmpty(original)) {
            Pattern pattern = Pattern.compile(regexValue);
            Matcher m = pattern.matcher(original);

            while (m.find()) {
                matches.add(m.group());
            }
        }

        return new Tuple<>(original, matches);
    }

    public static String removeMatches(final Tuple<String, List<String>> matchData, final int maxMatches, final boolean useMax) {
        String finalString = "";

        if (matchData != null) {
            finalString = matchData.getFirst();
            List<String> matchList = matchData.getSecond();

            if (!matchList.isEmpty()) {
                int foundMatches = 0;

                for (String match : matchList) {
                    if (!useMax || foundMatches > maxMatches) {
                        finalString = finalString.replaceFirst(match, "");
                    }
                    foundMatches++;
                }
            }
        }

        return finalString;
    }

    public static String replaceAnyCase(String source, String targetToReplace, String replaceWith) {
        if (!isNullOrEmpty(source)) {
            return Pattern.compile(targetToReplace, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(source)
                    .replaceAll(Matcher.quoteReplacement(replaceWith));
        } else {
            return "";
        }
    }

    public static String sequentialReplaceAnyCase(String source, List<Tuple<String, String>> replaceData) {
        if (!isNullOrEmpty(source)) {
            String finalResult = source;

            if (!replaceData.isEmpty()) {
                for (Tuple<String, String> replacementData : replaceData) {
                    finalResult = replaceAnyCase(finalResult, replacementData.getFirst(), replacementData.getSecond());
                }
            }
            return finalResult;
        } else {
            return "";
        }
    }

    public static boolean isNullOrEmpty(final String entry) {
        return entry == null || entry.isEmpty() || entry.equalsIgnoreCase("null");
    }

    public static boolean isValidBoolean(final String entry) {
        return !isNullOrEmpty(entry) && (entry.equalsIgnoreCase("true") || entry.equalsIgnoreCase("false"));
    }

    public static boolean isValidColorCode(final String entry) {
        return !isNullOrEmpty(entry) && ((entry.startsWith("#") || entry.length() == 6) || entry.startsWith("0x") || getValidInteger(entry).getFirst());
    }

    public static Tuple<Boolean, Integer> getValidInteger(final String entry) {
        Tuple<Boolean, Integer> finalSet = new Tuple<>();

        if (!isNullOrEmpty(entry)) {
            try {
                finalSet.setSecond(Integer.parseInt(entry));
                finalSet.setFirst(true);
            } catch (Exception ex) {
                finalSet.setFirst(false);
            }
        } else {
            finalSet.setFirst(false);
        }

        return finalSet;
    }

    public static Tuple<Boolean, Long> getValidLong(final String entry) {
        Tuple<Boolean, Long> finalSet = new Tuple<>();

        if (!isNullOrEmpty(entry)) {
            try {
                finalSet.setSecond(Long.parseLong(entry));
                finalSet.setFirst(true);
            } catch (Exception ex) {
                finalSet.setFirst(false);
            }
        } else {
            finalSet.setFirst(false);
        }

        return finalSet;
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
                if (!StringUtils.isNullOrEmpty(splitPart[searchIndex]) && splitPart[searchIndex].equalsIgnoreCase(searchTerm)) {
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

            if (formattedKey.toString().contains(":")) {
                formattedKey = new StringBuilder(formattedKey.toString().split(":", 2)[1]);
            }

            if (formattedKey.toString().contains("{") || formattedKey.toString().contains("}")) {
                formattedKey = new StringBuilder(formattedKey.toString().replaceAll("[{}]", ""));
            }

            if (formattedKey.toString().equalsIgnoreCase("surface")) {
                return "overworld";
            } else if (formattedKey.toString().equalsIgnoreCase("hell") || formattedKey.toString().equalsIgnoreCase("nether")) {
                return "the_nether";
            } else if (formattedKey.toString().equalsIgnoreCase("end") || formattedKey.toString().equalsIgnoreCase("sky")) {
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
            String subString = stringInput.substring(0, stringSizeToWidth);
            char currentCharacter = stringInput.charAt(stringSizeToWidth);
            boolean flag = Character.isSpaceChar(currentCharacter) || currentCharacter == '\n';
            String s1 = getFormatFromString(subString) + stringInput.substring(stringSizeToWidth + (flag ? 1 : 0));
            return subString + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    public static String getFormatFromString(String text) {
        StringBuilder s = new StringBuilder();
        int index = -1;
        int stringLength = text.length();

        while ((index = text.indexOf(167, index + 1)) != -1) {
            if (index < stringLength - 1) {
                char currentCharacter = text.charAt(index + 1);

                if (STRIP_COLOR_PATTERN.matcher(String.valueOf(currentCharacter)).find()) {
                    s = new StringBuilder("\u00a7" + currentCharacter);
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
                strLength += getCharWidth(strChar, ModUtils.TRANSLATOR.isUnicode);
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
            try {
                if (characterInput > 0 && characterInput <= MC_CHAR_WIDTH.length && !usingUnicode) {
                    return MC_CHAR_WIDTH[characterInput];
                } else if (MC_GLYPH_WIDTH[characterInput] != 0) {
                    int glyphIndex = MC_GLYPH_WIDTH[characterInput] & 255;
                    int shiftedIndex = glyphIndex >>> 4;
                    int remappedIndex = glyphIndex & 15;
                    ++remappedIndex;
                    return (remappedIndex - shiftedIndex) / 2 + 1;
                } else {
                    return 4; // Rather this be more, then it be cut off mid-text
                }
            } catch (Exception ex) {
                return 4; // Rather this be more, then it be cut off mid-text
            }
        }
    }

    public static int sizeStringToWidth(String stringEntry, int wrapWidth) {
        int stringLength = stringEntry.length();
        int charWidth = 0;
        int currentLine = 0;
        int currentIndex = -1;

        for (boolean flag = false; currentLine < stringLength; ++currentLine) {
            char currentCharacter = stringEntry.charAt(currentLine);

            if (currentCharacter == ' ' || currentCharacter == '\n') {
                currentIndex = currentLine;

                if (currentCharacter == '\n') {
                    break;
                }
            }

            if (currentCharacter == '\u00a7' && currentLine < stringLength - 1) {
                ++currentLine;
                currentCharacter = stringEntry.charAt(currentLine);
                String stringOfCharacter = String.valueOf(currentCharacter);

                flag = stringOfCharacter.equalsIgnoreCase("l") && !(stringOfCharacter.equalsIgnoreCase("r") || STRIP_COLOR_PATTERN.matcher(stringOfCharacter).find());
            }

            charWidth += getCharWidth(currentCharacter, ModUtils.TRANSLATOR.isUnicode);
            if (flag) {
                ++charWidth;
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
        char charIndex = ' ';
        for (int index = 0; index < str.length(); index++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (charIndex == ' ' && str.charAt(index) != ' ')
                s.append(Character.toUpperCase(str.charAt(index)));
            else
                s.append(str.charAt(index));
            charIndex = str.charAt(index);
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

    public static InputStream getResourceAsStream(final Class<?> fallbackClass, final String pathToSearch) {
        InputStream in = null;
        boolean useFallback = false;

        try {
            in = ModUtils.CLASS_LOADER.getResourceAsStream(pathToSearch);
        } catch (Exception ex) {
            useFallback = true;
        }

        if (useFallback || in == null) {
            in = fallbackClass.getResourceAsStream(pathToSearch);
        }
        return in;
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

    public static Object lookupObject(Class<?> classToAccess, Object instance, String... fieldNames) {
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

    public static void executeMethod(Class<?> classToAccess, Object instance, String[] methodNames, Object[] args, Class<?>... argumentTypes) {
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
