package com.htilssu.entity.game;

import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.GameManager;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

/** Mỗi {@link GamePlay} là 1 trận đấu giữa 2 người chơi */
public class GamePlay implements Renderable {
  public static final int WAITING_MODE = 0;
  public static final int PLAY_MODE = 1;
  private final int margin = 10;
  boolean isReady = false;
  int gameMode = WAITING_MODE;
  Map<Integer, Integer> shipInBoard = new HashMap<>();
  List<Sprite> sprites = new ArrayList<>();
  List<Player> playerList;
  Map<Player, Pair<PlayerBoard, byte[][]>> playerData = new HashMap<>();
  int turn;
  int size;
  int direction = Ship.HORIZONTAL;
  Sprite setUpSprite;
  boolean isMultiPlayer = false;
  Sprite readyButton = new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_READY_BUTTON));
  private GameManager gameManager;

  {
    sprites.add(new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_SHIP_2)));
    sprites.add(new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_SHIP_3)));
    sprites.add(new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_SHIP_4)));
    sprites.add(new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_SHIP_5)));

    shipInBoard.put(Ship.SHIP_2, 1);
    shipInBoard.put(Ship.SHIP_3, 2);
    shipInBoard.put(Ship.SHIP_4, 1);
    shipInBoard.put(Ship.SHIP_5, 1);
  }

  public GamePlay(List<Player> playerList, int turn, int size) {
    this.playerList = playerList;
    this.turn = turn;
    this.size = size;
    PlayerBoard playerBoard = new PlayerBoard(size);
    playerBoard.setGamePlay(this);
    playerData.put(
        GameManager.gamePlayer, new Pair<>(playerBoard, new byte[getBoardSize()][getBoardSize()]));
    initBoard();
  }

  public GamePlay(List<Player> playerList, int turn, int size, boolean isMultiPlayer) {
    this(playerList, turn, size);
    this.isMultiPlayer = isMultiPlayer;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    if (this.direction == direction || gameMode != WAITING_MODE) return;

    this.direction = direction;

    if (setUpSprite != null) {
      PlayerBoard playerBoard = playerData.get(GameManager.gamePlayer).getFirst();
      if (direction == Ship.VERTICAL) {
        setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
      } else {
        setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
        setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
        setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
      }

      float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
      if (ratio < 1 && ratio > 0) {
        ratio = 1 / ratio;
        setUpSprite.setSize((int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

      } else {
        setUpSprite.setSize(playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
      }
    }
  }

  public void renderShootBoard(Graphics g) {
    playerData.get(getCurrentPlayer()).getFirst().render(g);
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
    return size;
  }

  public void update() {

    switch (gameMode) {
      case WAITING_MODE -> {
        PlayerBoard playerBoard = playerData.get(GameManager.gamePlayer).getFirst();
        int margin = this.margin * Math.round(GameSetting.SCALE);

        JPanel currentScreen = gameManager.getBattleShip().getScreenManager().getCurrentScreen();
        int yMidPosition = currentScreen.getHeight() / 2;
        int xMidPosition = currentScreen.getWidth() / 2;

        // set ready button size
        readyButton.setSize(
            (int) (readyButton.getAsset().getWidth() / 2f * GameSetting.SCALE * 0.8f),
            (int) (readyButton.getAsset().getHeight() / 2f * GameSetting.SCALE * 0.8f));
        readyButton.setLocation(
            xMidPosition - readyButton.getWidth() / 2,
            currentScreen.getHeight() - readyButton.getHeight() - margin);
        //        playerBoard.update();
        playerBoard.setLocation(
            xMidPosition - 100,
            (int) (yMidPosition - playerBoard.getHeight() / 2f)
                - readyButton.getHeight() / 2
                - margin / 2);
        if (setUpSprite != null) {
          float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
          if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
            setUpSprite.setSize(
                (int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

          } else {
            setUpSprite.setSize(
                playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
          }
        }

        for (int i = 0; i < sprites.size(); i++) {
          Sprite sprite = sprites.get(i);

          sprite.setLocation(
              (int) (i * (sprite.getWidth() + margin) + 32 * GameSetting.SCALE),
              yMidPosition - sprite.getHeight() / 2);
        }
      }
    }

    playerData.forEach(
        (player, playerBoardPair) -> {
          playerBoardPair.getFirst().update();
        });
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
   */
  public void handleClick(Point position) {
    switch (gameMode) {
      case PLAY_MODE -> {
        PlayerBoard board = getCurrentPlayer().getBoard();

        if (!board.isInside(position)) return;
        Position pos = board.getBoardRowCol(position);

        GameLogger.log("Player " + getCurrentPlayer().getName() + " shoot at " + pos);

        // Call shot listener
        getGameManager()
            .getBattleShip()
            .getListenerManager()
            .callEvent(new PlayerShootEvent(getCurrentPlayer(), getOpponent(), pos, false));
      }
      case WAITING_MODE -> {
        PlayerBoard playerBoard = playerData.get(GameManager.gamePlayer).getFirst();

        // handle click on ship
        if (!playerBoard.isInside(position)) {
          for (Sprite sprite : sprites) {
            if (sprite.isInside(position.x, position.y)) {
              setUpSprite = sprite;
              int ratio = setUpSprite.getHeight() / setUpSprite.getWidth();
              setUpSprite.setSize(playerBoard.getCellSize(), playerBoard.getCellSize() * ratio);
              setUpSprite.setLocation(playerBoard.getX(), playerBoard.getY());
              sprites.remove(setUpSprite);
              return;
            }
          }
        } else {
          // handle put ship into board
          if (setUpSprite == null) {
            Ship clickShip = playerBoard.getShip(position);
            if (clickShip != null) {

              playerBoard.removeShip(clickShip);
              setUpSprite = clickShip.getSprite();
              int count = shipInBoard.get(clickShip.getShipType());
              shipInBoard.replace(clickShip.getShipType(), ++count);
              return;
            }

            return;
          }

          Position mousePos =
              playerBoard.getBoardRowCol(setUpSprite.getX() + 1, setUpSprite.getY() + 1);
          float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
          if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
          }
          Ship ship = new Ship(direction, new Sprite(setUpSprite), mousePos, (int) ratio);
          if (!playerBoard.canAddShip(ship)) return;
          int count = shipInBoard.get((int) ratio);
          if (count == 0) return;

          playerBoard.addShip(ship);
          count--;
          shipInBoard.replace((int) ratio, count);
          if (count == 0) {
            setUpSprite = null;
          }
        }
      }
    }
  }

  public GameManager getGameManager() {
    return gameManager;
  }

  public void setGameManager(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  public void handleMouseMoved(Point point) {
    switch (gameMode) {
      case WAITING_MODE -> {
        if (setUpSprite != null) {
          PlayerBoard playerBoard = playerData.get(GameManager.gamePlayer).getFirst();
          Position pos = playerBoard.getBoardRowCol(point);
          Point posB = playerBoard.getLocation();

          int x = pos.x * playerBoard.getCellSize() + posB.x,
              y = pos.y * playerBoard.getCellSize() + posB.y;
          if (x + setUpSprite.getWidth() > posB.x + playerBoard.getWidth()) {
            x = posB.x + playerBoard.getWidth() - setUpSprite.getWidth();
          }
          if (y + setUpSprite.getHeight() > posB.y + playerBoard.getHeight()) {
            y = posB.y + playerBoard.getHeight() - setUpSprite.getHeight();
          }

          setUpSprite.setLocation(x, y);
        }
      }
      case PLAY_MODE -> {
        // TODO: mouse move in play mode
      }
    }
  }

  @Override
  public void render(@NotNull Graphics g) {

    if (gameMode == WAITING_MODE) {
      renderWaitingMode(g);
      if (setUpSprite != null) setUpSprite.render(g);
    } else if (gameMode == PLAY_MODE) {
      renderPlayMode(g);
    }
  }

  private void renderPlayMode(Graphics g) {
    // TODO render play mode
  }

  private void renderWaitingMode(Graphics g) {

    PlayerBoard playerBoard = playerData.get(GameManager.gamePlayer).getFirst();

    for (Sprite sprite : sprites) {
      sprite.render(g);
    }
    // render ready button

    readyButton.render(g);

    playerBoard.render(g);
  }

  public int getGameMode() {
    return gameMode;
  }

  public void setGameMode(int gameMode) {
    this.gameMode = gameMode;
  }

  private void ready() {
    isReady = true;
    readyButton = new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_UNREADY_BUTTON));
  }

  private void unReady() {
    isReady = false;
    readyButton = new Sprite(AssetUtils.getAsset(AssetUtils.ASSET_READY_BUTTON));
  }
}
