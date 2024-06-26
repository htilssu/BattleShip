package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.dataPlayer.PlayerData;
import com.htilssu.manager.SoundManager;
import com.htilssu.state.BeginGame2;
import com.htilssu.state.EndGame2;
import com.htilssu.state.GameState;
import com.htilssu.state.MidGame2;
import com.htilssu.ui.component.GameButton;
import com.htilssu.util.AssetUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

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

    private JLabel loadingImageLabel;
    private ImageIcon[] images;

    public Start2Player(){}

    public Start2Player(BattleShip battleShip) {
        this.battleShip = battleShip;

        setLayout(new GridBagLayout());
        loadImageScreenStart();
        SoundManager.playBackGround(SoundManager.BACKGROUND_TEST);

        //loading image
        loadingImageLabel = new JLabel();
        loadingImageLabel.setVisible(false);

        GameButton btnStart = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_ATTACK), 1);
        btnStart.setPreferredSize(new Dimension(120, 60));
        btnStart.setText("Start");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa JButton
        add(btnStart, gbc); // Thêm JButton vào Start2Player

        // Đặt hình ảnh loading dưới thanh progress
        gbc.gridy = 2;
        add(loadingImageLabel, gbc);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playSound(SoundManager.START_SOUND);
                btnStart.setEnabled(false);
                loadingImageLabel.setVisible(true);
                startLoading();
            }
        });
    }

    private void startLoading() {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Giả lập tải tài nguyên game
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(25); // Giả lập thời gian tải tài nguyên
                    publish(i); // Cập nhật tiến trình
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Cập nhật giao diện với tiến trình hiện tại
                int progress = chunks.get(chunks.size() - 1);
                // Cập nhật hình ảnh loading
                int imageIndex = (progress / 25) % images.length;
                loadingImageLabel.setIcon(images[imageIndex]);
            }

            @Override
            protected void done() {
                SwingUtilities.getWindowAncestor(Start2Player.this).dispose();
                // Khi việc tải tài nguyên hoàn thành
                try {
                    get();
                    loadingImageLabel.setVisible(false);
                    SetNew();
                    player1Turn();
                    player2turn();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void loadImageScreenStart() {
        backgroundImage = AssetUtils.loadImage("/images/sea_of_thief.png");
        // Tải hình ảnh
        images = new ImageIcon[] {
                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading1.png"), 210, 35),
                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading2.png"), 210, 35),
                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading3.png"), 210, 35),
                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading4.png"), 210, 35)
        };
    }

    public void SetNew()
    {
        //SoundManager.playBackGround(SoundManager.BACKGROUND_TEST);
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
    public void player1Turn() {
        state.player1Turn();
    }

    @Override
    public void player2turn() {
        state.player2turn();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
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

    public boolean getTakeTurnAttack() {
        return takeTurnAttack;
    }

    public void setTakeTurnAttack(boolean isPlayerTurn) {
        this.takeTurnAttack = isPlayerTurn;
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
