package com.htilssu.entity.game;

import com.htilssu.BattleShip;
import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Bot;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.manager.ShipManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GameLabel;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.ui.component.GameProgress;
import com.htilssu.ui.screen.EndGameScreen;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.ScoreUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.htilssu.entity.Ship.VERTICAL;

/**
 * Mỗi {@link GamePlay} là 1 trận đấu giữa 2 người chơi
 */
public class GamePlay implements Renderable {

    public static final int SETUP_MODE = 0;
    public static final int PLAY_MODE = 1;
    public static final int END_MODE = 2;
    private static final int MARGIN = 30;
    public final Map<Integer, Sprite> sprites = new HashMap<>();
    private final Timer timer;
    private final GameProgress gameProgress = new GameProgress(GameManager.TIME_PER_TURN);
    private final Map<Integer, Integer> shipInBoard = new HashMap<>();
    private final List<Player> playerList;
    private final int size;
    private final Sprite selectSprite;
    private final Sprite readyButton = new Sprite(
            AssetUtils.getImage(AssetUtils.ASSET_READY_BUTTON));
    private final GameButton autoPlaceButton = new GameButton(
            AssetUtils.getImage(AssetUtils.ASSET_BUTTON_2)) {{
        setText("Auto Place");
        setTextSize(22);
    }};
    private final GameButton previewLabel = new GameButton(
            AssetUtils.getImage(AssetUtils.ASSET_TEXT_FIELD_2)) {{
        this.setText("Preview Board");
        this.setTextSize(24);
    }};
    private final GamePanel targetPanel = new GamePanel(
            AssetUtils.getImage(AssetUtils.ASSET_HOLDER));
    private final GameButton scorePanel = new GameButton(
            AssetUtils.getImage(AssetUtils.ASSET_TEXT_FIELD_2));
    private Timer matchTimer = null;
    private Timer botTurnTimer;
    private int totalShot = 0;
    private int timeCountDown;
    private int matchCountDown = GameManager.TIME_PER_MATCH;
    private int winner = -1;
    private boolean isReady = false;
    private int gameMode = SETUP_MODE;
    private int turn;
    private int direction = VERTICAL;
    private Sprite setUpSprite;
    private boolean isMultiPlayer = false;
    private GameManager gameManager;
    private boolean isSelectSpriteInBoard;
    private BattleShip battleShip;
    private Cursor playerCursor;
    private PlayerBoard previewBoard;
    private Player previewBoardPlayer;
    private long previewBoardVersion = -1;
    private int previewBoardX;
    private int previewBoardSize;

    {
        sprites.put(Ship.SHIP_2, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_2)));
        sprites.put(Ship.SHIP_3, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_3)));
        sprites.put(Ship.SHIP_4, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_4)));
        sprites.put(Ship.SHIP_5, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_5)));
    }

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
        timer = new Timer(5, e -> {
            timeCountDown--;
            gameProgress.setProgress(timeCountDown);
            if (timeCountDown == 0) {
                if (!isMultiPlayer) {
                    endTurn();
                    scheduleBotTurn();
                }
                else if (battleShip.getHost().isConnected()) {
                    battleShip.getHost().send(GameAction.END_TURN);
                    endTurn();
                }
            }
        });

        matchTimer = new Timer(1000, e -> {
            matchCountDown--;
            if (matchCountDown == 0) {
                if (matchTimer != null) {
                    matchTimer.stop();
                }

                if (this.gameMode != END_MODE) {
                    endGame();
                }
            }
        });


        //        initTargetBoard();
        resetCountDown();
        initBoard();
    }

    /**
     * Kết thúc lượt chơi của người chơi hiện tại
     */
    public void endTurn() {
        turn = (turn + 1) % playerList.size();


        resetCountDown();
        startCount();

        getCurrentPlayer().getGamePlay().update();
        getScreen().repaint();
    }

    public void startCount() {
        timer.start();
    }

    private void initBoard() {
        int boardSize = getBoardSize();

        for (Player player : playerList) {
            player.setPlayerBoard(new PlayerBoard(boardSize, player));
            player.setShot(new byte[boardSize][boardSize]);
            player.setGamePlay(this);
        }
    }

    public void prepareSinglePlayer() {
        for (Player player : playerList) {
            if (player instanceof Bot) {
                placeBotFleet(player.getBoard());
                break;
            }
        }
    }

    private void placeBotFleet(PlayerBoard board) {
        Random random = new Random();
        int[] fleet = {Ship.SHIP_2, Ship.SHIP_3, Ship.SHIP_3, Ship.SHIP_4, Ship.SHIP_5};

        for (int shipType : fleet) {
            boolean placed = false;
            while (!placed) {
                int shipDirection = random.nextBoolean() ? Ship.HORIZONTAL : Ship.VERTICAL;
                Ship ship = ShipManager.createShip(shipType, shipDirection);
                ship.setPosition(new Position(random.nextInt(size), random.nextInt(size)));
                if (board.canAddShip(ship)) {
                    board.addShip(ship);
                    placed = true;
                }
            }
        }
    }

    /**
     * Kết thúc game
     */
    public void endGame() {
        timer.stop();
        matchTimer.stop();
        if (botTurnTimer != null) botTurnTimer.stop();
        setGameMode(END_MODE);
        destroy();
        final JPanel endScreen = battleShip.getScreenManager()
                .getScreen(ScreenManager.END_GAME_SCREEN);
        Player opponent = playerList.get(1);

        if (opponent != null) {
            if (endScreen instanceof EndGameScreen endGameScreen) {
                endGameScreen.setOpponentBoard(opponent.getBoard());
                endGameScreen.setWin(getWinner() == 0);
            }
        }

        battleShip.changeScreen(ScreenManager.END_GAME_SCREEN);
    }

    public int getBoardSize() {
        return size;
    }

    private void initTargetBoard() {
        //set vertical layout
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.Y_AXIS));

        targetPanel.add(new GameLabel("Target") {{
            this.setFontSize(30);
        }});
    }

    public Player getLoserByScore() {
        Player winner = playerList.getFirst();
        for (Player player : playerList) {
            if (player.getScore() < winner.getScore()) {
                winner = player;
            }
        }
        return winner;

    }

    private Player getLoser() {
        for (Player player : playerList) {
            if (player.getBoard().isAllShipsDestroyed()) {
                return player;
            }
        }

        return null;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getTimeCountDown() {
        return timeCountDown;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if (this.direction == direction || gameMode != SETUP_MODE) return;
        this.direction = direction;


        if (setUpSprite != null) {
            PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();
            setUpSprite.setAsset(AssetUtils.rotate90(setUpSprite.getAsset()));

            float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
            if (ratio < 1 && ratio > 0) {
                ratio = 1 / ratio;
                setUpSprite.setSize((int) (playerBoard.getCellSize() * ratio),
                        playerBoard.getCellSize()
                );

            }
            else {
                setUpSprite.setSize(playerBoard.getCellSize(),
                        (int) (playerBoard.getCellSize() * ratio)
                );
            }
        }
    }

    /**
     * Cập nhật lại kích thước của game
     */
    public void update() {

        JPanel currentScreen = gameManager.getBattleShip().getScreenManager().getCurrentScreen();
        switch (gameMode) {
            case SETUP_MODE -> {
                PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();

                int shipSpriteMargin = MARGIN * Math.round(GameSetting.SCALE);
                int yMidPosition = currentScreen.getHeight() / 2;
                int xMidPosition = currentScreen.getWidth() / 2;

                // set ready button size
                readyButton.setSize((int) (readyButton.getAsset()
                                .getWidth() / 2f * GameSetting.SCALE * 0.8f),
                        (int) (readyButton.getAsset()
                                .getHeight() / 2f * GameSetting.SCALE * 0.8f)
                );
                readyButton.setLocation(xMidPosition - readyButton.getWidth() / 2,
                        currentScreen.getHeight() - readyButton.getHeight() - shipSpriteMargin / 2
                );
                autoPlaceButton.setBounds(
                        readyButton.getX() - readyButton.getWidth() - shipSpriteMargin / 2,
                        readyButton.getY(),
                        readyButton.getWidth(),
                        readyButton.getHeight()
                );
                playerBoard.setSize(currentScreen.getWidth() / 2,
                        currentScreen.getHeight() - (currentScreen.getHeight() - readyButton.getY()) - shipSpriteMargin
                );
                playerBoard.update();
                playerBoard.setLocation(xMidPosition - 100, shipSpriteMargin / 2);
                if (setUpSprite != null) {
                    float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
                    updateSpriteByRatio(ratio);
                }

                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = sprites.get(i + 2);

                    sprite.setLocation(
                            (int) (i * (sprite.getWidth() + shipSpriteMargin) + 32 * GameSetting.SCALE),
                            yMidPosition - sprite.getHeight() / 2
                    );
                }
            }
            case PLAY_MODE -> {
                for (Player player : playerList) {
                    var playerBoard = player.getBoard();
                    playerBoard.setSize(currentScreen.getWidth() / 2,
                            currentScreen.getHeight() - 2 * 100
                    );
                    //set center location
                    playerBoard.setLocation(
                            currentScreen.getWidth() / 2 - playerBoard.getWidth() / 2,
                            100
                    );
                    playerBoard.update();
                    gameProgress.setSize(currentScreen.getWidth() - 100, 30);
                    gameProgress.setLocation(50, 25);
                }
                layoutPlayComponents();
            }
        }

        currentScreen.revalidate();
        currentScreen.repaint();

    }

    public void handleClick(Point position) {
        switch (gameMode) {
            case PLAY_MODE -> {

                //check if it's not current player turn
                if (!getCurrentPlayer().getId().equals(GameManager.gamePlayer.getId())) return;

                PlayerBoard board = getOpponent().getBoard();

                if (!board.isInside(position)) return;
                Position pos = board.getBoardRowCol(position);

                if (!board.canShoot(pos)) return;

                shoot(pos);
            }
            case SETUP_MODE -> {
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

    public Player getOpponentOf(Player player) {
        for (Player candidate : playerList) {
            if (!candidate.getId().equals(player.getId())) return candidate;
        }
        return player;
    }

    /**
     * Bắn vào vị trí {@code pos}
     *
     * @param pos vị trí cần bắn
     */
    private void shoot(Position pos) {
        totalShot++;
        if (!isMultiPlayer) {
            resolveSinglePlayerShot(pos);
            return;
        }
        else if (battleShip.getHost().isConnected()) {
            battleShip.getHost().sendShoot(pos);
        }
        else {
            battleShip.getClient().sendShoot(pos);
        }

        timer.stop();
        resetCountDown();

        battleShip.getListenerManager()
                .callEvent(new PlayerShootEvent(getCurrentPlayer(), getOpponent().getBoard(), pos),
                        gameManager
                );
    }

    private void resolveSinglePlayerShot(Position pos) {
        Player shooter = getCurrentPlayer();
        PlayerBoard targetBoard = getOpponent().getBoard();
        if (!targetBoard.canShoot(pos)) return;

        Ship ship = targetBoard.getShipAtPosition(pos);
        int shootStatus = PlayerBoard.SHOOT_MISS;
        if (ship != null) {
            shootStatus = PlayerBoard.SHOOT_HIT;
            shooter.plusScore(ScoreUtil.calculateScore(getTimeCountDown()));
        }
        targetBoard.shoot(pos, shootStatus);

        if (ship != null && targetBoard.isShipDestroyed(ship)) {
            targetBoard.markShipDestroyed(ship);
        }

        battleShip.getListenerManager().callEvent(
                new PlayerShootEvent(shooter, targetBoard, pos), gameManager);

        if (targetBoard.isAllShipsDestroyed()) {
            setWinner(turn);
            endGame();
            return;
        }

        resetCountDown();
        if (shootStatus == PlayerBoard.SHOOT_MISS) {
            endTurn();
        }
        else {
            startCount();
        }
        scheduleBotTurn();
    }

    private void scheduleBotTurn() {
        if (isMultiPlayer || gameMode != PLAY_MODE || !(getCurrentPlayer() instanceof Bot)) return;
        if (botTurnTimer != null && botTurnTimer.isRunning()) return;

        botTurnTimer = new Timer(500, e -> {
            botTurnTimer.stop();
            botTurnTimer = null;
            PlayerBoard targetBoard = getOpponent().getBoard();
            Position pos;
            Random random = new Random();
            do {
                pos = new Position(random.nextInt(size), random.nextInt(size));
            } while (!targetBoard.canShoot(pos));
            resolveSinglePlayerShot(pos);
        });
        botTurnTimer.setRepeats(false);
        botTurnTimer.start();
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
                if (setUpSprite != null) return;
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
            direction = clickShip.getDirection();
            int count = shipInBoard.get(clickShip.getShipType());
            shipInBoard.replace(clickShip.getShipType(), ++count);
        }
    }

    /**
     * Xử lý việc thêm tàu vào bảng
     *
     * @param playerBoard bảng của người chơi
     */
    private void handleAddShipToBoard(PlayerBoard playerBoard) {
        Position mousePos = playerBoard.getBoardRowCol(setUpSprite.getX() + 1,
                setUpSprite.getY() + 1
        );
        float ratio = (float) setUpSprite.getHeight() / setUpSprite.getWidth();
        if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
        }
        Ship ship = new Ship(direction, new Sprite(setUpSprite), mousePos, (int) ratio);
        ship.setDirection(direction);

        if (!playerBoard.canAddShip(ship)) {
            SoundManager.playSound(SoundManager.ERROR_SOUND);
            return;
        }

        //Dat thuyen thanh cong
        int count = shipInBoard.get((int) ratio);
        if (count == 0) return;
        SoundManager.playSound(SoundManager.PUT_SHIP_SOUND);
        playerBoard.addShip(ship);
        count--;
        shipInBoard.replace((int) ratio, count);

        //reset status if ship placed
        if (count == 0) {
            direction = VERTICAL;
            setUpSprite = null;
        }
    }

    public void resetCountDown() {
        timer.stop();
        timeCountDown = GameManager.TIME_PER_TURN * 200;
    }

    private void unReady() {
        isReady = false;
        readyButton.setAsset(AssetUtils.getImage(AssetUtils.ASSET_READY_BUTTON), null);
        if (!isMultiPlayer) {
            return;
        }
        if (Host.getInstance().isConnected()) {
            Host.getInstance().unReady();
        }
        else {
            Client.getInstance().send(GameAction.UNREADY);
        }
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
        if (!isMultiPlayer) {
            setGameMode(PLAY_MODE);
            scheduleBotTurn();
        }
        else if (Host.getInstance().isConnected()) {
            Host.getInstance().ready();
        }
        else {
            Client.getInstance().send(GameAction.READY);
        }
    }

    private void updateSpriteByRatio(float ratio) {
        PlayerBoard playerBoard = GameManager.gamePlayer.getBoard();
        if (ratio < 1 && ratio > 0) {
            ratio = 1 / ratio;
            setUpSprite.setSize((int) (playerBoard.getCellSize() * ratio),
                    playerBoard.getCellSize()
            );

        }
        else {
            setUpSprite.setSize(playerBoard.getCellSize(),
                    (int) (playerBoard.getCellSize() * ratio)
            );
        }
    }

    public void handleMouseMoved(Point point) {
        switch (gameMode) {
            case SETUP_MODE -> {
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
                if (!getCurrentPlayer().getId().equals(GameManager.gamePlayer.getId())) {
                    isSelectSpriteInBoard = false;
                    return;
                }
                PlayerBoard playerBoard = getOpponent().getBoard();
                if (playerBoard.isInside(point)) {
                    if (playerCursor == null) playerCursor = getScreen().getCursor();
                    //hide cursor
                    //                    getScreen().setCursor(getScreen().getToolkit()
                    //                            .createCustomCursor(new BufferedImage(1,
                    //                                            1,
                    //                                            BufferedImage.TYPE_INT_ARGB
                    //                                    ),
                    //                                    new Point(0, 0),
                    //                                    "null"
                    //                            ));

                    var mouseLocation = playerBoard.getBoardRowCol(point);
                    selectSprite.setSize(playerBoard.getCellSize(), playerBoard.getCellSize());
                    selectSprite.setLocation(
                            playerBoard.getX() + mouseLocation.x * playerBoard.getCellSize(),
                            playerBoard.getY() + mouseLocation.y * playerBoard.getCellSize()
                    );

                    isSelectSpriteInBoard = true;
                }
                else {
                    isSelectSpriteInBoard = false;
                    if (playerCursor != null) {
                        getScreen().setCursor(playerCursor);
                        playerCursor = null;
                    }
                }
            }
        }
    }

    private void handleReadyButtonOnHover(Point point) {
        readyButton.handleHover(point.x, point.y);
    }

    public JPanel getScreen() {
        return battleShip.getScreenManager().getScreen(ScreenManager.PLAY_SCREEN);
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
        attachPlayComponents();
    }

    private void autoPlacePlayerFleet() {
        if (gameMode != SETUP_MODE) return;
        if (isReady) unReady();

        PlayerBoard board = GameManager.gamePlayer.getBoard();
        board.clearShips();
        setUpSprite = null;
        direction = VERTICAL;
        placeBotFleet(board);
        shipInBoard.replaceAll((shipType, count) -> 0);
        SoundManager.playSound(SoundManager.PUT_SHIP_SOUND);
        getScreen().repaint();
    }

    private void attachPlayComponents() {
        JPanel screen = getScreen();
        if (autoPlaceButton.getParent() != screen) {
            autoPlaceButton.addActionListener(e -> autoPlacePlayerFleet());
            screen.add(autoPlaceButton);
        }
        if (gameProgress.getParent() != screen) screen.add(gameProgress);
        if (targetPanel.getParent() != screen) screen.add(targetPanel);
        if (scorePanel.getParent() != screen) screen.add(scorePanel);
        if (previewLabel.getParent() != screen) screen.add(previewLabel);
        boolean visible = gameMode == PLAY_MODE;
        autoPlaceButton.setVisible(gameMode == SETUP_MODE);
        gameProgress.setVisible(visible);
        targetPanel.setVisible(visible);
        scorePanel.setVisible(visible);
        previewLabel.setVisible(visible);
    }

    @Override
    public void render(Graphics g) {

        if (gameMode == SETUP_MODE) {
            renderSetupMode(g);
            if (setUpSprite != null) setUpSprite.render(g);
        }
        else if (gameMode == PLAY_MODE) {
            renderPlayMode(g);
        }
    }

    /**
     * Vẽ màn hình đặt thuyền, hàm này sẽ được gọi và vẽ khi
     * {@link #gameMode} = {@link #SETUP_MODE}
     */
    private void renderSetupMode(Graphics g) {

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

    /**
     * Vẽ màn hình chơi game khi game bắt đầu, hàm này sẽ được gọi và vẽ khi
     * {@link #gameMode} = {@link #PLAY_MODE}
     */
    private void renderPlayMode(Graphics g) {
        renderShootBoard(g);
        renderPreviewBoard(g);
        //render select sprite
        if (isSelectSpriteInBoard && getCurrentPlayer().getId()
                .equals(playerList.getFirst().getId()))
            selectSprite.render(g);

    }

    /**
     * Vẽ bảng chơi của đối phương để người chơi hiện tại có thể thực hiện bắn
     */
    public void renderShootBoard(Graphics g) {
        boolean showShips = getOpponent().getId().equals(GameManager.gamePlayer.getId());
        getOpponent().getBoard().render(g, showShips);
    }

    /**
     * Vẽ bảng của đối thủ của người chơi hiện tại
     */
    private void renderPreviewBoard(Graphics g) {
        Player localPlayer = GameManager.gamePlayer;
        PlayerBoard playerBoard = localPlayer.getBoard();
        if (previewBoard == null || previewBoardPlayer != localPlayer
                || previewBoardVersion != playerBoard.getStateVersion()) {
            previewBoard = new PlayerBoard(playerBoard);
            previewBoardPlayer = localPlayer;
            previewBoardVersion = playerBoard.getStateVersion();
            previewBoard.setSize(previewBoardSize, previewBoardSize);
            previewBoard.setLocation(previewBoardX, 100);
            previewBoard.update();
        }
        previewBoard.render(g);
    }

    private void renderScoreBoard() {
        var scoreY = previewLabel.getY() + previewLabel.getHeight() + 25;
        var scoreX = previewLabel.getX();

        scorePanel.setLocation(scoreX, scoreY);
        scorePanel.setSize(previewLabel.getWidth(), 100);
        scorePanel.setText("Score: " + getCurrentPlayer().getScore());

    }

    private void renderTargetPanel() {
        var playerBoard = getCurrentPlayer().getBoard();

        targetPanel.setSize(playerBoard.getX() - 50, playerBoard.getHeight());

        targetPanel.setLocation(25, playerBoard.getY());
    }

    private void layoutPlayComponents() {
        if (gameManager == null || gameMode != PLAY_MODE) return;

        JPanel screen = getScreen();
        PlayerBoard playerBoard = getCurrentPlayer().getBoard();
        int startX = playerBoard.getX() + playerBoard.getWidth();
        int remainWidth = Math.max(0, screen.getWidth() - startX);
        int previewSize = Math.max(0, Math.min(300,
                Math.min(remainWidth - 40, screen.getHeight() - 320)));
        int previewX = startX + Math.max(0, (remainWidth - previewSize) / 2);
        previewBoardSize = previewSize;
        previewBoardX = previewX;

        if (previewBoard != null) {
            previewBoard.setSize(previewSize, previewSize);
            previewBoard.setLocation(previewX, 100);
            previewBoard.update();
        }
        previewLabel.setBounds(previewX, 100 + previewSize + 20, previewSize, 70);
        renderScoreBoard();
        renderTargetPanel();
    }

    /**
     * Thay đổi hướng của tàu
     * nếu đang ở chế độ {@link #PLAY_MODE} thì không thay đổi
     * nếu đang ở chế độ đặt tàu thì thay đổi hướng của tàu
     * nếu hướng hiện tại là {@link Ship#HORIZONTAL} thì chuyển thành {@link Ship#VERTICAL} và
     * ngược lại
     */
    public void changeDirection() {
        if (gameMode != SETUP_MODE) return;
        setDirection((direction + 1) % 2);
    }

    public int getGameMode() {
        return gameMode;
    }

    /**
     * Set trạng thái của {@link GamePlay}
     * trạng thái có thể là {@link #SETUP_MODE} hoặc {@link #PLAY_MODE}
     * khi trạng thái là {@link GamePlay#END_MODE} thì không thể set lại trạng thái
     *
     * @param gameMode trạng thái của {@link GamePlay}
     */
    public void setGameMode(int gameMode) {

        this.gameMode = gameMode;
        if (gameMode == PLAY_MODE) {
            attachPlayComponents();
            startCount();
        }
        else {
            autoPlaceButton.setVisible(gameMode == SETUP_MODE);
            gameProgress.setVisible(false);
            targetPanel.setVisible(false);
            scorePanel.setVisible(false);
            previewLabel.setVisible(false);
        }
        update();
    }

    /**
     * Hàm xóa các component khỏi playscreen
     */
    public void destroy() {
        if (botTurnTimer != null) botTurnTimer.stop();
        getScreen().remove(autoPlaceButton);
        getScreen().remove(previewLabel);
        getScreen().remove(gameProgress);
        getScreen().remove(targetPanel);
        getScreen().remove(scorePanel);
    }

    public int getTotalShot() {
        return totalShot;
    }

}
