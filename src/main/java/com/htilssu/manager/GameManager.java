package com.htilssu.manager;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class GameManager {

  public static final int HIT = 2;
  public static final int MISS = 1;
  public static final int UNKNOWN = 0;

  public static final int MAX_PLAYER = 2;
  public static final int MIN_PLAYER = 1;
  public static final Player gamePlayer = new Player();
  BattleShip battleShip;
  List<Player> players;
  GamePlay currentGamePlay;
  int turn = 0;
  private boolean multiPlayer;

  {
    initPlayerList();
  }

  public GameManager(BattleShip battleShip) {
    this.battleShip = battleShip;
  }

  public BattleShip getBattleShip() {
    return battleShip;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public void addPlayer(Player player) {
    if (players.size() < MAX_PLAYER) {
      players.add(player);
    }
  }

  public GamePlay createNewGamePlay() {
    if (players.size() < MIN_PLAYER) {
      return null;
    }

    GamePlay newGamePlay = new GamePlay(players, turn, DifficultyManager.DIFFICULTY, multiPlayer);
    newGamePlay.setGameManager(this);

    initPlayerList();
    setCurrentGamePlay(newGamePlay);
    return currentGamePlay;
  }

  private void initPlayerList() {
    players = new ArrayList<>();
    players.add(gamePlayer);
  }

  public GamePlay getCurrentGamePlay() {
    return currentGamePlay;
  }

  private void setCurrentGamePlay(GamePlay currentGamePlay) {
    this.currentGamePlay = currentGamePlay;
    this.currentGamePlay.setGameManager(this);
  }

  public void createTestGamePlay() {
    List<Player> testPlayers = new ArrayList<>();
    testPlayers.add(GameManager.gamePlayer);
    testPlayers.add(new Player());
    setCurrentGamePlay(new GamePlay(testPlayers, 0, DifficultyManager.DIFFICULTY));
  }

  public void setMultiPlayer(boolean multiPlayer) {
    this.multiPlayer = multiPlayer;
  }
}
