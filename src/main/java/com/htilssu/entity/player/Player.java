package com.htilssu.entity.player;

import com.htilssu.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.manager.GameManager;
import com.htilssu.util.GameLogger;

import java.util.UUID;

public class Player {
  String id = UUID.randomUUID().toString();
  String name = "DepTrai";
  PlayerBoard playerBoard;
  byte[][] shot;
  private GamePlay gamePlay;

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

  public GamePlay getGamePlay() {
    return gamePlay;
  }

  public void setGamePlay(GamePlay gamePlay) {
    this.gamePlay = gamePlay;
  }

  public boolean canShoot(Position position) {
    return shot[position.x][position.y] != GameManager.UNKNOWN;
  }

  public void shoot(Position position, int status) {
    shot[position.x][position.y] = (byte) status;
  }
}
