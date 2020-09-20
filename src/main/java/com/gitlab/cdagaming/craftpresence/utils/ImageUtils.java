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
import com.gitlab.cdagaming.craftpresence.impl.ImageFrame;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import java.awt.image.ColorModel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private static final Map<String, Tuple<Pair<InputType, Object>, Pair<Integer, List<ImageFrame>>, List<ResourceLocation>>> cachedImages = Maps.newHashMap();
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
                        final boolean isGif = request.getFirst().endsWith(".gif");

                        Pair<Integer, List<ImageFrame>> bufferData = cachedImages.get(request.getFirst()).getSecond();
                        if (bufferData == null) {
                            // Setup Initial Data
                            bufferData = new Pair<>(0, Lists.newArrayList());
                            try {
                                InputStream streamData = null;
                                switch (request.getSecond().getFirst()) {
                                    case FileData:
                                        streamData = new FileInputStream((File) request.getSecond().getSecond());
                                        break;
                                    case FileStream:
                                        streamData = new FileInputStream(request.getSecond().getSecond().toString());
                                        break;
                                    case Url:
                                        streamData = UrlUtils.getURLStream((URL) request.getSecond().getSecond());
                                        break;
                                    default:
                                }

                                if (streamData != null) {
                                    if (isGif) {
                                        ImageFrame[] frames = readGif(streamData);

                                        for (ImageFrame frame : frames) {
                                            try {
                                                bufferData.getSecond().add(frame);
                                            } catch (Exception ex) {
                                                if (ModUtils.IS_VERBOSE) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    } else {
                                        bufferData.getSecond().add(new ImageFrame(ImageIO.read(streamData)));
                                    }
                                    cachedImages.get(request.getFirst()).setSecond(bufferData);
                                    cachedImages.get(request.getFirst()).setThird(new ArrayList<>(bufferData.getSecond().size()));
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

            final Pair<Integer, List<ImageFrame>> bufferData = cachedImages.get(textureName).getSecond();

            if (bufferData == null || bufferData.getSecond() == null || bufferData.getSecond().isEmpty()) {
                return new ResourceLocation("");
            } else if (textureName != null) {
                final boolean shouldRepeat = textureName.endsWith(".gif");
                final boolean doesContinue = bufferData == null ? false : bufferData.getFirst() < bufferData.getSecond().size() - 1;

                List<ResourceLocation> resources = cachedImages.get(textureName).getThird();
                if (bufferData.getFirst() < resources.size()) {
                    ResourceLocation loc = resources.get(bufferData.getFirst());
                    if (bufferData.getSecond().get(bufferData.getFirst()).shouldRenderNext()) {
                        if (doesContinue) {
                            bufferData.getSecond().get(bufferData.setFirst(bufferData.getFirst() + 1)).setRenderTime(System.currentTimeMillis());
                        } else if (shouldRepeat) {
                            bufferData.getSecond().get(bufferData.setFirst(0)).setRenderTime(System.currentTimeMillis());
                        }
                    }
                    return loc;
                }
                final DynamicTexture dynTexture = new DynamicTexture(bufferData.getSecond().get(bufferData.getFirst()).getImage());
                final ResourceLocation cachedTexture = CraftPresence.instance.getRenderManager().renderEngine.getDynamicTextureLocation(textureName + (textureName.endsWith(".gif") ? "_" + cachedImages.get(textureName).getFirst().getFirst() : ""), dynTexture);
                if (bufferData.getSecond().get(bufferData.getFirst()).shouldRenderNext()) {
                    if (doesContinue) {
                        bufferData.getSecond().get(bufferData.setFirst(bufferData.getFirst() + 1)).setRenderTime(System.currentTimeMillis());
                    } else if (shouldRepeat) {
                        bufferData.setFirst(0);
                    }
                }
                if (!resources.contains(cachedTexture)) {
                    resources.add(cachedTexture);
                }
                return cachedTexture;
            } else {
                return new ResourceLocation("");
            }
        }
    }

    /**
     * A Mapping storing the available Input Types for External Image Parsing
     *
     * <p>
     * FileData: Parsing with Raw File Data (IE a File Object Type), to be put into
     * a FileInputStream
     * <p>
     * FileStream: Parsing with the String representation of a file path, to be put
     * into a FileInputStream
     * <p>
     * Url: Parsing with a direct or string representation of a Url, to be converted
     * to an InputStream
     * <p>
     * Unknown: Unknown property, experience can be iffy using this
     */
    public enum InputType {
        FileData, FileStream, Url, Unknown
    }

    public static ImageFrame[] readGif(InputStream stream) throws IOException {
        ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);
    
        ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(stream));
    
        int lastx = 0;
        int lasty = 0;
    
        int width = -1;
        int height = -1;
    
        IIOMetadata metadata = reader.getStreamMetadata();
    
        Color backgroundColor = null;
    
        if(metadata != null) {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
    
            NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
            NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");
    
            if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0){
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);
    
                if (screenDescriptor != null){
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }
    
            if (globalColorTable != null && globalColorTable.getLength() > 0){
                IIOMetadataNode colorTable = (IIOMetadataNode) globalColorTable.item(0);
    
                if (colorTable != null) {
                    String bgIndex = colorTable.getAttribute("backgroundColorIndex");
    
                    IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
                    while (colorEntry != null) {
                        if (colorEntry.getAttribute("index").equals(bgIndex)) {
                            int red = Integer.parseInt(colorEntry.getAttribute("red"));
                            int green = Integer.parseInt(colorEntry.getAttribute("green"));
                            int blue = Integer.parseInt(colorEntry.getAttribute("blue"));
    
                            backgroundColor = new Color(red, green, blue);
                            break;
                        }
    
                        colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
                    }
                }
            }
        }
    
        BufferedImage master = null;
        boolean hasBackround = false;
    
        for (int frameIndex = 0;; frameIndex++) {
            BufferedImage image;
            try{
                image = reader.read(frameIndex);
            }catch (IndexOutOfBoundsException io){
                break;
            }
    
            if (width == -1 || height == -1){
                width = image.getWidth();
                height = image.getHeight();
            }
    
            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            NodeList children = root.getChildNodes();
    
            int delay = Integer.valueOf(gce.getAttribute("delayTime"));
    
            String disposal = gce.getAttribute("disposalMethod");
    
            if (master == null){
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                master.createGraphics().setColor(backgroundColor);
                master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());
    
            hasBackround = image.getWidth() == width && image.getHeight() == height;
    
                master.createGraphics().drawImage(image, 0, 0, null);
            }else{
                int x = 0;
                int y = 0;
    
                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++){
                    Node nodeItem = children.item(nodeIndex);
    
                    if (nodeItem.getNodeName().equals("ImageDescriptor")){
                        NamedNodeMap map = nodeItem.getAttributes();
    
                        x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }
    
                if (disposal.equals("restoreToPrevious")){
                    BufferedImage from = null;
                    for (int i = frameIndex - 1; i >= 0; i--){
                        if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0){
                            from = frames.get(i).getImage();
                            break;
                        }
                    }
    
                    {
                        ColorModel model = from.getColorModel();
                        boolean alpha = from.isAlphaPremultiplied();
                        WritableRaster raster = from.copyData(null);
                        master = new BufferedImage(model, raster, alpha, null);
                    }
                }else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null){
                    if (!hasBackround || frameIndex > 1){
                        master.createGraphics().fillRect(lastx, lasty, frames.get(frameIndex - 1).getWidth(), frames.get(frameIndex - 1).getHeight());
                    }
                }
                master.createGraphics().drawImage(image, x, y, null);
    
                lastx = x;
                lasty = y;
            }
    
            {
                BufferedImage copy;
    
                {
                    ColorModel model = master.getColorModel();
                    boolean alpha = master.isAlphaPremultiplied();
                    WritableRaster raster = master.copyData(null);
                    copy = new BufferedImage(model, raster, alpha, null);
                }
                frames.add(new ImageFrame(copy, delay, disposal, image.getWidth(), image.getHeight()));
            }
    
            master.flush();
        }
        reader.dispose();
    
        return frames.toArray(new ImageFrame[frames.size()]);
    }
}
