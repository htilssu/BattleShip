package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.ui.component.CustomSliderUI;
import com.htilssu.manager.ScreenManager;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.Color;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class SettingScreen extends JPanel {
    private final BattleShip window;
    private JLabel volumeIconLabel;
    private JSlider volumeSlider;
    private JLabel volumePercentageLabel;
    private boolean isMuted = false;
    private int currentVolume = 100; // Giả định giá trị ban đầu của âm lượng
    private BufferedImage cursorImage, backgroundImage;

    public SettingScreen(BattleShip battleShip) {
        this.window = battleShip;
        setLayout(null);
        setPreferredSize(
                new Dimension(800, 600)); // Assuming 800x600 as GameSetting.WIDTH, GameSetting.HEIGHT
        initializeComponents();
        loadCursorImage();
        setCustomCursor();
        loadBackgroundImage();
    }

  private void backToMenu() {
    // Sử dụng BattleShip để chuyển đổi màn hình
    window.changeScreen(ScreenManager.MENU_SCREEN);
  }

  private void loadBackgroundImage() {

    backgroundImage = AssetUtils.loadImage("/sea2.png"); // Load background image
  }

  private void initializeComponents() {
    // Tạo JLabel cho biểu tượng cái loa
    volumeIconLabel = new JLabel(new ImageIcon(AssetUtils.loadImage("/soundon.png")));
    volumeIconLabel.setBounds(100, 100, 100, 80);
    volumeIconLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            toggleMute(); // Khi bấm vào biểu tượng âm thanh
          }
        });
    add(volumeIconLabel);

        // Calculate positions dynamically
        int iconWidth = volumeIconLabel.getWidth();
        int iconHeight = volumeIconLabel.getHeight();
        int sliderWidth = 300;
        int sliderHeight = 50;
        int spacing = 20;
        int sliderX = volumeIconLabel.getX() + iconWidth + spacing;
        int sliderY =
                volumeIconLabel.getY()
                        + (iconHeight - 30) / 2; // Center the slider vertically relative to the icon

        // Tạo JSlider để điều chỉnh âm lượng
        volumeSlider = new JSlider(0, 100, currentVolume);
        volumeSlider.setBounds(sliderX, sliderY, sliderWidth, 50);
        volumeSlider.setBackground(Color.TRANSPARENT);
        volumeSlider.setUI(new CustomSliderUI(volumeSlider));
        volumeSlider.addChangeListener(
                e -> {
                    int value = volumeSlider.getValue();
                    volumePercentageLabel.setText(value + "%"); // Sử dụng biến thành viên
                    adjustVolume(value);
                });
        add(volumeSlider);

        // Tạo JLabel cho phần trăm âm lượng
        volumePercentageLabel = new JLabel(currentVolume + "%"); // Khởi tạo biến thành viên
        volumePercentageLabel.setBounds(sliderX + sliderWidth + spacing, sliderY, 50, 50);
        add(volumePercentageLabel);

    // Tạo nút "Back" để quay lại màn hình menu
    JButton backButton = new JButton(new ImageIcon(AssetUtils.loadImage("/back.png")));
    backButton.setBounds(100, 300, 200, 52); // Điều chỉnh vị trí và kích thước theo nhu cầu của bạn
    backButton.setContentAreaFilled(false);
    backButton.setBorderPainted(false);
    backButton.setOpaque(false);
    backButton.addActionListener(e -> backToMenu());
    add(backButton);
  }

  private void loadCursorImage() {
    cursorImage = AssetUtils.loadImage("/Layer2.png"); // Load cursor image
  }

    private void setCustomCursor() {
        Cursor customCursor =
                Toolkit.getDefaultToolkit()
                        .createCustomCursor(cursorImage, new Point(0, 0), "Custom Cursor");
        setCursor(customCursor);
    }

    private void loadBackgroundImage() {

    backgroundImage = AssetUtils.loadImage("/sea2.png"); // Load background image
  }

  private void toggleMute() {
    isMuted = !isMuted;
    MenuScreen menuScreen = MenuScreen.getInstance();
    if (menuScreen != null) {
      Clip backgroundMusicClip = menuScreen.getBackgroundMusicClip();
      if (backgroundMusicClip != null) {
        if (isMuted) {
          backgroundMusicClip.stop();
          volumeIconLabel.setIcon(
              new ImageIcon(
                  AssetUtils.loadImage("/soundoff.png"))); // Thay đổi hình ảnh khi tắt âm thanh
        } else {
          backgroundMusicClip.start();
          volumeIconLabel.setIcon(
              new ImageIcon(
                  AssetUtils.loadImage("/soundon.png"))); // Thay đổi hình ảnh khi mở âm thanh
        }
      }
    }
  }

    private void adjustVolume(int volume) {
        if (volume < 0) volume = 0;
        if (volume > 100) volume = 100;
        currentVolume = volume;

        MenuScreen menuScreen = MenuScreen.getInstance();
        if (menuScreen != null) {
            menuScreen.setVolume(volume);
        }
    }

    private void loadCursorImage() {
        cursorImage = AssetUtils.loadImage("/Layer2.png"); // Load cursor image
    }

    private void setCustomCursor() {
        Cursor customCursor =
                Toolkit.getDefaultToolkit()
                        .createCustomCursor(cursorImage, new Point(0, 0), "Custom Cursor");
        setCursor(customCursor);
    }

    private void backToMenu() {
        // Sử dụng BattleShip để chuyển đổi màn hình
        window.changeScreen(ScreenManager.MENU_SCREEN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
