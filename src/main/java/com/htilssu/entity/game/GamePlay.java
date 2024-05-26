package com.htilssu.entity.game;

import com.htilssu.entity.player.Player;

/** */
public class GamePlay {
    Player[] players = new Player[2];

    public GamePlay(Player player1, Player player2) {
        this.players[1] = player2;
        this.players[0] = player1;
    }
}
