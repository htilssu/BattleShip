package com.htilssu;

import com.htilssu.listener.PlayerListener;
import com.htilssu.manager.ListenerManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.multiplayer.MultiHandler;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BattleShip extends JFrame implements Runnable, KeyListener {

    /** Số frame mỗi giây hiện tại */
    static int currentFPS = 0;

    /** Luồng render và update game */
    private final Thread thread = new Thread(this);

    /** Quản lý sự kiện */
    private final ListenerManager listenerManager = new ListenerManager();

    /** Quản lý host */
    private final Host host = new Host();

    /** Quản lý các màn hình trong game */
    ScreenManager screenManager = new ScreenManager(this);

    /** Quản lý giao tiếp với server */
    Client client = new Client(this);

    /** Biến đánh dấu có đang chạy thread render hay không */
    private boolean running;

    private boolean isFullScreen = false;
    private JPanel currentScreen = screenManager.getCurrentScreen();

    private BattleShip() {

        setUp();

        pack();

        // Dang ky cac listener
        registerListener();
    }

    /**
     * Lấy FPS hiện tại
     *
     * @return trả về số frame mỗi giây hiện tại
     */
    public static int getCurrentFPS() {
        return currentFPS;
    }

    public static void main(String[] args) {
        BattleShip battleShip = new BattleShip();
        battleShip.start();
    }

    private void setUp() {
        setTitle("BattleShip");
        setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(currentScreen);
        setIconImage(AssetUtils.loadAsset("/game_icon.png"));
        addKeyListener(this);
        setFocusable(true);
        setResizable(false);
        MultiHandler.createInstance(this);
    }

    /** Hàm cài đặt các sự kiện */
    private void registerListener() {
        listenerManager.registerListener(PlayerListener.class);
    }

    /** Gọi hàm này để bắt đầu chạy app */
    public void start() {
        running = true;
        thread.start();
        host.start();

        client.scanHost();
        client.getHostList()
                .forEach(
                        host -> {
                            client.connect(host, GameSetting.DEFAULT_PORT);
                        });

        setVisible(true);
    }

    /** Gọi hàm này để dừng app */
    public void stop() {
        running = false;
        host.stop();
    }

    @Override
    public void run() {
        long lastFrameTime = System.nanoTime();
        long lastTickTime = System.nanoTime();
        float nsPerTick = 1e9f / GameSetting.TPS;
        float nsPerFrame = 1e9f / GameSetting.FPS;
        int frame = 0;
        long lastTime = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();

            if (now - lastTickTime > nsPerTick) {
                updateData();
                checkEvent();
                lastTickTime = now;
            }

            if (now - lastFrameTime > nsPerFrame) {
                render();
                frame++;
                lastFrameTime = now;
            }
            if (System.currentTimeMillis() - lastTime > 1000) {
                currentFPS = frame;
                frame = 0;
                lastTime = System.currentTimeMillis();
            }
        }
    }

    /** Hàm check event */
    private void checkEvent() {}

    /** Hàm render mỗi frame */
    private void render() {
        setTitle("BattleShip - FPS: " + currentFPS);
        currentScreen.repaint();
    }

    /** Hàm update dữ liệu mỗi tick */
    private void updateData() {}

    /**
     * Thay đổi màn hình hiện tại
     *
     * @param screen màn hình cần chuyển đến
     */
    public void changeScreen(int screen) {
        JPanel targetScreen = screenManager.getScreen(screen);
        if (targetScreen == currentScreen) return;
        remove(currentScreen);
        currentScreen = targetScreen;
        add(currentScreen);
        pack();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F11:
                toggleFullScreen();
                break;
            case KeyEvent.VK_ESCAPE:
                changeScreen(ScreenManager.MENU_SCREEN);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void updateScreenSize() {
        currentScreen.setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    }

    public Client getClient() {
        return client;
    }

    /** Hàm chuyển đổi giữa chế độ full screen và window */
    private void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        GraphicsDevice graphicsDevice =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        dispose();
        setUndecorated(isFullScreen);
        if (isFullScreen) {
            graphicsDevice.setFullScreenWindow(this);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            currentScreen.setPreferredSize(new Dimension(getWidth(), getHeight()));
        } else {
            graphicsDevice.setFullScreenWindow(null);
            updateScreenSize();
        }

        pack();
        setVisible(true);
    }
}
