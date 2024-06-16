package com.htilssu.entity;

import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends Collision implements Renderable {
  BufferedImage asset;

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
    this(0, 0, 0, 0, AssetUtils.loadImage(filePath));
  }

  public Sprite(Sprite sprite) {
    this(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.asset);
  }

  public BufferedImage getAsset() {
    return asset;
  }

  public void setAsset(BufferedImage asset) {
    this.asset = asset;
    setWidth(asset.getWidth());
    setHeight(asset.getHeight());
  }

  @Override
  public void render(@NotNull Graphics g) {
    g.drawImage(asset, getX(), getY(), getWidth(), getHeight(), null);
  }
}
