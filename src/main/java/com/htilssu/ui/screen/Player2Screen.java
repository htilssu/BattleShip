package com.htilssu.ui.screen;

import com.htilssu.entity.component.AttackGrid;
import com.htilssu.entity.component.SelfGrid;
import com.htilssu.listener.ButtonClickListener;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player2Screen extends JFrame {

    public static final int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH;
    public JLabel ownShipSunk;
    public JLabel enemyShipSunk;
    int size;
    boolean isbeginningOfTheGameOfPlayer1 = true;
    boolean isbeginningOfTheGameOfPlayer2 = true;
    Start2Player battleShip;
    JLabel shipBeginning;
    private BufferedImage backgroundImage;
    private boolean isSelfGridVisible = true; // Biến để theo dõi trạng thái hiển thị của SelfGrid

    public Player2Screen(String name, boolean show, Start2Player startScreen) {
        super(name);
        this.battleShip = startScreen;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));  //kich thuoc tu gameSetting

        // Thêm các thành phần khác vào contentPane
        SelfGrid selfGrid = new SelfGrid(name, startScreen);
        AttackGrid attackGrid = new AttackGrid(name, startScreen, this);
        JLabel nameLabel = new JLabel(name);

        // Đặt vị trí và kích thước cho các thành phần
        selfGrid.setBounds(1000, 50, 360, 360); //  vị trí và kích thước cho SelfGrid
        attackGrid.setBounds(100, 50, 420, 420); //  vị trí và kích thước cho AttackGrid
        nameLabel.setBounds(350, 10, 100, 30); //  vị trí và kích thước cho nameLabel

        contentPane.add(selfGrid);
        contentPane.add(attackGrid);
        contentPane.add(nameLabel);

        GameButton btnNext = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_1),1); //truyen hinh nen duoc
        btnNext.setBounds(700, 290, 100, 30); //xet vi tri
        btnNext.setText("Next");

        GameButton btnRotate = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_2),1);
        btnRotate.setBounds(1000, 420, 170, 40);
        btnRotate.setText("Horizontal");
        //Nut an hien Luoi dat thuyen
        GameButton btnHideSelfGrid = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_1),1);
        btnHideSelfGrid.setBounds(1190, 420, 210, 40);
        btnHideSelfGrid.setText("Hide SelfGrid");

        contentPane.add(btnNext);
        contentPane.add(btnRotate);
        contentPane.add(btnHideSelfGrid);

        setInforBox(contentPane, name);

        btnRotate.addActionListener(e -> {
            selfGrid.rotateShip();
            if (btnRotate.getText().equals("Vertical")) {
                btnRotate.setText("Horizontal");
            } else {
                btnRotate.setText("Vertical");
            }
        });

        btnHideSelfGrid.addActionListener(e -> {
            isSelfGridVisible = !isSelfGridVisible;
            selfGrid.setVisible(isSelfGridVisible);
            btnHideSelfGrid.setText(isSelfGridVisible ? "Hide SelfGrid" : "Show SelfGrid");
        });

        ButtonClickListener buttonClickListener = new ButtonClickListener(name, startScreen, shipBeginning, isbeginningOfTheGameOfPlayer1, this);
        btnNext.setActionCommand("next");
        btnNext.addActionListener(buttonClickListener);

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
        backgroundImage = AssetUtils.loadImage("/images/sea1.png");
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
        statusLabel.setBounds(650, 490, 300, 30);
        ownShipsLabel.setBounds(650, 510, 300, 30);
        shipBeginning.setBounds(650, 530, 300, 30);
        ownShipsSunkLabel.setBounds(650, 550, 300, 30);
        ownShipSunk.setBounds(650, 570, 300, 30);
        enemyShipsSunkLabel.setBounds(650, 590, 300, 30);
        enemyShipSunk.setBounds(650, 610, 300, 30);

        // Thêm các thông tin vào contentPane
        contentPane.add(statusLabel);
        contentPane.add(ownShipsLabel);
        contentPane.add(shipBeginning);
        contentPane.add(ownShipsSunkLabel);
        contentPane.add(ownShipSunk);
        contentPane.add(enemyShipsSunkLabel);
        contentPane.add(enemyShipSunk);

        contentPane.setBackground(Color.BLUE);
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
