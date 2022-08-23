package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class ImageToTextConv implements TextGraphicsConverter {
    private int maxWidth = 0;
    private int maxHeight = 0;
    private char[] oneString;
    private double maxRatio;
    private double ratio;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        String imageFinal = "";
        int newWidth;
        int newHeight;
        BufferedImage img = ImageIO.read(new URL(url));
        if (maxWidth == 0 && maxHeight == 0) {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        } else if (img.getWidth() <= maxWidth && img.getHeight() <= maxHeight) {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        } else {
            if (img.getWidth() > img.getHeight()) {
                double divider = (double) img.getWidth() / maxWidth;
                newWidth = (int) (img.getWidth() / divider);
                newHeight = (int) (img.getHeight() / divider);
                System.out.println("width = " + img.getWidth() + " height = " + img.getHeight());
                System.out.println("width/maxWidth = " + divider);
            } else {
                double divider = (double) img.getHeight() / maxHeight;
                newWidth = (int) (img.getWidth() / divider);
                newHeight = (int) (img.getHeight() / divider);
            }
        }
        if (newWidth > newHeight) {
            ratio = (double) newWidth / newHeight;
        } else {
            ratio = (double) newHeight / newWidth;
        }
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        Image scaleImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaleImage, 0, 0, null);
        // test
        //ImageIO.write(bwImg, "png", new File("Outbw.png"));
        WritableRaster bwRaster = bwImg.getRaster();
        ColorConverter schema = new ColorConverter();
        oneString = new char[bwRaster.getWidth()];
        String doubleString = "";
        for (int y = 0; y < bwRaster.getHeight(); y++) {
            for (int x = 0; x < bwRaster.getWidth(); x++) {
                int color = bwRaster.getPixel(x, y, new int[3])[0];
                char c = schema.convert(color);
                oneString[x] = c;
                doubleString += String.valueOf(oneString[x]) + oneString[x];
            }
            imageFinal += doubleString + "\n";
            doubleString = "";
            //imageFinal += new String(oneString) + "\n";
        }
        return imageFinal;
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;

    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {

    }

}
