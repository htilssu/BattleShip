package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomButton extends JButton {

    private BufferedImage normalImage;
    private boolean isHovered; // tao check xem da hover chua

    public CustomButton(String imagePath) {
        normalImage = AssetUtils.loadImage(imagePath);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { // khi tro chuot vao
                        isHovered = true;
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                    }
                });
    }

    public CustomButton(BufferedImage image) {
        this.normalImage = image;

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isHovered) { // neu da hover
            int shrinkAmount = 10; // Kích thước thu nhỏ
            int newWidth = getWidth() - shrinkAmount; // chieu rong moi khi hover
            int newHeight = getHeight() - shrinkAmount; // chieu dai moi khi hover
            int xOffset = (getWidth() - newWidth) / 2; // toa do x kho hover
            int yOffset = (getHeight() - newHeight) / 2; // toa do y khi hover
            g.drawImage(normalImage, xOffset, yOffset, newWidth, newHeight, this);
        }
        else {
            g.drawImage(normalImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
