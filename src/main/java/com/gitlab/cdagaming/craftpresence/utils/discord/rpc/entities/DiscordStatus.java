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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

/**
 * Constants representing various Discord client statuses,
 * such as Ready, Errored, Disconnected, and event trigger names
 */
public enum DiscordStatus {
    /**
     * Constant for the "disconnected" Discord Status.
     */
    Disconnected,

    /**
     * Constant for the "ready" Discord Status.
     */
    Ready,

    /**
     * Constant for the "joinGame" Discord Status
     * <p>Triggers when accepting and queuing a Join Request
     */
    JoinGame("Join Game"),

    /**
     * Constant for the "joinRequest" Discord Status
     * <p>Triggers when receiving a Join Request
     */
    JoinRequest("Join Request"),

    /**
     * Constant for the "spectateGame" Discord Status
     * <p>Triggers when queuing to spectate a game
     */
    SpectateGame("Spectate Game"),

    /**
     * 'Wildcard' build constant used to specify an errored or invalid status
     */
    Invalid;

    private final String displayName;

    DiscordStatus() {
        displayName = StringUtils.formatWord(name());
    }

    DiscordStatus(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets a {@link DiscordStatus} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param displayName The display name to get from.
     * @return The DiscordStatus corresponding to the display name, or
     * {@link DiscordStatus#Invalid} if none match.
     */
    public static DiscordStatus from(String displayName) {
        for (DiscordStatus value : values()) {
            if (value.getDisplayName() != null && value.getDisplayName().equals(displayName)) {
                return value;
            }
        }
        return Invalid;
    }

    public String getDisplayName() {
        return displayName;
    }
}
