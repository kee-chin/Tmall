package com.chinkee.tmall.util;

import java.awt.*;
import java.awt.image.*;
import java.io.File;

public class ImageUtil {
    public static BufferedImage change2jpg(File file){

        try{
            Image image = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());
            PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, -1, -1, true);
            pixelGrabber.grabPixels();

            int width = pixelGrabber.getWidth();
            int height = pixelGrabber.getHeight();
            final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
            DataBuffer dataBuffer = new DataBufferInt((int[]) pixelGrabber.getPixels(), pixelGrabber.getWidth() * pixelGrabber.getHeight());
            WritableRaster writableRaster = Raster.createPackedRaster(dataBuffer, width, height, width, RGB_MASKS, null);
            BufferedImage bufferedImage = new BufferedImage(RGB_OPAQUE, writableRaster, false, null);
            return bufferedImage;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image resizeImage(Image srcImage, int width, int height){

        try {
            BufferedImage bufferedImage2 = null;
            bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage2.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            return bufferedImage2;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
