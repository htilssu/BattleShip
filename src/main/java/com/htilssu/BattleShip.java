package com.htilssu;


import com.htilssu.managers.EventManager;
import com.htilssu.managers.ScreenManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.settings.GameSetting;
import com.htilssu.utils.NetworkUtils;

import javax.swing.*;


public class BattleShip extends JFrame implements Runnable {

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
    private final EventManager eventManager = new EventManager();
    private final Host host = new Host();


    /**
     * Quản lý các màn hình trong game
     */
    ScreenManager screenManager = new ScreenManager(this);
    /**
     * Quản lý giao tiếp với server
     */
    Client client = new Client();
    /**
     * Biến đánh dấu có đang chạy thread render hay không
     */
    private boolean running;


    private BattleShip() {
        setTitle("BattleShip");
        setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(screenManager.getCurrentScreen());

        pack();

        setUp();
    }

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
//        eventManager.registerEvent(new PlayerShootEvent());
    }

    /**
     * Gọi hàm này để bắt đầu chạy app
     */
    public void start() {
        running = true;
        thread.start();
        host.start();
        NetworkUtils.find(5555);
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
        new Host();


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
        eventManager.checkEvents();
    }

    private void render() {
        setTitle("BattleShip - FPS: " + currentFPS);
        screenManager.getCurrentScreen().repaint();
    }

    private void updateData() {

    }

    public void changeScreen(int screen
    ) {
        remove(screenManager.getCurrentScreen());
        add(screenManager.getScreen(screen));
    }
}
