package com.htilssu.manager;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class GameManager {

    public static final int MAX_PLAYER = 2;
    public static final int MIN_PLAYER = 1;
    public static final Player gamePlayer = new Player();

    BattleShip battleShip;
    List<Player> players;
    GamePlay currentGamePlay;
    int turn = 0;
    private boolean multiPlayer;

    public GameManager(BattleShip battleShip) {
        this.battleShip = battleShip;
        initPlayerList();
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

        GamePlay newGamePlay = new GamePlay(players, turn, DifficultyManager.getGameBoardSize(DifficultyManager.difficulty), multiPlayer);
        newGamePlay.setGameManager(this);

        initPlayerList();
        setCurrentGamePlay(newGamePlay);
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

        setCurrentGamePlay(
                new GamePlay(
                        testPlayers, 0, DifficultyManager.getGameBoardSize(DifficultyManager.difficulty)));
    }

    public void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }
}
