package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.manager.GameManager;
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

    public MultiHandler(BattleShip battleShip) {
        this.battleShip = battleShip;
    }

    public MultiHandler() {
        //empty
    }

    private void handle(String message) {
        List<String> messageParts = Arrays.asList(message.split("\\|"));
        Integer action = null;
        try {
            action = Integer.parseInt(messageParts.get(0));
        } catch (NumberFormatException e) {
            GameLogger.error("Invalid action: " + messageParts.get(0));
        }

        if (action != null) {
            switch (action) {
                case GameAction.JOIN:
                    if (messageParts.size() < 3) {
                        GameLogger.error("Invalid message (Player Join): " + message);
                        return;
                    }
                    String playerId = messageParts.get(1);
                    String playerName = messageParts.get(2);
                    Player player = new Player(playerId, playerName);
                    battleShip.getListenerManager().callEvent(new PlayerJoinEvent(player), battleShip.getGameManager());
                    if (this instanceof Host) {
                        this.send(GameAction.JOIN, GameManager.gamePlayer.getId(), GameManager.gamePlayer.getName());
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
                                    new PlayerShootEvent(currentPlayer, gamePlay.getOpponent().getBoard(), pos), battleShip.getGameManager()
                            );
                        }
                    }
                    break;

                case PING:
                    if (this instanceof Host) {
                        ((Host) this).send(Integer.toString(PONG));
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

                default:
                    GameLogger.log("Unknown message: " + message);
                    break;
            }
        }
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

    public abstract void send(Object... message);
}