package com.htilssu.screen;

import com.htilssu.BattleShip;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình chơi game
 */
public class PlayScreen extends JPanel {

    private final BattleShip window;

    public PlayScreen(BattleShip battleShip) {
        this.window = battleShip;
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(AssetUtils.getAsset(AssetUtils.ASSET_BOARD_FRAME), 0, 0, 10 * GameSetting.TILE_SIZE, 10 * GameSetting.TILE_SIZE, null);

    }
}
