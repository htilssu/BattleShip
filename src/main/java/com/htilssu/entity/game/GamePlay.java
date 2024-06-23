package com.htilssu.entity.game;

import com.htilssu.BattleShip;
import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.htilssu.entity.Ship.VERTICAL;

/**
 * Mỗi {@link GamePlay} là 1 trận đấu giữa 2 người chơi
 */
public class GamePlay implements Renderable {

    public static final int WAITING_MODE = 0;
    public static final int PLAY_MODE = 1;
    public static final Map<Integer, Sprite> sprites = new HashMap<>();
    private static final int MARGIN = 30;

    static {
        sprites.put(Ship.SHIP_2, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_2)));
        sprites.put(Ship.SHIP_3, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_3)));
        sprites.put(Ship.SHIP_4, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_4)));
        sprites.put(Ship.SHIP_5, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_5)));

    }

    private final Map<Integer, Integer> shipInBoard = new HashMap<>();
    private final List<Player> playerList;
    private final int size;
    private final Sprite selectSprite;
    private final Sprite readyButton = new Sprite(AssetUtils.getImage(AssetUtils.ASSET_READY_BUTTON));
    private boolean isReady = false;
    private int gameMode = WAITING_MODE;
    private int turn;
    private int direction = VERTICAL;
    private Sprite setUpSprite;
    private boolean isMultiPlayer = false;
    private GameManager gameManager;
    private boolean isSelectSpriteInBoard;
    private BattleShip battleShip;

    {
        shipInBoard.put(Ship.SHIP_2, 1);
        shipInBoard.put(Ship.SHIP_3, 2);
        shipInBoard.put(Ship.SHIP_4, 1);
        shipInBoard.put(Ship.SHIP_5, 1);
    }

    public GamePlay(List<Player> playerList, int turn, int size, boolean isMultiPlayer) {
        this(playerList, turn, size);
        this.isMultiPlayer = isMultiPlayer;
    }

    public GamePlay(List<Player> playerList, int turn, int size) {
        this.playerList = playerList;
        this.turn = turn;
        this.size = size;
        selectSprite = new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SELECT));

        initBoard();
    }

    private void initBoard() {
        int boardSize = getBoardSize();

        for (Player player : playerList) {
            player.setPlayerBoard(new PlayerBoard(boardSize, player));
            player.setShot(new byte[boardSize][boardSize]);
            player.setGamePlay(this);
        }
    }

    private int getBoardSize() {
        return size;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if (this.direction == direction || gameMode != WAITING_MODE) return;
        this.direction = direction;


        if (setUpSprite != null) {
            PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();
            if (direction == VERTICAL) {
                setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
            }
            else {
                setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
                setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
                setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));
            }

            float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
            if (ratio < 1 && ratio > 0) {
                ratio = 1 / ratio;
                setUpSprite.setSize((int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

            }
            else {
                setUpSprite.setSize(playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
            }
        }
    }

    public void handleClick(Point position) {
        switch (gameMode) {
            case PLAY_MODE -> {
                //check if it's not current player turn
                if (!getCurrentPlayer().getId().equals(GameManager.gamePlayer.getId())) return;
                PlayerBoard board = getOpponent().getBoard();

                if (!board.isInside(position)) return;
                Position pos = board.getBoardRowCol(position);

                GameLogger.log("Player " + getCurrentPlayer().getName() + " shoot at " + pos);

                shoot(pos);
            }
            case WAITING_MODE -> {
                PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();

                handleReadyButtonOnClick(position);

                // handle click on ship
                if (!playerBoard.isInside(position)) {
                    handleGetShipSpriteClick(position, playerBoard);
                }
                else {
                    // handle change ship location
                    if (setUpSprite == null) {
                        handleShipOnBoardClick(position, playerBoard);
                        return;
                    }

                    // handle add ship to board
                    handleAddShipToBoard(playerBoard);
                }
            }
        }
    }

    /**
     * Lấy người chơi hiện tại đang tới lượt chơi
     *
     * @return người chơi hiện tại
     */
    public Player getCurrentPlayer() {
        return playerList.get(turn);
    }

    /**
     * Lấy đối thủ của người chơi hiện tại
     * phương thức sẽ trả về 1 đối tượng {@link Player} là đối thủ của người chơi hiện tại
     * trong turn bắn hiện tại
     *
     * @return {@link Player} là đối thủ của người chơi hiện tại
     */
    public Player getOpponent() {
        return playerList.get((turn + 1) % playerList.size());
    }

    private void shoot(Position pos) {

        if (battleShip.getHost().isConnected()) {
            battleShip.getHost().sendShoot(pos);
        }
        else {
            battleShip.getClient().sendShoot(pos);
        }

        battleShip.getListenerManager()
                .callEvent(new PlayerShootEvent(getCurrentPlayer(), getOpponent().getBoard(), pos), gameManager);
    }

    private void handleReadyButtonOnClick(Point position) {
        if (readyButton.isInside(position)) {
            if (isReady) {
                unReady();
            }
            else if (isOutOfShip()) {
                ready();
            }
        }
    }

    private void handleGetShipSpriteClick(Point position, PlayerBoard playerBoard) {
        Collection<Sprite> spriteValues = sprites.values();
        for (Sprite sprite : spriteValues) {
            if (sprite.isInside(position.x, position.y)) {
                setUpSprite = sprite;
                int ratio = setUpSprite.getHeight() / setUpSprite.getWidth();
                setUpSprite.setSize(playerBoard.getCellSize(), playerBoard.getCellSize() * ratio);
                setUpSprite.setLocation(playerBoard.getX(), playerBoard.getY());
                return;
            }
        }
    }

    private void handleShipOnBoardClick(Point position, PlayerBoard playerBoard) {
        Ship clickShip = playerBoard.getShip(position);
        if (clickShip != null) {

            playerBoard.removeShip(clickShip);
            setUpSprite = clickShip.getSprite();
            int count = shipInBoard.get(clickShip.getShipType());
            shipInBoard.replace(clickShip.getShipType(), ++count);
        }
    }

    //Dat thuyen thanh cong ?
    private void handleAddShipToBoard(PlayerBoard playerBoard) {
        Position mousePos = playerBoard.getBoardRowCol(setUpSprite.getX() + 1, setUpSprite.getY() + 1);
        float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
        if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
        }
        Ship ship = new Ship(direction, new Sprite(setUpSprite), mousePos, (int) ratio);
        if (!playerBoard.canAddShip(ship)) {
            SoundManager.playSound(SoundManager.ERROR_SOUND);
            return;
        }
        int count = shipInBoard.get((int) ratio);
        if (count == 0) return;
        SoundManager.playSound(SoundManager.PUT_SHIP_SOUND);
        playerBoard.addShip(ship);
        count--;
        shipInBoard.replace((int) ratio, count);
        if (count == 0) {
            direction = VERTICAL;
            setUpSprite = null;
        }
    }

    private void unReady() {
        isReady = false;
        readyButton.setAsset(AssetUtils.getImage(AssetUtils.ASSET_READY_BUTTON), null);
        Client.getInstance().send(GameAction.UNREADY);
    }

    /**
     * Kiểm tra xem người chơi đã đặt hết tàu lên bảng chưa
     *
     * @return {@code true} nếu đã đặt hết tàu, ngược lại {@code false}
     */
    private boolean isOutOfShip() {
        AtomicInteger count = new AtomicInteger();

        shipInBoard.forEach((integer, integer2) -> count.addAndGet(integer2));

        return count.get() == 0;
    }

    /**
     * Toggle trạng thái ready
     * nếu trạng thái hiện tại là {@code ready} thì chuyển thành {@code unready} và ngược lại
     */
    private void ready() {
        isReady = true;
        readyButton.setAsset(AssetUtils.getImage(AssetUtils.ASSET_UNREADY_BUTTON), null);
        if (Host.getInstance().isConnected()) {
            Host.getInstance().ready();
        }
        else {
            Client.getInstance().send(GameAction.READY);
        }
    }

    /**
     * Kết thúc lượt chơi của người chơi hiện tại
     */
    public void endTurn() {
        turn = (turn + 1) % playerList.size();
    }

    /**
     * Lấy {@link GameManager} của game
     *
     * @return {@link GameManager} của game
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Set {@link GameManager} cho game
     *
     * @param gameManager {@link GameManager} của game
     */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        battleShip = gameManager.getBattleShip();
    }

    public void handleMouseMoved(Point point) {
        switch (gameMode) {
            case WAITING_MODE -> {
                handleReadyButtonOnHover(point);
                if (setUpSprite == null) return;
                PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();
                Position pos = playerBoard.getBoardRowCol(point);
                Point posB = playerBoard.getLocation();

                int x = pos.x * playerBoard.getCellSize() + posB.x;
                int y = pos.y * playerBoard.getCellSize() + posB.y;
                if (x + setUpSprite.getWidth() > posB.x + playerBoard.getWidth()) {
                    x = posB.x + playerBoard.getWidth() - setUpSprite.getWidth();
                }
                if (y + setUpSprite.getHeight() > posB.y + playerBoard.getHeight()) {
                    y = posB.y + playerBoard.getHeight() - setUpSprite.getHeight();
                }

                setUpSprite.setLocation(x, y);
            }
            case PLAY_MODE -> {
                PlayerBoard playerBoard = getCurrentPlayer().getBoard();
                if (playerBoard.isInside(point)) {
                    var mouseLocation = playerBoard.getBoardRowCol(point);
                    selectSprite.setSize(playerBoard.getCellSize(), playerBoard.getCellSize());
                    selectSprite.setLocation(playerBoard.getX() + mouseLocation.x * playerBoard.getCellSize(),
                                             playerBoard.getY() + mouseLocation.y * playerBoard.getCellSize());

                    isSelectSpriteInBoard = true;
                }
                else isSelectSpriteInBoard = false;
            }
        }
    }

    private void handleReadyButtonOnHover(Point point) {
        readyButton.handleHover(point.x, point.y);
    }

    @Override
    public void render(@NotNull Graphics g) {

        if (gameMode == WAITING_MODE) {
            renderWaitingMode(g);
            if (setUpSprite != null) setUpSprite.render(g);
        }
        else if (gameMode == PLAY_MODE) {
            renderPlayMode(g);
        }
    }

    private void renderWaitingMode(Graphics g) {

        PlayerBoard playerBoard = playerList.getFirst().getBoard();


        for (Map.Entry<Integer, Sprite> entry : sprites.entrySet()) {
            if (shipInBoard.get(entry.getKey()) != 0) {
                entry.getValue().render(g);
            }
        }
        // render ready button

        readyButton.render(g);

        playerBoard.render(g);
    }

    private void renderPlayMode(Graphics g) {
        renderShootBoard(g);

        //render select sprite
        if (isSelectSpriteInBoard) selectSprite.render(g);
    }

    public void renderShootBoard(Graphics g) {
        getOpponent().getBoard().render(g);
    }

    public int getGameMode() {
        return gameMode;
    }

    /**
     * Set trạng thái của {@link GamePlay}
     * trạng thái có thể là {@link #WAITING_MODE} hoặc {@link #PLAY_MODE}
     *
     * @param gameMode trạng thái của {@link GamePlay}
     */
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
        update();
    }

    /**
     * Cập nhật lại kích thước của game
     */
    public void update() {

        JPanel currentScreen = gameManager.getBattleShip().getScreenManager().getCurrentScreen();
        switch (gameMode) {
            case WAITING_MODE -> {
                PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();

                int shipSpriteMargin = MARGIN * Math.round(GameSetting.SCALE);
                int yMidPosition = currentScreen.getHeight() / 2;
                int xMidPosition = currentScreen.getWidth() / 2;

                // set ready button size
                readyButton.setSize((int) (readyButton.getAsset().getWidth() / 2f * GameSetting.SCALE * 0.8f),
                                    (int) (readyButton.getAsset().getHeight() / 2f * GameSetting.SCALE * 0.8f));
                readyButton.setLocation(xMidPosition - readyButton.getWidth() / 2,
                                        currentScreen.getHeight() - readyButton.getHeight() - shipSpriteMargin / 2);
                playerBoard.setSize(currentScreen.getWidth() / 2,
                                    currentScreen.getHeight() - (currentScreen.getHeight() - readyButton.getY()) - shipSpriteMargin);
                playerBoard.update();
                playerBoard.setLocation(xMidPosition - 100, shipSpriteMargin / 2);
                if (setUpSprite != null) {
                    float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
                    updateSpriteByRatio(ratio);
                }

                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = sprites.get(i + 2);

                    sprite.setLocation((int) (i * (sprite.getWidth() + shipSpriteMargin) + 32 * GameSetting.SCALE),
                                       yMidPosition - sprite.getHeight() / 2);
                }
            }
            case PLAY_MODE -> {
                for (Player player : playerList) {
                    var playerBoard = player.getBoard();
                    playerBoard.setSize(currentScreen.getWidth() / 2, currentScreen.getHeight() - 2 * 100);
                    //set center location
                    playerBoard.setLocation(currentScreen.getWidth() / 2 - playerBoard.getWidth() / 2, 100);
                    playerBoard.update();
                }
            }
        }

    }

    private void updateSpriteByRatio(float ratio) {
        PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();
        if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
            setUpSprite.setSize((int) (playerBoard.getCellSize() * ratio), playerBoard.getCellSize());

        }
        else {
            setUpSprite.setSize(playerBoard.getCellSize(), (int) (playerBoard.getCellSize() * ratio));
        }
    }

    /**
     * Thay đổi hướng của tàu
     * nếu đang ở chế độ {@link #PLAY_MODE} thì không thay đổi
     * nếu đang ở chế độ đặt tàu thì thay đổi hướng của tàu
     * nếu hướng hiện tại là {@link Ship#HORIZONTAL} thì chuyển thành {@link Ship#VERTICAL} và ngược lại
     */
    public void changeDirection() {
        if (gameMode != WAITING_MODE) return;
        setDirection((direction + 1) % 2);
    }

    public Component getScreen() {
        return battleShip.getScreenManager().getCurrentScreen();
    }
}
