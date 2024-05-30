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

    {
        initPlayerList();
    }

    public GameManager(BattleShip battleShip) {
        this.battleShip = battleShip;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void addPlayer(Player player) {
        if (players.size() < MAX_PLAYER) {
            players.add(player);
        }
    }

    public GamePlay createNewGamePlay() {
        if (players.size() < MIN_PLAYER) {
            return null;
        }

        GamePlay newGamePlay = new GamePlay(players, turn, DifficultyManager.DIFFICULTY);

        initPlayerList();
        currentGamePlay = newGamePlay;
        return currentGamePlay;
    }

    private void initPlayerList() {
        players = new ArrayList<>();
        players.add(gamePlayer);
    }

    public GamePlay getCurrentGamePlay() {
        return currentGamePlay;
    }

    public void setCurrentGamePlay(GamePlay currentGamePlay) {
        this.currentGamePlay = currentGamePlay;
    }

    public void createTestGamePlay() {
        List<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player());
        testPlayers.add(new Player());
        currentGamePlay = new GamePlay(testPlayers, 0, DifficultyManager.DIFFICULTY);
    }
}
