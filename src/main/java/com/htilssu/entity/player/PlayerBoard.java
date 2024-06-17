package com.htilssu.entity.player;

import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Collision implements Renderable {

    public static final int SHOOT_MISS = 1;
    public static final int SHOOT_HIT = 2;
    private final BufferedImage bg;
    private final byte[][] shotBoard;
    Player player;
    List<Ship> ships = new ArrayList<>();
    int size;
    int cellSize;
    private GamePlay gamePlay;

    /**
     * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
     *
     * @param size Kích thước của bảng
     */
    public PlayerBoard(int size, Player player) {
        this.size = size;
        shotBoard = new byte[size][size];
        update();
        this.bg = AssetUtils.getImage(AssetUtils.ASSET_BACK_SEA);
    }

    public void shoot(Position position, int status) {
        if (canShoot(position)) {
            shotBoard[position.x][position.y] = (byte) status;
        }
    }

    /**
     * Kiểm tra xem người chơi có thể bắn vào ô này không
     * vị trí sẽ là hai số nguyên {@code x}, {@code y} đại diện cho hàng và cột
     *
     * @param position vị trí cần bắn
     * @return {@code true} nếu có thể bắn, {@code false} nếu không thể bắn
     */
    public boolean canShoot(Position position) {
        return canShoot(position.x, position.y);
    }

    public int getCellSize() {
        return cellSize;
    }

    /**
     * Cập nhật kích thước của bảng người chơi
     */
    public void update() {
        cellSize = getHeight() / size;
        setSize(cellSize*size, cellSize*size);
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

        //vẽ ô
        for (int i = 0; i <= Math.pow(size, 2); i++) {
            if (i < Math.pow(size, 2)) {

                //vẽ ô đã bắn
                renderShot(g, i % size, i / size);

                rect.setLocation(getX() + i % size * cellSize, (getY() + cellSize * (i / size)));
                rect.setSize(cellSize, cellSize);
                g2d.fill(rect);
            }
        }


        g2d.drawImage(bg, getX(), getY(), getWidth(), getHeight(), null);
        //vẽ tàu
        for (Ship ship : ships) {
            ship.render(g);
        }

        //vẽ đường kẻ
        g2d.setColor(Color.black);
        for (int i = 0; i < size; i++) {

            g2d.drawLine(getX(), getY() + i * cellSize, getX() + getWidth() , getY() + i * cellSize);
            g2d.drawLine(getX() + i * cellSize, getY(), getX() + i * cellSize, getY() + getHeight());
        }

    }

    private void renderShot(Graphics g, int row, int col) {
        if (canShoot(row, col)) return;

        int x = getX() + row * cellSize;
        int y = getY() + col * cellSize;

        switch (shotBoard[row][col]) {
            case (byte) SHOOT_MISS:
                g.drawImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_MISS), x, y, cellSize, cellSize, null);
                break;
            case (byte) SHOOT_HIT:
                g.drawImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_HIT), x, y, cellSize, cellSize, null);
                break;
        }

    }

    public boolean canShoot(int row, int col) {
        return shotBoard[row][col] != 0;
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
        if (canAddShip(ship.getSprite().getX(), ship.getSprite().getY(), ship.getSprite().getWidth(), ship.getSprite().getHeight())) {
            ships.add(ship);
            ship.setBoard(this);
        }
    }

    public boolean canAddShip(int x, int y, int width, int height) {
        int xMax = x + width;
        int yMax = y + height;
        for (Ship s : ships) {
            Sprite sp = s.getSprite();
            if (xMax > sp.getX() && sp.getX() + sp.getWidth() > x && yMax > sp.getY() && y < sp.getY() + sp.getHeight()) {
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
        return canAddShip(ship.getSprite().getX(), ship.getSprite().getY(), ship.getSprite().getWidth(), ship.getSprite().getHeight());
    }

    public byte shoot(Position position) {

        for (Ship ship : ships) {
            Position pos = ship.getPosition();
            switch (ship.getDirection()) {
                case Ship.HORIZONTAL -> {
                    if (position.x == pos.x && position.y >= pos.y && position.y < pos.y + ship.getShipType()) {
                        shoot(position, SHOOT_HIT);
                        return SHOOT_HIT;
                    } else {
                        shoot(position, SHOOT_MISS);
                        return SHOOT_MISS;

                    }
                }
                case Ship.VERTICAL -> {
                    if (position.y == pos.y && position.x >= pos.x && position.x < pos.x + ship.getShipType()) {
                        shoot(position, SHOOT_HIT);
                        return SHOOT_HIT;
                    } else {
                        shoot(position, SHOOT_MISS);
                        return SHOOT_MISS;
                    }
                }
            }
        }
        return SHOOT_MISS;
    }
}


