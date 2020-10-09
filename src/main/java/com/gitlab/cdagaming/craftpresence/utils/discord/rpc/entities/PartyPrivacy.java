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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

/**
 * Constants representing various Discord client party privacy levels,
 * such as Public or Private
 */
public enum PartyPrivacy {
    /**
     * Constant for the "Private" Discord RPC Party privacy level.
     */
    Private(0),

    /**
     * Constant for the "Public" Discord RPC Party privacy level.
     */
    Public(1);

    private final String displayName;

    private final int partyIndex;

    PartyPrivacy(final int partyIndex) {
        this.partyIndex = partyIndex;
        this.displayName = StringUtils.formatWord(name());
    }

    PartyPrivacy(final int partyIndex, final String displayName) {
        this.partyIndex = partyIndex;
        this.displayName = displayName;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param partyIndex The party index to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(int partyIndex) {
        for (PartyPrivacy value : values()) {
            if (value.getPartyIndex() == partyIndex) {
                return value;
            }
        }
        return Public;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param displayName The display name to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(String displayName) {
        for (PartyPrivacy value : values()) {
            if (value.getDisplayName() != null && value.getDisplayName().equals(displayName)) {
                return value;
            }
        }
        return Public;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param displayName The display name to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(int partyIndex, String displayName) {
        for (PartyPrivacy value : values()) {
            if (value.getDisplayName() != null && value.getDisplayName().equals(displayName) && value.getPartyIndex() == partyIndex) {
                return value;
            }
        }
        return Public;
    }

    public int getPartyIndex() {
        return partyIndex;
    }

    public String getDisplayName() {
        return displayName;
    }
}
