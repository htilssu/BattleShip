package com.htilssu.entity.component;

import com.htilssu.ui.component.GamePanel;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public abstract class BattleGrid extends JPanel {

    GamePanel self;
    private JPanel temp;
    private BufferedImage backgroundImage;

    public BattleGrid() {
        self = new GamePanel();
        self.setLayout(new GridLayout(10, 10));
        self.setOpaque(false); // Đảm bảo các ô trong suốt

        backgroundImage = AssetUtils.loadImage("/images/backgroundgame2.png");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                temp = getCell();
                self.add(temp);
            }
        }

        this.add(self);
    }

    protected abstract JPanel getCell();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 20; // Bán kính góc bo tròn
            int borderWidth = 5; // Độ rộng viền
            int width = getWidth() - borderWidth;
            int height = getHeight() - borderWidth;

            // Vẽ viền
            RoundRectangle2D border = new RoundRectangle2D.Float(borderWidth / 2, borderWidth / 2,
                    width, height, arc, arc);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.draw(border);

            // Vẽ nền với góc bo tròn
            RoundRectangle2D background = new RoundRectangle2D.Float(borderWidth / 2,
                    borderWidth / 2, width, height, arc, arc);
            g2.setClip(background);
            g2.drawImage(backgroundImage, borderWidth / 2, borderWidth / 2, width, height, this);

            g2.dispose();
        }
    }

    //return the cell that selected at point p
    public JPanel getComponentAt(Point p) {
        for (Component child : self.getComponents()) {
            if (child.getBounds().contains(p)) {
                return (JPanel) child;
            }
        }
        return null;
    }
}