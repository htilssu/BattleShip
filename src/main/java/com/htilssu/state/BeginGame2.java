package com.htilssu.state;

import com.htilssu.screen.Start2Player;
import com.htilssu.ui.screen.Player2Screen;

public class BeginGame2 implements GameState {
    private Start2Player battleShip;
    private Player2Screen player1;
    private Player2Screen player2;

    public BeginGame2(Start2Player battleShip,Player2Screen player1,Player2Screen player2){
        this.player1 = player1;
        this.player2 = player2;
        this.battleShip = battleShip;
    }

    //turn ON SelfGrid for player 1
    public void player1Turn (){
        player1.getSelfGrid().setSelfGridListener(true);
    }

    //turn OFF SelfGrid for player 2
    public void player2turn (){
        player2.getSelfGrid().setSelfGridListener(true);
        battleShip.setState(battleShip.getMiddleOfTheGame());
    }
}
