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

package com.gitlab.cdagaming.craftpresence.impl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Image Conversion Layers and Utilities used to translate other Image Types
 * <p>Reference: https://stackoverflow.com/a/17269591
 *
 * @author CDAGaming
 */
public class ImageFrame {
    /**
     * The delay between image transitions
     */
    private final int delay;
    /**
     * The buffered image instance being stored
     */
    private final BufferedImage image;
    /**
     * The disposal method flag being used for this frame
     */
    private final String disposal;
    /**
     * The width of the current frame
     */
    private final int width;
    /**
     * The height of the current frame
     */
    private final int height;
    /**
     * The time, in milliseconds, for the frame to render until from current
     */
    private long renderTime = 0;

    /**
     * Initializes an Image Frame, with the specified arguments
     *
     * @param image    The buffered image, if any, to be stored for this frame
     * @param delay    The delay between now and the next image transition
     * @param disposal The disposal method flag to use for this frame
     * @param width    The width of this image
     * @param height   The height of this image
     */
    public ImageFrame(BufferedImage image, int delay, String disposal, int width, int height) {
        this.image = image;
        this.delay = delay;
        this.disposal = disposal;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes an Image Frame, with the specified arguments
     *
     * @param image The buffered image, if any, to be stored for this frame
     */
    public ImageFrame(BufferedImage image) {
        this.image = image;
        this.delay = -1;
        this.disposal = null;
        this.width = -1;
        this.height = -1;
    }

    /**
     * Reads an array of Image Frames from an InputStream
     *
     * @param stream The stream of data to be interpreted
     * @return The resulting array of Image Frames, if successful
     * @throws IOException If an error occurs during operation
     */
    public static ImageFrame[] readGif(InputStream stream) throws IOException {
        ArrayList<ImageFrame> frames = new ArrayList<>(2);

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(stream));

        int lastX = 0;
        int lastY = 0;

        int width = -1;
        int height = -1;

        IIOMetadata metadata = reader.getStreamMetadata();

        Color backgroundColor = null;

        if (metadata != null) {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

            NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
            NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

            if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0) {
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);

                if (screenDescriptor != null) {
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }

            if (globalColorTable != null && globalColorTable.getLength() > 0) {
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
        boolean hasBackground = false;

        for (int frameIndex = 0; ; frameIndex++) {
            BufferedImage image;
            try {
                image = reader.read(frameIndex);
            } catch (IndexOutOfBoundsException io) {
                break;
            }

            if (width == -1 || height == -1) {
                width = image.getWidth();
                height = image.getHeight();
            }

            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            NodeList children = root.getChildNodes();

            int delay = Integer.parseInt(gce.getAttribute("delayTime"));

            String disposal = gce.getAttribute("disposalMethod");

            if (master == null) {
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                master.createGraphics().setColor(backgroundColor);
                master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());

                hasBackground = image.getWidth() == width && image.getHeight() == height;

                master.createGraphics().drawImage(image, 0, 0, null);
            } else {
                int x = 0;
                int y = 0;

                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
                    Node nodeItem = children.item(nodeIndex);

                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        NamedNodeMap map = nodeItem.getAttributes();

                        x = Integer.parseInt(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.parseInt(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }

                if (disposal.equals("restoreToPrevious")) {
                    BufferedImage from = null;
                    for (int i = frameIndex - 1; i >= 0; i--) {
                        if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0) {
                            from = frames.get(i).getImage();
                            break;
                        }
                    }

                    if (from != null) {
                        ColorModel model = from.getColorModel();
                        boolean alpha = from.isAlphaPremultiplied();
                        WritableRaster raster = from.copyData(null);
                        master = new BufferedImage(model, raster, alpha, null);
                    }
                } else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null) {
                    if (!hasBackground || frameIndex > 1) {
                        master.createGraphics().fillRect(lastX, lastY, frames.get(frameIndex - 1).getWidth(), frames.get(frameIndex - 1).getHeight());
                    }
                }
                master.createGraphics().drawImage(image, x, y, null);

                lastX = x;
                lastY = y;
            }

            ColorModel model = master.getColorModel();
            boolean alpha = master.isAlphaPremultiplied();
            WritableRaster raster = master.copyData(null);
            BufferedImage copy = new BufferedImage(model, raster, alpha, null);
            frames.add(new ImageFrame(copy, delay, disposal, image.getWidth(), image.getHeight()));

            master.flush();
        }
        reader.dispose();

        return frames.toArray(new ImageFrame[0]);
    }

    /**
     * Retrieves the current buffered image being stored
     *
     * @return The current buffered image being stored
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Retrieves the delay between image transitions
     *
     * @return The delay between image transitions
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Retrieves the disposal method flag being used for this frame
     *
     * @return The disposal method flag being used for this frame
     */
    public String getDisposal() {
        return disposal;
    }

    /**
     * Retrieves the width of the current frame
     *
     * @return The width of the current frame
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the current frame
     *
     * @return The height of the current frame
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the time, in milliseconds, for the frame to render until from current
     *
     * @return The time, in milliseconds, for the frame to render until from current
     */
    public long getRenderTime() {
        return renderTime;
    }

    /**
     * Sets the time, in milliseconds, for the frame to render until from current
     *
     * @param renderTime The new timestamp to render until
     */
    public void setRenderTime(long renderTime) {
        this.renderTime = renderTime;
    }

    /**
     * Determine whether or not this frame has rendered up to or past the delay
     *
     * @return Whether or not this frame has rendered up to or past the delay
     */
    public boolean shouldRenderNext() {
        return System.currentTimeMillis() - renderTime > delay * 10;
    }
}
