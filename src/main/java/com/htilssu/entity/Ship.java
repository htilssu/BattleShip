package com.htilssu.entity;

import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import java.awt.*;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

// Lớp Ship đại diện cho một con TÀU
public class Ship extends Collision implements Renderable {
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

  Sprite sprite;
  Position position;
  PlayerBoard playerBoard;
  int shipType;

  public Ship(Ship ship) {
    Field[] classField = this.getClass().getDeclaredFields();
    for (Field field : classField) {
      try {
        field.set(this, field.get(ship));
      } catch (IllegalAccessException ignored) {
      }
    }
  }

  public Ship(int direction, Sprite sprite, Position position, int shipType) {

    this.direction = direction;
    this.position = position;
    this.shipType = shipType;
    this.sprite = sprite;
  }

  public Ship(
      int direction, Sprite sprite, Position position, int shipType, PlayerBoard playerBoard) {
    this(direction, sprite, position, shipType);
    this.playerBoard = playerBoard;
  }

  public int getDirection() {
    return direction;
  }

  public Sprite getSprite() {
    return sprite;
  }

  public Position getPosition() {
    return position;
  }

  public PlayerBoard getPlayerBoard() {
    return playerBoard;
  }

  public int getShipType() {
    return shipType;
  }

  @Override
  public void render(@NotNull Graphics g) {
    sprite.render(g);
  }

  @Override
  public boolean isInside(int x, int y) {
    return sprite.isInside(x, y);
  }

  public PlayerBoard getBoard() {
    return playerBoard;
  }

  public void setBoard(PlayerBoard board) {
    this.playerBoard = board;
  }

  public void update() {
    float ratio = (float) sprite.getHeight() / sprite.getWidth();
    sprite.setLocation(
        playerBoard.getX() + playerBoard.getCellSize() * position.x,
        playerBoard.getY() + playerBoard.getCellSize() * position.y);

    if (ratio < 1 && ratio > 0) {
      ratio = 1 / ratio;
      sprite.setSize((int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

    } else {
      sprite.setSize(playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
    }
  }
}
