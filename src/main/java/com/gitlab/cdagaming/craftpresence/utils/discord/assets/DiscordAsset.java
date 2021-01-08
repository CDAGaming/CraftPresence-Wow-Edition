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

package com.gitlab.cdagaming.craftpresence.utils.discord.assets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Json Parsing Syntax for a Discord Asset
 *
 * @author CDAGaming
 */
public class DiscordAsset {
    /**
     * The {@link AssetType} of this Asset
     */
    @SerializedName("type")
    @Expose
    private AssetType type;

    /**
     * The Parsed ID for this Asset
     */
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * The Parsed Name for this Asset
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Retrieves the {@link AssetType} for this Asset
     *
     * @return The parsed {@link AssetType} for this Asset
     */
    public AssetType getType() {
        return type;
    }

    /**
     * Retrieves the Parsed ID for this Asset
     *
     * @return The Parsed ID for this Asset
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the Parsed Name for this Asset
     *
     * @return The Parsed Name for this Asset
     */
    public String getName() {
        return name;
    }

    /**
     * Prints Asset Data as a Readable String
     *
     * @return A readable version of this Asset
     */
    @Override
    public String toString() {
        return "DiscordAsset{" + "type=" + getType() + ", id='" + getId() + '\'' + ", name='" + getName() + '\'' + '}';
    }

    /**
     * A Mapping for the Parsed Asset Type for this Asset
     */
    public enum AssetType {
        @SerializedName("1")
        @Expose
        SMALL,
        @SerializedName("2")
        @Expose
        LARGE
    }
}
