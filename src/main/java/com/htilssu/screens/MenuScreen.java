package com.htilssu.screens;
import com.htilssu.BattleShip;
import com.htilssu.components.CustomButton;
import com.htilssu.managers.ScreenManager;
import com.htilssu.settings.GameSetting;
import com.htilssu.utils.AssetUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuScreen extends JPanel {
    private BufferedImage backgroundImage,menuImage;
    private CardLayout cardLayout;//card layout quan ly nhieu the card khac nhau trong 1 container
    private JPanel parentPanel;//tao 1 hieu ung chuyen gia 2 man gamemenu va gamepanel
    private BattleShip window;


    public MenuScreen(BattleShip battleShip) {
        window = battleShip;
        cardLayout = new CardLayout();
        parentPanel = new JPanel(cardLayout);
        setLayout(null); // We will use absolute positioning
        loadBackgroundImage();
        loadMenu();
        createButtons();
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    }

    private void loadBackgroundImage() {

            backgroundImage = AssetUtils.loadAsset("/sea.png");// Load background image

    }
    private void loadMenu() {
        menuImage = AssetUtils.loadAsset("/MENU_.png"); // Tải hình ảnh biểu tượng menu
    }
    private void createButtons() {
        int buttonWidth = 200;
        int buttonHeight = 80;
        int centerX = (GameSetting.WIDTH - buttonWidth) / 2;
        int totalButtons = 5;
        int spacing = 5; // Increased spacing between buttons
        int totalHeight = (buttonHeight * totalButtons) + (spacing * (totalButtons - 1));
        int menuImageHeight = (menuImage != null) ? menuImage.getHeight() : 0;
        int startY = (GameSetting.HEIGHT - totalHeight) / 2 + menuImageHeight -20; // Adjusted to avoid overlapping

        addButton("/btnplay.png", centerX, startY, buttonWidth, buttonHeight, "PLAY");
        addButton("/MUTIPLAYER.png", centerX, startY + (buttonHeight + spacing), buttonWidth, buttonHeight, "MULTIPLAYER");
        addButton("/HELP.png", centerX, startY + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight, "HELP");
        addButton("/SETTINGS.png", centerX, startY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight, "SETTING");
        addButton("/QUIT.png", centerX, startY + 4 * (buttonHeight + spacing), buttonWidth, buttonHeight, "QUIT");
    }

    private void addButton( String imagePath, int x, int y, int width, int height,String actionCommand) {
        CustomButton button = new CustomButton(imagePath);
        button.setBounds(x, y, width, height);
        button.setActionCommand(actionCommand);
        button.addActionListener(e -> handleButtonClick(e.getActionCommand()));
        add(button);
    }
    private void handleButtonClick(String actionCommand) {
        switch (actionCommand) {
            case "PLAY" -> window.changeScreen(ScreenManager.GAME_SCREEN);
            case "SETTING" -> cardLayout.show(parentPanel, "SETTINGS_SCREEN");
            case "QUIT" -> System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (menuImage != null) {
            // Vẽ biểu tượng menu tại vị trí mong muốn
            int iconX = (GameSetting.WIDTH - menuImage.getWidth()) / 2;
            int iconY = 60;
            g.drawImage(menuImage,iconX,iconY, this);
        }
    }
}
