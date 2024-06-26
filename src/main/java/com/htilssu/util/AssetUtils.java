package com.htilssu.util;

import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
    public static final int ASSET_HOST_LIST_TEXT = 8;
    public static final int ASSET_READY_BUTTON = 9;
    public static final int ASSET_BACK_SEA = 10;
    public static final int ASSET_BACK_SEA_2 = 11;
    public static final int ASSET_UNREADY_BUTTON = 12;
    public static final int ASSET_REFRESH_BUTTON = 13;
    public static final int ASSET_BACK_BUTTON = 14;
    public static final int ASSET_TEXT_FIELD = 15;
    public static final int ASSET_BUTTON_1 = 16;
    public static final int ASSET_BUTTON_2 = 17;
    public static final int ASSET_BUTTON_3 = 18;
    public static final int ASSET_BUTTON_4 = 19;
    public static final int ASSET_SELECT = 20;
    public static final int ASSET_BG_START = 21;
    public static final int ASSET_BG_NEXT = 22;
    public static final int ASSET_BG_ATTACK = 23;
    public static final int ASSET_BG_SELFGRID = 24;
    public static final int ASSET_BG_BTNLISTENER = 25;
    public static final int ASSET_TEXT_FIELD_2 = 26;
    public static final int ASSET_PROGRESS_BACKGROUND = 27;
    public static final int ASSET_PROGRESS = 28;
    public static final int ASSET_HOLDER = 29;

    public static Font gameFont;

    static Map<Integer, BufferedImage> assetMap = new HashMap<>();

    static {
        initAsset();
        loadFont();
    }

    private static void loadFont() {
        try (var is = AssetUtils.class.getResourceAsStream("/font/Junter.otf")) {
            if (is != null) {
                gameFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            }
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initAsset() {
        BufferedImage asset1 = loadImage("/images/assets_1.png");
        BufferedImage buttonAsset = loadImage("/images/button_asset.png");
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
            assetMap.put(ASSET_REFRESH_BUTTON, buttonAsset.getSubimage(64 * 12, 0, 64 * 6, 64 * 2));
            assetMap.put(ASSET_BACK_BUTTON, buttonAsset.getSubimage(0, 64 * 2, 64 * 3, 64 * 3));
        }

        assetMap.put(ASSET_BACK_SEA, blur(loadImage("/images/sea.png")));
        assetMap.put(ASSET_BACK_SEA_2, loadImage("/images/sea_of_thief_2.png"));

        assetMap.forEach(
                (integer, bufferedImage) -> {
                    if (bufferedImage == null) {
                        GameLogger.error("asset: " + integer + " is null");
                    }
                });


        assetMap.put(ASSET_TEXT_FIELD, loadImage("/images/Item_TextField.png"));
        assetMap.put(ASSET_BUTTON_1, loadImage("/images/button_1.png"));
        assetMap.put(ASSET_BUTTON_2, loadImage("/images/button_2.png"));
        assetMap.put(ASSET_BUTTON_3, loadImage("/images/button_3.png"));
        assetMap.put(ASSET_BUTTON_4, loadImage("/images/button_4.png"));
        assetMap.put(ASSET_SELECT, loadImage("/images/select_2.png"));
        assetMap.put(ASSET_BG_START, loadImage("/images/BackgroundStart.png"));
        assetMap.put(ASSET_BG_NEXT, loadImage("/images/BackgroundNext.png"));
        assetMap.put(ASSET_BG_ATTACK, loadImage("/images/BackgroundAttack.png"));
        assetMap.put(ASSET_BG_SELFGRID, loadImage("/images/BackgroundAttack.png"));
        assetMap.put(ASSET_BG_BTNLISTENER, loadImage("/images/btnListener.png"));
        assetMap.put(ASSET_TEXT_FIELD_2, loadImage("/images/text_bg.png"));
        assetMap.put(ASSET_PROGRESS, loadImage("/images/healthbarProgress.png"));
        assetMap.put(ASSET_PROGRESS_BACKGROUND, loadImage("/images/healthbarUnder.png"));
        assetMap.put(ASSET_HOLDER, loadImage("/images/holder.png"));
    }

    /**
     * Phương thức load asset từ resources folder. Nếu không load được sẽ trả về null
     *
     * @param path đường dẫn file phải bắt đầu bằng /
     *
     * @return trả về {@link BufferedImage} nếu load thành công, ngược lại trả về {@code null}
     */
    public static BufferedImage loadImage(String path) {
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
     * Phương thức làm mờ ảnh
     *
     * @param image ảnh cần làm mờ
     *
     * @return ảnh đã được làm mờ
     */
    public static BufferedImage blur(@Nullable BufferedImage image) {
        if (image == null) {
            return null;
        }
        BufferedImage opaBg =
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        AlphaComposite aC = AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 0.7f);

        float[] a = new float[3 * 3];
        Arrays.fill(a, 0.1111f);
        Kernel kernel = new Kernel(3, 3, a);

        ConvolveOp cOP = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        Graphics2D g2dd = opaBg.createGraphics();

        g2dd.setComposite(aC);
        g2dd.drawImage(image, 0, 0, opaBg.getWidth(), opaBg.getHeight(), null);
        g2dd.dispose();
        return cOP.filter(opaBg, null);
    }

    /**
     * Phương thức lấy asset đã được load trước đó và lưu vào map ở phương thức {@link
     * AssetUtils#initAsset()}
     *
     * @param assetBoardFrame id của asset cần lấy
     *
     * @return trả về {@link BufferedImage} nếu asset tồn tại, ngược lại trả về {@code null}
     */
    public static BufferedImage getImage(int assetBoardFrame) {
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

    public static AudioInputStream loadSound(String path) {
        //read sounds file
        InputStream ip = AssetUtils.class.getResourceAsStream(path);
        if (ip != null) {
            try {
                return AudioSystem.getAudioInputStream(new BufferedInputStream(ip));
            } catch (UnsupportedAudioFileException | IOException e) {
                GameLogger.log("Sound not found: " + path);
            }
        }

        return null;
    }
}

