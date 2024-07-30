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
        headerLabel.setBounds(350, 50, 200, 40);
        headerLabel.setForeground(Color.BLACK); // Đặt màu chữ là màu trắng
        add(headerLabel);

        //gioi thieu tau
        JLabel headerLabel1 = new JLabel("Description For Each Ship");
        headerLabel1.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel1.setBounds(1000, 50, 500, 40);
        headerLabel1.setForeground(Color.BLACK); // Đặt màu chữ là màu trắng
        add(headerLabel1);

        // GamePanel for introduction text
        GamePanel introPanel = createIntroductionPanel();
        add(introPanel);

        //gioi thieu Ship
        GamePanel introPanel1 = IntroductionShip();
        add(introPanel1);

        // How to Play button
        GameButton howToPlayButton = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BG_ATTACK),1);
        howToPlayButton.setBounds(350, 450, 200, 50);
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

        // Tạo JTextArea với kích thước cố định hoặc có thể là kích thước phù hợp
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
        introText.setWrapStyleWord(true);
        introText.setLineWrap(true); // Kích hoạt tính năng tự động xuống dòng
        introText.setOpaque(false);
        introText.setEditable(false);
        introText.setForeground(Color.WHITE);

        // Đặt JTextArea trong JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(introText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 10)); // Loại bỏ viền nếu cần
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);  //xóa background scroll.
        //scrollPane.getViewport().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thay vì setBounds, sử dụng layout manager để tự động điều chỉnh kích thước
        gamePanelBox.setLayout(new BorderLayout());
        gamePanelBox.add(scrollPane, BorderLayout.CENTER);

        //gamePanelBox.setPreferredSize(new Dimension(700, 250)); // Kích thước mong muốn cho panel
        gamePanelBox.setBounds(120, 100, 700, 300); // Nếu không sử dụng layout manager, bạn vẫn có thể đặt bounds nếu cần
        gamePanelBox.setBackground(new Color(0, 0, 0, 0.5f)); // Màu nền với độ trong suốt 50%
        gamePanelBox.setRadius(20); // Giả định rằng GamePanel có phương thức setRadius

        return gamePanelBox;
    }

    private GamePanel IntroductionShip() {
        GamePanel gamePanelBox1 = new GamePanel();

        // Tạo JTextArea với kích thước cố định hoặc có thể là kích thước phù hợp
        JTextArea introText = new JTextArea();
        introText.setText("Ship Features:\n\n" +
                "1- Destroyer: Fast and agile.\n\n" +
                "2- Submarine: Stealthy and deadly.\n\n" +
                "3- Cruiser: Balanced in speed.\n\n" +
                "4- Battleship: Heavy armor.\n\n" +
                "5- Aircraft Carrier: Launches aircraft.\n");
        introText.setFont(new Font("Arial", Font.PLAIN, 16));
        introText.setWrapStyleWord(true);
        introText.setLineWrap(true); // Kích hoạt tính năng tự động xuống dòng
        introText.setOpaque(false);
        introText.setEditable(false);
        introText.setForeground(Color.WHITE);

        // Đặt JTextArea trong JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(introText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 10)); // Loại bỏ viền nếu cần
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);  //xóa background scroll.

        // Thay vì setBounds, sử dụng layout manager để tự động điều chỉnh kích thước
        gamePanelBox1.setLayout(new BorderLayout());
        gamePanelBox1.add(scrollPane, BorderLayout.CENTER);

        //gamePanelBox.setPreferredSize(new Dimension(700, 250)); // Kích thước mong muốn cho panel
        gamePanelBox1.setBounds(980, 100, 500, 300); // Nếu không sử dụng layout manager, bạn vẫn có thể đặt bounds nếu cần
        gamePanelBox1.setBackground(new Color(0, 0, 0, 0.5f)); // Màu nền với độ trong suốt 50%
        gamePanelBox1.setRadius(20); // Giả định rằng GamePanel có phương thức setRadius

        return gamePanelBox1;
    }


    private JPanel createHowToPlayPanel() {
        JPanel howToPlayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)); // Căn giữa các panel, khoảng cách giữa các panel là 30, 10
        howToPlayPanel.setBackground(new Color(0, 0, 0, 0.5f));
        howToPlayPanel.setOpaque(false);
        howToPlayPanel.setBounds(50, 510, 1100, 500); // Đặt lại kích thước và vị trí của panel

        // Create each step panel
        JPanel stepPanel1 = createStepPanel("Step 1", "Deploy your ships on the grid.", "/images/createHost.png");
        JPanel stepPanel2 = createStepPanel("Step 2", "Take turns to attack enemy positions.", "/images/selectMap.png");
        JPanel stepPanel3 = createStepPanel("Step 3", "If you hit an enemy ship, you get another turn.", "/images/putShip.png");
        JPanel stepPanel4 = createStepPanel("Step 4", "Additional steps description here.", "/images/attackShip.png");
        JPanel stepPanel5 = createStepPanel("Step 5", "The first to sink all enemy ships wins.", "/images/endGame.png");

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
        gamePanelBox.setPreferredSize(new Dimension(190, 220));

        // Thiết lập background với màu đen trong suốt
        gamePanelBox.setBackground(new Color(0, 0, 0, 0.5f));

        JLabel stepLabel = new JLabel(stepTitle);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stepLabel.setForeground(Color.WHITE);
        stepLabel.setBounds(45, 5, 90, 20); // Đặt vị trí và kích thước cho stepLabel
        gamePanelBox.add(stepLabel);

        GamePanel imagePanel = new GamePanel();
        imagePanel.setBackgroundImage(AssetUtils.loadImage(imagePath));
        imagePanel.setBounds(10, 30, 160, 90); // Đặt vị trí và kích thước cho imageLabel
        gamePanelBox.add(imagePanel);

        JTextArea stepText = new JTextArea(stepDescription);
        stepText.setFont(new Font("Arial", Font.PLAIN, 16));
        stepText.setLineWrap(true);
        stepText.setWrapStyleWord(true);
        stepText.setOpaque(false);
        stepText.setEditable(false);
        stepText.setForeground(Color.WHITE); // Đặt màu chữ là màu trắng
        stepText.setBounds(18, 130, 180, 100); // Đặt vị trí và kích thước cho stepText
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
