package com.htilssu.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class AssetUtils {
    public static BufferedImage loadAsset(String path) {
        try {
            InputStream ip = AssetUtils.class.getResourceAsStream(path);
            if (ip != null) {
                return ImageIO.read(ip);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
