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
     * <p>Format: textureName;textureUrl
     */
    private static BlockingQueue<Pair<String, URL>> urlRequests = Queues.newLinkedBlockingQueue();
    /**
     * Cached Images retrieved from URL Texture Retrieval
     * <p>Format: textureName;<textureUrl, imageData, textureData>
     */
    private static Map<String, Tuple<URL, BufferedImage, ResourceLocation>> cachedImages = Maps.newHashMap();
    /**
     * The thread used for Url Image Events to take place within
     */
    private static Thread urlQueue;

    static {
        urlQueue = new Thread("URL Queue") {
            @Override
            public void run() {

                Pair<String, URL> request = null;
                try {
                    while (true) {
                        request = urlRequests.take();

                        DynamicTexture dynTexture = null;
                        if (dynTexture == null) {
                            BufferedImage bufferedImage = null;
                            try {
                                bufferedImage = ImageIO.read(UrlUtils.getURLStream(request.getSecond()));
                                cachedImages.get(request.getFirst()).setSecond(bufferedImage);
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
     * Retrieves a Texture from an external URL, and caching it for further usage
     *
     * @param textureName The texture name to Identify this as
     * @param url         The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromURL(final String textureName, final URL url) {
        synchronized (cachedImages) {
            if (!cachedImages.containsKey(textureName)) {
                cachedImages.put(textureName, new Tuple<>(url, null, null));
                try {
                    urlRequests.put(new Pair<>(textureName, url));
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
                } else if (textureName != null && url != null) {
                    final DynamicTexture dynTexture = new DynamicTexture(bufferedImage);
                    final ResourceLocation cachedTexture = CraftPresence.instance.getRenderManager().renderEngine.getDynamicTextureLocation(textureName, dynTexture);
                    cachedImages.get(textureName).setThird(cachedTexture);
                }
            }
            return cachedImages.get(textureName).getThird();
        }
    }
}
