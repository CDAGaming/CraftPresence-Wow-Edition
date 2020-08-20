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

package com.gitlab.cdagaming.craftpresence.impl;

import com.gitlab.cdagaming.craftpresence.ModUtils;

import java.util.HashMap;

/**
 * KeyCode Conversion Layer used to translate between other Keyboard Data Types
 *
 * @author CDAGaming, deftware
 */
public class KeyConverter {
    /**
     * Mapping from lwjgl2 -> lwjgl3
     * Note: Characters that are Unavailable in lwjgl3 are listed as lwjgl3's Unknown Keycode (-1)
     * Format: LWJGL2 Key;[LWJGL3 Key, Universal Key Name]
     */
    public static final HashMap<Integer, Tuple<Integer, String>> toGlfw = new HashMap<Integer, Tuple<Integer, String>>() {{
        put(0, new Tuple<>(-1, "None"));
        put(1, new Tuple<>(256, "Escape"));
        put(2, new Tuple<>(49, "1"));
        put(3, new Tuple<>(50, "2"));
        put(4, new Tuple<>(51, "3"));
        put(5, new Tuple<>(52, "4"));
        put(6, new Tuple<>(53, "5"));
        put(7, new Tuple<>(54, "6"));
        put(8, new Tuple<>(55, "7"));
        put(9, new Tuple<>(56, "8"));
        put(10, new Tuple<>(57, "9"));
        put(11, new Tuple<>(48, "0"));
        put(12, new Tuple<>(45, "Minus"));
        put(13, new Tuple<>(61, "Equals"));
        put(14, new Tuple<>(259, "Backspace"));
        put(15, new Tuple<>(258, "Tab"));
        put(16, new Tuple<>(81, "Q"));
        put(17, new Tuple<>(87, "W"));
        put(18, new Tuple<>(69, "E"));
        put(19, new Tuple<>(82, "R"));
        put(20, new Tuple<>(84, "T"));
        put(21, new Tuple<>(89, "Y"));
        put(22, new Tuple<>(85, "U"));
        put(23, new Tuple<>(73, "I"));
        put(24, new Tuple<>(79, "O"));
        put(25, new Tuple<>(80, "P"));
        put(26, new Tuple<>(91, "Left Bracket"));
        put(27, new Tuple<>(93, "Right Bracket"));
        put(28, new Tuple<>(257, "Return"));
        put(29, new Tuple<>(341, "Left Control"));
        put(30, new Tuple<>(65, "A"));
        put(31, new Tuple<>(83, "S"));
        put(32, new Tuple<>(68, "D"));
        put(33, new Tuple<>(70, "F"));
        put(34, new Tuple<>(71, "G"));
        put(35, new Tuple<>(72, "H"));
        put(36, new Tuple<>(74, "J"));
        put(37, new Tuple<>(75, "K"));
        put(38, new Tuple<>(76, "L"));
        put(39, new Tuple<>(59, "Semicolon"));
        put(40, new Tuple<>(39, "Apostrophe"));
        put(41, new Tuple<>(96, "Grave"));
        put(42, new Tuple<>(340, "Left Shift"));
        put(43, new Tuple<>(92, "Backslash"));
        put(44, new Tuple<>(90, "Z"));
        put(45, new Tuple<>(88, "X"));
        put(46, new Tuple<>(67, "C"));
        put(47, new Tuple<>(86, "V"));
        put(48, new Tuple<>(66, "B"));
        put(49, new Tuple<>(78, "N"));
        put(50, new Tuple<>(77, "M"));
        put(51, new Tuple<>(44, "Comma"));
        put(52, new Tuple<>(46, "Period"));
        put(53, new Tuple<>(47, "Slash"));
        put(54, new Tuple<>(344, "Right Shift"));
        put(55, new Tuple<>(332, "Keypad - Multiply"));
        put(56, new Tuple<>(342, "Left Alt"));
        put(57, new Tuple<>(32, "Space"));
        put(58, new Tuple<>(280, "Caps Lock"));
        put(59, new Tuple<>(290, "F1"));
        put(60, new Tuple<>(291, "F2"));
        put(61, new Tuple<>(292, "F3"));
        put(62, new Tuple<>(293, "F4"));
        put(63, new Tuple<>(294, "F5"));
        put(64, new Tuple<>(295, "F6"));
        put(65, new Tuple<>(296, "F7"));
        put(66, new Tuple<>(297, "F8"));
        put(67, new Tuple<>(298, "F9"));
        put(68, new Tuple<>(299, "F10"));
        put(69, new Tuple<>(282, "Number Lock"));
        put(70, new Tuple<>(281, "Scroll Lock"));
        put(71, new Tuple<>(327, "Keypad - 7"));
        put(72, new Tuple<>(328, "Keypad - 8"));
        put(73, new Tuple<>(329, "Keypad - 9"));
        put(74, new Tuple<>(333, "Keypad - Subtract"));
        put(75, new Tuple<>(324, "Keypad - 4"));
        put(76, new Tuple<>(325, "Keypad - 5"));
        put(77, new Tuple<>(326, "Keypad - 6"));
        put(78, new Tuple<>(334, "Keypad - Add"));
        put(79, new Tuple<>(321, "Keypad - 1"));
        put(80, new Tuple<>(322, "Keypad - 2"));
        put(81, new Tuple<>(323, "Keypad - 3"));
        put(82, new Tuple<>(320, "Keypad - 0"));
        put(83, new Tuple<>(330, "Keypad - Decimal"));
        put(87, new Tuple<>(300, "F11"));
        put(88, new Tuple<>(301, "F12"));
        put(100, new Tuple<>(302, "F13"));
        put(101, new Tuple<>(303, "F14"));
        put(102, new Tuple<>(304, "F15"));
        put(103, new Tuple<>(305, "F16"));
        put(104, new Tuple<>(306, "F17"));
        put(105, new Tuple<>(307, "F18"));
        put(112, new Tuple<>(-1, "Kana"));
        put(113, new Tuple<>(308, "F19"));
        put(121, new Tuple<>(-1, "Convert"));
        put(123, new Tuple<>(-1, "NoConvert"));
        put(125, new Tuple<>(-1, "Symbol - Yen"));
        put(141, new Tuple<>(336, "Keypad - Equals"));
        put(144, new Tuple<>(-1, "Symbol - Circumflex"));
        put(145, new Tuple<>(-1, "Symbol - At"));
        put(146, new Tuple<>(-1, "Symbol - Colon"));
        put(147, new Tuple<>(-1, "Underline"));
        put(148, new Tuple<>(-1, "Kanji"));
        put(149, new Tuple<>(-1, "Stop"));
        put(150, new Tuple<>(-1, "AX"));
        put(151, new Tuple<>(-1, "Unlabeled"));
        put(156, new Tuple<>(335, "Keypad - Enter"));
        put(157, new Tuple<>(345, "Right Control"));
        put(179, new Tuple<>(-1, "Keypad - Comma"));
        put(181, new Tuple<>(331, "Keypad - Divide"));
        put(183, new Tuple<>(-1, "SysRq"));
        put(184, new Tuple<>(346, "Right Alt"));
        put(196, new Tuple<>(-1, "Function"));
        put(197, new Tuple<>(284, "Pause"));
        put(199, new Tuple<>(268, "Home"));
        put(200, new Tuple<>(265, "Up Arrow"));
        put(201, new Tuple<>(266, "Page Up"));
        put(203, new Tuple<>(263, "Left Arrow"));
        put(205, new Tuple<>(262, "Right Arrow"));
        put(207, new Tuple<>(269, "End"));
        put(208, new Tuple<>(264, "Down Arrow"));
        put(209, new Tuple<>(267, "Page Down"));
        put(210, new Tuple<>(260, "Insert"));
        put(211, new Tuple<>(261, "Delete"));
        put(219, new Tuple<>(343, "Left Meta"));
        put(220, new Tuple<>(347, "Right Meta"));
        put(221, new Tuple<>(-1, "Apps"));
        put(222, new Tuple<>(-1, "Power"));
        put(223, new Tuple<>(-1, "Sleep"));
    }};

    /**
     * Mapping from lwjgl3 -> lwjgl2
     * Note: Characters that are Unavailable in lwjgl2 are listed as lwjgl2's Unknown Keycode (0)
     * Format: LWJGL3 Key;[LWJGL2 Key, Universal Key Name]
     */
    public static final HashMap<Integer, Tuple<Integer, String>> fromGlfw = new HashMap<Integer, Tuple<Integer, String>>() {{
        put(-1, new Tuple<>(0, "None"));
        put(32, new Tuple<>(57, "Space"));
        put(39, new Tuple<>(40, "Apostrophe"));
        put(44, new Tuple<>(51, "Comma"));
        put(45, new Tuple<>(12, "Minus"));
        put(46, new Tuple<>(52, "Period"));
        put(47, new Tuple<>(53, "Slash"));
        put(48, new Tuple<>(11, "0"));
        put(49, new Tuple<>(2, "1"));
        put(50, new Tuple<>(3, "2"));
        put(51, new Tuple<>(4, "3"));
        put(52, new Tuple<>(5, "4"));
        put(53, new Tuple<>(6, "5"));
        put(54, new Tuple<>(7, "6"));
        put(55, new Tuple<>(8, "7"));
        put(56, new Tuple<>(9, "8"));
        put(57, new Tuple<>(10, "9"));
        put(59, new Tuple<>(39, "Semicolon"));
        put(61, new Tuple<>(13, "Equals"));
        put(65, new Tuple<>(30, "A"));
        put(66, new Tuple<>(48, "B"));
        put(67, new Tuple<>(46, "C"));
        put(68, new Tuple<>(32, "D"));
        put(69, new Tuple<>(18, "E"));
        put(70, new Tuple<>(33, "F"));
        put(71, new Tuple<>(34, "G"));
        put(72, new Tuple<>(35, "H"));
        put(73, new Tuple<>(23, "I"));
        put(74, new Tuple<>(36, "J"));
        put(75, new Tuple<>(37, "K"));
        put(76, new Tuple<>(38, "L"));
        put(77, new Tuple<>(50, "M"));
        put(78, new Tuple<>(49, "N"));
        put(79, new Tuple<>(24, "O"));
        put(80, new Tuple<>(25, "P"));
        put(81, new Tuple<>(16, "Q"));
        put(82, new Tuple<>(19, "R"));
        put(83, new Tuple<>(31, "S"));
        put(84, new Tuple<>(20, "T"));
        put(85, new Tuple<>(22, "U"));
        put(86, new Tuple<>(47, "V"));
        put(87, new Tuple<>(17, "W"));
        put(88, new Tuple<>(45, "X"));
        put(89, new Tuple<>(21, "Y"));
        put(90, new Tuple<>(44, "Z"));
        put(91, new Tuple<>(26, "Left Bracket"));
        put(92, new Tuple<>(43, "Backslash"));
        put(93, new Tuple<>(27, "Right Bracket"));
        put(96, new Tuple<>(41, "Grave"));
        put(161, new Tuple<>(0, "WORLD_1"));
        put(162, new Tuple<>(0, "WORLD_2"));
        put(256, new Tuple<>(1, "Escape"));
        put(257, new Tuple<>(28, "Return / Enter"));
        put(258, new Tuple<>(15, "Tab"));
        put(259, new Tuple<>(14, "Backspace"));
        put(260, new Tuple<>(210, "Insert"));
        put(261, new Tuple<>(211, "Delete"));
        put(262, new Tuple<>(205, "Right Arrow"));
        put(263, new Tuple<>(203, "Left Arrow"));
        put(264, new Tuple<>(208, "Down Arrow"));
        put(265, new Tuple<>(200, "Up Arrow"));
        put(266, new Tuple<>(201, "Page Up"));
        put(267, new Tuple<>(209, "Page Down"));
        put(268, new Tuple<>(199, "Home"));
        put(269, new Tuple<>(207, "End"));
        put(280, new Tuple<>(58, "Caps Lock"));
        put(281, new Tuple<>(70, "Scroll Lock"));
        put(282, new Tuple<>(69, "Number Lock"));
        put(283, new Tuple<>(0, "Print Screen"));
        put(284, new Tuple<>(197, "Pause"));
        put(290, new Tuple<>(59, "F1"));
        put(291, new Tuple<>(60, "F2"));
        put(292, new Tuple<>(61, "F3"));
        put(293, new Tuple<>(62, "F4"));
        put(294, new Tuple<>(63, "F5"));
        put(295, new Tuple<>(64, "F6"));
        put(296, new Tuple<>(65, "F7"));
        put(297, new Tuple<>(66, "F8"));
        put(298, new Tuple<>(67, "F9"));
        put(299, new Tuple<>(68, "F10"));
        put(300, new Tuple<>(87, "F11"));
        put(301, new Tuple<>(88, "F12"));
        put(302, new Tuple<>(100, "F13"));
        put(303, new Tuple<>(101, "F14"));
        put(304, new Tuple<>(102, "F15"));
        put(305, new Tuple<>(103, "F16"));
        put(306, new Tuple<>(104, "F17"));
        put(307, new Tuple<>(105, "F18"));
        put(308, new Tuple<>(113, "F19"));
        put(309, new Tuple<>(0, "F20"));
        put(310, new Tuple<>(0, "F21"));
        put(311, new Tuple<>(0, "F22"));
        put(312, new Tuple<>(0, "F23"));
        put(313, new Tuple<>(0, "F24"));
        put(314, new Tuple<>(0, "F25"));
        put(320, new Tuple<>(82, "Keypad - 0"));
        put(321, new Tuple<>(79, "Keypad - 1"));
        put(322, new Tuple<>(80, "Keypad - 2"));
        put(323, new Tuple<>(81, "Keypad - 3"));
        put(324, new Tuple<>(75, "Keypad - 4"));
        put(325, new Tuple<>(76, "Keypad - 5"));
        put(326, new Tuple<>(77, "Keypad - 6"));
        put(327, new Tuple<>(71, "Keypad - 7"));
        put(328, new Tuple<>(72, "Keypad - 8"));
        put(329, new Tuple<>(73, "Keypad - 9"));
        put(330, new Tuple<>(83, "Keypad - Decimal"));
        put(331, new Tuple<>(181, "Keypad - Divide"));
        put(332, new Tuple<>(55, "Keypad - Multiply"));
        put(333, new Tuple<>(74, "Keypad - Subtract"));
        put(334, new Tuple<>(78, "Keypad - Add"));
        put(335, new Tuple<>(156, "Keypad - Enter"));
        put(336, new Tuple<>(141, "Keypad - Equals"));
        put(340, new Tuple<>(42, "Left Shift"));
        put(341, new Tuple<>(29, "Left Control"));
        put(342, new Tuple<>(56, "Left Alt"));
        put(343, new Tuple<>(219, "Left Meta"));
        put(344, new Tuple<>(54, "Right Shift"));
        put(345, new Tuple<>(157, "Right Control"));
        put(346, new Tuple<>(184, "Right Alt"));
        put(347, new Tuple<>(220, "Right Meta"));
        put(348, new Tuple<>(0, "KEY_MENU"));
    }};

    /**
     * Converts a KeyCode using the Specified Conversion Mode, if possible
     * <p>
     * Note: If None is Used on a Valid Value, this function can be used as verification, if any
     *
     * @param originalKey The original Key to Convert
     * @param mode        The Conversion Mode to convert the keycode to
     * @return The resulting converted KeyCode, or the mode's unknown key
     */
    public static int convertKey(final int originalKey, final ConversionMode mode) {
        final Tuple<Integer, String> unknownKeyData = mode == ConversionMode.Lwjgl2 ? fromGlfw.get(-1) : toGlfw.get(0);
        int resultKey = originalKey;

        if (fromGlfw.containsKey(originalKey) || toGlfw.containsKey(originalKey)) {
            if (mode == ConversionMode.Lwjgl2 || (mode == ConversionMode.None && fromGlfw.containsKey(originalKey) && toGlfw.containsValue(new Tuple<>(originalKey, fromGlfw.get(originalKey).getSecond())) && ModUtils.MCProtocolID <= 340)) {
                resultKey = fromGlfw.getOrDefault(originalKey, unknownKeyData).getFirst();
            } else if (mode == ConversionMode.Lwjgl3 || (mode == ConversionMode.None && toGlfw.containsKey(originalKey) && fromGlfw.containsValue(new Tuple<>(originalKey, toGlfw.get(originalKey).getSecond())) && ModUtils.MCProtocolID > 340)) {
                resultKey = toGlfw.getOrDefault(originalKey, unknownKeyData).getFirst();
            }
        }

        if (resultKey == unknownKeyData.getFirst() || (resultKey == originalKey && mode != ConversionMode.None)) {
            ModUtils.LOG.debugWarn(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.convert.invalid", Integer.toString(resultKey), mode.name()));
        }

        return resultKey;
    }

    /**
     * A Mapping storing the possible Conversion Modes for this module
     */
    public enum ConversionMode {
        Lwjgl2, Lwjgl3, None, Unknown
    }

}
