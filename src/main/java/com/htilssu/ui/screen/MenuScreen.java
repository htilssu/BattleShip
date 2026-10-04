package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends JPanel {

    private BufferedImage backgroundImage, menuImage;
    private final BattleShip window;
    private final List<GameButton> buttons;

    public MenuScreen(BattleShip battleShip) {
        window = battleShip;
        setLayout(null);
        loadBackgroundImage();
        loadMenu();
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
        buttons = new ArrayList<>();
        createButtons();


        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        repositionButtons();
                        playBackgroundMusic();
                    }
                }
        );
    }

    private void loadBackgroundImage() {
        backgroundImage = AssetUtils.loadImage("/images/sea1.png");
    }

    private void loadMenu() {
        menuImage = AssetUtils.loadImage("/images/MENU2.png"); // Tải hình ảnh biểu tượng menu
    }

    private void createButtons() {

        addButton("Play", "PLAY");
        addButton("Multiplayer", "MULTIPLAYER");
        addButton("Continue", "CONTINUE");
        addButton("Settings", "SETTING");
        addButton("Introduction", "INTRODUCTION");
        addButton("Quit", "QUIT");
        repositionButtons();
    }

    private void repositionButtons() {
        int buttonWidth = 240;
        int buttonHeight = 64;
        int centerX = (getWidth() - buttonWidth) / 2;
        int totalButtons = buttons.size();
        int spacing = 12;
        int totalHeight = (buttonHeight * totalButtons) + (spacing * (totalButtons - 1));
        int startY = Math.max(180, (getHeight() - totalHeight) / 2 + 50);

        for (int i = 0; i < buttons.size(); i++) {
            GameButton button = buttons.get(i);
            button.setBounds(centerX, startY + i * (buttonHeight + spacing), buttonWidth,
                    buttonHeight);
        }
    }

    private void playBackgroundMusic() {
        if (!SoundManager.isBackgroundPlaying()) {
            SoundManager.playBackGround(SoundManager.BACKGROUND_MENU);
        }
    }

    private void addButton(String text, String actionCommand) {
        GameButton button = new GameButton(AssetUtils.getImage(AssetUtils.ASSET_BUTTON_2));
        button.setText(text);
        button.setTextSize(22);
        button.setActionCommand(actionCommand);
        button.addActionListener(e -> handleButtonClick(e.getActionCommand()));
        buttons.add(button);
        add(button);
    }

    private void handleButtonClick(String actionCommand) {
        switch (actionCommand) {
            case "PLAY":
                window.changeScreen(ScreenManager.PICK_SCREEN);
                break;
            case "SETTING":
                window.changeScreen(ScreenManager.SETTING_SCREEN);
                break;
            case "MULTIPLAYER":
                window.changeScreen(ScreenManager.NETWORK_SCREEN);
                break;
            case "INTRODUCTION":
                window.changeScreen(ScreenManager.INTRODUCTION_SCREEN);
                break;
            case "QUIT":
                System.exit(0);
                break;
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
            int iconX = (getWidth() - menuImage.getWidth()) / 2;
            int iconY = 60;
            g.drawImage(menuImage, iconX, iconY, this);
        }
    }
}
