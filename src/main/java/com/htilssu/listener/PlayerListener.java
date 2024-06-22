package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.entity.player.Player;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.event.player.PlayerShootEvent;
import com.htilssu.multiplayer.MultiHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerShoot(PlayerShootEvent e, MultiHandler multiHandler) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e, Object object) {
        Player player = e.getPlayer();

    }

    @EventHandler
    public void onPlayerShoot(PlayerShootEvent e, Object o) {

    }
}
