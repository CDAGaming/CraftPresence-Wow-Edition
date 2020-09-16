package com.gitlab.cdagaming.craftpresence.utils;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class ImageUtils {

    private static BlockingQueue<Pair<String, URL>> urlRequests = Queues.newLinkedBlockingQueue();
    private static Map<String, Tuple<URL, BufferedImage, ResourceLocation>> cachedImages = Maps.newHashMap();
    private static Thread urlQueue;
    
    /**
     * Retrieves a Texture from an external URL, and caching it for further usage
     * 
     * @param textureName The texture name to Identify this as
     * @param url The url to retrieve the texture
     * @return The Resulting Texture Data
     */
    public static ResourceLocation getTextureFromURL(final String textureName, final URL url) {
        synchronized (cachedImages) {
            if (!cachedImages.containsKey(textureName)) {
                cachedImages.put(textureName,  new Tuple<>(url, null, null));
                try {
                    urlRequests.put(new Pair<>(textureName, url));
                } catch (InterruptedException e) {}
            }
            
            if (cachedImages.get(textureName).getThird() == null) {
                BufferedImage bufferedImage = cachedImages.get(textureName).getSecond();
                if (bufferedImage == null) {
                    return new ResourceLocation("");
                } else if (textureName != null && url != null) {
                    DynamicTexture dynTexture = new DynamicTexture(bufferedImage);
                    ResourceLocation cachedTexture = CraftPresence.instance.getRenderManager().renderEngine.getDynamicTextureLocation(textureName, dynTexture);
                    cachedImages.get(textureName).setThird(cachedTexture);
                }
            }
            return cachedImages.get(textureName).getThird();
        }
    }
    
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
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        urlQueue.start();
    }
}
