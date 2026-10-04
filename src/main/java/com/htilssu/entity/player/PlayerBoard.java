package com.htilssu.entity.player;

import com.htilssu.entity.Ship;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.util.AssetUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp chứa thông tin bảng của người chơi Bảng này lưu trữ các ô mà người chơi đã bắn và chứa các
 * tàu của người chơi
 */
public class PlayerBoard extends Collision implements Renderable {

    public static final int SHOOT_MISS = 1;
    public static final int SHOOT_HIT = 2;
    public static final int SHOOT_DESTROYED = 3;
    private final BufferedImage bg;
    private final byte[][] shotBoard;
    private final Ship[][] shipBoard;
    Player player;
    private final List<Ship> ships = new ArrayList<>();
    int size;
    int cellSize;
    private int remainingShips;
    private long stateVersion;
    private GamePlay gamePlay;

    /**
     * Khởi tạo bảng người chơi Bảng sẽ có size là size x size ô
     *
     * @param size Kích thước của bảng
     */
    public PlayerBoard(int size, Player player) {
        this.size = size;
        this.player = player;
        shotBoard = new byte[size][size];
        shipBoard = new Ship[size][size];
        update();
        this.bg = AssetUtils.getImage(AssetUtils.ASSET_BACK_SEA);
    }

    /**
     * Cập nhật kích thước của bảng người chơi bao gồm {@link PlayerBoard#cellSize} và kích thước
     * của các tàu
     */
    public void update() {
        cellSize = getHeight() / size;
        setSize(cellSize * size, cellSize * size);
        for (Ship ship : ships) {
            ship.update();
        }
    }

    /**
     * Đặt kích thước của bảng người chơi
     * mỗi khi set lại kích thước, gọi hàm {@link PlayerBoard#update()} để cập nhật lại kích
     * thước của thuyền và {@link PlayerBoard#cellSize}
     *
     * @param width  Kích thước chiều rộng
     * @param height Kích thước chiều cao
     */
    @Override
    public void setSize(int width, int height) {
        int side = Math.max(0, Math.min(width, height));
        super.setSize(side, side);
    }

    public PlayerBoard(PlayerBoard board) {
        super();
        this.size = board.size;
        this.cellSize = board.cellSize;
        this.gamePlay = board.gamePlay;
        this.player = board.player;
        this.shotBoard = new byte[size][size];
        this.shipBoard = new Ship[size][size];
        remainingShips = 0;
        for (Ship ship : board.ships) {
            addShip(new Ship(ship));
        }
        for (int i = 0; i < size; i++) {
            System.arraycopy(board.shotBoard[i], 0, shotBoard[i], 0, size);
        }
        this.stateVersion = board.stateVersion;
        this.bg = AssetUtils.getImage(AssetUtils.ASSET_BACK_SEA);
    }

    public void addShip(Ship ship) {
        if (canAddShip(ship)) {
            ships.add(ship);
            remainingShips++;
            ship.setBoard(this);
            setShipCells(ship, ship);
            stateVersion++;
        }
    }

    public boolean canAddShip(Ship ship) {
        return ship != null && ship.getPosition() != null && canAddShip(ship.getPosition().y,
                ship.getPosition().x,
                ship.getDirection(),
                ship.getShipType()
        );
    }

    public boolean canAddShip(int row, int col, int direction, int shipType) {
        if (row < 0 || col < 0 || shipType <= 0
                || (direction != Ship.HORIZONTAL && direction != Ship.VERTICAL)) {
            return false;
        }

        int lastRow = row + (direction == Ship.VERTICAL ? shipType - 1 : 0);
        int lastCol = col + (direction == Ship.HORIZONTAL ? shipType - 1 : 0);
        if (lastRow >= size || lastCol >= size) {
            return false;
        }

        for (int offset = 0; offset < shipType; offset++) {
            int targetRow = row + (direction == Ship.VERTICAL ? offset : 0);
            int targetCol = col + (direction == Ship.HORIZONTAL ? offset : 0);
            if (shipBoard[targetRow][targetCol] != null) {
                return false;
            }
        }
        return true;
    }

    private void setShipCells(Ship ship, Ship value) {
        Position position = ship.getPosition();
        for (int offset = 0; offset < ship.getShipType(); offset++) {
            int row = position.y + (ship.getDirection() == Ship.VERTICAL ? offset : 0);
            int col = position.x + (ship.getDirection() == Ship.HORIZONTAL ? offset : 0);
            shipBoard[row][col] = value;
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    @Override
    public void render(Graphics g) {
        render(g, true);
    }

    public void render(Graphics g, boolean showShips) {

        // Vẽ bảng người chơi
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.white);
        Rectangle rect = new Rectangle(getX(), getY(), getWidth(), getHeight());

        //vẽ ô
        for (int i = 0; i <= Math.pow(size, 2); i++) {
            if (i < Math.pow(size, 2)) {

                rect.setLocation(getX() + i % size * cellSize, (getY() + cellSize * (i / size)));
                rect.setSize(cellSize, cellSize);
                g2d.fill(rect);
            }
        }


        g2d.drawImage(bg, getX(), getY(), getWidth(), getHeight(), null);
        //vẽ tàu
        for (Ship ship : ships) {
            if (showShips || ship.isSunk()) {
                ship.render(g);
            }
        }

        //set màu cho ô đã bắn
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (shotBoard[i][j] != 0 && shotBoard[i][j] != SHOOT_MISS) {
                    g2d.setColor(new Color(243, 35, 35, 148));
                    int x = getX() + j * cellSize;
                    int y = getY() + i * cellSize;
                    g2d.fill(new Rectangle(x, y, cellSize, cellSize));
                }
            }
        }

        //vẽ đường kẻ
        g2d.setColor(Color.black);
        for (int i = 0; i <= size; i++) {

            g2d.drawLine(getX(), getY() + i * cellSize, getX() + getWidth(), getY() + i * cellSize);
            g2d.drawLine(getX() + i * cellSize,
                    getY(),
                    getX() + i * cellSize,
                    getY() + getHeight()
            );
        }


        //render shot mark
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                renderShot(g, i, j);
            }
        }

        g2d.dispose();

    }

    private void renderShot(Graphics g, int row, int col) {
        if (canShoot(row, col)) return;

        int x = getX() + col * cellSize;
        int y = getY() + row * cellSize;

        switch (shotBoard[row][col]) {
            case (byte) SHOOT_MISS:
                g.drawImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_MISS),
                        x,
                        y,
                        cellSize,
                        cellSize,
                        null
                );
                break;
            case (byte) SHOOT_HIT:
                g.drawImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_HIT),
                        x,
                        y,
                        cellSize,
                        cellSize,
                        null
                );
                break;
        }

    }

    public boolean canShoot(int row, int col) {
        return shotBoard[row][col] == 0;
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

    public void removeShip(Ship ship) {
        if (ships.remove(ship)) {
            setShipCells(ship, null);
            remainingShips--;
            stateVersion++;
        }
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

    public Ship getShipAtPosition(Position position) {
        if (position == null || position.x < 0 || position.x >= size
                || position.y < 0 || position.y >= size) {
            return null;
        }
        return shipBoard[position.y][position.x];
    }

    public void shoot(Position position, int status) {
        if (canShoot(position)) {
            shotBoard[position.y][position.x] = (byte) status;
            stateVersion++;
            //repaint
            gamePlay.getScreen().repaint();
        }
    }

    /**
     * Kiểm tra xem người chơi có thể bắn vào ô này không
     * vị trí sẽ là hai số nguyên {@code x}, {@code y} đại diện cho hàng và cột
     *
     * @param position vị trí cần bắn
     *
     * @return {@code true} nếu có thể bắn, {@code false} nếu không thể bắn
     */
    public boolean canShoot(Position position) {
        return canShoot(position.y, position.x);
    }

    public boolean isShipDestroyed(Ship ship) {
        Position pos = ship.getPosition();

        for (int i = 0; i < ship.getShipType(); i++) {
            switch (ship.getDirection()) {
                case Ship.HORIZONTAL -> {
                    if (shotBoard[pos.y][pos.x + i] != SHOOT_HIT) {
                        return false;
                    }
                }
                case Ship.VERTICAL -> {
                    if (shotBoard[pos.y + i][pos.x] != SHOOT_HIT) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void markShipDestroyed(Ship ship) {
        Position pos = ship.getPosition();
        remainingShips--;
        ship.setIsSunk(true);
        stateVersion++;

        for (int i = 0; i < ship.getShipType(); i++) {
            switch (ship.getDirection()) {
                case Ship.HORIZONTAL -> {
                    shotBoard[pos.y][pos.x + i] = (byte) SHOOT_DESTROYED;
                }
                case Ship.VERTICAL -> {
                    shotBoard[pos.y + i][pos.x] = (byte) SHOOT_DESTROYED;
                }
            }
        }
    }

    public boolean isAllShipsDestroyed() {
        return remainingShips == 0;
    }

    public long getStateVersion() {
        return stateVersion;
    }

    /**
     * Lấy những thuyền chưa chìm
     * chỉ trả về khi {@link GamePlay#getGameMode()}  == {@link GamePlay#END_MODE} nếu không phải
     * thì luôn trả về {@code null}
     *
     * @return danh sách thuyền
     */
    public List<Ship> getRemainingShips() {
        List<Ship> remainingShips = new ArrayList<>();
        if (gamePlay.getGameMode() == GamePlay.END_MODE) {
            for (Ship ship : ships) {
                if (!ship.isSunk()) {
                    remainingShips.add(ship);
                }
            }
        }

        return remainingShips;
    }
}


