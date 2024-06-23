package com.htilssu.ui.screen;

import com.htilssu.entity.component.AttackGrid;
import com.htilssu.entity.component.SelfGrid;
import com.htilssu.listener.ButtonClickListener;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GamePanel;
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
        GamePanel contentPane = new GamePanel(backgroundImage);
        contentPane.setLayout(null);
        setContentPane(contentPane); // Sử dụng lớp JPanel mới làm nội dung của JFrame

        //full kich thuoc man hinh.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));  //kich thuoc tu gameSetting

        // Thêm các thành phần khác vào contentPane
        SelfGrid selfGrid = new SelfGrid(name, startScreen);
        AttackGrid attackGrid = new AttackGrid(name, startScreen, this);

        // Đặt vị trí và kích thước cho các thành phần
        selfGrid.setBounds(1000, 70, 360, 360); //  vị trí và kích thước cho SelfGrid
        attackGrid.setBounds(100, 70, 420, 420); //  vị trí và kích thước cho AttackGrid


        GameButton btnNext = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_1),1); //truyen hinh nen duoc
        btnNext.setBounds(700, 290, 100, 30); //xet vi tri
        btnNext.setText("Next");

        GameButton btnRotate = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_2),1);
        btnRotate.setBounds(1000, 470, 170, 40);
        btnRotate.setText("Horizontal");
        //Nut an hien Luoi dat thuyen
        GameButton btnHideSelfGrid = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_1),1);
        btnHideSelfGrid.setBounds(1190, 470, 210, 40);
        btnHideSelfGrid.setText("Hide SelfGrid");

        //Add cac phan tu vao GamePanel
        contentPane.add(selfGrid);
        contentPane.add(attackGrid);
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

    private void setInforBox(GamePanel gamePanel, String name) {
        GamePanel gamePanelBox = new GamePanel();
        // Tạo các hộp chứa thông tin
        JLabel statusLabel = new JLabel("Status for " + name);
        statusLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        statusLabel.setForeground(Color.RED);

        JLabel ownShipsLabel = new JLabel("So tau cua minh : ");
        shipBeginning = new JLabel("" + Integer.toString(size));
        setTypeText(ownShipsLabel);
        setTypeText(shipBeginning);

        JLabel ownShipsSunkLabel = new JLabel("Tau da bi chim : ");
        ownShipSunk = new JLabel("" + Integer.toString(size));
        setTypeText(ownShipsSunkLabel);
        setTypeText(ownShipSunk);

        JLabel enemyShipsSunkLabel = new JLabel("Tau dich da bi chim : ");
        enemyShipSunk = new JLabel("" + size);
        setTypeText(enemyShipsSunkLabel);
        setTypeText(enemyShipSunk);

        // Thêm các thông tin vào contentPane
        gamePanelBox.add(createCenteredPanel(statusLabel));
        gamePanelBox.add(Box.createVerticalStrut(10));

        gamePanelBox.add(createCenteredPanel(ownShipsLabel, shipBeginning));
        gamePanelBox.add(Box.createVerticalStrut(2));

        gamePanelBox.add(createCenteredPanel(ownShipsSunkLabel, ownShipSunk));
        gamePanelBox.add(Box.createVerticalStrut(2));

        gamePanelBox.add(createCenteredPanel(enemyShipsSunkLabel, enemyShipSunk));

        gamePanelBox.setBackground(new Color(0, 0, 0, 0.5f));
        gamePanelBox.setBounds(600, 460, 300, 300);
        gamePanelBox.setLayout(new BoxLayout(gamePanelBox, BoxLayout.Y_AXIS));

        gamePanel.add(gamePanelBox);
    }
    private static JPanel createCenteredPanel(JComponent... components) {
        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.add(Box.createHorizontalGlue());
        for (JComponent component : components) {
            panel.add(component);
            panel.add(Box.createHorizontalGlue());
        }
        return panel;
    }
    private void setTypeText(JLabel label) {
        label.setFont(new Font("Roboto", Font.BOLD, 15));
        label.setForeground(Color.RED);
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

}
