package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.entity.component.Position;
import com.htilssu.entity.player.Player;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.manager.GameManager;
import com.htilssu.multiplayer.MultiHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerShoot(PlayerShootEvent e, MultiHandler multiHandler) {
        Position position = e.getPosition();
        if (e.getBoard().canShoot(position)) {
            byte shootStatus = e.getBoard().shoot(position);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e, Object object) {
        Player player = e.getPlayer();
        if ((object instanceof GameManager gameManager)) {
            gameManager.addPlayer(player);
            gameManager.setMultiPlayer(true);
            gameManager.createNewGamePlay();
        }

    }
}
