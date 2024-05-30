package com.htilssu.entity.player;

import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import java.awt.*;
import org.jetbrains.annotations.NotNull;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Dimension implements Renderable {

  int size;
  Point position = new Point(64, 64);

  /**
   * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
   *
   * @param size Kích thước của bảng
   */
  public PlayerBoard(int size) {
    super(
        Math.round(size * GameSetting.TILE_SIZE * GameSetting.SCALE),
        Math.round(size * GameSetting.TILE_SIZE * GameSetting.SCALE));
    this.size = size;
  }

  /** Cập nhật kích thước của bảng người chơi */
  public void update() {
    setSize(
        Math.round(size * GameSetting.TILE_SIZE * GameSetting.SCALE),
        Math.round(size * GameSetting.TILE_SIZE * GameSetting.SCALE));
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
                (int) Math.ceil(position.x + i % size * GameSetting.TILE_SIZE * GameSetting.SCALE),
                (int) (position.y + GameSetting.TILE_SIZE * GameSetting.SCALE * (i / size)),
                (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE),
                (int) Math.ceil(GameSetting.TILE_SIZE * GameSetting.SCALE)));
      }
    }

    for (int i = 0; i <= size; i++) {
      g2d.setColor(Color.black);
      g2d.drawLine(
          position.x,
          (int) Math.ceil(position.y + i * GameSetting.TILE_SIZE * GameSetting.SCALE),
          (int) Math.ceil(position.x + size * GameSetting.TILE_SIZE * GameSetting.SCALE),
          (int) Math.ceil(position.y + i * GameSetting.TILE_SIZE * GameSetting.SCALE));
      g2d.drawLine(
          (int) Math.ceil(position.x + i * GameSetting.TILE_SIZE * GameSetting.SCALE),
          position.y,
          (int) Math.ceil(position.x + i * GameSetting.TILE_SIZE * GameSetting.SCALE),
          (int) Math.ceil(position.y + size * GameSetting.TILE_SIZE * GameSetting.SCALE));
    }
  }

  /**
   * Lấy vị trí của bảng người chơi so với panel chứa nó
   *
   * @return vị trí của bảng người chơi
   */
  public Point getPosition() {
    return position;
  }
}
