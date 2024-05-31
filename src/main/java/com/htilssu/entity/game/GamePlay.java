package com.htilssu.entity.game;

import com.htilssu.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.DifficultyManager;
import com.htilssu.manager.GameManager;
import com.htilssu.setting.GameSetting;

import java.awt.*;
import java.util.List;

/** Mỗi {@link GamePlay} là 1 trận đấu giữa 2 người chơi */
public class GamePlay {
  public static final int MODE_SETUP = 0;
  public static final int MODE_PLAY = 1;
  int gameMode = MODE_SETUP;
  List<Player> playerList;
  int turn;
  int difficulty;
  boolean isMultiPlayer = false;
  private GameManager gameManager;

  public GamePlay(List<Player> playerList, int turn, int difficulty) {
    this.playerList = playerList;
    this.turn = turn;
    this.difficulty = difficulty;
    initBoard();
  }

  public GamePlay(List<Player> playerList, int turn, int difficulty, boolean isMultiPlayer) {
    this(playerList, turn, difficulty);
    this.isMultiPlayer = isMultiPlayer;
  }

  public int getGameMode() {
    return gameMode;
  }

  public void setGameMode(int gameMode) {
    this.gameMode = gameMode;
  }

  public void renderShootBoard(Graphics g) {
    getCurrentPlayer().getBoard().render(g);
  }

  private void initBoard() {
    int boardSize = getBoardSize();
    for (Player player : playerList) {
      player.setPlayerBoard(new PlayerBoard(boardSize));
      player.setShot(new byte[boardSize][boardSize]);
      player.setGamePlay(this);
    }
  }

  private int getBoardSize() {
    return DifficultyManager.getGameBoardSize(difficulty);
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

  /**
   * Lấy vị trí của hàng và cột trên bảng mà chuột của người chơi hiện tại đang trỏ tới ví dụ: nếu
   * chuột đang trỏ tới hàng 2 cột 3 thì trả về (2, 3)
   *
   * @param position vị trí của chuột hiện tại
   * @return vị trí hàng và cột trên bảng
   */
  public void handleClick(Point position) {
    PlayerBoard board = getCurrentPlayer().getBoard();

    if (!board.isInsideBoard(position)) return;
    Position pos = board.getBoardRowCol(position);

    // Call shot listener
    getGameManager()
        .getBattleShip()
        .getListenerManager()
        .callEvent(new PlayerShootEvent(getCurrentPlayer(), getOpponent(), pos, false));
  }

  public GameManager getGameManager() {
    return gameManager;
  }

  public void setGameManager(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  public void handleMouseMoved(Point point) {
    // TODO: xử lý đặt thuyền

  }
}
