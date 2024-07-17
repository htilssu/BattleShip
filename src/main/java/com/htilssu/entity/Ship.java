package com.htilssu.entity;

import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

// Lớp Ship đại diện cho một con TÀU
public class Ship extends Collision implements Renderable {

    public static final int SHIP_2 = 2;
    public static final int SHIP_3 = 3;
    public static final int SHIP_4 = 4;
    public static final int SHIP_5 = 5;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /**
     * <pre>{@code
     * 0 - Ngang
     * 1 - Dọc
     * }</pre>
     */
    int direction;

    Sprite sprite;
    Position position;
    PlayerBoard playerBoard;
    int shipType;
    private boolean isSunk = false;

    public Ship(
            int direction, Sprite sprite, Position position, int shipType, PlayerBoard playerBoard) {
        this(direction, sprite, position, shipType);
        this.playerBoard = playerBoard;
    }

    public Ship(int direction, Sprite sprite, Position position, int shipType) {
        this.direction = direction;
        this.position = position;
        this.shipType = shipType;
        this.sprite = sprite;
    }

    public Ship(int direction, Sprite sprite, int shipType) {
        super();
        this.direction = direction;
        this.shipType = shipType;
        this.sprite = sprite;
    }

    public Ship(Ship ship) {
        super();
        this.direction = ship.direction;
        this.sprite = new Sprite(ship.sprite);
        this.position = new Position(ship.position.x, ship.position.y);
        this.shipType = ship.shipType;
        this.playerBoard = ship.playerBoard;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        checkDirection();
    }

    private void checkDirection() {
        float ratio = (float) sprite.getHeight() / sprite.getWidth(); // < 1 là ngang, > 1 là doc

        switch (direction) {
            case HORIZONTAL -> {
                if (ratio > 1) {
                    sprite.rotate();
                }
            }
            case VERTICAL -> {
                if (ratio < 1) {
                    sprite.rotate();
                }
            }
        }

    }

    public Sprite getSprite() {
        return sprite;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        update();
    }

    /**
     * Cập nhật lại kích thước, vị trí của tàu dựa theo {@link Ship#playerBoard}
     */
    public void update() {
        float ratio = (float) sprite.getHeight() / sprite.getWidth();
        sprite.setLocation(
                playerBoard.getX() + playerBoard.getCellSize() * position.x,
                playerBoard.getY() + playerBoard.getCellSize() * position.y);

        if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
            sprite.setSize((int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

        }
        else {
            sprite.setSize(playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
        }
    }

    public void setIsSunk(boolean b) {
        this.isSunk = b;
    }

    public boolean isSunk() {
        return isSunk;
    }
}
