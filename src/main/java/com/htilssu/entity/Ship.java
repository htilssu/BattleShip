package com.htilssu.entity;

import com.htilssu.component.Position;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.render.Renderable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

// Lớp Ship đại diện cho một con TÀU
public class Ship implements Renderable {
  public static final int SHIP_2 = 2;
  public static final int SHIP_3 = 3;
  public static final int SHIP_4 = 4;
  public static final int SHIP_5 = 5;
  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;

  /**
   *
   *
   * <pre>{@code
   * 0 - Ngang
   * 1 - Dọc
   * }</pre>
   */
  int direction = HORIZONTAL;

  BufferedImage asset;
  Position position;
  PlayerBoard playerBoard;
  int shipType;

  public Ship(
      int direction,
      BufferedImage asset,
      Position position,
      PlayerBoard playerBoard,
      int shipType) {
    this(direction, asset, position, shipType);
    this.playerBoard = playerBoard;
  }

  public Ship(int direction, BufferedImage asset, Position position, int shipType) {
    this.direction = direction;
    this.asset = asset;
    this.position = position;
    this.shipType = shipType;
  }

  @Override
  public void render(@NotNull Graphics g) {}
}
