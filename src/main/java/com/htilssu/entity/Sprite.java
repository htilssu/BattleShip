package com.htilssu.entity;

import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite implements Renderable, Collision {
  int x;
  int y;
  int width;
  int height;
  BufferedImage asset;

  public Sprite(int x, int y, int width, int height, BufferedImage asset) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.asset = asset;
  }

  public Sprite(BufferedImage asset) {
    this(0, 0, asset.getWidth(), asset.getHeight(), asset);
  }

  public Sprite(String filePath) {
    this(0, 0, 0, 0, AssetUtils.loadAsset(filePath));
  }

  public Sprite(Sprite sprite) {
    this(sprite.x, sprite.y, sprite.width, sprite.height, sprite.asset);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public BufferedImage getAsset() {
    return asset;
  }

  public void setAsset(BufferedImage asset) {
    this.asset = asset;
    setWidth(asset.getWidth());
    setHeight(asset.getHeight());
  }

  public void setSize(int width, int height) {
    setWidth(width);
    setHeight(height);
  }

  public void setPosition(int x, int y) {
    setX(x);
    setY(y);
  }

  @Override
  public void render(@NotNull Graphics g) {
    g.drawImage(asset, x, y, width, height, null);
  }

  @Override
  public boolean isInside(int x, int y) {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }

  public boolean isInside(Point point) {
    return isInside(point.x, point.y);
  }
}
