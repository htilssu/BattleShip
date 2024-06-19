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
        Shape rect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius);
        this.getGraphics().setClip(rect);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderBackground(g);
    }

    private void renderBackground(Graphics g) {
        if (roundBackgroundImage != null) {
            g.drawImage(roundBackgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
