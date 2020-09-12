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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.exceptions;

import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.DiscordBuild;

/**
 * An exception thrown when the {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient IPCClient}
 * cannot find the proper application to use for RichPresence when
 * attempting to {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient#connect(DiscordBuild...) connect}.
 * <p>
 * This purely and always means the IPCClient in question (specifically the client ID)
 * is <i>invalid</i> and features using this library cannot be accessed using the instance.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class NoDiscordClientException extends Exception {
    /**
     * The serialized unique version identifier
     */
    private static final long serialVersionUID = 1L;

    public NoDiscordClientException() {
        super("No Valid Discord Client was found for this Instance");
    }
}
