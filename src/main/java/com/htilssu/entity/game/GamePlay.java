package com.htilssu.entity.game;

import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.manager.DifficultyManager;
import java.awt.*;
import java.util.List;

/** Mỗi {@link GamePlay} là 1 trận đấu giữa 2 người chơi */
public class GamePlay {
  List<Player> playerList;
  int turn;
  int difficulty;

  public GamePlay(List<Player> playerList, int turn, int difficulty) {
    this.playerList = playerList;
    this.turn = turn;
    this.difficulty = difficulty;
    initBoard();
  }

  public void renderShootBoard(Graphics g) {
    getCurrentPlayer().getBoard().render(g);
  }

  private void initBoard() {
    int boardSize = DifficultyManager.getGameBoardSize(difficulty);
    for (Player player : playerList) {
      player.setPlayerBoard(new PlayerBoard(boardSize));
      player.setShot(new byte[boardSize][boardSize]);
    }
  }

  /**
   * Lấy người chơi hiện tại
   *
   * @return người chơi hiện tại
   */
  public Player getCurrentPlayer() {
    return playerList.get(turn);
  }

  /**
   * Lấy đối thủ
   *
   * @return đối thủ
   */
  public Player getOpponent() {
    return playerList.get((turn + 1) % playerList.size());
  }

  /** Kết thúc lượt chơi của người chơi hiện tại */
  public void endTurn() {
    turn = (turn + 1) % playerList.size();
  }

  public void handleClick(Point position) {
    PlayerBoard playerBoard = getCurrentPlayer().getBoard();
    Point boardPoint = playerBoard.getPosition();

    int maxX = boardPoint.x + playerBoard.width;
    int maxY = boardPoint.y + playerBoard.height;

    if (position.x < boardPoint.x
        || position.y < boardPoint.y
        || position.x > maxX
        || position.y > maxY) return;

    // TODO: call shot method or listener

    /*  GameLogger.log(
        "HIT row: "
            + (position.x - boardPoint.x) / (int) (GameSetting.SCALE * GameSetting.TILE_SIZE)));
    GameLogger.log(
        "HIT col: "
            + (position.y - boardPoint.y) / (int) (GameSetting.SCALE * GameSetting.TILE_SIZE));*/
  }
}
