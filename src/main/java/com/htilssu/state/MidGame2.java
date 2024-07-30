package com.htilssu.state;

import com.htilssu.ui.screen.Player2Screen;
import com.htilssu.ui.screen.Start2Player;

public class MidGame2 implements GameState {

    private Start2Player battleShip;
    private Player2Screen player1;
    private Player2Screen player2;

    public MidGame2(Start2Player startScreen, Player2Screen player1, Player2Screen player2) {
        this.battleShip = startScreen;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void player1Turn() {
        player1.getSelfGrid().setSelfGridListener(false);
        player1.getAttackGrid().setAttackGridListener(true);
    }

    public void player2turn() {
        player2.getSelfGrid().setSelfGridListener(false);
        player2.getAttackGrid().setAttackGridListener(true);
    }
}
