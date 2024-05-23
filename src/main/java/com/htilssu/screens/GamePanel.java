package com.htilssu.screens;

import com.htilssu.BattleShip;
import com.htilssu.settings.GameSetting;
import com.htilssu.utils.AssetUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final BattleShip window;

    public GamePanel(BattleShip battleShip) {
        this.window = battleShip;
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = AssetUtil.loadAsset("/ship.png");
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < GameSetting.TILE_IN_WIDTH; i++) {
            for (int j = 0; j < GameSetting.TILE_IN_HEIGHT; j++) {
                g2d.drawRect(i * GameSetting.TILE_SIZE, j * GameSetting.TILE_SIZE, GameSetting.TILE_SIZE, GameSetting.TILE_SIZE);
            }
        }

    }
}
