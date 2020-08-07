package org.lwjgl.glfw;

import com.gitlab.cdagaming.craftpresence.ModUtils;

import java.util.HashMap;

/**
 * KeyCode Conversion Layer, Used to translate between lwjgl2 and lwjgl3
 *
 * @author CDAGaming, deftware
 */
public class GLFW {
    /**
     * Mapping from lwjgl2 -> lwjgl3
     * Note: Characters that are Unavailable in lwjgl3 are listed as lwjgl3's UNKNOWN Keycode
     */
    private static final HashMap<Integer, Integer> toGLFW = new HashMap<Integer, Integer>() {{
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
        put(181, 331); // Divide
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

    private static final HashMap<Integer, Integer> fromGLFW = new HashMap<Integer, Integer>() {{
        // TODO
    }};

    /**
     * Converts a KeyCode using the Specified Conversion Mode, if possible
     *
     * @param originalKey The original Key to Convert
     * @param mode The Conversion Mode to convert the keycode to
     * @return The resulting converted KeyCode
     */
    public static int convertKey(final int originalKey, final ConversionMode mode) {
        final int unknownKey = mode == ConversionMode.LWJGL2 ? 0 : -1;
        int resultKey = unknownKey;

        if (mode == ConversionMode.LWJGL2) {
            resultKey = fromGLFW.getOrDefault(originalKey, originalKey);
        } else if (mode == ConversionMode.LWJGL3) {
            resultKey = toGLFW.getOrDefault(originalKey, originalKey);
        }

        if (resultKey == unknownKey) {
            ModUtils.LOG.debugWarn(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.keybind.convert.invalid", Integer.toString(resultKey), mode.name()));
        }

        return resultKey;
    }

    /**
     * A Mapping storing the possible Conversion Modes
     */
    public enum ConversionMode {
        LWJGL2, LWJGL3, UNKNOWN
    }

}
