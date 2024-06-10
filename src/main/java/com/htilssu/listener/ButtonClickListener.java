package com.htilssu.listener;

import com.htilssu.ui.screen.Start2Player;
import com.htilssu.ui.screen.Player2Screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ButtonClickListener implements ActionListener {
    private String name;
    private Start2Player battleShip;
    private JLabel shipBeginning;
    private boolean isbeginningOfTheGameOfPlayer1;
    private Player2Screen player;

    public ButtonClickListener(String name, Start2Player startScreen, JLabel shipBeginning, boolean isbeginningOfTheGameOfPlayer1, Player2Screen player) {
        this.name = name;
        this.battleShip = startScreen;
        this.shipBeginning = shipBeginning;
        this.isbeginningOfTheGameOfPlayer1 = isbeginningOfTheGameOfPlayer1;
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("next")) {
            if (name.equals("Player1")) {
                int size = battleShip.getPlayer1Data().getFleet().size();
                if (isbeginningOfTheGameOfPlayer1) {
                    shipBeginning.setText(Integer.toString(size));
                    isbeginningOfTheGameOfPlayer1 = false;
                }
                if (!isbeginningOfTheGameOfPlayer1) {
                    battleShip.player1Turn();
                }
                player.hideScreen();
                battleShip.getPlayer2().showScreen();
            }
            if (name.equals("Player2")) {
                int size = battleShip.getPlayer2Data().getFleet().size();
                if (isbeginningOfTheGameOfPlayer1) {
                    shipBeginning.setText(Integer.toString(size));
                    isbeginningOfTheGameOfPlayer1 = false;
                }
                if (!isbeginningOfTheGameOfPlayer1) {
                    battleShip.player2turn();
                }
                player.hideScreen();
                battleShip.getPlayer1().showScreen();
            }
        } else if (e.getActionCommand().equals("Đặt Dọc")) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Đặt Dọc")) {
                button.setText("Đặt Ngang");
            } else {
                button.setText("Đặt Dọc");
            }
        }
    }
}
