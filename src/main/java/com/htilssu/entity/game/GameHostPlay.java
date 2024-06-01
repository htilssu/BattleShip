package com.htilssu.entity.game;

import com.htilssu.entity.player.Player;
import com.htilssu.multiplayer.Host;

import java.util.List;

public class GameHostPlay extends GamePlay {

  int ready = 0;

  public GameHostPlay(List<Player> playerList, int turn, int difficulty, boolean isMultiPlayer) {
    super(playerList, turn, difficulty, isMultiPlayer);
  }

  public void unReady() {
    ready -= 1;
    if (ready < 0) {
      ready = 0;
    }
  }

  public void ready() {
    ready += 1;
    if (ready > 2) {
      ready = 2;
    }

    if (ready == 2) {
      //TODO : ready
    }
  }
}
