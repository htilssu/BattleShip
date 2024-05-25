package com.htilssu.event.player;

import com.htilssu.entity.Player;
import com.htilssu.event.GameEvent;

public class PlayerEvent extends GameEvent {
    Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
