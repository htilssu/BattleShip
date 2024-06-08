package com.htilssu.ui.component;

import com.htilssu.entity.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GameButton extends JButton {

    Sprite sprite;

    public GameButton(Sprite sprite) throws HeadlessException {
        this.sprite = sprite;
        this.sprite.setDimension(this.getSize());
        this.sprite.setLocation(this.getLocation());
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        sprite.render(g);
    }


}
