/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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
    public static final HashMap<Integer, Pair<Integer, String>> toGlfw = new HashMap<Integer, Pair<Integer, String>>() {
        /**
         * The serialized unique version identifier
         */
        private static final long serialVersionUID = 1L;

        {
            put(0, new Pair<>(-1, "None"));
            put(1, new Pair<>(256, "Escape"));
            put(2, new Pair<>(49, "1"));
            put(3, new Pair<>(50, "2"));
            put(4, new Pair<>(51, "3"));
            put(5, new Pair<>(52, "4"));
            put(6, new Pair<>(53, "5"));
            put(7, new Pair<>(54, "6"));
            put(8, new Pair<>(55, "7"));
            put(9, new Pair<>(56, "8"));
            put(10, new Pair<>(57, "9"));
            put(11, new Pair<>(48, "0"));
            put(12, new Pair<>(45, "Minus"));
            put(13, new Pair<>(61, "Equals"));
            put(14, new Pair<>(259, "Backspace"));
            put(15, new Pair<>(258, "Tab"));
            put(16, new Pair<>(81, "Q"));
            put(17, new Pair<>(87, "W"));
            put(18, new Pair<>(69, "E"));
            put(19, new Pair<>(82, "R"));
            put(20, new Pair<>(84, "T"));
            put(21, new Pair<>(89, "Y"));
            put(22, new Pair<>(85, "U"));
            put(23, new Pair<>(73, "I"));
            put(24, new Pair<>(79, "O"));
            put(25, new Pair<>(80, "P"));
            put(26, new Pair<>(91, "Left Bracket"));
            put(27, new Pair<>(93, "Right Bracket"));
            put(28, new Pair<>(257, "Return"));
            put(29, new Pair<>(341, "Left Control"));
            put(30, new Pair<>(65, "A"));
            put(31, new Pair<>(83, "S"));
            put(32, new Pair<>(68, "D"));
            put(33, new Pair<>(70, "F"));
            put(34, new Pair<>(71, "G"));
            put(35, new Pair<>(72, "H"));
            put(36, new Pair<>(74, "J"));
            put(37, new Pair<>(75, "K"));
            put(38, new Pair<>(76, "L"));
            put(39, new Pair<>(59, "Semicolon"));
            put(40, new Pair<>(39, "Apostrophe"));
            put(41, new Pair<>(96, "Grave"));
            put(42, new Pair<>(340, "Left Shift"));
            put(43, new Pair<>(92, "Backslash"));
            put(44, new Pair<>(90, "Z"));
            put(45, new Pair<>(88, "X"));
            put(46, new Pair<>(67, "C"));
            put(47, new Pair<>(86, "V"));
            put(48, new Pair<>(66, "B"));
            put(49, new Pair<>(78, "N"));
            put(50, new Pair<>(77, "M"));
            put(51, new Pair<>(44, "Comma"));
            put(52, new Pair<>(46, "Period"));
            put(53, new Pair<>(47, "Slash"));
            put(54, new Pair<>(344, "Right Shift"));
            put(55, new Pair<>(332, "Keypad - Multiply"));
            put(56, new Pair<>(342, "Left Alt"));
            put(57, new Pair<>(32, "Space"));
            put(58, new Pair<>(280, "Caps Lock"));
            put(59, new Pair<>(290, "F1"));
            put(60, new Pair<>(291, "F2"));
            put(61, new Pair<>(292, "F3"));
            put(62, new Pair<>(293, "F4"));
            put(63, new Pair<>(294, "F5"));
            put(64, new Pair<>(295, "F6"));
            put(65, new Pair<>(296, "F7"));
            put(66, new Pair<>(297, "F8"));
            put(67, new Pair<>(298, "F9"));
            put(68, new Pair<>(299, "F10"));
            put(69, new Pair<>(282, "Number Lock"));
            put(70, new Pair<>(281, "Scroll Lock"));
            put(71, new Pair<>(327, "Keypad - 7"));
            put(72, new Pair<>(328, "Keypad - 8"));
            put(73, new Pair<>(329, "Keypad - 9"));
            put(74, new Pair<>(333, "Keypad - Subtract"));
            put(75, new Pair<>(324, "Keypad - 4"));
            put(76, new Pair<>(325, "Keypad - 5"));
            put(77, new Pair<>(326, "Keypad - 6"));
            put(78, new Pair<>(334, "Keypad - Add"));
            put(79, new Pair<>(321, "Keypad - 1"));
            put(80, new Pair<>(322, "Keypad - 2"));
            put(81, new Pair<>(323, "Keypad - 3"));
            put(82, new Pair<>(320, "Keypad - 0"));
            put(83, new Pair<>(330, "Keypad - Decimal"));
            put(87, new Pair<>(300, "F11"));
            put(88, new Pair<>(301, "F12"));
            put(100, new Pair<>(302, "F13"));
            put(101, new Pair<>(303, "F14"));
            put(102, new Pair<>(304, "F15"));
            put(103, new Pair<>(305, "F16"));
            put(104, new Pair<>(306, "F17"));
            put(105, new Pair<>(307, "F18"));
            put(112, new Pair<>(-1, "Kana"));
            put(113, new Pair<>(308, "F19"));
            put(121, new Pair<>(-1, "Convert"));
            put(123, new Pair<>(-1, "NoConvert"));
            put(125, new Pair<>(-1, "Symbol - Yen"));
            put(141, new Pair<>(336, "Keypad - Equals"));
            put(144, new Pair<>(-1, "Symbol - Circumflex"));
            put(145, new Pair<>(-1, "Symbol - At"));
            put(146, new Pair<>(-1, "Symbol - Colon"));
            put(147, new Pair<>(-1, "Underline"));
            put(148, new Pair<>(-1, "Kanji"));
            put(149, new Pair<>(-1, "Stop"));
            put(150, new Pair<>(-1, "AX"));
            put(151, new Pair<>(-1, "Unlabeled"));
            put(156, new Pair<>(335, "Keypad - Enter"));
            put(157, new Pair<>(345, "Right Control"));
            put(179, new Pair<>(-1, "Keypad - Comma"));
            put(181, new Pair<>(331, "Keypad - Divide"));
            put(183, new Pair<>(-1, "SysRq"));
            put(184, new Pair<>(346, "Right Alt"));
            put(196, new Pair<>(-1, "Function"));
            put(197, new Pair<>(284, "Pause"));
            put(199, new Pair<>(268, "Home"));
            put(200, new Pair<>(265, "Up Arrow"));
            put(201, new Pair<>(266, "Page Up"));
            put(203, new Pair<>(263, "Left Arrow"));
            put(205, new Pair<>(262, "Right Arrow"));
            put(207, new Pair<>(269, "End"));
            put(208, new Pair<>(264, "Down Arrow"));
            put(209, new Pair<>(267, "Page Down"));
            put(210, new Pair<>(260, "Insert"));
            put(211, new Pair<>(261, "Delete"));
            put(219, new Pair<>(343, "Left Meta"));
            put(220, new Pair<>(347, "Right Meta"));
            put(221, new Pair<>(-1, "Apps"));
            put(222, new Pair<>(-1, "Power"));
            put(223, new Pair<>(-1, "Sleep"));
        }
    };

    /**
     * Mapping from lwjgl3 -> lwjgl2
     * Note: Characters that are Unavailable in lwjgl2 are listed as lwjgl2's Unknown Keycode (0)
     * Format: LWJGL3 Key;[LWJGL2 Key, Universal Key Name]
     */
    public static final HashMap<Integer, Pair<Integer, String>> fromGlfw = new HashMap<Integer, Pair<Integer, String>>() {
        /**
         * The serialized unique version identifier
         */
        private static final long serialVersionUID = 1L;

        {
            put(-1, new Pair<>(0, "None"));
            put(32, new Pair<>(57, "Space"));
            put(39, new Pair<>(40, "Apostrophe"));
            put(44, new Pair<>(51, "Comma"));
            put(45, new Pair<>(12, "Minus"));
            put(46, new Pair<>(52, "Period"));
            put(47, new Pair<>(53, "Slash"));
            put(48, new Pair<>(11, "0"));
            put(49, new Pair<>(2, "1"));
            put(50, new Pair<>(3, "2"));
            put(51, new Pair<>(4, "3"));
            put(52, new Pair<>(5, "4"));
            put(53, new Pair<>(6, "5"));
            put(54, new Pair<>(7, "6"));
            put(55, new Pair<>(8, "7"));
            put(56, new Pair<>(9, "8"));
            put(57, new Pair<>(10, "9"));
            put(59, new Pair<>(39, "Semicolon"));
            put(61, new Pair<>(13, "Equals"));
            put(65, new Pair<>(30, "A"));
            put(66, new Pair<>(48, "B"));
            put(67, new Pair<>(46, "C"));
            put(68, new Pair<>(32, "D"));
            put(69, new Pair<>(18, "E"));
            put(70, new Pair<>(33, "F"));
            put(71, new Pair<>(34, "G"));
            put(72, new Pair<>(35, "H"));
            put(73, new Pair<>(23, "I"));
            put(74, new Pair<>(36, "J"));
            put(75, new Pair<>(37, "K"));
            put(76, new Pair<>(38, "L"));
            put(77, new Pair<>(50, "M"));
            put(78, new Pair<>(49, "N"));
            put(79, new Pair<>(24, "O"));
            put(80, new Pair<>(25, "P"));
            put(81, new Pair<>(16, "Q"));
            put(82, new Pair<>(19, "R"));
            put(83, new Pair<>(31, "S"));
            put(84, new Pair<>(20, "T"));
            put(85, new Pair<>(22, "U"));
            put(86, new Pair<>(47, "V"));
            put(87, new Pair<>(17, "W"));
            put(88, new Pair<>(45, "X"));
            put(89, new Pair<>(21, "Y"));
            put(90, new Pair<>(44, "Z"));
            put(91, new Pair<>(26, "Left Bracket"));
            put(92, new Pair<>(43, "Backslash"));
            put(93, new Pair<>(27, "Right Bracket"));
            put(96, new Pair<>(41, "Grave"));
            put(161, new Pair<>(0, "WORLD_1"));
            put(162, new Pair<>(0, "WORLD_2"));
            put(256, new Pair<>(1, "Escape"));
            put(257, new Pair<>(28, "Return / Enter"));
            put(258, new Pair<>(15, "Tab"));
            put(259, new Pair<>(14, "Backspace"));
            put(260, new Pair<>(210, "Insert"));
            put(261, new Pair<>(211, "Delete"));
            put(262, new Pair<>(205, "Right Arrow"));
            put(263, new Pair<>(203, "Left Arrow"));
            put(264, new Pair<>(208, "Down Arrow"));
            put(265, new Pair<>(200, "Up Arrow"));
            put(266, new Pair<>(201, "Page Up"));
            put(267, new Pair<>(209, "Page Down"));
            put(268, new Pair<>(199, "Home"));
            put(269, new Pair<>(207, "End"));
            put(280, new Pair<>(58, "Caps Lock"));
            put(281, new Pair<>(70, "Scroll Lock"));
            put(282, new Pair<>(69, "Number Lock"));
            put(283, new Pair<>(0, "Print Screen"));
            put(284, new Pair<>(197, "Pause"));
            put(290, new Pair<>(59, "F1"));
            put(291, new Pair<>(60, "F2"));
            put(292, new Pair<>(61, "F3"));
            put(293, new Pair<>(62, "F4"));
            put(294, new Pair<>(63, "F5"));
            put(295, new Pair<>(64, "F6"));
            put(296, new Pair<>(65, "F7"));
            put(297, new Pair<>(66, "F8"));
            put(298, new Pair<>(67, "F9"));
            put(299, new Pair<>(68, "F10"));
            put(300, new Pair<>(87, "F11"));
            put(301, new Pair<>(88, "F12"));
            put(302, new Pair<>(100, "F13"));
            put(303, new Pair<>(101, "F14"));
            put(304, new Pair<>(102, "F15"));
            put(305, new Pair<>(103, "F16"));
            put(306, new Pair<>(104, "F17"));
            put(307, new Pair<>(105, "F18"));
            put(308, new Pair<>(113, "F19"));
            put(309, new Pair<>(0, "F20"));
            put(310, new Pair<>(0, "F21"));
            put(311, new Pair<>(0, "F22"));
            put(312, new Pair<>(0, "F23"));
            put(313, new Pair<>(0, "F24"));
            put(314, new Pair<>(0, "F25"));
            put(320, new Pair<>(82, "Keypad - 0"));
            put(321, new Pair<>(79, "Keypad - 1"));
            put(322, new Pair<>(80, "Keypad - 2"));
            put(323, new Pair<>(81, "Keypad - 3"));
            put(324, new Pair<>(75, "Keypad - 4"));
            put(325, new Pair<>(76, "Keypad - 5"));
            put(326, new Pair<>(77, "Keypad - 6"));
            put(327, new Pair<>(71, "Keypad - 7"));
            put(328, new Pair<>(72, "Keypad - 8"));
            put(329, new Pair<>(73, "Keypad - 9"));
            put(330, new Pair<>(83, "Keypad - Decimal"));
            put(331, new Pair<>(181, "Keypad - Divide"));
            put(332, new Pair<>(55, "Keypad - Multiply"));
            put(333, new Pair<>(74, "Keypad - Subtract"));
            put(334, new Pair<>(78, "Keypad - Add"));
            put(335, new Pair<>(156, "Keypad - Enter"));
            put(336, new Pair<>(141, "Keypad - Equals"));
            put(340, new Pair<>(42, "Left Shift"));
            put(341, new Pair<>(29, "Left Control"));
            put(342, new Pair<>(56, "Left Alt"));
            put(343, new Pair<>(219, "Left Meta"));
            put(344, new Pair<>(54, "Right Shift"));
            put(345, new Pair<>(157, "Right Control"));
            put(346, new Pair<>(184, "Right Alt"));
            put(347, new Pair<>(220, "Right Meta"));
            put(348, new Pair<>(0, "KEY_MENU"));
        }
    };

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
        final Pair<Integer, String> unknownKeyData = mode == ConversionMode.Lwjgl2 ? fromGlfw.get(-1) : toGlfw.get(0);
        int resultKey = originalKey;

        if (fromGlfw.containsKey(originalKey) || toGlfw.containsKey(originalKey)) {
            if (mode == ConversionMode.Lwjgl2 || (mode == ConversionMode.None && fromGlfw.containsKey(originalKey) && toGlfw.containsValue(new Pair<>(originalKey, fromGlfw.get(originalKey).getSecond())) && ModUtils.MCProtocolID <= 340)) {
                resultKey = fromGlfw.getOrDefault(originalKey, unknownKeyData).getFirst();
            } else if (mode == ConversionMode.Lwjgl3 || (mode == ConversionMode.None && toGlfw.containsKey(originalKey) && fromGlfw.containsValue(new Pair<>(originalKey, toGlfw.get(originalKey).getSecond())) && ModUtils.MCProtocolID > 340)) {
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
