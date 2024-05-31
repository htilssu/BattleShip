package com.htilssu.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetUtils {
  public static final int ASSET_BOARD_FRAME = 1;
  public static final int ASSET_SHOOT_MISS = 2;
  public static final int ASSET_SHOOT_HIT = 3;
  public static final int ASSET_SHIP_2 = 4;
  public static final int ASSET_SHIP_3 = 5;
  public static final int ASSET_SHIP_4 = 6;
  public static final int ASSET_SHIP_5 = 7;

  static Map<Integer, BufferedImage> assetMap = new HashMap<>();

  static {
    initAsset();
  }

  private static void initAsset() {
    BufferedImage asset_1 = loadAsset("/assets_1.png");
    if (asset_1 != null) {
      assetMap.put(ASSET_SHOOT_MISS, asset_1.getSubimage(0, 0, 64, 64));
      assetMap.put(ASSET_SHOOT_HIT, asset_1.getSubimage(64, 0, 64, 64));
      assetMap.put(ASSET_SHIP_2, asset_1.getSubimage(0, 64, 64, 64 * 2));
      assetMap.put(ASSET_SHIP_3, asset_1.getSubimage(64, 64, 64, 64 * 3));
      assetMap.put(ASSET_SHIP_4, asset_1.getSubimage(128, 0, 64, 64 * 4));
      assetMap.put(ASSET_SHIP_5, asset_1.getSubimage(192, 0, 64, 64 * 5));
    }
  }

  /**
   * Phương thức load asset từ resources folder. Nếu không load được sẽ trả về null
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
   * Phương thức lấy asset đã được load trước đó và lưu vào map ở phương thức {@link
   * AssetUtils#initAsset()}
   *
   * @param assetBoardFrame id của asset cần lấy
   * @return trả về {@link BufferedImage} nếu asset tồn tại, ngược lại trả về {@code null}
   */
  public static BufferedImage getAsset(int assetBoardFrame) {
    return assetMap.get(assetBoardFrame);
  }

  public static BufferedImage rotateImage(BufferedImage image, double angle) {
    int w = image.getWidth();
    int h = image.getHeight();
    BufferedImage rotatedImage = new BufferedImage(h, w, image.getType());

    Graphics2D g2d = rotatedImage.createGraphics();

    AffineTransform transform = new AffineTransform();
    transform.rotate(angle, (double) w, (double) h / 2);

    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
    op.filter(image, rotatedImage);

    g2d.dispose();

    File outputfile = new File("image.png");
    GameLogger.log(outputfile.getAbsolutePath());
    try {
      ImageIO.write(rotatedImage, "png", outputfile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return rotatedImage;
  }
}
