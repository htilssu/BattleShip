package com.htilssu.screens;

import com.htilssu.BattleShip;
import com.htilssu.settings.GameSetting;

import javax.swing.*;
import java.awt.*;


public class MenuScreen extends JPanel {
    public Rectangle playButton = new Rectangle(GameSetting.WIDTH / 2 + 120, 150, 100, 50);
    public Rectangle helpButton = new Rectangle(GameSetting.WIDTH / 2 + 120, 250, 100, 50);
    public Rectangle quitButton = new Rectangle(GameSetting.WIDTH / 2 + 120, 350, 100, 50);

    public MenuScreen(BattleShip battleShip) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("TimesRoman", Font.BOLD, 50));
        g2d.setColor(Color.black);
        g.drawString("Menu", GameSetting.WIDTH / 2, 100);
        g2d.draw(playButton);
        g2d.draw(helpButton);
        g2d.draw(quitButton);
    }
}