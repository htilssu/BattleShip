package com.htilssu.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.GameManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Màn hình chơi game */
public class PlayScreen extends JPanel implements MouseListener, ComponentListener {

  private final BattleShip window;

  public PlayScreen(BattleShip battleShip) {
    this.window = battleShip;
    setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    setFocusable(true);
    setBackground(Color.GRAY);
    addMouseListener(this);
    addComponentListener(this);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    window.getGameManager().getCurrentGamePlay().renderShootBoard(g2d);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point position = new Point(e.getX(), e.getY());

    // handle click on gameBoard
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
    window.getGameManager().getCurrentGamePlay().getCurrentPlayer().getBoard().update();
  }

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentShown(ComponentEvent e) {}

  @Override
  public void componentHidden(ComponentEvent e) {}
}
