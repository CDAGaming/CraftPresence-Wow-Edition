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

/**
 * Constants representing various Discord client builds,
 * such as Stable, Canary, Public Test Build (PTB)
 */
public enum DiscordBuild {
    /**
     * Constant for the current Discord Canary release.
     */
    CANARY("//canary.discord.com/api"),

    /**
     * Constant for the current Discord Public Test Build or PTB release.
     */
    PTB("//ptb.discord.com/api"),

    /**
     * Constant for the current stable Discord release.
     */
    STABLE("//discord.com/api"),

    /**
     * 'Wildcard' build constant used in {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient#connect(DiscordBuild...)
     * IPCClient#connect(DiscordBuild...)} to signify that the build to target is not important, and
     * that the first valid build will be used.
     * <p>
     * Other than this exact function, there is no use for this value.
     */
    ANY;

    private final String endpoint;

    DiscordBuild(String endpoint) {
        this.endpoint = endpoint;
    }

    DiscordBuild() {
        this(null);
    }

    /**
     * Gets a {@link DiscordBuild} matching the specified endpoint.
     * <p>
     * This is only internally implemented.
     *
     * @param endpoint The endpoint to get from.
     * @return The DiscordBuild corresponding to the endpoint, or
     * {@link DiscordBuild#ANY} if none match.
     */
    public static DiscordBuild from(String endpoint) {
        for (DiscordBuild value : values()) {
            if (value.endpoint != null && value.endpoint.equals(endpoint)) {
                return value;
            }
        }
        return ANY;
    }
}
