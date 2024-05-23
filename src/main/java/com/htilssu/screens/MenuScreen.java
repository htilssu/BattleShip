package com.htilssu.screens;

import com.htilssu.settings.GameSetting;
import com.htilssu.utils.AssetUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuScreen extends JPanel {
    private BufferedImage backgroundImage;
    public MenuScreen() {
        setLayout(null); // We will use absolute positioning
        loadBackgroundImage();
        createButtons();

    }
    private void loadBackgroundImage() {

            backgroundImage = AssetUtil.loadAsset("/sea.png");// Load background image

    }
    private void createButtons() {
        addButton( "/btnplay.png", (GameSetting.WIDTH-128)/2 , 100, 200, 100);
        addButton("/QUIT.png",(GameSetting.WIDTH-128)/2 , 200, 200, 100);
    }

    private void addButton( String imagePath, int x, int y, int width, int height) {
        CustomButton button = new CustomButton(imagePath);
        button.setBounds(x, y, width, height);
        add(button);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("TimesRoman", Font.BOLD, 50));
        g2d.setColor(Color.BLACK);
        g.drawString("Menu", (GameSetting.WIDTH-128) / 2, 80);
    }
}
