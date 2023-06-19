package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a static method for loading game images and returning
 * a BufferedImage from the specified file.
 */
public class Image {

    /**
     * Loads a BufferedImage from the specified file.
     *
     * @param fileName name of the image file to load.
     * @return BufferedImage object with loaded image.
     */
    public static BufferedImage loadImage(String fileName) {
        BufferedImage img = null;
        InputStream is = Image.class.getResourceAsStream(fileName);
        try (is) {
            assert is != null;
            img = ImageIO.read(is);
        } catch (IOException ignored) {}
        return img;
    }
}
