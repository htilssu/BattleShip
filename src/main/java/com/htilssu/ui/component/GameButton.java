package com.htilssu.ui.component;

import com.htilssu.entity.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GameButton extends JButton {

  Sprite sprite;

  public GameButton(Sprite sprite) throws HeadlessException {
    this.sprite = sprite;
    this.sprite.setLocation(this.getLocation());
    setBorder(null);
    setBackground(Color.BLACK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    sprite.render(g);
  }
}
