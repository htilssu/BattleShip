package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class GameTextField extends JTextField {

    BufferedImage backgroundImage;
    int radius = 0;

    public GameTextField(BufferedImage backgroundImage) {
        this();
        this.backgroundImage = backgroundImage;
    }

    public GameTextField() {
        super();
        setBorder(null);
        //set font to center
        setHorizontalAlignment(JTextField.CENTER);
        setFont(AssetUtils.gameFont.deriveFont(20f));
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
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
        //anti alias
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //set clip round
        g2d.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        paintBackground(g2d);


        super.paintComponent(g2d);
    }

    private void paintBackground(Graphics2D g2d) {
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

}
