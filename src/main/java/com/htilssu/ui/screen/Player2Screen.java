package com.htilssu.ui.screen;

import com.htilssu.listener.ButtonClickListener;
import com.htilssu.screen.Start2Player;
import com.htilssu.entity.component.AttackGrid;
import com.htilssu.entity.component.SelfGrid;
import com.htilssu.setting.GameSetting;
import com.htilssu.setting.SoundPlayer;
import com.htilssu.util.AssetUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player2Screen extends JFrame {
    int size;
    boolean isbeginningOfTheGameOfPlayer1 = true;
    boolean isbeginningOfTheGameOfPlayer2 = true;
    Start2Player battleShip;
    public JLabel ownShipSunk;
    JLabel shipBeginning;
    public JLabel enemyShipSunk;
    private BufferedImage backgroundImage;
    private boolean isSelfGridVisible = true; // Biến để theo dõi trạng thái hiển thị của SelfGrid
    public static final int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH ;

    public Player2Screen(String name, boolean show, Start2Player startScreen) {
        super(name);
        this.battleShip = startScreen;
        SoundPlayer soundPlayer = new SoundPlayer();
        //am thanh nen
        soundPlayer.playSound_Nen();

        loadBackgroundImage();

        // Tạo lớp JPanel mới để chứa các thành phần khác và vẽ hình nền
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane); // Sử dụng lớp JPanel mới làm nội dung của JFrame

        //full kich thuoc man hinh.
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));  //kich thuoc tu gameSetting

        // Thêm các thành phần khác vào contentPane
        SelfGrid selfGrid = new SelfGrid(name, startScreen);
        AttackGrid attackGrid = new AttackGrid(name, startScreen, this);
        JLabel nameLabel = new JLabel(name);

        // Đặt vị trí và kích thước cho các thành phần
        selfGrid.setBounds(590, 50, 280, 270); //  vị trí và kích thước cho SelfGrid
        attackGrid.setBounds(20, 50, 320, 320); //  vị trí và kích thước cho AttackGrid
        nameLabel.setBounds(350, 10, 100, 30); //  vị trí và kích thước cho nameLabel

        contentPane.add(selfGrid);
        contentPane.add(attackGrid);
        contentPane.add(nameLabel);

        JButton next = new JButton("next");
        JButton rotateButton = new JButton("Đặt Dọc");
        JButton toggleSelfGridButton = new JButton("Hide SelfGrid"); // Thêm nút ẩn/hiện SelfGrid

        // Đặt vị trí và kích thước cho các nút
        next.setBounds(350, 400, 100, 30); // vị trí và kích thước cho nút next
        rotateButton.setBounds(600, 360, 100, 30); // vị trí và kích thước cho nút rotateButton
        toggleSelfGridButton.setBounds(750, 360, 100, 30); // Đặt vị trí và kích thước cho nút toggleSelfGridButton

        contentPane.add(next);
        contentPane.add(rotateButton);
        contentPane.add(toggleSelfGridButton);

        setInforBox(contentPane, name);

        rotateButton.addActionListener(e -> {
            selfGrid.rotateShip();
            if (rotateButton.getText().equals("Đặt Dọc")) {
                rotateButton.setText("Đặt Ngang");
            } else {
                rotateButton.setText("Đặt Dọc");
            }
        });

        toggleSelfGridButton.addActionListener(e -> {
            isSelfGridVisible = !isSelfGridVisible;
            selfGrid.setVisible(isSelfGridVisible);
            toggleSelfGridButton.setText(isSelfGridVisible ? "Hide SelfGrid" : "Show SelfGrid");
        });

        ButtonClickListener buttonClickListener = new ButtonClickListener(name, startScreen, shipBeginning, isbeginningOfTheGameOfPlayer1, this);
        next.addActionListener(buttonClickListener);

        pack();
        setVisible(show);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        setLocation(x, y);
    }
    //am thanh

    private void loadBackgroundImage() {
        backgroundImage = AssetUtils.loadAsset("/sea1.png");
    }

    private void setInforBox(JPanel contentPane, String name) {
        // Tạo các hộp chứa thông tin
        JLabel statusLabel = new JLabel("Status for: " + name);
        JLabel ownShipsLabel = new JLabel("Own ships: ");
        shipBeginning = new JLabel("" + Integer.toString(size));
        JLabel ownShipsSunkLabel = new JLabel("Tau da bi danh ");
        ownShipSunk = new JLabel("" + Integer.toString(size));
        JLabel enemyShipsSunkLabel = new JLabel("Tau dich da bi danh ");
        enemyShipSunk = new JLabel("" + Integer.toString(size));

        // Đặt vị trí và kích thước cho các thông tin
        statusLabel.setBounds(350, 50, 200, 30);
        ownShipsLabel.setBounds(350, 90, 100, 30);
        shipBeginning.setBounds(460, 90, 50, 30);
        ownShipsSunkLabel.setBounds(350, 130, 100, 30);
        ownShipSunk.setBounds(460, 130, 50, 30);
        enemyShipsSunkLabel.setBounds(350, 170, 100, 30);
        enemyShipSunk.setBounds(460, 170, 50, 30);

        // Thêm các thông tin vào contentPane
        contentPane.add(statusLabel);
        contentPane.add(ownShipsLabel);
        contentPane.add(shipBeginning);
        contentPane.add(ownShipsSunkLabel);
        contentPane.add(ownShipSunk);
        contentPane.add(enemyShipsSunkLabel);
        contentPane.add(enemyShipSunk);
    }

    public void showScreen(){
        this.setVisible(true);
    }

    public void hideScreen() {
        this.setVisible(false);
    }

    public SelfGrid getSelfGrid(){

        for(Component child : this.getContentPane().getComponents()){

            if(child instanceof SelfGrid ){
                return (SelfGrid)child;
            }
        }
        return null;
    }

    public AttackGrid getAttackGrid(){
        for(Component child : this.getContentPane().getComponents()){
            if(child instanceof AttackGrid ){
                return (AttackGrid) child;
            }

        }
        return null;
    }
    public JButton getNextButton(){
        for(Component child : this.getContentPane().getComponents()){
            if(child instanceof JButton ){
                return (JButton) child;
            }

        }
        return null;
    }
    public boolean getIsbeginningOfTheGameOfPlayer2() {
        return isbeginningOfTheGameOfPlayer2;
    }
}
