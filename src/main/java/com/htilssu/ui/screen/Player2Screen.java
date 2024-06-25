package com.htilssu.ui.screen;

import com.htilssu.entity.component.AttackGrid;
import com.htilssu.entity.component.SelfGrid;
import com.htilssu.listener.ButtonClickListener;
import com.htilssu.manager.SoundManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Player2Screen extends JFrame implements ComponentListener {

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
    private GameButton turnSound;

    public Player2Screen(String name, boolean show, Start2Player startScreen) {
        super(name);
        this.battleShip = startScreen;

        loadBackgroundImage();
        addComponentListener(this);

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

        GameButton HeaderAttack = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_ATTACK)); //truyen hinh nen duoc
        HeaderAttack.setBounds(170, 17, 250, 50); //xet vi tri
        HeaderAttack.setText("ATTACK SHIP");
        GameButton HeaderSelf = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_ATTACK)); //truyen hinh nen duoc
        HeaderSelf.setBounds(1070, 17, 250, 50); //xet vi tri
        HeaderSelf.setText("SELF SHIP");

        GameButton btnNext = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_NEXT), 1); //truyen hinh nen duoc
        btnNext.setBounds(700, 290, 150, 50); //xet vi tri
        btnNext.setText("Next");

        GameButton btnRotate = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_BTNLISTENER), 1);
        btnRotate.setBounds(1000, 470, 170, 50);
        btnRotate.setText("Horizontal");
        //Nut an hien Luoi dat thuyen
        GameButton btnHideSelfGrid = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_BTNLISTENER), 1);
        btnHideSelfGrid.setBounds(1190, 470, 210, 50);
        btnHideSelfGrid.setText("Hide SelfGrid");

        //Add cac phan tu vao GamePanel
        contentPane.add(selfGrid);
        contentPane.add(attackGrid);
        contentPane.add(HeaderAttack);
        contentPane.add(HeaderSelf);
        contentPane.add(btnNext);
        contentPane.add(btnRotate);
        contentPane.add(btnHideSelfGrid);

        //cac du lieu dang choi game
        setInforBox(contentPane, name);
        setButtonGame(contentPane);

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

    private void setButtonGame(GamePanel gamePanel) {
        turnSound = new GameButton(AssetUtils.loadImage("/images/turnSound.png"));
        turnSound.setBounds(1450, 30, 60, 60);
        gamePanel.add(turnSound);
        turnSound.addActionListener(e -> {
            SoundManager.flagVolumeplaySound = !SoundManager.flagVolumeplaySound;
            if (SoundManager.flagVolumeplaySound) {
                turnSound.setBackgroundImage(AssetUtils.loadImage("/images/turnSound.png"));
            } else {
                turnSound.setBackgroundImage(AssetUtils.loadImage("/images/offSound.png"));
            }
        });

        repaint();

        // Tạo các mục cho JComboBox
        String[] items = { "Item 1", "Item 2", "Item 3", "Item 4" };
        // Tạo JComboBox nhưng không hiển thị ngay
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBounds(670, 290, 200, 30);
        comboBox.setVisible(false); // Ẩn JComboBox ban đầu

        GameButton Menu1 = new GameButton(AssetUtils.loadImage("/images/Menu1.png"));
        Menu1.setBounds(1450, 100, 60, 60);

        // Thêm ActionListener cho JButton
        Menu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hiển thị hoặc ẩn JComboBox khi nhấn vào JButton
                comboBox.setVisible(!comboBox.isVisible());
            }
        });

        gamePanel.add(Menu1);
        gamePanel.add(comboBox);
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

        //bo goc nhon
        gamePanelBox.setRadius(50);

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

    public void showScreen() {
        this.setVisible(true);
    }

    public void hideScreen() {
        this.setVisible(false);
    }

    public SelfGrid getSelfGrid() {

        for (Component child : this.getContentPane().getComponents()) {

            if (child instanceof SelfGrid) {
                return (SelfGrid) child;
            }
        }
        return null;
    }

    public AttackGrid getAttackGrid() {
        for (Component child : this.getContentPane().getComponents()) {
            if (child instanceof AttackGrid) {
                return (AttackGrid) child;
            }
        }
        return null;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }
    @Override
    public void componentMoved(ComponentEvent e) {

    }
    @Override
    public void componentShown(ComponentEvent e) {
        if (SoundManager.flagVolumeplaySound) {
            turnSound.setBackgroundImage(AssetUtils.loadImage("/images/turnSound.png"));
        } else {
            turnSound.setBackgroundImage(AssetUtils.loadImage("/images/offSound.png"));
        }
        repaint();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
