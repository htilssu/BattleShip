package com.htilssu.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.util.AssetUtils;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingScreen extends JPanel {
    private BattleShip window;
    private JLabel volumeIconLabel;
    private JSlider volumeSlider;
    private JButton muteButton;
    private JLabel volumePercentageLabel;
    private boolean isMuted = false;
    private int currentVolume = 100; // Giả định giá trị ban đầu của âm lượng

    public SettingScreen(BattleShip battleShip) {
        this.window = battleShip;
        setLayout(null);
        setPreferredSize(new Dimension(800, 600)); // Assuming 800x600 as GameSetting.WIDTH, GameSetting.HEIGHT
        initializeComponents();
    }
    private void backToMenu() {
        // Sử dụng BattleShip để chuyển đổi màn hình
        window.changeScreen(ScreenManager.MENU_SCREEN);
    }
    private void initializeComponents() {
        // Tạo JLabel cho biểu tượng cái loa
        volumeIconLabel = new JLabel(new ImageIcon(AssetUtils.loadAsset("/MusicOn.png")));
        volumeIconLabel.setBounds(100, 100, 100, 100);
        volumeIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleMute(); // Khi bấm vào biểu tượng âm thanh
            }
        });
        add(volumeIconLabel);

        // Tạo JSlider để điều chỉnh âm lượng
        volumeSlider = new JSlider(0, 100, currentVolume);
        volumeSlider.setBounds(volumeIconLabel.getX() + volumeIconLabel.getWidth() + 10, volumeIconLabel.getY() + 25, 200, 30);
        volumeSlider.addChangeListener(e -> {
            int value = volumeSlider.getValue();
            volumePercentageLabel.setText(value + "%"); // Sử dụng biến thành viên
            adjustVolume(value);
        });
        add(volumeSlider);

        // Tạo JLabel cho phần trăm âm lượng
        volumePercentageLabel = new JLabel(currentVolume + "%"); // Khởi tạo biến thành viên
        volumePercentageLabel.setBounds(volumeSlider.getX() + volumeSlider.getWidth() + 10, volumeSlider.getY(), 50, 30);
        add(volumePercentageLabel);


        // Tạo nút "Back" để quay lại màn hình menu
        JButton backButton = new JButton("Back");
        backButton.setBounds(100, 200, 100, 30); // Điều chỉnh vị trí và kích thước theo nhu cầu của bạn
        backButton.addActionListener(e -> backToMenu());
        add(backButton);
    }
    private JButton createButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(AssetUtils.loadAsset(imagePath)));
        button.setBounds(x, y, width, height);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
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

    private void toggleMute() {
        isMuted = !isMuted;
        MenuScreen menuScreen = MenuScreen.getInstance();
        if (menuScreen != null) {
            Clip backgroundMusicClip = menuScreen.getBackgroundMusicClip();
            if (backgroundMusicClip != null) {
                if (isMuted) {
                    backgroundMusicClip.stop();
                    volumeIconLabel.setIcon(new ImageIcon(AssetUtils.loadAsset("/MusicOff.png"))); // Thay đổi hình ảnh khi tắt âm thanh
                } else {
                    backgroundMusicClip.start();
                    volumeIconLabel.setIcon(new ImageIcon(AssetUtils.loadAsset("/MusicOn.png"))); // Thay đổi hình ảnh khi mở âm thanh
                }
            }
        }
    }
}
