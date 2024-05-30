package com.htilssu.entity.player;

import java.util.UUID;

public class Player {
    String id = UUID.randomUUID().toString();
    String name = "DepTrai";
    PlayerBoard playerBoard;
    byte[][] shot;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }

    public Player() {}

    public byte[][] getShot() {
        return shot;
    }

    public void setShot(byte[][] shot) {
        this.shot = shot;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public PlayerBoard getBoard() {
        return playerBoard;
    }
}
