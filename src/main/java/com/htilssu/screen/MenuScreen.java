package com.htilssu.screen;

import com.htilssu.BattleShip;
import com.htilssu.component.CustomButton;
import com.htilssu.manager.ScreenManager;
import com.htilssu.setting.GameSetting;
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
    private BufferedImage backgroundImage, menuImage;
    private CardLayout cardLayout; // card layout quan ly nhieu the card khac nhau trong 1 container
    private JPanel parentPanel; // tao 1 hieu ung chuyen gia 2 man gamemenu va gamepanel
    private BattleShip window;
    private Clip backgroundMusicClip;
    private List<CustomButton> buttons;
    private static MenuScreen instance; // Tham chiếu tĩnh

    public MenuScreen(BattleShip battleShip) {
        instance = this; // Gán tham chiếu tĩnh
        window = battleShip;
        cardLayout = new CardLayout();
        parentPanel = new JPanel(cardLayout);
        setLayout(null); // We will use absolute positioning
        loadBackgroundImage();
        loadMenu();
        setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
        buttons = new ArrayList<>();
        createButtons();
        playBackgroundMusic();
        // Thêm listener để phát hiện khi cửa sổ thay đổi kích thước
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons();

            }
        });



    }

    public static MenuScreen getInstance() {
        return instance;
    }

    private void loadBackgroundImage() {

        backgroundImage = AssetUtils.loadAsset("/sea.png"); // Load background image
    }

    private void loadMenu() {
        menuImage = AssetUtils.loadAsset("/MENU_.png"); // Tải hình ảnh biểu tượng menu
    }
    public Clip getBackgroundMusicClip() {
        return backgroundMusicClip;
    }

    public void setBackgroundMusicClip(Clip clip) {
        this.backgroundMusicClip = clip;
    }
    public void setVolume(int volume) {
        if (backgroundMusicClip != null) {
            FloatControl volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float minVolume = volumeControl.getMinimum();
            float maxVolume = volumeControl.getMaximum();
            float newVolume = minVolume + (maxVolume - minVolume) * (volume / 100f);
            volumeControl.setValue(newVolume);
        }
    }

    private void createButtons() {

        addButton("/btnplay.png", "PLAY");
        addButton("/MUTIPLAYER.png", "MULTIPLAYER");
        addButton("/HELP.png", "HELP");
        addButton("/SETTINGS.png", "SETTING");
        addButton("/QUIT.png", "QUIT");
        repositionButtons();
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
                window.changeScreen(ScreenManager.GAME_SCREEN);
                break;
            case "SETTING":
                window.changeScreen(ScreenManager.SETTING_SCREEN);
                break;
            case "QUIT":
                System.exit(0);
                break;
        }
    }
    private void repositionButtons() {//định hinh cac nut khi thay doi kich thuoc man hinh
        int buttonWidth = 200;
        int buttonHeight = 80;
        int centerX = (getWidth() - buttonWidth) / 2;
        int totalButtons = buttons.size();
        int spacing = 20;
        int totalHeight = (buttonHeight * totalButtons) + (spacing * (totalButtons - 1));
        int menuImageHeight = (menuImage != null) ? menuImage.getHeight() : 0;
        int startY = (getHeight() - totalHeight) / 2 + menuImageHeight-10;


        for (int i = 0; i < buttons.size(); i++) {
            CustomButton button = buttons.get(i);
            button.setBounds(centerX, startY + i * (buttonHeight + spacing), buttonWidth, buttonHeight);
        }
    }
    private void playBackgroundMusic() {
        try {
            URL musicURL = getClass().getResource("/Action_4.wav");//nhac nen
            if (musicURL != null) { //neu tim dc nhac nen
                System.out.println("Music file found at: " + musicURL.getPath());//in ra tep am thanh tim thay xem co dung k
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicURL);//Đoạn này tạo một AudioInputStream từ đường dẫn của tệp âm thanh. AudioInputStream là một luồng dữ liệu âm thanh có thể được sử dụng để đọc dữ liệu từ tệp âm thanh.
                backgroundMusicClip = AudioSystem.getClip();//òng này tạo một đối tượng Clip mới. Clip là một loại đối tượng trong Java Sound API được sử dụng để phát lại các tệp âm thanh ngắn.
                backgroundMusicClip.open(audioInputStream);//Dòng này mở Clip với AudioInputStream đã được tạo trước đó, nạp dữ liệu âm thanh từ tệp vào bộ nhớ để chuẩn bị cho việc phát.
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);//òng này thực hiện việc phát lại âm thanh lặp đi lặp lại không ngừng. Điều này có nghĩa là khi âm thanh kết thúc, nó sẽ tự động phát lại từ đầu.
                System.out.println("Background music started.");
            } else {
                System.err.println("Music file not found: /Action_4.wav");
            }
            //cac dong case la cac ngoai le khi xay ra loi
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
