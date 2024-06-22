package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameButton extends JButton {


    private BufferedImage backgroundImage;

    public GameButton(BufferedImage bufferedImage) throws HeadlessException {
        this();
        this.backgroundImage = bufferedImage;
    }

    public GameButton() {
        super();
        setFont(AssetUtils.gameFont.deriveFont(20f));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {


        renderBackground(g);

        super.paintComponent(g);
    }

    private void renderBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

}
