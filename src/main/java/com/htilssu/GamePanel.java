package com.htilssu;

import com.htilssu.utils.AssetUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final GamePanel window;

    public GamePanel(BattleShip battleShip) {
        this.window = this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = AssetUtil.loadAsset("/ship.png");
        Graphics2D g2d = (Graphics2D) g;
    }
}
