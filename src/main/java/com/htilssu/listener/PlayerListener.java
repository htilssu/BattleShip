package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.event.player.PlayerAttackEvent;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerAttack(PlayerAttackEvent e) {}
}
