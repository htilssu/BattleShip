package com.htilssu.event.game;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.event.GameEvent;

public class GameStartEvent implements GameEvent {

    GamePlay gamePlay;
    BattleShip battleShip;

    public GameStartEvent(GamePlay gamePlay, BattleShip battleShip) {
        this.gamePlay = gamePlay;
        this.battleShip = battleShip;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public BattleShip getBattleShip() {
        return battleShip;
    }
}
