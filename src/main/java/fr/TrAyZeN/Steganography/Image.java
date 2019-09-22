package fr.TrAyZeN.Steganography;

import fr.TrAyZeN.Steganography.util.BitQueue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {
    private BufferedImage image;
    private int width;
    private int height;

    public Image(String path) {
        open(path);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    private void open(String path) {
        File file = new File(path);

        try {
            this.image = ImageIO.read(file);
        }
        catch (IOException error) {
            System.out.println("Failed to decode image : " + error);
            System.exit(1);
        }
    }

    public void encode(String message, short nb) {
        BitQueue bitqueue = new BitQueue();
        bitqueue.addString(message);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!bitqueue.isEmpty()) {
                    this.writeBitsToPixel(x, y, bitqueue, nb);
                }
            }
        }
    }

    private void writeBitsToPixel(int x, int y, BitQueue bitqueue, short nb) {
        int pixel = image.getRGB(x, y);

        int byteIndex[] = { 0, 8, 16 };

        // don't need to generate the mask every time
        int mask = 0;
        for (int i = 0; i < 3; i++) {
            mask |= ((0xFF >> nb) << nb) << byteIndex[i];
        }
        mask |= 0xFF << 24; // add alpha channel

        pixel &= mask;

        for (int i = 2; i >= 0 && !bitqueue.isEmpty(); i--) {
            int color = (pixel >> byteIndex[i]) & 0xFF;

            for (int b = nb - 1; b >= 0 && !bitqueue.isEmpty(); b--) {
                color |= bitqueue.removeFirstAsInt() << b;
            }

            pixel |= color << byteIndex[i];
        }

        image.setRGB(x, y, pixel);
    }

    public String decode(short nb) {
        BitQueue bitqueue = new BitQueue();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.readPixel(x, y, bitqueue, nb);
            }
        }

        String message = bitqueue.asString();

        return message;
    }

    private void readPixel(int x , int y, BitQueue bitqueue, short nb) {
        int pixel = image.getRGB(x, y);

        int byteIndex[] = { 0, 8, 16 };

        for (int i = 2; i >= 0; i--) {
            int color = (pixel >> byteIndex[i]) & 0xFF;

            for (int b = nb - 1; b >= 0; b--) {
                bitqueue.addIntAsBool((color >> b) & 1);
            }
        }
    }


    public void save(String outputPath) {
        File outputFile = new File(outputPath);

        try {
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException error) {
            System.out.println("Failed to write image file : " + error);
            System.exit(1);
        }
    }
}
