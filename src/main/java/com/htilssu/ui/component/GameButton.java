package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameButton extends JButton {

    private BufferedImage backgroundImage;
    private boolean isHovered = false;

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

    //dung de xet su kien re chuot de thay doi background
    public GameButton(BufferedImage normalImage, int a) throws HeadlessException {
        this();
        this.backgroundImage = normalImage;

        // Thêm MouseListener để xử lý sự kiện rê chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setFont(getFont().deriveFont(23f));  // Tăng kích thước chữ
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setFont(getFont().deriveFont(20f));  // Giảm kích thước chữ về ban đầu
                repaint();
            }
        });
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
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

    public void setTextSize(int size) {
        this.setFont(getFont().deriveFont((float) size));
    }

}
