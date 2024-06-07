package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;
import com.htilssu.event.game.GameStartEvent;
import com.htilssu.manager.ScreenManager;

public class GameStartListener implements Listener {
    @EventHandler
    public void onGameStart(GameStartEvent e) {
        e.getBattleShip().getGameManager().createNewGamePlay();
    }
}
