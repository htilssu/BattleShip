package com.htilssu.entity.player;

import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Dimension implements Renderable, Collision {

  List<Ship> ships = new ArrayList<>();

  int size;
  int cellSize;
  Point position = new Point(64, 64);
  private GamePlay gamePlay;

  /**
   * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
   *
   * @param size Kích thước của bảng
   */
  public PlayerBoard(int size) {
    super(0, 0);
    this.size = size;
    update();
  }

  public int getCellSize() {
    return cellSize;
  }

  /** Cập nhật kích thước của bảng người chơi */
  public void update() {
    cellSize = (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE + 16);
    for (Ship ship : ships) {
      ship.update();
    }
    setSize(cellSize * size, cellSize * size);
  }

  @Override
  public void render(@NotNull Graphics g) {
    update();

    // Vẽ bảng người chơi
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    for (int i = 0; i <= Math.pow(size, 2); i++) {
      g2d.setColor(Color.white);
      if (i < Math.pow(size, 2)) {
        g2d.fill(
            new Rectangle(
                position.x + i % size * cellSize,
                (position.y + cellSize * (i / size)),
                cellSize,
                cellSize));
        // TODO: Vẽ tàu khi ở chế độ MODE_SETUP

        //        g2d.drawImage(
        //            asset_shoot_miss,
        //            position.x + i % size * cellSize,
        //            position.y + cellSize * (i / size),
        //            cellSize,
        //            cellSize,
        //            null);
      }
    }

    for (Ship ship : ships) {
      ship.render(g);
    }

    for (int i = 0; i <= size; i++) {
      g2d.setColor(Color.black);
      g2d.drawLine(
          position.x,
          position.y + i * cellSize,
          position.x + size * cellSize,
          position.y + i * cellSize);
      g2d.drawLine(
          position.x + i * cellSize,
          position.y,
          position.x + i * cellSize,
          position.y + size * cellSize);
      g2d.drawString(
          (position.x + size * cellSize) + ", " + (position.y + i * cellSize),
          position.x + size * cellSize + 32,
          position.y + i * cellSize);
    }
  }

  /**
   * Lấy vị trí của bảng người chơi so với panel chứa nó (vị trí tướng đối)
   *
   * @return vị trí của bảng
   */
  public Point getPosition() {
    return position;
  }

  /**
   * Check xem vị trí của chuột có nằm trong bảng hay không, Vị trí của chuột phải là vị trí tương
   * đối (relative) so với panel chứa bảng
   *
   * @param position Vị trí của chuột
   * @return {@code true} nếu vị trí của chuột nằm trong bảng, ngược lại trả về {@code false}
   * @see MouseEvent#getX()
   * @see MouseEvent#getY()
   */
  public boolean isInsideBoard(Point position) {
    int maxX = this.position.x + width;
    int maxY = this.position.y + height;

    return position.x >= this.position.x
        && position.y >= this.position.y
        && position.x <= maxX
        && position.y <= maxY;
  }

  public Position getBoardRowCol(Point point) {
    return getBoardRowCol(point.x, point.y);
  }

  public Position getBoardRowCol(int x, int y) {
    int row = (x - this.position.x) / cellSize;
    int col = (y - this.position.y) / cellSize;
    if (row >= size) {
      row = size - 1;
    }
    if (col >= size) {
      col = size - 1;
    }
    if (row < 0) {
      row = 0;
    }
    if (col < 0) {
      col = 0;
    }
    return new Position(row, col);
  }

  public void setPosition(int x, int y) {
    position.setLocation(x, y);
  }

  @Override
  public boolean isInside(int x, int y) {
    return isInsideBoard(new Point(x, y));
  }

  public void addShip(Ship ship) {
    if (canAddShip(
        ship.getSprite().getX(),
        ship.getSprite().getY(),
        ship.getSprite().getWidth(),
        ship.getSprite().getHeight())) {
      ships.add(ship);
      ship.setBoard(this);
    }
  }

  public boolean canAddShip(int x, int y, int width, int height) {
    int xMax = x + width;
    int yMax = y + height;
    for (Ship s : ships) {
      Sprite sp = s.getSprite();
      if (xMax > sp.getX()
          && sp.getX() + sp.getWidth() > x
          && yMax > sp.getY()
          && y < sp.getY() + sp.getHeight()) {
        return false;
      }
    }
    return true;
  }

  public void removeShip(Ship ship) {
    ships.remove(ship);
  }

  public Ship getShip(Point point) {
    for (Ship ship : ships) {
      if (ship.isInside(point.x, point.y)) return ship;
    }

    return null;
  }

  public GamePlay getGamePlay() {
    return gamePlay;
  }

  public void setGamePlay(GamePlay gamePlay) {
    this.gamePlay = gamePlay;
  }

  public boolean canAddShip(Ship ship) {
    return canAddShip(
        ship.getSprite().getX(),
        ship.getSprite().getY(),
        ship.getSprite().getWidth(),
        ship.getSprite().getHeight());
  }
}
