package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

  private final GamePanel window;
  private final SelfGrid selfGrid;

  int boardWidth;
  int boardHeight;
  int titleSize;

  public GamePanel(BattleShip battleShip) {
    this.window = this;

    this.setLayout(new BorderLayout());
    this.selfGrid = new SelfGrid("play", battleShip);
    //        this.add(selfGrid, BorderLayout.CENTER);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    BufferedImage image = AssetUtils.loadAsset("/ship.png");
    Graphics2D g2d = (Graphics2D) g;

    boardWidth = GameSetting.TILE_IN_WIDTH;
    boardHeight = GameSetting.TILE_IN_HEIGHT;
    titleSize = GameSetting.TILE_SIZE;
    draw(g2d);
  }

  private void draw(Graphics g) {}
}
