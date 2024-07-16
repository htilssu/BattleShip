package com.htilssu.ui.component;

import com.htilssu.entity.player.PlayerBoard;

import java.awt.*;

public class PlayerBoardPanel extends GamePanel {

    PlayerBoard playerBoard;

    public PlayerBoardPanel(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerBoard != null) {
            playerBoard.render(g);
        }
    }
}
