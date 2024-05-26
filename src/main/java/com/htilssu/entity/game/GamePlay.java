package com.htilssu.entity.game;

import com.htilssu.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GamePlay {
    List<Player> players = new ArrayList<>(2);

    public GamePlay() {}

    public void addPlayer(Player player) {
        players.add(player);
    }
}
