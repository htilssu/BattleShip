package com.htilssu.entity.player;

import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Collision implements Renderable {

  List<Ship> ships = new ArrayList<>();

  int size;
  int cellSize;
  private GamePlay gamePlay;

  /**
   * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
   *
   * @param size Kích thước của bảng
   */
  public PlayerBoard(int size) {
    this.size = size;
    update();
  }

  public int getCellSize() {
    return cellSize;
  }

  /** Cập nhật kích thước của bảng người chơi */
  public void update() {
    cellSize = (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE + 16);
    setSize(cellSize * size, cellSize * size);
    for (Ship ship : ships) {
      ship.update();
    }
  }

  @Override
  public void render(@NotNull Graphics g) {

    // Vẽ bảng người chơi
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    for (int i = 0; i <= Math.pow(size, 2); i++) {
      g2d.setColor(Color.white);
      if (i < Math.pow(size, 2)) {
        g2d.fill(
            new Rectangle(
                getX() + i % size * cellSize,
                (getY() + cellSize * (i / size)),
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

    AlphaComposite aC = AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 0.7f);
    BufferedImage bg = AssetUtils.getAsset(AssetUtils.ASSET_BACK_SEA);
    BufferedImage opaBg =
        new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB);

    float[] a = new float[3 * 3];
    Arrays.fill(a, 0.1111f);
    Kernel kernel = new Kernel(3, 3, a);

    ConvolveOp cOP = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

    Graphics2D g2dd = opaBg.createGraphics();

    g2dd.setComposite(aC);
    g2dd.drawImage(bg, 0, 0, opaBg.getWidth(), opaBg.getHeight(), null);
    g2dd.dispose();
    opaBg = cOP.filter(opaBg, null);

    g2d.drawImage(opaBg, getX(), getY(), getWidth(), getHeight(), null);

    for (Ship ship : ships) {
      ship.render(g);
    }

    for (int i = 0; i < size; i++) {
      g2d.setColor(Color.black);

      g2d.drawLine(getX(), getY() + i * cellSize, getX() + size * cellSize, getY() + i * cellSize);
      g2d.drawLine(getX() + i * cellSize, getY(), getX() + i * cellSize, getY() + size * cellSize);
    }
  }

  /**
   * Lấy vị trí của bảng người chơi so với panel chứa nó (vị trí tướng đối)
   *
   * @return vị trí của bảng
   */
  public Position getBoardRowCol(Point point) {
    return getBoardRowCol(point.x, point.y);
  }

  public Position getBoardRowCol(int x, int y) {
    int row = (x - getX()) / cellSize;
    int col = (y - getY()) / cellSize;
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
