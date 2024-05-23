package com.htilssu.screens;

import com.htilssu.utils.AssetUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomButton extends JButton {
    private BufferedImage backgroundImage;

    public CustomButton( String imagePath) {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("TimesRoman", Font.BOLD, 20));
        backgroundImage = AssetUtil.loadAsset(imagePath);


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
