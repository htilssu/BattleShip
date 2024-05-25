package com.htilssu;


import com.htilssu.manager.ListenerManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class BattleShip extends JFrame implements Runnable, KeyListener {

    /**
     * Số frame mỗi giây hiện tại
     */
    static int currentFPS = 0;
    /**
     * Luồng render và update game
     */
    private final Thread thread = new Thread(this);
    /**
     * Quản lý sự kiện
     */
    private final ListenerManager listenerManager = new ListenerManager();
    /**
     * Quản lý host
     */
    private final Host host = new Host();
    /**
     * Quản lý các màn hình trong game
     */
    ScreenManager screenManager = new ScreenManager(this);
    /**
     * Quản lý giao tiếp với server
     */
    Client client = new Client(this);
    /**
     * Biến đánh dấu có đang chạy thread render hay không
     */
    private boolean running;
    private boolean isFullScreen = false;


    private BattleShip() {
        setTitle("BattleShip");
        setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(screenManager.getCurrentScreen());
        setIconImage(AssetUtils.loadAsset("/game_icon.png"));
        addKeyListener(this);
        setFocusable(true);
        setResizable(false);

        pack();
        setUp();
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

    /**
     * Hàm cài đặt các sự kiện
     */
    private void setUp() {

    }

    /**
     * Gọi hàm này để bắt đầu chạy app
     */
    public void start() {
        running = true;
        thread.start();
        host.start();

        client.scanHost();
        client.getHostList().forEach(host -> {
            client.connect(host, GameSetting.DEFAULT_PORT);
        });

        setVisible(true);
    }

    /**
     * Gọi hàm này để dừng app
     */
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

    /**
     * Hàm check event
     */
    private void checkEvent() {

    }

    /**
     * Hàm render mỗi frame
     */
    private void render() {
        setTitle("BattleShip - FPS: " + currentFPS);
        screenManager.getCurrentScreen().repaint();
    }

    /**
     * Hàm update dữ liệu mỗi tick
     */
    private void updateData() {

    }

    /**
     * Thay đổi màn hình hiện tại
     *
     * @param screen màn hình cần chuyển đến
     */
    public void changeScreen(int screen) {
        remove(screenManager.getCurrentScreen());
        repaint();
        screenManager.setCurrentScreen(screen);
        add(screenManager.getCurrentScreen());
        pack();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F11 -> {
                toggleFullScreen();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void updateScreenSize() {
        screenManager.getCurrentScreen().setPreferredSize(new Dimension(GameSetting.WIDTH, GameSetting.HEIGHT));
    }

    private void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        dispose();
        setUndecorated(isFullScreen);
        if (isFullScreen) {
            graphicsDevice.setFullScreenWindow(this);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            screenManager.getCurrentScreen().setPreferredSize(new Dimension(getWidth(), getHeight()));
        } else {
            graphicsDevice.setFullScreenWindow(null);
            updateScreenSize();
        }
        pack();
        setVisible(true);

    }

}
