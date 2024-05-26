package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.entity.player.Player;
import com.htilssu.event.player.PlayerAttackEvent;
import com.htilssu.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerAttack(PlayerAttackEvent e) {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        GamePlay gamePlay = e.getGamePlay();
        gamePlay.addPlayer(player);
        // TODO add player to game play
    }
}
