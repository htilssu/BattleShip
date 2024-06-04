package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.entity.Ship;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Màn hình chơi game */
public class PlayScreen extends JPanel
    implements MouseListener, ComponentListener, MouseMotionListener, KeyListener {

  private final BattleShip window;

  public PlayScreen(BattleShip battleShip) {
    this.window = battleShip;
    setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    setFocusable(true);
    setBackground(Color.GRAY);
    addMouseListener(this);
    addMouseMotionListener(this);
    addComponentListener(this);
    addKeyListener(this);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g.drawImage(
        AssetUtils.getAsset(AssetUtils.ASSET_BACK_SEA_2), 0, 0, getWidth(), getHeight(), null);
    window.getGameManager().getCurrentGamePlay().render(g2d);
    g2d.setColor(Color.BLACK);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point position = new Point(e.getX(), e.getY());

    // handle click on gameBoard (shoot)
    window.getGameManager().getCurrentGamePlay().handleClick(position);
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void componentResized(ComponentEvent e) {
    window.getGameManager().getCurrentGamePlay().update();
  }

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentShown(ComponentEvent e) {}

  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {
    Point pos = new Point(e.getX(), e.getY());
    window.getGameManager().getCurrentGamePlay().handleMouseMoved(pos);
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_R -> {
        GamePlay cP = window.getGameManager().getCurrentGamePlay();
        if (cP.getGameMode() == GamePlay.WAITING_MODE) {
          if (cP.getDirection() == Ship.HORIZONTAL) {
            cP.setDirection(Ship.VERTICAL);
          } else {
            cP.setDirection(Ship.HORIZONTAL);
          }
        }
      }
    }
  }
}
