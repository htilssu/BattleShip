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
import java.awt.geom.Rectangle2D;
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

    private final BufferedImage bg;
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
        this.bg = AssetUtils.getAsset(AssetUtils.ASSET_BACK_SEA);
    }

    public int getCellSize() {
        return cellSize;
    }

    /**
     * Cập nhật kích thước của bảng người chơi
     */
    public void update() {
        cellSize = getHeight() / size;
        for (Ship ship : ships) {
            ship.update();
        }
    }

    @Override
    public void setSize(int width, int height) {
        //noinspection SuspiciousNameCombination
        super.setSize(height, height);
    }

    @Override
    public void render(@NotNull Graphics g) {

        // Vẽ bảng người chơi
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        Rectangle rect = new Rectangle(getX(), getY(), getWidth(), getHeight());

        for (int i = 0; i <= Math.pow(size, 2); i++) {
            if (i < Math.pow(size, 2)) {
                rect.setLocation(getX() + i % size * cellSize,
                        (getY() + cellSize * (i / size)));
                rect.setSize(cellSize, cellSize);
                g2d.fill(rect);

                
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


        g2d.drawImage(bg, getX(), getY(), getWidth(), getHeight(), null);

        for (Ship ship : ships) {
            ship.render(g);
        }

        g2d.setColor(Color.black);
        for (int i = 0; i < size; i++) {

            g2d.drawLine(getX(), getY() + i * cellSize, getX() + getWidth(), getY() + i * cellSize);
            g2d.drawLine(getX() + i * cellSize, getY(), getX() + i * cellSize, getY() + getHeight());
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
