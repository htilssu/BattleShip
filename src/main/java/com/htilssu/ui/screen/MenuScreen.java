package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.ui.component.CustomButton;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends JPanel {
    private static MenuScreen instance; // Tham chiếu tĩnh
    private BufferedImage backgroundImage, menuImage, cursorImage;
    private BattleShip window;
    private Clip backgroundMusicClip;
    private List<CustomButton> buttons;

    public MenuScreen(BattleShip battleShip) {
        instance = this; // Gán tham chiếu tĩnh
        window = battleShip;

        setLayout(null); // We will use absolute positioning
        loadBackgroundImage();
        loadMenu();
        buttons = new ArrayList<>();
        createButtons();
        //  playBackgroundMusic();

        loadCursorImage();
        setCustomCursor(); // Ensure this method is called

        // Thêm listener để phát hiện khi cửa sổ thay đổi kích thước
        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repositionButtons();
                    }
                });
    }

    private void loadBackgroundImage() {

        backgroundImage = AssetUtils.loadImage("/images/sea1.png");
    // Load background image
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

    private void setCustomCursor() {

        Cursor customCursor =
                Toolkit.getDefaultToolkit()
                        .createCustomCursor(cursorImage, new Point(0, 0), "Custom Cursor");
        setCursor(customCursor);
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
            case "Multiplayer":
                window.changeScreen(ScreenManager.NETWORK_SCREEN);
                break;
            case "SETTING":
                window.changeScreen(ScreenManager.SETTING_SCREEN);
                break;

            case "QUIT":
                System.exit(0);
                break;
        }
    }

    public static MenuScreen getInstance() {
        return instance;
    }

    public Clip getBackgroundMusicClip() {
        return backgroundMusicClip;
    }

    public void setBackgroundMusicClip(Clip clip) {
        this.backgroundMusicClip = clip;
    }

    public void setVolume(int volume) {
        if (backgroundMusicClip != null) {
            FloatControl volumeControl =
                    (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float minVol = volumeControl.getMinimum();
            float maxVolume = volumeControl.getMaximum();
            float newVolume = -30 + Math.abs(-30 - maxVolume) * volume / 100;
            GameLogger.log(newVolume + "");
            volumeControl.setValue(newVolume);
        }
    }

    private void playBackgroundMusic() {
        try {
            URL musicURL = getClass().getResource("/sounds/Action_4.wav"); // nhac nen
            if (musicURL != null) { // neu tim dc nhac nen
                AudioInputStream audioInputStream =
                        AudioSystem.getAudioInputStream(
                                musicURL); // Đoạn này tạo một AudioInputStream từ đường dẫn của tệp
                // âm thanh. AudioInputStream là một luồng dữ liệu âm
                // thanh có thể được sử dụng để đọc dữ liệu từ tệp âm
                // thanh.
                backgroundMusicClip =
                        AudioSystem.getClip(); // òng này tạo một đối tượng Clip mới. Clip là một loại
                // đối tượng trong Java Sound API được sử dụng để phát
                // lại các tệp âm thanh ngắn.
                backgroundMusicClip.open(
                        audioInputStream); // Dòng này mở Clip với AudioInputStream đã được tạo
                // trước đó, nạp dữ liệu âm thanh từ tệp vào bộ nhớ để
                // chuẩn bị cho việc phát.
                backgroundMusicClip.loop(
                        Clip.LOOP_CONTINUOUSLY); // òng này thực hiện việc phát lại âm thanh lặp
                // đi lặp lại không ngừng. Điều này có nghĩa là
                // khi âm thanh kết thúc, nó sẽ tự động phát
                // lại từ đầu.
            }
            // cac dong case la cac ngoai le khi xay ra loi
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading the audio file: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
