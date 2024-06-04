package com.htilssu.event.player;

import com.htilssu.entity.player.Player;
import com.htilssu.event.GameEvent;

public class PlayerEvent implements GameEvent {
  Player player;

  public PlayerEvent(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
