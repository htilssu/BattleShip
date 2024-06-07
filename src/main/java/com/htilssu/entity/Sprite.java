package com.htilssu.entity;

import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends Collision implements Renderable {
    static int hoverOffset = 6;
    BufferedImage asset;
    boolean isHover = false;
    int hoverWidth;
    int hoverHeight;

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

    public Sprite(BufferedImage asset) {
        this(0, 0, asset.getWidth(), asset.getHeight(), asset);
    }

    public Sprite(String filePath) {
        this(0, 0, 0, 0, AssetUtils.loadAsset(filePath));
    }

    public Sprite(Sprite sprite) {
        this(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.asset);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        hoverWidth = width - hoverOffset;
        hoverHeight = height - hoverOffset;
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

    @Override
    public void render(@NotNull Graphics g) {
        if (isHover) {
            g.drawImage(asset, getX() + hoverOffset / 2, getY() + hoverOffset / 2, hoverWidth, hoverHeight, null);
        } else {
            g.drawImage(asset, getX(), getY(), getWidth(), getHeight(), null);
        }
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
