package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.DifficultyManager;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.util.GameLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

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

            while (socket.isConnected()) {
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


                    if (messageParts.size() == 4) {
                        try {
                            DifficultyManager.difficulty = Integer.parseInt(messageParts.get(3));
                        } catch (NumberFormatException e) {
                            GameLogger.error("Invalid difficulty: " + messageParts.get(3));
                        }
                    }
                    battleShip.getGameManager().addPlayer(player);
                    battleShip.getGameManager().createNewGamePlay();
                    battleShip.getListenerManager()
                            .callEvent(new PlayerJoinEvent(player, battleShip.getGameManager().getCurrentGamePlay()),
                                       battleShip.getGameManager());
                    battleShip.changeScreen(ScreenManager.GAME_SCREEN);
                    if (this instanceof Host) {
                        this.send(GameAction.JOIN, GameManager.gamePlayer.getId(), GameManager.gamePlayer.getName(),
                                  DifficultyManager.difficulty);
                    }
                    break;

                case GameAction.SHOOT:
                    if (messageParts.size() < 4) {
                        GameLogger.error("Invalid message (Player Shoot): " + message);
                        return;
                    }
                    playerId = messageParts.get(1);
                    Position pos = new Position(
                            Integer.parseInt(messageParts.get(2)), Integer.parseInt(messageParts.get(3))
                    );
                    GamePlay gamePlay = battleShip.getGameManager().getCurrentGamePlay();
                    if (gamePlay != null) {
                        Player currentPlayer = gamePlay.getCurrentPlayer();
                        if (currentPlayer.getId().equals(playerId)) {
                            battleShip.getListenerManager().callEvent(
                                    new PlayerShootEvent(currentPlayer, gamePlay.getOpponent().getBoard(), pos),
                                    battleShip.getGameManager()
                            );
                        }
                    }
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
                        if (battleShip.getHost().getId().equals(hostId))
                            client.getHostList().add(client.socket.getInetAddress());
                    }
                    break;

                case GameAction.START_GAME:
                    if (this instanceof Client) {
                        battleShip.getGameManager().getCurrentGamePlay().setGameMode(GamePlay.PLAY_MODE);
                    }

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

                default:
                    GameLogger.log("Unknown message: " + message);
                    break;
            }
        }
    }

    public abstract void send(Object... message);
}