package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.CustomButton;
import com.htilssu.util.AssetUtils;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends JPanel {
  private BufferedImage backgroundImage, menuImage, cursorImage;
  private BattleShip window;
  private List<CustomButton> buttons;
  private int currentVolume = 100; // Giả định giá trị ban đầu của âm lượng

  public MenuScreen(BattleShip battleShip) {
    window = battleShip;

    setLayout(null); // We will use absolute positioning
    loadBackgroundImage();
    loadMenu();
    setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    buttons = new ArrayList<>();
    createButtons();
    playBackgroundMusic();

    addComponentListener(
            new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    repositionButtons();
                }
            }
    );
  }


  public void setVolume(int volume) {
    if (volume < 0) volume = 0;
    if (volume > 100) volume = 100;
    currentVolume = volume;
    SoundManager.setBackgroundVolume(volume);
  }

  private void loadBackgroundImage() {
    backgroundImage = AssetUtils.loadImage("/images/sea1.png");
  }
    private void repositionButtons() { // định hinh cac nut khi thay doi kich thuoc man hinh
        int buttonWidth = 200;
        int buttonHeight = 60;
        int centerX = (getWidth() - buttonWidth) / 2;
        int totalButtons = buttons.size();
        int spacing = 20;
        int totalHeight = (buttonHeight * totalButtons) + (spacing * (totalButtons - 1));
        int menuImageHeight = (menuImage != null) ? menuImage.getHeight() : 0;
        int startY = (getHeight() - totalHeight) / 2 + menuImageHeight - 10;

        for (int i = 0; i < buttons.size(); i++) {
            CustomButton button = buttons.get(i);
            button.setBounds(centerX, startY + i * (buttonHeight + spacing), buttonWidth, buttonHeight);
        }
  }

    private void loadMenu() {
        menuImage = AssetUtils.loadImage("/images/MENU2.png"); // Tải hình ảnh biểu tượng menu
    }

    private void createButtons() {

        addButton("/images/play2.png", "PLAY");
        addButton("/images/MultiPlayer.png", "Multiplayer");
        addButton("/images/continue.png", "Continue");
        addButton("/images/setting2.png", "SETTING");
        addButton("/images/exit.png", "QUIT");
        repositionButtons();
    }

  private void loadCursorImage() {
      cursorImage = AssetUtils.loadImage("/images/Layer2.png"); // Load cursor image
  }




    private void addButton(String imagePath, String actionCommand) {
        CustomButton button = new CustomButton(imagePath);
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
      case "Multiplayer":
        window.changeScreen(ScreenManager.NETWORK_SCREEN);
        break;
      case "QUIT":
        System.exit(0);
        break;
    }
  }


  private void playBackgroundMusic() {
    if (!SoundManager.isBackgroundPlaying()) {
      SoundManager.playBackGround(SoundManager.BACKGROUND_MENU);
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
