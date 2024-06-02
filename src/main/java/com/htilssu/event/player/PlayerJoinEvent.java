package com.htilssu.event.player;

import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;

public class PlayerJoinEvent extends PlayerEvent {

    public PlayerJoinEvent(Player player) {
        super(player);
    }
}
