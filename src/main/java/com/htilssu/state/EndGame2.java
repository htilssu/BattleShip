package com.htilssu.state;

import com.htilssu.screen.Start2Player;
import com.htilssu.ui.screen.Player2Screen;

public class EndGame2 implements GameState {
        private Start2Player battleShip;
        private Player2Screen player1;
        private Player2Screen player2;

    public EndGame2(Start2Player startScreen, Player2Screen player1, Player2Screen player2){
            this.battleShip = startScreen;
            this.player1 = player1;
            this.player2 = player2;
        }

        public void player1Turn (){
            System.out.println("end of the game player 1 ");
            System.exit(0);
        }
        public void player2turn (){
            System.out.println("end of the game player 2 ");
            System.exit(0);
        }
    }
