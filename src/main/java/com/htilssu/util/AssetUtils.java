package com.htilssu.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetUtils {
    public static final int ASSET_BOARD_FRAME = 1;

    static Map<Integer, BufferedImage> assetMap = new HashMap<>();

    static {
        initAsset();
    }

    private static void initAsset() {
        BufferedImage asset_1 = loadAsset("/assets_1.png");
        if (asset_1 != null) {
            assetMap.put(ASSET_BOARD_FRAME, asset_1.getSubimage(0, 0, 64 * 10, 64 * 10));
        }
    }

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

    public static BufferedImage getAsset(int assetBoardFrame) {
        return assetMap.get(assetBoardFrame);
    }
}
