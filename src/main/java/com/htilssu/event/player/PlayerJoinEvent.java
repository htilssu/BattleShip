package com.htilssu.event.player;

import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

public class PlayerJoinEvent {

    Player player;
    GamePlay gamePlay;

    public PlayerJoinEvent(Player player, GamePlay gamePlay) {
        this.player = player;
        this.gamePlay = gamePlay;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public Player getPlayer() {
        return player;
    }
}
