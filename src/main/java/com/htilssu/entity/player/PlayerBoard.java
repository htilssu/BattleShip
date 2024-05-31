package com.htilssu.entity.player;

import com.htilssu.component.Position;
import com.htilssu.entity.Ship;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Dimension implements Renderable {

  int size;

  int cellSize;
  Point position = new Point(64, 64);
  BufferedImage setUpShip = AssetUtils.getAsset(AssetUtils.ASSET_SHIP_2);

  {
    setUpShip = AssetUtils.rotateImage(setUpShip, Math.toRadians(90));
  }

  /**
   * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
   *
   * @param size Kích thước của bảng
   */
  public PlayerBoard(int size) {
    super(0, 0);
    this.size = size;
    cellSize = (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE + 16);
    update();
  }

  public void setSetUpShip(BufferedImage setUpShip) {
    this.setUpShip = setUpShip;
  }

  /** Cập nhật kích thước của bảng người chơi */
  public void update() {
    cellSize = (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE + 16);
    setSize(cellSize * size, cellSize * size);
  }

  @Override
  public void render(@NotNull Graphics g) {
    BufferedImage asset_shoot_miss = AssetUtils.getAsset(AssetUtils.ASSET_SHOOT_HIT);

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

        if (setUpShip != null) {
          int size = setUpShip.getWidth() / 64;
          if (i < size) {
            g2d.drawImage(
                setUpShip.getSubimage(64 * i, 0, 64, 64),
                cellSize * i + position.x,
                position.y,
                cellSize,
                cellSize,
                null);
          }
        }

        //        g2d.drawImage(
        //            asset_shoot_miss,
        //            position.x + i % size * cellSize,
        //            position.y + cellSize * (i / size),
        //            cellSize,
        //            cellSize,
        //            null);
      }
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
    int maxX = position.x + width;
    int maxY = position.y + height;

    return position.x >= this.position.x
        && position.y >= this.position.y
        && position.x <= maxX
        && position.y <= maxY;
  }

  private void renderShip(Graphics g, Ship ship) {
    int direction = Ship.HORIZONTAL;
  }

  public Position getBoardRowCol(Point point) {
    int row = (point.x - this.position.x) / cellSize;
    int col = (point.y - this.position.y) / cellSize;
    row = row >= size ? size - 1 : row;
    col = col >= size ? size - 1 : col;
    return new Position(row, col);
  }
}
