package com.htilssu.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class AssetUtil {
    public static BufferedImage loadAsset(String path) {
        try {
            InputStream ip = AssetUtil.class.getResourceAsStream(path);
            if (ip != null) {
                return ImageIO.read(ip);
            }
        } catch (Exception ignored){}
        return null;
    }
}
