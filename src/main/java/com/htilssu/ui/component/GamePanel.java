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
        setRadius(radius);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;

        if (getWidth() == 0 || getHeight() == 0) return;

        var buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics = buff.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        var roundRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius);

        graphics.setClip(roundRect);
        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        else {
            graphics.setColor(getBackground());
            setBackground(Color.TRANSPARENT);
            graphics.fill(roundRect);
        }
        graphics.dispose();

        roundBackgroundImage = buff;
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
