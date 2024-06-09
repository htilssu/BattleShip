package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Màn hình chơi game
 */
public class PlayScreen extends JPanel
        implements MouseListener, ComponentListener, MouseMotionListener {

    private final BattleShip window;

    public PlayScreen(BattleShip battleShip) {
        this.window = battleShip;
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
        setFocusable(true);
        setBackground(Color.GRAY);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
        addKeyListener(window);
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

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // empty
    }

    @Override
    public void componentResized(ComponentEvent e) {
        window.getGameManager().getCurrentGamePlay().update();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // empty
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // empty
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // empty
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());
        window.getGameManager().getCurrentGamePlay().handleMouseMoved(pos);
        repaint();
    }

}
