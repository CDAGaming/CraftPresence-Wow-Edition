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

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Image Utilities used to Parse External Image Data and rendering tasks
 *
 * @author CDAGaming, wagyourtail
 */
public class ImageUtils {

    /**
     * The Blocking Queue for URL Requests
     * <p>
     * Format: textureName;textureData
     */
    private static final BlockingQueue<Pair<String, Pair<InputType, Object>>> urlRequests = Queues.newLinkedBlockingQueue();
    /**
     * Cached Images retrieved from URL Texture Retrieval
     * <p>
     * Format: textureName;[[textureInputType, textureObj], imageData, textureData]
     */
    private static final Map<String, Tuple<Pair<InputType, Object>, BufferedImage, ResourceLocation>> cachedImages = Maps.newHashMap();
    /**
     * The thread used for Url Image Events to take place within
     */
    private static final Thread urlQueue;

    static {
        urlQueue = new Thread("Url Queue") {
            @Override
            public void run() {
                try {
                    while (!CraftPresence.closing) {
                        final Pair<String, Pair<InputType, Object>> request = urlRequests.take();

                        BufferedImage bufferedImage = cachedImages.get(request.getFirst()).getSecond();
                        if (bufferedImage == null) {
                            try {
                                InputStream streamData = null;
                                switch (cachedImages.get(request.getFirst()).getFirst().getFirst()) {
                                    case FileData:
                                        streamData = new FileInputStream((File) cachedImages.get(request.getFirst()).getFirst().getSecond());
                                        break;
                                    case FileStream:
                                        streamData = new FileInputStream(cachedImages.get(request.getFirst()).getFirst().getSecond().toString());
                                        break;
                                    case Url:
                                        streamData = UrlUtils.getURLStream((URL) cachedImages.get(request.getFirst()).getFirst().getSecond());
                                        break;
                                    default:
                                }

                                if (streamData != null) {
                                    bufferedImage = ImageIO.read(streamData);
                                    cachedImages.get(request.getFirst()).setSecond(bufferedImage);
                                }
                            } catch (Exception ex) {
                                if (ModUtils.IS_VERBOSE) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    if (ModUtils.IS_VERBOSE) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        urlQueue.start();
    }

    /**
     * Retrieves a Texture from an external Url, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param url         The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromUrl(final String textureName, final String url) {
        try {
            return getTextureFromUrl(textureName, new URL(url));
        } catch (Exception ex) {
            if (ModUtils.IS_VERBOSE) {
                ex.printStackTrace();
            }
            return new ResourceLocation("");
        }
    }

    /**
     * Retrieves a Texture from an external Url, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param url         The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromUrl(final String textureName, final URL url) {
        try {
            return getTextureFromUrl(textureName, new Pair<>(InputType.Url, url));
        } catch (Exception ex) {
            if (ModUtils.IS_VERBOSE) {
                ex.printStackTrace();
            }
            return new ResourceLocation("");
        }
    }

    /**
     * Retrieves a Texture from an external Url, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param url         The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromUrl(final String textureName, final File url) {
        try {
            return getTextureFromUrl(textureName, new Pair<>(InputType.FileData, url));
        } catch (Exception ex) {
            if (ModUtils.IS_VERBOSE) {
                ex.printStackTrace();
            }
            return new ResourceLocation("");
        }
    }

    /**
     * Retrieves a Texture from an external Url, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param url         The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromUrl(final String textureName, final Object url) {
        if (url instanceof File) {
            return getTextureFromUrl(textureName, (File) url);
        } else if (url instanceof URL) {
            return getTextureFromUrl(textureName, (URL) url);
        } else {
            if (url.toString().toLowerCase().startsWith("http")) {
                return getTextureFromUrl(textureName, url.toString());
            } else {
                return getTextureFromUrl(textureName, new Pair<>(InputType.FileStream, url.toString()));
            }
        }
    }

    /**
     * Retrieves a Texture from an external Url, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param stream      Streaming Data containing data to read later
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromUrl(final String textureName, final Pair<InputType, Object> stream) {
        synchronized (cachedImages) {
            if (!cachedImages.containsKey(textureName)) {
                cachedImages.put(textureName, new Tuple<>(stream, null, null));
                try {
                    urlRequests.put(new Pair<>(textureName, stream));
                } catch (Exception ex) {
                    if (ModUtils.IS_VERBOSE) {
                        ex.printStackTrace();
                    }
                }
            }

            if (cachedImages.get(textureName).getThird() == null) {
                final BufferedImage bufferedImage = cachedImages.get(textureName).getSecond();
                if (bufferedImage == null) {
                    return new ResourceLocation("");
                } else if (textureName != null) {
                    final DynamicTexture dynTexture = new DynamicTexture(bufferedImage);
                    final ResourceLocation cachedTexture = CraftPresence.instance.getRenderManager().renderEngine.getDynamicTextureLocation(textureName, dynTexture);
                    cachedImages.get(textureName).setThird(cachedTexture);
                }
            }
            return cachedImages.get(textureName).getThird();
        }
    }

    /**
     * A Mapping storing the available Input Types for External Image Parsing
     *
     * <p>FileData: Parsing with Raw File Data (IE a File Object Type)
     * <p>FileStream: Parsing with the String representation of a file path, to be put into a filestream
     * <p>Url: Parsing with a direct or string representation of a Url, to be converted to an InputStream
     * <p>Unknown: Unknown property, experience can be iffy using this
     */
    public enum InputType {
        FileData, FileStream, Url, Unknown
    }
}
