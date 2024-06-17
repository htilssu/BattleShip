package com.htilssu.ui.component;

import com.htilssu.entity.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GameButton extends JButton {


    private final BufferedImage backgroundImage;

    public GameButton(BufferedImage bufferedImage) throws HeadlessException {
      this.backgroundImage = bufferedImage;
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
