package com.htilssu.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class AssetUtils {
  public static final int ASSET_BOARD_FRAME = 1;
  public static final int ASSET_SHOOT_MISS = 2;
  public static final int ASSET_SHOOT_HIT = 3;
  public static final int ASSET_SHIP_2 = 4;
  public static final int ASSET_SHIP_3 = 5;
  public static final int ASSET_SHIP_4 = 6;
  public static final int ASSET_SHIP_5 = 7;
  public static final int ASSET_HOST_LIST_TEXT = 8;
  public static final int ASSET_READY_BUTTON = 9;
  public static final Integer ASSET_BACK_SEA = 10;
  public static final Integer ASSET_BACK_SEA_2 = 11;
  public static final Integer ASSET_UNREADY_BUTTON = 12;

  static Map<Integer, BufferedImage> assetMap = new HashMap<>();

  static {
    initAsset();
  }

  private static void initAsset() {
    BufferedImage asset1 = loadAsset("/assets_1.png");
    BufferedImage buttonAsset = loadAsset("/button_asset.png");
    if (asset1 != null) {
      assetMap.put(ASSET_SHOOT_MISS, asset1.getSubimage(0, 0, 64, 64));
      assetMap.put(ASSET_SHOOT_HIT, asset1.getSubimage(64, 0, 64, 64));
      assetMap.put(ASSET_SHIP_2, asset1.getSubimage(0, 64, 64, 64 * 2));
      assetMap.put(ASSET_SHIP_3, asset1.getSubimage(64, 64, 64, 64 * 3));
      assetMap.put(ASSET_SHIP_4, asset1.getSubimage(128, 0, 64, 64 * 4));
      assetMap.put(ASSET_SHIP_5, asset1.getSubimage(192, 0, 64, 64 * 5));
      assetMap.put(ASSET_HOST_LIST_TEXT, asset1.getSubimage(256, 0, 512, 64));
    }

    if (buttonAsset != null) {
      assetMap.put(ASSET_READY_BUTTON, buttonAsset.getSubimage(0, 0, 64 * 6, 64 * 2));
      assetMap.put(ASSET_UNREADY_BUTTON, buttonAsset.getSubimage(64 * 6, 0, 64 * 6, 64 * 2));
    }

    assetMap.put(ASSET_BACK_SEA, loadAsset("/sea.png"));
    assetMap.put(ASSET_BACK_SEA_2, loadAsset("/sea_of_thief_2.png"));

    assetMap.forEach(
        (integer, bufferedImage) -> {
          if (bufferedImage == null) {
            GameLogger.error("asset: " + integer + " is null");
          }
        });
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
      GameLogger.log("Asset not found: " + path);
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

  public static BufferedImage rotate90(BufferedImage image) {
    int w = image.getWidth();
    int h = image.getHeight();
    BufferedImage rotatedImage = new BufferedImage(h, w, image.getType());

    Graphics2D g2d = rotatedImage.createGraphics();

    AffineTransform transform = new AffineTransform();
    transform.rotate(Math.toRadians(90), 0, 0);
    transform.translate(0, -h);
    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
    op.filter(image, rotatedImage);

    g2d.dispose();
    return rotatedImage;
  }
}
