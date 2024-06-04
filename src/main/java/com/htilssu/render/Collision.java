package com.htilssu.render;

import java.awt.*;

public abstract class Collision {
  protected Point location = new Point(0, 0);
  Dimension dimension = new Dimension(0, 0);

  public Dimension getDimension() {
    return dimension;
  }

  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

  public int getWidth() {
    return dimension.width;
  }

  public void setWidth(int width) {
    dimension.width = width;
  }

  public int getHeight() {
    return dimension.height;
  }

  public void setHeight(int height) {
    dimension.height = height;
  }

  public void setSize(int width, int height) {
    dimension.setSize(width, height);
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public int getX() {
    return location.x;
  }

  public void setX(int x) {
    location.x = x;
  }

  public int getY() {
    return location.y;
  }

  public void setY(int y) {
    location.y = y;
  }

  public void setLocation(int x, int y) {
    location.setLocation(x, y);
  }

  public boolean isInside(Point point) {
    return isInside(point.x, point.y);
  }

  public boolean isInside(int x, int y) {
    return x > location.x
        && x <= location.x + dimension.width
        && y > location.y
        && y <= location.y + dimension.height;
  }
}
