package com.htilssu.entity;

import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends Collision implements Renderable {

    static int hoverOffset = 6;
    BufferedImage asset;
    boolean isHover = false;
    int hoverWidth;
    int hoverHeight;

    public Sprite(BufferedImage asset) {
        this(0, 0, asset.getWidth(), asset.getHeight(), asset);
    }

    /**
     * Khởi tạo {@code Sprite} với vị trí, kích thước và asset cho trước
     *
     * @param x      vị trí x
     * @param y      vị trí y
     * @param width  kích thước chiều rộng
     * @param height kích thước chiều cao
     * @param asset  asset
     */
    public Sprite(int x, int y, int width, int height, BufferedImage asset) {
        location.x = x;
        location.y = y;
        setSize(width, height);
        this.asset = asset;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        hoverWidth = width - hoverOffset;
        hoverHeight = height - hoverOffset;
    }

    public Sprite(String filePath) {
        this(0, 0, 0, 0, AssetUtils.loadImage(filePath));
    }

    public Sprite(Sprite sprite) {
        this(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.asset);
        this.isHover = sprite.isHover;
        this.hoverHeight = sprite.hoverHeight;
        this.hoverWidth = sprite.hoverWidth;
    }

    public Sprite(int i, int i1, BufferedImage bufferedImage) {
        setLocation(i, i1);
        setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
        this.asset = bufferedImage;
    }

    @Override
    public void render(Graphics g) {
        if (isHover) {
            g.drawImage(asset, getX() + hoverOffset / 2, getY() + hoverOffset / 2, hoverWidth,
                    hoverHeight, null);
        }
        else {
            g.drawImage(asset, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public BufferedImage getAsset() {
        return asset;
    }

    /**
     * Set lại asset cho {@code Sprite} nhưng thay đổi kích thước theo kích thước của {@code asset}
     *
     * @param asset asset mới
     */
    public void setAsset(BufferedImage asset) {
        this.asset = asset;
        setWidth(asset.getWidth());
        setHeight(asset.getHeight());
    }

    public void rotate() {
        asset = AssetUtils.rotate90(asset);
        this.setSize(asset.getWidth(), asset.getHeight());
    }

    /**
     * Set lại asset cho {@code Sprite} nhưng giữ nguyên kích thước cũ và vị trí cũ
     *
     * @param asset asset mới
     * @param o     null
     */
    public void setAsset(BufferedImage asset, Object o) {
        this.asset = asset;
    }

    public void handleHover(int x, int y) {
        isHover = isInside(x, y);
    }

}
