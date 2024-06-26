package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.ui.component.CustomSliderUI;
import com.htilssu.util.AssetUtils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class SettingScreen extends JPanel {
  private final BattleShip window;
  private JLabel volumeIconLabel;
  private JSlider volumeSlider;
  private JLabel volumePercentageLabel;
  private boolean isMuted = false;
  private int currentVolume = SoundManager.getCurrentVolume();
  private BufferedImage cursorImage, backgroundImage;

  public SettingScreen(BattleShip battleShip) {
    this.window = battleShip;
    setLayout(null);
    setPreferredSize(new Dimension(800, 600));
    initializeComponents();
    loadBackgroundImage();
  }

  private void backToMenu() {
    window.changeScreen(ScreenManager.MENU_SCREEN);
  }

  private void loadBackgroundImage() {
    backgroundImage = AssetUtils.loadImage("/images/sea2.png");
  }

  private void initializeComponents() {
    volumeIconLabel = new JLabel(new ImageIcon(Objects.requireNonNull(AssetUtils.loadImage("/images/soundon.png"))));
    volumeIconLabel.setBounds(550, 250, 100, 80);
    volumeIconLabel.addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                toggleMute();
              }
            });
    add(volumeIconLabel);

    int iconWidth = volumeIconLabel.getWidth();
    int iconHeight = volumeIconLabel.getHeight();
    int sliderWidth = 300;
    int sliderHeight = 50;
    int spacing = 20;
    int sliderX = volumeIconLabel.getX() + iconWidth + spacing;
    int sliderY = volumeIconLabel.getY() + (iconHeight - 30) / 2;

    volumeSlider = new JSlider(0, 100, currentVolume);
    volumeSlider.setBounds(sliderX, sliderY, sliderWidth, 50);
    volumeSlider.setBackground(new Color(0, 0, 0, 0));
    volumeSlider.setUI(new CustomSliderUI(volumeSlider));
    volumeSlider.addChangeListener(
            e -> {
              int value = volumeSlider.getValue();
              volumePercentageLabel.setText(value + "%");
              adjustVolume(value);
              volumeSlider.repaint(); // Ensure the slider is repainted
            });
    add(volumeSlider);

    volumePercentageLabel = new JLabel(currentVolume + "%");
    volumePercentageLabel.setBounds(sliderX + sliderWidth + spacing, sliderY, 50, 50);
    add(volumePercentageLabel);

    JButton backButton = new JButton(new ImageIcon(AssetUtils.loadImage("/images/BACK1.png")));
    backButton.setBounds(volumeIconLabel.getX() + 155, 500, 200, 68);
    backButton.setContentAreaFilled(false);
    backButton.setBorderPainted(false);
    backButton.setOpaque(false);
    backButton.addActionListener(e -> backToMenu());
    add(backButton);
  }

  private void adjustVolume(int volume) {
    if (volume < 0) volume = 0;
    if (volume > 100) volume = 100;
    currentVolume = volume;

    SoundManager.setBackgroundVolume(volume);
    SoundManager.setVolume(SoundManager.getBackgroundClip(), volume);
  }

  private void toggleMute() {
    isMuted = !isMuted;
    Clip backgroundMusicClip = SoundManager.getBackgroundClip();
    if (backgroundMusicClip != null) {
      if (isMuted) {
        backgroundMusicClip.stop();
        volumeIconLabel.setIcon(new ImageIcon(AssetUtils.loadImage("/images/soundoff.png")));
      } else {
        backgroundMusicClip.start();
        volumeIconLabel.setIcon(new ImageIcon(AssetUtils.loadImage("/images/soundon.png")));
      }
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
