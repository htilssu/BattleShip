package com.htilssu.entity.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BattleGrid extends JPanel {
  JPanel self;
    private JPanel temp;
  private BufferedImage backgroundImage;

  public BattleGrid() {
    self = new JPanel();
    self.setLayout(new GridLayout(10,10));
    self.setOpaque(false); // Đảm bảo các ô trong suốt
      backgroundImage = AssetUtils.loadImage("/images/sea.png");

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
      g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
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