package com.htilssu.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameButton extends JButton {


    private BufferedImage backgroundImage;

    public GameButton(BufferedImage bufferedImage) throws HeadlessException {
        this();
        this.backgroundImage = bufferedImage;
    }

    public GameButton() {
        super();
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderBackground(g);
    }

    private void renderBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }


}
