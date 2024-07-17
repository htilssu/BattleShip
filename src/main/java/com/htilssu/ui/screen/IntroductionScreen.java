package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.htilssu.util.AssetUtils.ASSET_BACK_BUTTON;

public class IntroductionScreen extends JPanel {
    private final BattleShip window;
    private BufferedImage backgroundImage;
    private JPanel howToPlayPanel; // Panel to contain all steps

    public IntroductionScreen(BattleShip battleShip) {
        this.window = battleShip;
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        loadBackgroundImage();
        initComponents();
    }

    private void initComponents() {
        var backButton = new GameButton(AssetUtils.getImage(ASSET_BACK_BUTTON));
        backButton.addActionListener(e -> window.changeScreen(ScreenManager.MENU_SCREEN));
        backButton.setSize(new Dimension(64, 64));
        backButton.setLocation(40, 40);
        add(backButton, 0);

        JLabel headerLabel = new JLabel("Introduction");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setBounds(300, 20, 200, 40);
        headerLabel.setForeground(Color.WHITE); // Đặt màu chữ là màu trắng
        add(headerLabel);

        // GamePanel for introduction text
        GamePanel introPanel = createIntroductionPanel();
        add(introPanel);

        // How to Play button
        GameButton howToPlayButton = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_ATTACK));
        howToPlayButton.setBounds(300, 450, 200, 50);
        howToPlayButton.setText("HOW TO PLAY"); // Thêm nhãn cho nút
        howToPlayButton.addActionListener(e -> toggleHowToPlay());
        add(howToPlayButton);

        // Panel to contain all steps, initially hidden
        howToPlayPanel = createHowToPlayPanel();
        howToPlayPanel.setVisible(false);
        add(howToPlayPanel);
    }

    private GamePanel createIntroductionPanel() {
        GamePanel gamePanelBox = new GamePanel();
        JTextArea introText = new JTextArea();
        introText.setText("Welcome to BattleShip Game!\n\n" +
                "In this game, you will command a fleet of ships and engage in naval battles. " +
                "The objective is to sink all enemy ships before they sink yours.\n\n" +
                "Game Features:\n" +
                "- Single Player Mode\n" +
                "- Multiplayer Mode\n" +
                "- Various levels of difficulty\n\n" +
                "Good luck, Commander!");
        introText.setFont(new Font("Arial", Font.PLAIN, 16));
        introText.setLineWrap(true);
        introText.setWrapStyleWord(true);
        introText.setOpaque(false);
        introText.setEditable(false);
        introText.setForeground(Color.WHITE); // Đặt màu chữ là màu trắng
        gamePanelBox.add(introText);
        gamePanelBox.setBackground(new Color(0, 0, 0, 0.5f)); // Màu nền bán trong suốt
        gamePanelBox.setRadius(20); // Bo góc cho panel
        gamePanelBox.setBounds(50, 100, 700, 250); // Đặt lại kích thước để văn bản không bị tràn xuống
        return gamePanelBox;
    }

    private JPanel createHowToPlayPanel() {
        JPanel howToPlayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)); // Căn giữa các panel, khoảng cách giữa các panel là 30, 10
        howToPlayPanel.setBackground(new Color(0, 0, 0, 0.5f));
        howToPlayPanel.setOpaque(false);
        howToPlayPanel.setBounds(20, 510, 1000, 500); // Đặt lại kích thước và vị trí của panel

        // Create each step panel
        JPanel stepPanel1 = createStepPanel("Step 1", "Deploy your ships on the grid.", "/images/sea.png");
        JPanel stepPanel2 = createStepPanel("Step 2", "Take turns to attack enemy positions.", "/images/sea.png");
        JPanel stepPanel3 = createStepPanel("Step 3", "If you hit an enemy ship, you get another turn.", "/images/sea.png");
        JPanel stepPanel4 = createStepPanel("Step 4", "Additional steps description here.", "/images/sea.png");
        JPanel stepPanel5 = createStepPanel("Step 5", "The first to sink all enemy ships wins.", "/images/sea.png");

        // Add each step panel to howToPlayPanel
        howToPlayPanel.add(stepPanel1);
        howToPlayPanel.add(stepPanel2);
        howToPlayPanel.add(stepPanel3);
        howToPlayPanel.add(stepPanel4);
        howToPlayPanel.add(stepPanel5);

        return howToPlayPanel;
    }

    private JPanel createStepPanel(String stepTitle, String stepDescription, String imagePath) {
        JPanel gamePanelBox = new JPanel();
        gamePanelBox.setLayout(null); // Sử dụng Layout null

        // Đặt kích thước cố định cho panel
        gamePanelBox.setPreferredSize(new Dimension(150, 200));

        // Thiết lập background với màu đen trong suốt
        gamePanelBox.setBackground(new Color(0, 0, 0, 0.5f));

        JLabel stepLabel = new JLabel(stepTitle);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stepLabel.setForeground(Color.WHITE);
        stepLabel.setBounds(10, 10, 110, 20); // Đặt vị trí và kích thước cho stepLabel
        gamePanelBox.add(stepLabel);

        JLabel imageLabel = new JLabel(new ImageIcon(AssetUtils.loadImage(imagePath)));
        imageLabel.setBounds(10, 30, 110, 60); // Đặt vị trí và kích thước cho imageLabel
        gamePanelBox.add(imageLabel);

        JTextArea stepText = new JTextArea(stepDescription);
        stepText.setFont(new Font("Arial", Font.PLAIN, 14));
        stepText.setLineWrap(true);
        stepText.setWrapStyleWord(true);
        stepText.setOpaque(false);
        stepText.setEditable(false);
        stepText.setForeground(Color.WHITE); // Đặt màu chữ là màu trắng
        stepText.setBounds(10, 100, 110, 40); // Đặt vị trí và kích thước cho stepText
        gamePanelBox.add(stepText);

        return gamePanelBox;
    }



    private void toggleHowToPlay() {
        howToPlayPanel.setVisible(!howToPlayPanel.isVisible());
    }

    private void loadBackgroundImage() {
        backgroundImage = AssetUtils.loadImage("/images/sea2.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
