package com.htilssu.ui.component;

import com.htilssu.util.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GameButton extends JButton {
  BufferedImage asset;

  public GameButton(BufferedImage asset) throws HeadlessException {
    this.asset = asset;
    setBorder(null);
    setBackground(Color.BLACK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(asset, 0, 0, getWidth(), getHeight(), null);
  }
}
