package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.entity.player.PlayerBoard;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GameLabel;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.ui.component.PlayerBoardPanel;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class EndGameScreen extends GamePanel implements ComponentListener {

    private final BattleShip battleShip;
    private GamePanel resultBoard, statisticPanel;
    private GameButton backToMainMenuButton;
    private PlayerBoardPanel playerBoardPanel;
    private PlayerBoard opponentBoard = new PlayerBoard(10, GameManager.gamePlayer);
    private Box endPanel;
    private boolean isWin = false;

    public EndGameScreen(BattleShip battleShip) {
        this.battleShip = battleShip;
        this.setBackgroundImage(AssetUtils.loadImage("/images/ground.png"));
        setLayout(null);
        addComponentListener(this);
        initComponents();
    }

    private void initComponents() {
        initResultBoard();
        initEndGameLabel();
        initButton();
    }

    private void initResultBoard() {
        resultBoard = new GamePanel();
        resultBoard.setBackground(new Color(.1f, .45f, .4f, .5f));
        resultBoard.setRadius(40);
        resultBoard.setLayout(new BoxLayout(resultBoard, BoxLayout.X_AXIS));

        initPlayerBoardPanel();
        initStatisticPanel();

        add(resultBoard, -1);
    }

    private void initEndGameLabel() {

        endPanel = Box.createHorizontalBox();
        endPanel.add(Box.createHorizontalGlue());

        GameButton endGameLabel = new GameButton(
                AssetUtils.getImage(AssetUtils.ASSET_TEXT_FIELD_2));
        endGameLabel.setText(isWin ? "You Win" : "You Lose");
        endGameLabel.setPreferredSize(new Dimension(300, 100));
        endGameLabel.setMaximumSize(new Dimension(300, 100));

        endGameLabel.setTextSize(26);
        endPanel.add(endGameLabel);
        endPanel.add(Box.createHorizontalGlue());


        add(endPanel, 0);
    }

    private void initButton() {
        backToMainMenuButton = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_2));
        backToMainMenuButton.setText("Back to Menu");
        backToMainMenuButton.setSize(new Dimension(200, 70));
        backToMainMenuButton.addActionListener(e -> {
            battleShip.changeScreen(ScreenManager.MENU_SCREEN);
        });

        add(backToMainMenuButton, 0);
    }

    private void initPlayerBoardPanel() {
        playerBoardPanel = new PlayerBoardPanel(opponentBoard);


        resultBoard.add(playerBoardPanel);
    }

    private void initStatisticPanel() {
        Color color = Color.white;
        final Box titleBox = Box.createHorizontalBox();
        titleBox.add(Box.createHorizontalGlue());
        titleBox.add(new GameLabel("Statistic") {{
            setForeground(color);

        }});
        titleBox.add(Box.createHorizontalGlue());


        statisticPanel = new GamePanel();
        statisticPanel.setLayout(new BoxLayout(statisticPanel, BoxLayout.Y_AXIS));
        statisticPanel.add(titleBox);
        statisticPanel.add(Box.createVerticalStrut(10));
        if (battleShip.getGameManager() != null && battleShip.getGameManager()
                .getCurrentGamePlay() != null) {
            statisticPanel.add(new GameLabel("Total Shot: " + battleShip.getGameManager()
                    .getCurrentGamePlay()
                    .getTotalShot()) {{
                setForeground(color);
            }});
        }
        //        statisticPanel.add(new GameLabel("Total Hit: " + 1){{
        //            setForeground(color);
        //        }});
        //        statisticPanel.add(new GameLabel("Total Miss: " + 1){{
        //            setForeground(color);
        //        }});
        //        statisticPanel.add(new GameLabel("Total Ship Destroyed: " + 1){{
        //            setForeground(color);
        //        }});
        statisticPanel.add(new GameLabel("Score: " + GameManager.gamePlayer.getScore()) {{
            setForeground(color);
        }});


        resultBoard.add(statisticPanel);
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public PlayerBoard getOpponentBoard() {
        return opponentBoard;
    }

    public void setOpponentBoard(PlayerBoard opponentBoard) {
        this.opponentBoard = opponentBoard;
        updateComponents();
    }

    private void updateComponents() {
        int marginX = 600;
        int marginY = 150;

        resultBoard.setSize(new Dimension(getWidth() - marginX, getHeight() - marginY));
        resultBoard.setLocation(getWidth() / 2 - resultBoard.getWidth() / 2,
                getHeight() / 2 - resultBoard.getHeight() / 2
        );

        endPanel.setSize(new Dimension(300, 100));
        endPanel.setLocation((getWidth() - endPanel.getWidth()) / 2,
                (marginY - endPanel.getHeight()) / 2
        );


        playerBoardPanel.setMaximumSize(new Dimension(resultBoard.getWidth() - 300,
                resultBoard.getHeight() - 150
        ));
        if (opponentBoard != null) {
            opponentBoard.setLocation(50, 0);
            opponentBoard.setSize(playerBoardPanel.getMaximumSize().width,
                    playerBoardPanel.getMaximumSize().height
            );
            opponentBoard.update();
        }


        statisticPanel.setMaximumSize(new Dimension(300, resultBoard.getHeight() - 150));


        backToMainMenuButton.setLocation((getWidth() - backToMainMenuButton.getWidth()) / 2,
                (resultBoard.getY() + resultBoard.getHeight()) - backToMainMenuButton.getHeight() / 2
        );

    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateComponents();
        repaint();
        updateUI();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintChildren(g);
    }

    public BattleShip getBattleShip() {
        return battleShip;
    }
}
