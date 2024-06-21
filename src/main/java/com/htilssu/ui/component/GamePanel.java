package com.htilssu.ui.component;

import com.htilssu.util.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private BufferedImage backgroundImage;
    private BufferedImage roundBackgroundImage;
    private int radius = 0;

    public GamePanel(BufferedImage bufferedImage) {
        this();
        setBackgroundImage(bufferedImage);
    }

    public GamePanel() {
        super();
        setBackground(Color.TRANSPARENT);
    }

    public static GamePanel createVerticalBox(){
        var gamePanel = new  GamePanel();
        gamePanel.setLayout(new BoxLayout(gamePanel,BoxLayout.Y_AXIS));
        return gamePanel;
    }

    public static GamePanel createHorizontalBox() {
        var gamePanel = new GamePanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
        return gamePanel;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        roundBackgroundImage = backgroundImage;
        setRoundBackgroundImage(roundBackgroundImage);
    }

    public BufferedImage getRoundBackgroundImage() {
        return roundBackgroundImage;
    }

    public void setRoundBackgroundImage(BufferedImage roundBackgroundImage) {
        this.roundBackgroundImage = roundBackgroundImage;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
        super.paintComponent(g2d);
        renderBackground(g2d);
        g2d.dispose();
    }

    private void renderBackground(Graphics g) {
        if (roundBackgroundImage != null) {
            g.drawImage(roundBackgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
