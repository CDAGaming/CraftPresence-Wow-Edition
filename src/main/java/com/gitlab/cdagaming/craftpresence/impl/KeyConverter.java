package com.gitlab.cdagaming.craftpresence.impl;

import com.gitlab.cdagaming.craftpresence.ModUtils;

import java.util.HashMap;

/**
 * KeyCode Conversion Layer, Used to translate between other Keyboard Data Types
 *
 * @author CDAGaming, deftware
 */
public class KeyConverter {
    /**
     * Mapping from lwjgl2 -> lwjgl3
     * Note: Characters that are Unavailable in lwjgl3 are listed as lwjgl3's Unknown Keycode (-1)
     */
    private static final HashMap<Integer, Integer> toGlfw = new HashMap<Integer, Integer>() {{
        put(0, -1); // UNKNOWN / NONE
        put(1, 256); // Escape
        put(2, 49); // 1
        put(3, 50); // 2
        put(4, 51); // 3
        put(5, 52); // 4
        put(6, 53); // 5
        put(7, 54); // 6
        put(8, 55); // 7
        put(9, 56); // 8
        put(10, 57); // 9
        put(11, 48); // 0
        put(12, 45); // Minus
        put(13, 61); // Equals
        put(14, 259); // Backspace
        put(15, 258); // Tab
        put(16, 81); // Q
        put(17, 87); // W
        put(18, 69); // E
        put(19, 82); // R
        put(20, 84); // T
        put(21, 89); // Y
        put(22, 85); // U
        put(23, 73); // I
        put(24, 79); // O
        put(25, 80); // P
        put(26, 91); // Left Bracket
        put(27, 93); // Right Bracket
        put(28, 257); // Return / Enter
        put(29, 341); // Left Control
        put(30, 65); // A
        put(31, 83); // S
        put(32, 68); // D
        put(33, 70); // F
        put(34, 71); // G
        put(35, 72); // H
        put(36, 74); // J
        put(37, 75); // K
        put(38, 76); // L
        put(39, 59); // Semicolon
        put(40, 39); // Apostrophe
        put(41, 96); // Grave / Tilde
        put(42, 340); // Left Shift
        put(43, 92); // Backslash
        put(44, 90); // Z
        put(45, 88); // X
        put(46, 67); // C
        put(47, 86); // V
        put(48, 66); // B
        put(49, 78); // N
        put(50, 77); // M
        put(51, 44); // Comma
        put(52, 46); // Period
        put(53, 47); // Slash
        put(54, 344); // Right Shift
        put(55, 332); // Keypad - Multiply
        put(56, 342); // Left Alt
        put(57, 32); // Space
        put(58, 280); // Caps Lock
        put(59, 290); // F1
        put(60, 291); // F2
        put(61, 292); // F3
        put(62, 293); // F4
        put(63, 294); // F5
        put(64, 295); // F6
        put(65, 296); // F7
        put(66, 297); // F8
        put(67, 298); // F9
        put(68, 299); // F10
        put(69, 282); // Number Lock
        put(70, 281); // Scroll Lock
        put(71, 327); // Keypad - 7
        put(72, 328); // Keypad - 8
        put(73, 329); // Keypad - 9
        put(74, 333); // Keypad - Subtract
        put(75, 324); // Keypad - 4
        put(76, 325); // Keypad - 5
        put(77, 326); // Keypad - 6
        put(78, 334); // Keypad - Add
        put(79, 321); // Keypad - 1
        put(80, 322); // Keypad - 2
        put(81, 323); // Keypad - 3
        put(82, 320); // Keypad - 0
        put(83, 330); // Keypad - Decimal
        put(87, 300); // F11
        put(88, 301); // F12
        put(100, 302); // F13
        put(101, 303); // F14
        put(102, 304); // F15
        put(103, 305); // F16
        put(104, 306); // F17
        put(105, 307); // F18
        put(112, -1); // KANA
        put(113, 308); // F19
        put(121, -1); // CONVERT
        put(123, -1); // NOCONVERT
        put(125, -1); // Symbol - Yen
        put(141, 336); // Keypad - Equals
        put(144, -1); // Symbol - Circumflex
        put(145, -1); // Symbol - At
        put(146, -1); // Symbol - Colon
        put(147, -1); // Underline
        put(148, -1); // KANJI
        put(149, -1); // STOP
        put(150, -1); // AX
        put(151, -1); // UNLABELED
        put(156, 335); // Keypad - Enter
        put(157, 345); // Right Control
        put(179, -1); // Keypad - Comma
        put(181, 331); // Keypad - Divide
        put(183, -1); // SYSRQ
        put(184, 346); // Right Alt
        put(196, -1); // Function
        put(197, 284); // Pause
        put(199, 268); // Home
        put(200, 265); // Up Arrow
        put(201, 266); // Page Up
        put(203, 263); // Left Arrow
        put(205, 262); // Right Arrow
        put(207, 269); // End
        put(208, 264); // Down Arrow
        put(209, 267); // Page Down
        put(210, 260); // Insert
        put(211, 261); // Delete
        put(219, 343); // Left Meta/Super
        put(220, 347); // Right Meta/Super
        put(221, -1); // APPS
        put(222, -1); // POWER
        put(223, -1); // SLEEP
    }};

    /**
     * Mapping from lwjgl3 -> lwjgl2
     * Note: Characters that are Unavailable in lwjgl2 are listed as lwjgl2's Unknown Keycode (0)
     */
    private static final HashMap<Integer, Integer> fromGlfw = new HashMap<Integer, Integer>() {{
        put(-1, 0); // UNKNOWN / NONE
        put(32, 57); // Space
        put(39, 40); // Apostrophe
        put(44, 51); // Comma
        put(45, 12); // Minus
        put(46, 52); // Period
        put(47, 53); // Slash
        put(48, 11); // 0
        put(49, 2); // 1
        put(50, 3); // 2
        put(51, 4); // 3
        put(52, 5); // 4
        put(53, 6); // 5
        put(54, 7); // 6
        put(55, 8); // 7
        put(56, 9); // 8
        put(57, 10); // 9
        put(59, 39); // Semicolon
        put(61, 13); // Equals
        put(65, 30); // A
        put(66, 48); // B
        put(67, 46); // C
        put(68, 32); // D
        put(69, 18); // E
        put(70, 33); // F
        put(71, 34); // G
        put(72, 35); // H
        put(73, 23); // I
        put(74, 36); // J
        put(75, 37); // K
        put(76, 38); // L
        put(77, 50); // M
        put(78, 49); // N
        put(79, 24); // O
        put(80, 25); // P
        put(81, 16); // Q
        put(82, 19); // R
        put(83, 31); // S
        put(84, 20); // T
        put(85, 22); // U
        put(86, 47); // V
        put(87, 17); // W
        put(88, 45); // X
        put(89, 21); // Y
        put(90, 44); // Z
        put(91, 26); // Left Bracket
        put(92, 43); // Backslash
        put(93, 27); // Right Bracket
        put(96, 41); // Grave / Tilde
        put(161, 0); // WORLD_1
        put(162, 0); // WORLD_2
        put(256, 1); // Escape
        put(257, 28); // Return / Enter
        put(258, 15); // Tab
        put(259, 14); // Backspace
        put(260, 210); // Insert
        put(261, 211); // Delete
        put(262, 205); // Right Arrow
        put(263, 203); // Left Arrow
        put(264, 208); // Down Arrow
        put(265, 200); // Up Arrow
        put(266, 201); // Page Up
        put(267, 209); // Page Down
        put(268, 199); // Home
        put(269, 207); // End
        put(280, 58); // Caps Lock
        put(281, 70); // Scroll Lock
        put(282, 69); // Number Lock
        put(283, 0); // Print Screen
        put(284, 197); // Pause
        put(290, 59); // F1
        put(291, 60); // F2
        put(292, 61); // F3
        put(293, 62); // F4
        put(294, 63); // F5
        put(295, 64); // F6
        put(296, 65); // F7
        put(297, 66); // F8
        put(298, 67); // F9
        put(299, 68); // F10
        put(300, 87); // F11
        put(301, 88); // F12
        put(302, 100); // F13
        put(303, 101); // F14
        put(304, 102); // F15
        put(305, 103); // F16
        put(306, 104); // F17
        put(307, 105); // F18
        put(308, 113); // F19
        put(309, 0); // F20
        put(310, 0); // F21
        put(311, 0); // F22
        put(312, 0); // F23
        put(313, 0); // F24
        put(314, 0); // F25
        put(320, 82); // Keypad - 0
        put(321, 79); // Keypad - 1
        put(322, 80); // Keypad - 2
        put(323, 81); // Keypad - 3
        put(324, 75); // Keypad - 4
        put(325, 76); // Keypad - 5
        put(326, 77); // Keypad - 6
        put(327, 71); // Keypad - 7
        put(328, 72); // Keypad - 8
        put(329, 73); // Keypad - 9
        put(330, 83); // Keypad - Decimal
        put(331, 181); // Keypad - Divide
        put(332, 55); // Keypad - Multiply
        put(333, 74); // Keypad - Subtract
        put(334, 78); // Keypad - Add
        put(335, 156); // Keypad - Enter
        put(336, 141); // Keypad - Equals
        put(340, 42); // Left Shift
        put(341, 29); // Left Control
        put(342, 56); // Left Alt
        put(343, 219); // Left Meta/Super
        put(344, 54); // Right Shift
        put(345, 157); // Right Control
        put(346, 184); // Right Alt
        put(347, 220); // Right Meta/Super
        put(348, 0); // KEY_MENU
    }};

    /**
     * Converts a KeyCode using the Specified Conversion Mode, if possible
     * <p>
     * Note: If None is Used on a Valid Value, this function can be used as verification, if any
     *
     * @param originalKey The original Key to Convert
     * @param mode The Conversion Mode to convert the keycode to
     * @return The resulting converted KeyCode, or the mode's unknown key
     */
    public static int convertKey(final int originalKey, final ConversionMode mode) {
        final int unknownKey = mode == ConversionMode.Lwjgl2 ? 0 : -1;
        int resultKey = originalKey;

        if (fromGlfw.containsKey(originalKey) || toGlfw.containsKey(originalKey)) {
            if (mode == ConversionMode.Lwjgl2 || (mode == ConversionMode.None && fromGlfw.containsKey(originalKey) && toGlfw.containsValue(originalKey) && ModUtils.MCProtocolID <= 340)) {
                resultKey = fromGlfw.getOrDefault(originalKey, unknownKey);
            } else if (mode == ConversionMode.Lwjgl3 || (mode == ConversionMode.None && toGlfw.containsKey(originalKey) && fromGlfw.containsValue(originalKey) && ModUtils.MCProtocolID > 340)) {
                resultKey = toGlfw.getOrDefault(originalKey, unknownKey);
            }
        }

        if (resultKey == unknownKey || (resultKey == originalKey && mode != ConversionMode.None)) {
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
