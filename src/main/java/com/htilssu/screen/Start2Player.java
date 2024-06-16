package com.htilssu.screen;

import com.htilssu.BattleShip;
import com.htilssu.dataPlayer.PlayerData;
import com.htilssu.setting.SoundPlayer;
import com.htilssu.state.BeginGame2;
import com.htilssu.state.EndGame2;
import com.htilssu.state.GameState;
import com.htilssu.state.MidGame2;
import com.htilssu.ui.screen.Player2Screen;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Start2Player extends JPanel implements GameState {
    GameState beginningOfTheGame;
    GameState middleOfTheGame;
    GameState endOfTheGame;
    GameState state;
    boolean takeTurnAttack;

    PlayerData player1Data;
    PlayerData player2Data;
    Player2Screen player1;
    Player2Screen player2;

    private BattleShip battleShip;
    private BufferedImage backgroundImage;
    SoundPlayer soundPlayer = new SoundPlayer();

    public Start2Player(BattleShip battleShip) {
        this.battleShip = battleShip;

        setLayout(new GridBagLayout());
        loadBackground();

        JButton playButton = new JButton("Bắt Đầu");
        playButton.setPreferredSize(new Dimension(100, 50)); // Kích thước cụ thể cho JButton

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa JButton
        add(playButton, gbc); // Thêm JButton vào Start2Player

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Dang bi loi am thanh cho nay
                soundPlayer.playSound_Start();
                // Đóng màn hình hiện tại
                SwingUtilities.getWindowAncestor(Start2Player.this).dispose();
                SetNew();
                soundPlayer.wait_Giay(150);
                player1Turn();
                player2turn();
            }
        });
    }

    public void SetNew()
    {
        SoundPlayer soundPlayer = new SoundPlayer();
        //am thanh nen
        soundPlayer.playSound_Nen();
        player1 = new Player2Screen("Player1", true,this);
        player2 = new Player2Screen("Player2", false,this);
        player1Data = new PlayerData(player1);
        player2Data = new PlayerData(player2);
        beginningOfTheGame = new BeginGame2(this, player1,player2);
        middleOfTheGame = new MidGame2(this, player1,player2);
        endOfTheGame = new EndGame2(this, player1,player2);
        state = beginningOfTheGame; //set the state of the game to be the beginning of the game
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    private void loadBackground(){
        backgroundImage = AssetUtils.loadAsset("/player2game.png");
    }

    @Override
    public void player1Turn() {
        state.player1Turn();
    }
    @Override
    public void player2turn() {
        state.player2turn();
    }

    public GameState getMiddleOfTheGame() {
        return middleOfTheGame;
    }

    public GameState getEndOfTheGame() {
        return endOfTheGame;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setTakeTurnAttack(boolean isPlayerTurn) {
        this.takeTurnAttack = isPlayerTurn;
    }

    public boolean getTakeTurnAttack() {
        return takeTurnAttack;
    }

    public PlayerData getPlayer2Data() {
        return player2Data;
    }

    public PlayerData getPlayer1Data() {
        return player1Data;
    }

    public Player2Screen getPlayer1() {
        return player1;
    }

    public Player2Screen getPlayer2() {
        return player2;
    }
}
