/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utilities for interpreting Strings and Basic Data Types
 *
 * @author CDAGaming
 */
public class StringUtils {
    /**
     * The Character to be interpreted as the start to a Formatting Character
     */
    private static final char COLOR_CHAR = '\u00A7';

    /**
     * Regex Pattern for Color and Formatting Codes
     */
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");

    /**
     * Regex Pattern for Brackets containing Digits
     */
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");

    /**
     * The Stored Character Render Widths to interpret when rendering tooltips
     */
    public static int[] MC_CHAR_WIDTH = new int[256];

    /**
     * The Stored Unicode Character Glyph Render Widths to interpret when rendering tooltips
     */
    public static byte[] MC_GLYPH_WIDTH = new byte[65536];

    /**
     * Attempts to Convert a Hexadecimal String into a Valid interpretable Java Color
     *
     * @param hexColor The inputted Hexadecimal Color String
     * @return A Valid Java Color, if successful
     */
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

    /**
     * Converts a String and it's bytes to that of the Specified Charset
     *
     * @param original The original String
     * @param encoding The Charset to encode the String under
     * @param decode   If we are Decoding an already encoded String
     * @return The converted UTF_8 String, if successful
     */
    public static String getConvertedString(String original, String encoding, boolean decode) {
        try {
            if (decode) {
                return new String(original.getBytes(), encoding).replaceAll("\\s+", " ");
            } else {
                return new String(original.getBytes(encoding)).replaceAll("\\s+", " ");
            }
        } catch (Exception ex) {
            return original;
        }
    }

    /**
     * Rounds a Double to the defined decimal place, if possible
     *
     * @param value  the original value to round
     * @param places The amount of places to round upon
     * @return The rounded Double value
     */
    public static double roundDouble(double value, int places) {
        if (places >= 0) {
            return Double.parseDouble(String.format("%." + places + "f", value));
        } else {
            // Do not Round if Places is less then 0
            return value;
        }
    }

    /**
     * Converts a Java Color Variable into a Hexadecimal String
     *
     * @param color The original Java Color Type to interpret
     * @return The converted hexadecimal String
     */
    public static String getHexFromColor(Color color) {
        return "0x" + toSafeHexValue(color.getAlpha()) + toSafeHexValue(color.getRed()) + toSafeHexValue(color.getGreen()) + toSafeHexValue(color.getBlue());
    }

    /**
     * Converts an inputted number to a compatible Hexadecimal String
     *
     * @param number The original number
     * @return The converted and compatible hexadecimal String
     */
    private static String toSafeHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    /**
     * Retrieve Matching Values from an input that matches the defined regex
     *
     * @param regexValue The Regex Value to test against
     * @param original   The original Object to get matches from
     * @return A Pair with the Format of originalString:listOfMatches
     */
    public static Pair<String, List<String>> getMatches(final String regexValue, final Object original) {
        return original != null ? getMatches(regexValue, original.toString()) : new Pair<>("", Lists.newArrayList());
    }

    /**
     * Retrieve Matching Values from an input that matches the defined regex
     *
     * @param regexValue The Regex Value to test against
     * @param original   The original String to get matches from
     * @return A Pair with the Format of originalString:listOfMatches
     */
    public static Pair<String, List<String>> getMatches(final String regexValue, final String original) {
        final List<String> matches = Lists.newArrayList();

        if (!isNullOrEmpty(original)) {
            final Pattern pattern = Pattern.compile(regexValue);
            final Matcher m = pattern.matcher(original);

            while (m.find()) {
                matches.add(m.group());
            }
        }

        return new Pair<>(original, matches);
    }

    /**
     * Remove an Amount of Matches from an inputted Match Set
     *
     * @param matchData       The Match Data to remove from with the form of originalString:listOfMatches
     * @param parsedMatchData The Parsed Argument Data to match against, if available, to prevent Null Arguments
     * @param maxMatches      The maximum amount of matches to remove (Set to -1 to Remove All)
     * @return The original String from Match Data with the matches up to maxMatches removed
     */
    public static String removeMatches(final Pair<String, List<String>> matchData, final List<Pair<String, String>> parsedMatchData, final int maxMatches) {
        String finalString = "";

        if (matchData != null) {
            finalString = matchData.getFirst();
            final List<String> matchList = matchData.getSecond();

            if (!matchList.isEmpty()) {
                int foundMatches = 0;

                for (String match : matchList) {
                    final boolean isValidScan = foundMatches >= maxMatches;
                    boolean alreadyRemoved = false;

                    if (parsedMatchData != null && !parsedMatchData.isEmpty()) {
                        // Scan through Parsed Argument Data if Possible
                        for (Pair<String, String> parsedArgument : parsedMatchData) {
                            // If found a matching argument to the match, and the parsed argument is null
                            // Remove the match without counting it as a found match
                            if (parsedArgument.getFirst().equalsIgnoreCase(match) && isNullOrEmpty(parsedArgument.getSecond())) {
                                finalString = finalString.replaceFirst(match, "");
                                alreadyRemoved = true;
                                break;
                            }
                        }
                    }

                    if (!alreadyRemoved) {
                        if (isValidScan) {
                            finalString = finalString.replaceFirst(match, "");
                        }
                        foundMatches++;
                    }
                }
            }
        }

        return finalString;
    }

    /**
     * Replaces Data in a String with Case-Insensitivity
     *
     * @param source          The original String to replace within
     * @param targetToReplace The value to replace on
     * @param replaceWith     The value to replace the target with
     * @return The completed and replaced String
     */
    public static String replaceAnyCase(final String source, final String targetToReplace, final String replaceWith) {
        return replaceAnyCase(source, targetToReplace, replaceWith, true);
    }

    /**
     * Replaces Data in a String with Case-Insensitivity
     *
     * @param source          The original String to replace within
     * @param targetToReplace The value to replace on
     * @param replaceWith     The value to replace the target with
     * @param allowMinified   Flag for whether or not to allow Minified Placeholders (Trimmed String down to a length of 4)
     * @return The completed and replaced String
     */
    public static String replaceAnyCase(final String source, final String targetToReplace, final String replaceWith, final boolean allowMinified) {
        if (!isNullOrEmpty(source)) {
            String finalString = Pattern.compile(targetToReplace, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(source)
                    .replaceAll(Matcher.quoteReplacement(replaceWith));

            if (allowMinified) {
                String minifiedTarget = minifyString(targetToReplace, 4);
                if (!minifiedTarget.endsWith("&")) {
                    minifiedTarget += "&";
                }
                finalString = Pattern.compile(minifiedTarget, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(finalString)
                        .replaceAll(Matcher.quoteReplacement(replaceWith));
            }
            return finalString;
        } else {
            return "";
        }
    }

    /**
     * Replaces Data in a sequential order, following Case-Insensitivity
     *
     * @param source      The original String to replace within
     * @param replaceData The replacement list to follow with the form of: targetToReplace:replaceWithValue
     * @return The completed and replaced String
     */
    public static String sequentialReplaceAnyCase(final String source, final List<Pair<String, String>> replaceData) {
        return sequentialReplaceAnyCase(source, replaceData, true);
    }

    /**
     * Replaces Data in a sequential order, following Case-Insensitivity
     *
     * @param source        The original String to replace within
     * @param replaceData   The replacement list to follow with the form of: targetToReplace:replaceWithValue
     * @param allowMinified Flag for whether or not to allow Minified Placeholders (Trimmed String down to a length of 4)
     * @return The completed and replaced String
     */
    public static String sequentialReplaceAnyCase(final String source, final List<Pair<String, String>> replaceData, final boolean allowMinified) {
        if (!isNullOrEmpty(source)) {
            String finalResult = source;

            if (!replaceData.isEmpty()) {
                for (Pair<String, String> replacementData : replaceData) {
                    finalResult = replaceAnyCase(finalResult, replacementData.getFirst(), replacementData.getSecond(), allowMinified);
                }
            }
            return finalResult;
        } else {
            return "";
        }
    }

    /**
     * Reduces the Length of a String to the Specified Length
     *
     * @param source The String to evaluate
     * @param length The Maximum Length to reduce the String down towards, beginning at 0
     * @return The newly reduced/minified String
     */
    public static String minifyString(final String source, final int length) {
        if (!isNullOrEmpty(source)) {
            return length >= 0 ? source.substring(0, length) : source;
        } else {
            return "";
        }
    }

    /**
     * Determines whether a String classifies as NULL or EMPTY
     *
     * @param entry The String to evaluate
     * @return {@code true} if Entry is classified as NULL or EMPTY
     */
    public static boolean isNullOrEmpty(final String entry) {
        return entry == null || entry.isEmpty() || entry.equalsIgnoreCase("null");
    }

    /**
     * Determines whether the Object's String Interpretation classifies as a valid Boolean
     *
     * @param entry The Object to evaluate
     * @return {@code true} if Entry is classified as a valid Boolean
     */
    public static boolean isValidBoolean(final Object entry) {
        return entry != null && isValidBoolean(entry.toString());
    }

    /**
     * Determines whether a String classifies as a valid Boolean
     *
     * @param entry The String to evaluate
     * @return {@code true} if Entry is classified as a valid Boolean
     */
    public static boolean isValidBoolean(final String entry) {
        return !isNullOrEmpty(entry) && (entry.equalsIgnoreCase("true") || entry.equalsIgnoreCase("false"));
    }

    /**
     * Determines whether an inputted String classifies as a valid Color Code
     *
     * @param entry The String to evaluate
     * @return {@code true} if Entry is classified as a valid Color Code
     */
    public static boolean isValidColorCode(final String entry) {
        return !isNullOrEmpty(entry) && ((entry.startsWith("#") || entry.length() == 6) || entry.startsWith("0x") || getValidInteger(entry).getFirst());
    }

    /**
     * Determine whether an inputted Object classifies as a valid Integer
     *
     * @param entry The Object to evaluate
     * @return A Pair with the format of isValid:parsedIntegerIfTrue
     */
    public static Pair<Boolean, Integer> getValidInteger(final Object entry) {
        return entry != null ? getValidInteger(entry.toString()) : new Pair<>(false, 0);
    }

    /**
     * Determine whether an inputted String classifies as a valid Integer
     *
     * @param entry The String to evaluate
     * @return A Pair with the format of isValid:parsedIntegerIfTrue
     */
    public static Pair<Boolean, Integer> getValidInteger(final String entry) {
        final Pair<Boolean, Integer> finalSet = new Pair<>();

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

    /**
     * Determine whether an inputted String classifies as a valid Long
     *
     * @param entry The String to evaluate
     * @return A Pair with the format of isValid:parsedLongIfTrue
     */
    public static Pair<Boolean, Long> getValidLong(final String entry) {
        final Pair<Boolean, Long> finalSet = new Pair<>();

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

    /**
     * Formats an IP Address based on Input
     *
     * @param input      The original String to evaluate
     * @param returnPort Whether to return the port or the IP without the Port
     * @return Either the IP or the port on their own, depending on conditions
     */
    public static String formatAddress(final String input, final boolean returnPort) {
        if (!isNullOrEmpty(input)) {
            final String[] formatted = input.split(":", 2);
            return !returnPort ? (elementExists(formatted, 0) ? formatted[0].trim() : "127.0.0.1") : (elementExists(formatted, 1) ? formatted[1].trim() : "25565");
        } else {
            return !returnPort ? "127.0.0.1" : "25565";
        }
    }

    /**
     * Converts a String into a Valid and Acceptable Icon Format
     *
     * @param original The original String to evaluate
     * @return The converted and valid String, in an iconKey Format
     */
    public static String formatAsIcon(final String original) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey)) {
            return formattedKey;
        } else {
            if (formattedKey.contains("\\s")) {
                formattedKey = formattedKey.replaceAll("\\s+", "");
            }
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
            return formattedKey.toLowerCase().trim();
        }
    }

    /**
     * Expands or Contracts an Array, depending on Conditions
     *
     * @param theArray The original Array to adjust
     * @param adjustBy The value to either expand (Positive Number) or contract (Negative Number)
     * @return The evaluated and adjusted array
     */
    public static String[] adjustArraySize(final String[] theArray, final int adjustBy) {
        int currentSize = theArray.length;
        int newSize = currentSize + adjustBy;

        String[] newArray = new String[newSize];
        System.arraycopy(theArray, 0, newArray, 0, theArray.length);
        return newArray;
    }

    /**
     * Adds the Specified message to the defined index in the target Array
     *
     * @param array   The original Array to evaluate
     * @param index   The index to add at
     * @param message The String Message to input at the index of the array
     * @return The evaluated array
     */
    public static String[] addToArray(final String[] array, final int index, final String message) {
        if (array.length <= index) {
            int extendNum = index - array.length;
            String[] newArray = adjustArraySize(array, extendNum + 1);
            newArray[index] = message;
            return newArray;
        } else {
            array[index] = message;
            return array;
        }
    }

    /**
     * Removes specified search term at specified index of an array
     *
     * @param originalArray  The original array
     * @param searchTerm     The search term to look for
     * @param searchIndex    The index to remove at
     * @param splitCharacter The delimiter to split parts of the array at (Optional)
     * @return The evaluated array
     */
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

    /**
     * Retrieves a config entry from an Array, following the specified Search Terms
     * <p><b>Internal Use Only</b>
     *
     * @param original       The original Array to interpret formatted as: firstArg[splitChar]secondArg[splitChar]thirdArgOptional
     * @param searchTerm     The search term to locate
     * @param searchIndex    The expected index to locate the search term at within an Array Element
     * @param resultIndex    The part of the found Array Element index to retrieve
     * @param splitCharacter The delimiter being expected to separate chunks of an Array Element
     * @param alternativeMSG The alternative value to return if no matches found in the target Array Element Index
     * @return The found or Alternative value from the search within the Array
     */
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

    /**
     * Sets new config Entry for Array Data Types with delimiter
     * <p><b>Internal Use Only</b>
     *
     * @param original       The original Array to interpret formatted as: firstArg[splitChar]secondArg[splitChar]thirdArgOptional
     * @param searchTerm     The search term to locate
     * @param searchIndex    The expected index to locate the search term at within an Array Element
     * @param resultIndex    The part of the found Array Element index to modify
     * @param splitCharacter The delimiter being expected to separate chunks of an Array Element
     * @param newMessage     The new value to insert into the target Array Element Index
     * @return The modified Array from the original
     */
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

    /**
     * Converts input into a Properly Readable String
     *
     * @param original The original String to format
     * @return The formatted and evaluated String
     */
    public static String formatWord(final String original) {
        return formatWord(original, false);
    }

    /**
     * Converts input into a Properly Readable String
     *
     * @param original The original String to format
     * @param avoid    Flag to ignore method if true
     * @return The formatted and evaluated String
     */
    public static String formatWord(final String original, final boolean avoid) {
        return formatWord(original, avoid, false);
    }

    /**
     * Converts input into a Properly Readable String
     *
     * @param original              The original String to format
     * @param avoid                 Flag to ignore method if true
     * @param skipSymbolReplacement Flag to Skip Symbol Replacement if true
     * @return The formatted and evaluated String
     */
    public static String formatWord(final String original, final boolean avoid, final boolean skipSymbolReplacement) {
        return formatWord(original, avoid, skipSymbolReplacement, -1);
    }

    /**
     * Converts input into a Properly Readable String
     *
     * @param original              The original String to format
     * @param avoid                 Flag to ignore method if true
     * @param skipSymbolReplacement Flag to Skip Symbol Replacement if true
     * @param caseCheckTimes        Times to replace Parts of the String during Capitalization (Use -1 for Infinite)
     * @return The formatted and evaluated String
     */
    public static String formatWord(final String original, final boolean avoid, final boolean skipSymbolReplacement, final int caseCheckTimes) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey) || avoid) {
            return formattedKey;
        } else {
            if (formattedKey.contains(" ")) {
                formattedKey = formattedKey.replaceAll("\\s+", " ");
            }

            if (!skipSymbolReplacement) {
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
            }

            return removeRepeatWords(capitalizeWord(formattedKey, caseCheckTimes)).trim();
        }
    }

    /**
     * Removes Duplicated Words within an inputted String
     *
     * @param original The original String
     * @return The evaluated String without duplicate words
     */
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

    /**
     * Converts an Identifier into a properly formatted and interpretable Name
     * <p>
     * Note: Additional Logic in Place for Older MC Versions
     *
     * @param originalId The Identifier to format
     * @param formatToId Whether to format as an Icon Key
     * @return The formatted name/icon key
     */
    public static String formatIdentifier(final String originalId, final boolean formatToId) {
        return formatIdentifier(originalId, formatToId, false);
    }

    /**
     * Converts an Identifier into a properly formatted and interpretable Name
     * <p>
     * Note: Additional Logic in Place for Older MC Versions
     *
     * @param originalId The Identifier to format
     * @param formatToId Whether to format as an Icon Key
     * @param avoid      Flag to ignore formatting identifier, if formatToId is false
     * @return The formatted name/icon key
     */
    public static String formatIdentifier(final String originalId, final boolean formatToId, final boolean avoid) {
        StringBuilder formattedKey = new StringBuilder(originalId);
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
                if (formatToId) {
                    return formatAsIcon(formattedKey.toString().replace(" ", "_"));
                } else {
                    return formatWord(formattedKey.toString(), avoid);
                }
            }
        }
    }

    /**
     * Wraps a String based on the specified target width per line<p>
     * Separated by newline characters, as needed
     *
     * @param stringInput The original String to wrap
     * @param wrapWidth   The target width per line, to wrap the input around
     * @return The converted and wrapped version of the original input
     */
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

    /**
     * Returns the Color and Formatting Characters within a String<p>
     * Defined by {@link StringUtils#STRIP_COLOR_PATTERN}
     *
     * @param text The original String to evaluate
     * @return The formatting and color codes found within the input
     */
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

    /**
     * Returns the combined rendering width of the String entry
     *
     * @param originalString The original String to evaluate
     * @return The expected rendering width for the input
     */
    public static int getStringWidth(final String originalString) {
        if (isNullOrEmpty(originalString)) {
            return 0;
        } else {
            int strLength = 0;
            boolean flag = false;

            for (int index = 0; index < originalString.length(); ++index) {
                char strChar = originalString.charAt(index);
                int charWidth = getCharWidth(strChar, ModUtils.TRANSLATOR.isUnicode);

                if (charWidth < 0 && index < originalString.length() - 1) {
                    ++index;
                    strChar = originalString.charAt(index);

                    if (strChar != 'l' && strChar != 'L') {
                        if (strChar == 'r' || strChar == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    charWidth = 0;
                }

                strLength += charWidth;

                if (flag && charWidth > 0) {
                    ++strLength;
                }
            }

            return strLength;
        }
    }

    /**
     * Returns the Render Character/Glyph Width of the specified character
     *
     * @param characterInput The character to evaluate
     * @param usingUnicode   Whether the specified character is a Unicode Character
     * @return The expected render character/glyph width for the input
     */
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

    /**
     * Returns the Wrapped Width of a String, defined by the target wrapWidth
     *
     * @param stringEntry The original String to evaluate
     * @param wrapWidth   The target width to wrap within
     * @return The expected wrapped width the String should be
     */
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

    /**
     * Capitalizes the words within a specified string
     *
     * @param str          The String to capitalize
     * @param timesToCheck The amount of times to replace within the String (Use -1 for Infinite)
     * @return The capitalized output string
     */
    public static String capitalizeWord(final String str, final int timesToCheck) {
        final StringBuilder s = new StringBuilder();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char charIndex = ' ';
        int timesLeft = timesToCheck;
        for (int index = 0; index < str.length(); index++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            // We only replace however, whilst the times
            // remaining is more then 0 or is -1 (Infinite)
            if (charIndex == ' ' && str.charAt(index) != ' ' && (timesLeft > 0 || timesLeft == -1)) {
                s.append(Character.toUpperCase(str.charAt(index)));
                if (timesLeft > 0) {
                    timesLeft--;
                }
            } else {
                s.append(str.charAt(index));
            }

            charIndex = str.charAt(index);
        }

        // Return the string with trimming
        return s.toString().trim();
    }

    /**
     * Capitalizes the words within a specified string
     *
     * @param str The String to capitalize
     * @return The capitalized output string
     */
    public static String capitalizeWord(String str) {
        return capitalizeWord(str, -1);
    }

    /**
     * Converts a String into a List of Strings, split up by new lines
     *
     * @param original The original String
     * @return The converted, newline-split list from the original String
     */
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

    /**
     * Display a Message to the Player, via the in-game Chat Hud
     *
     * @param sender  The Entity to Send to (Must be a Player)
     * @param message The Message to send and display in chat
     */
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

    /**
     * Determines if the Specified index exists in the List with a non-null value
     *
     * @param data  The Array of Strings to check within
     * @param index The index to check
     * @return {@code true} if the index element exists in the list with a non-null value
     */
    public static boolean elementExists(final String[] data, final int index) {
        return elementExists(Arrays.asList(data), index);
    }

    /**
     * Determines if the Specified index exists in the List with a non-null value
     *
     * @param data  The List of Strings to check within
     * @param index The index to check
     * @return {@code true} if the index element exists in the list with a non-null value
     */
    public static boolean elementExists(final List<String> data, final int index) {
        boolean result;
        try {
            result = data.size() >= index && !isNullOrEmpty(data.get(index));
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    /**
     * Retrieves the Specified Field(s) via Reflection
     *
     * @param classToAccess The class to access with the field(s)
     * @param instance      An Instance of the Class, if needed
     * @param fieldNames    A List of Field Names to search for
     * @return The Found Field Data, if any
     */
    public static Object lookupObject(Class<?> classToAccess, Object instance, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field lookupField = classToAccess.getDeclaredField(fieldName);
                lookupField.setAccessible(true);
                return lookupField.get(instance);
            } catch (Exception | Error ex) {
                if (ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Adjusts the Specified Field(s) in the Target Class via Reflection
     *
     * @param classToAccess The class to access with the field(s)
     * @param instance      An Instance of the Class, if needed
     * @param fieldData     A Pair with the format of fieldName:valueToSet:modifierData
     */
    public static void updateField(Class<?> classToAccess, Object instance, Tuple<?, ?, ?>... fieldData) {
        for (Tuple<?, ?, ?> currentData : fieldData) {
            try {
                Field lookupField = classToAccess.getDeclaredField(currentData.getFirst().toString());
                lookupField.setAccessible(true);

                if (currentData.getThird() != null) {
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(lookupField, lookupField.getModifiers() & Integer.parseInt(currentData.getThird().toString()));
                }

                lookupField.set(instance, currentData.getSecond());
                if (ModUtils.IS_VERBOSE) {
                    ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.update.dynamic", currentData.toString(), classToAccess.getName()));
                }
            } catch (Exception ex) {
                if (ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Invokes the specified Method(s) in the Target Class via Reflection
     *
     * @param classToAccess The class to access with the method(s)
     * @param instance      An Instance of the Class, if needed
     * @param methodData    The Methods and Necessary Argument Data for execution, in the form of methodName:argsAndTypesForMethod
     */
    public static void executeMethod(final Class<?> classToAccess, final Object instance, final List<Pair<String, Pair<Object[], Class<?>[]>>> methodData) {
        for (Pair<String, Pair<Object[], Class<?>[]>> methodInstance : methodData) {
            try {
                final Method lookupMethod = classToAccess.getDeclaredMethod(methodInstance.getFirst(), methodInstance.getSecond().getSecond());
                lookupMethod.setAccessible(true);
                lookupMethod.invoke(instance, methodInstance.getSecond().getFirst());
            } catch (Exception | Error ex) {
                if (ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Strips Color and Formatting Codes from the inputted String
     *
     * @param input The original String to evaluate
     * @return The Stripped and evaluated String
     */
    public static String stripColors(final String input) {
        return isNullOrEmpty(input) ? input : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
