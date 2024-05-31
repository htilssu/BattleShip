package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.event.player.PlayerAction;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.manager.GameManager;
import com.htilssu.multiplayer.Client;

import java.net.Socket;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerShoot(PlayerShootEvent e) {
    Position pos = e.getPosition();
    Player player = e.getPlayer();
    if (!e.isEnemy()) {
      if (player.canShoot(pos)) {
        Client client = player.getGamePlay().getGameManager().getBattleShip().getClient();
        client.sendData(String.format("%d|%d|%d", PlayerAction.SHOOT, pos.x, pos.y));
      }
    } else {
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e, GameManager gameManager) {
    Player player = e.getPlayer();
    gameManager.addPlayer(player);
    gameManager.setMultiPlayer(true);
  }
}
