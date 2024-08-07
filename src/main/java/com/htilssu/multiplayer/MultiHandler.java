package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.entity.Ship;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.*;
import com.htilssu.ui.screen.NetworkScreen;
import com.htilssu.ui.screen.PickScreen;
import com.htilssu.util.GameLogger;
import com.htilssu.util.ScoreUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.htilssu.entity.player.PlayerBoard.SHOOT_HIT;
import static com.htilssu.entity.player.PlayerBoard.SHOOT_MISS;
import static com.htilssu.event.game.GameAction.*;
import static com.htilssu.manager.GameManager.gamePlayer;
import static com.htilssu.manager.ScreenManager.NETWORK_SCREEN;

public abstract class MultiHandler {

    public static final int PING = -1;
    public static final int PONG = -2;
    protected BattleShip battleShip;
    boolean isHost = false;

    public MultiHandler(BattleShip battleShip) {
        this.battleShip = battleShip;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public void readData(Socket socket) {
        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String message = bis.readLine();
                if (message == null) {
                    break;
                }
                handle(message);
            }
        } catch (IOException e) {
            //empty
        }
    }

    private void handle(String message) {
        List<String> messageParts = Arrays.asList(message.split("\\|"));
        GameManager gameManager = battleShip.getGameManager();
        GamePlay currentGamePlay = gameManager.getCurrentGamePlay();
        Integer action = null;
        try {
            action = Integer.parseInt(messageParts.getFirst());
        } catch (NumberFormatException e) {
            GameLogger.error("Invalid action: " + messageParts.getFirst());
        }

        if (action != null) {
            String playerId;
            switch (action) {
                case GameAction.JOIN:
                    if (messageParts.size() < 3) {
                        GameLogger.error("Invalid message (Player Join): " + message);
                        return;
                    }
                    playerId = messageParts.get(1);
                    String playerName = messageParts.get(2);
                    Player player = new Player(playerId, playerName);


                    if (messageParts.size() >= 4) {
                        try {
                            DifficultyManager.difficulty = Integer.parseInt(messageParts.get(3));
                        } catch (NumberFormatException e) {
                            GameLogger.error("Invalid difficulty: " + messageParts.get(3));
                        }
                    }

                    gameManager.addPlayer(player);

                    if (messageParts.size() == 5) {
                        try {
                            gameManager.turn = Integer.parseInt(messageParts.get(4));
                        } catch (NumberFormatException e) {
                            GameLogger.error("Invalid turn: " + messageParts.get(4));
                        }
                    }
                    else gameManager.setTurn(new Random().nextInt(1));


                    if (this instanceof Client) {
                        gameManager.createNewGamePlay();
                        battleShip.changeScreen(ScreenManager.PLAY_SCREEN);
                    }
                    battleShip.getListenerManager()
                            .callEvent(new PlayerJoinEvent(player,
                                            battleShip.getGameManager()
                                                    .getCurrentGamePlay()
                                    ),
                                    battleShip.getGameManager()
                            );

                    if (this instanceof Host) {
                        PickScreen screen = (PickScreen) battleShip.getScreenManager()
                                .getScreen(ScreenManager.PICK_SCREEN);
                        screen.setGameMode(GameManager.MULTI_PLAYER);
                        battleShip.changeScreen(ScreenManager.PICK_SCREEN);
                    }

                    break;

                case GameAction.SHOOT:
                    if (messageParts.size() < 4) {
                        GameLogger.error("Invalid message (Player Shoot): " + message);
                        return;
                    }
                    handleShootRequest(messageParts);
                    break;

                case RESPONSE_SHOOT:
                    handleResponseShoot(messageParts);
                    break;

                case PING:
                    if (this instanceof Host host) {
                        this.send(PONG, host.getId());
                    }
                    break;

                case PONG:
                    if (messageParts.size() > 1) {
                        var hostId = messageParts.get(1);
                        Client client = battleShip.getClient();
                        if (!battleShip.getHost().getId().equals(hostId)) {
                            client.getHostList().add(client.socket.getInetAddress());
                        }

                        client.disconnect();

                        var networkScreen = (NetworkScreen) battleShip.getScreenManager()
                                .getScreen(NETWORK_SCREEN);
                        networkScreen.updateListHost(client.getHostList());
                        networkScreen.repaint();
                    }
                    break;

                case GameAction.START_GAME:
                    if (this instanceof Client) {
                        gameManager.getCurrentGamePlay().setGameMode(GamePlay.PLAY_MODE);
                        battleShip.getScreenManager()
                                .getScreen(ScreenManager.PLAY_SCREEN)
                                .repaint();
                    }
                    break;

                case GameAction.READY:
                    if (this instanceof Host) {
                        ((Host) this).ready();
                    }
                    break;

                case GameAction.UNREADY:
                    if (this instanceof Host) {
                        ((Host) this).unReady();
                    }
                    break;

                case END_GAME:
                    currentGamePlay.setWinner(0);
                    currentGamePlay.endGame();
                    sendRemainingShips();
                    battleShip.getScreenManager().getCurrentScreen().repaint();
                    battleShip.getScreenManager().getCurrentScreen().updateUI();
                    break;
                case END_TURN:
                    currentGamePlay.endTurn();
                    break;

                case SHIP_LEAK:
                    handleShipLeak(messageParts);
                    battleShip.getScreenManager().getCurrentScreen().repaint();
                    battleShip.getScreenManager().getCurrentScreen().updateUI();
                    break;
                default:
                    GameLogger.log("Unknown message: " + message);
                    break;
            }
        }
    }

    private void handleShootRequest(List<String> messageParts) {
        var playerId = messageParts.get(1);
        Position pos = new Position(Integer.parseInt(messageParts.get(2)),
                Integer.parseInt(messageParts.get(3))
        );
        GamePlay gamePlay = battleShip.getGameManager().getCurrentGamePlay();
        Player currentPlayer = gamePlay.getCurrentPlayer();
        if (currentPlayer.getId().equals(playerId)) {
            PlayerBoard playerBoard = gamePlayer.getBoard();
            var ship = playerBoard.getShipAtPosition(pos);
            var responseStatus = SHOOT_MISS;

            if (ship != null) {
                responseStatus = PlayerBoard.SHOOT_HIT;
                gamePlay.getCurrentPlayer()
                        .plusScore(ScoreUtil.calculateScore(gamePlay.getTimeCountDown()));
                playerBoard.shoot(pos, responseStatus);
                var status = playerBoard.isShipDestroyed(ship);
                if (status) {
                    responseStatus = PlayerBoard.SHOOT_DESTROYED;
                    playerBoard.markShipDestroyed(ship);
                }
            }
            else {
                playerBoard.shoot(pos, responseStatus);
            }

            sendResponseShoot(responseStatus, pos.getX(), pos.getY(), ship);
            if (responseStatus == SHOOT_MISS) gamePlay.endTurn();

            //check player lose
            if (playerBoard.isAllShipsDestroyed()) {
                this.send(GameAction.END_GAME);
                gamePlay.setWinner(1);
                gamePlay.endGame();

                battleShip.getScreenManager().getCurrentScreen().repaint();
                battleShip.getScreenManager().getCurrentScreen().updateUI();
            }
            else {
                gamePlay.resetCountDown();
                gamePlay.startCount();
            }


            battleShip.getListenerManager()
                    .callEvent(new PlayerShootEvent(currentPlayer,
                                    gamePlay.getOpponent().getBoard(),
                                    pos
                            ),
                            battleShip.getGameManager()
                    );
        }
    }

    private void handleResponseShoot(List<String> messageParts) {
        var shootStatus = Integer.parseInt(messageParts.get(1));
        var x = Integer.parseInt(messageParts.get(2));
        var y = Integer.parseInt(messageParts.get(3));
        Position pos = new Position(x, y);


        GamePlay gamePlay = battleShip.getGameManager().getCurrentGamePlay();

        Player currentPlayer = gamePlay.getCurrentPlayer();
        PlayerBoard playerBoard = gamePlay.getOpponent().getBoard();

        Ship ship;
        if (shootStatus == PlayerBoard.SHOOT_DESTROYED) {
            if (messageParts.size() > 4) {
                var shipType = Integer.parseInt(messageParts.get(4));
                var direction = Integer.parseInt(messageParts.get(5));
                ship = ShipManager.createShip(shipType, direction);
                ship.setPosition(pos);
                playerBoard.markShipDestroyed(ship);

                playerBoard.addShip(ship);
                gamePlay.getScreen().repaint();
            }

            return;
        }

        playerBoard.shoot(pos, shootStatus);

        if (shootStatus == SHOOT_MISS) {
            SoundManager.playSound(SoundManager.DUCK_SOUND);
            gamePlay.endTurn();
        }
        if (shootStatus == SHOOT_HIT) {
            gamePlay.getCurrentPlayer()
                    .plusScore(ScoreUtil.calculateScore(gamePlay.getTimeCountDown()));
            SoundManager.playSound(SoundManager.ATTACK_SOUND);

        }

        gamePlay.resetCountDown();
        gamePlay.startCount();


        battleShip.getListenerManager()
                .callEvent(new PlayerShootEvent(currentPlayer,
                                gamePlay.getOpponent().getBoard(),
                                new Position(x, y)
                        ),
                        battleShip.getGameManager()
                );

    }

    public abstract void send(Object... message);

    /**
     * Gửi tất cả thuyền chưa chìm của bản thân đến đối thủ
     */
    private void sendRemainingShips() {
        for (Ship remainingShip : gamePlayer.getBoard().getRemainingShips()) {
            this.send(GameAction.SHIP_LEAK,
                    remainingShip.getPosition().x,
                    remainingShip.getPosition().y,
                    remainingShip.getShipType(), remainingShip.getDirection()
            );
        }
    }

    /**
     * Xử lý ship leak khi kết thúc game hiển thị tất cả thuyền chưa chìm của đối thủ
     *
     * @param messageParts danh sách tham số nhận được
     */
    private void handleShipLeak(List<String> messageParts) {
        if (messageParts.size() >= 5) {
            int x = Integer.parseInt(messageParts.get(1));
            int y = Integer.parseInt(messageParts.get(2));

            int shipType = Integer.parseInt(messageParts.get(3));
            int direction = Integer.parseInt(messageParts.get(4));

            var opponent = battleShip.getGameManager().getCurrentGamePlay().getOpponent();
            Ship ship = ShipManager.createShip(shipType, direction);
            ship.setPosition(new Position(x, y));
            opponent.getBoard().addShip(ship);
            battleShip.getGameManager().getCurrentGamePlay().update();
            battleShip.getScreenManager().getCurrentScreen().repaint();
        }
    }

    private void sendResponseShoot(int shootStatus, int x, int y, Ship ship) {

        if (shootStatus == PlayerBoard.SHOOT_DESTROYED) {
            send(RESPONSE_SHOOT, SHOOT_HIT, x, y);
            send(RESPONSE_SHOOT,
                    PlayerBoard.SHOOT_DESTROYED,
                    ship.getPosition().x,
                    ship.getPosition().y,
                    ship.getShipType(),
                    ship.getDirection()
            );
        }
        else send(RESPONSE_SHOOT, shootStatus, x, y);
    }

    public void sendShoot(Position pos) {
        send(GameAction.SHOOT, gamePlayer.getId(), pos.getX(), pos.getY());
    }
}
