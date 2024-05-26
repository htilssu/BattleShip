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

    /**
     * Phương thức load asset từ resources folder.
     * Nếu không load được sẽ trả về null
     *
     * @param path đường dẫn file phải bắt đầu bằng /
     * @return trả về {@link BufferedImage} nếu load thành công, ngược lại trả về {@code null}
     */
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

    /**
     * Phương thức lấy asset đã được load trước đó và lưu vào map ở phương thức {@link AssetUtils#initAsset()}
     *
     * @param assetBoardFrame id của asset cần lấy
     * @return trả về {@link BufferedImage} nếu asset tồn tại, ngược lại trả về {@code null}
     */
    public static BufferedImage getAsset(int assetBoardFrame) {
        return assetMap.get(assetBoardFrame);
    }
}
