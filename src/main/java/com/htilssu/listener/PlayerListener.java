package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.entity.player.Player;
import com.htilssu.event.player.PlayerJoinEvent;
import com.htilssu.event.player.PlayerShootEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerShoot(PlayerShootEvent e, Object multiHandler) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e, Object object) {
        Player player = e.getPlayer();

    }
}
