package com.htilssu.manager;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class GameManager {

    public static final int MAX_PLAYER = 2;
    public static final Player gamePlayer = new Player();
    public static final int TIME_PER_TURN = 40;
    public static final int TIME_PER_MATCH = 10 * 60;
    public static final int SINGLE_PLAYER = 1;
    public static final int MULTI_PLAYER = 2;
    public int turn = 0;
    BattleShip battleShip;
    List<Player> players;
    GamePlay currentGamePlay;
    private boolean multiPlayer;

    public GameManager(BattleShip battleShip) {
        this.battleShip = battleShip;
        initPlayerList();
    }

    private void initPlayerList() {
        players = new ArrayList<>();
        players.add(gamePlayer);
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

    public synchronized void createNewGamePlay() {
        if (players.isEmpty()) {
            return;
        }

        GamePlay newGamePlay = new GamePlay(players,
                turn,
                DifficultyManager.getGameBoardSize(DifficultyManager.difficulty),
                multiPlayer
        );
        newGamePlay.setGameManager(this);

        initPlayerList();
        setCurrentGamePlay(newGamePlay);
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

        setCurrentGamePlay(
                new GamePlay(
                        testPlayers,
                        0,
                        DifficultyManager.getGameBoardSize(DifficultyManager.difficulty)
                ));
    }

    public void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }
}
