package com.htilssu.event.player;

import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

public class PlayerJoinEvent extends PlayerEvent {

    GamePlay gamePlay;

    public PlayerJoinEvent(Player player, GamePlay currentGamePlay) {
        super(player);
        gamePlay = currentGamePlay;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }
}
