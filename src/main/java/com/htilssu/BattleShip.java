package com.htilssu;


import com.htilssu.screens.GamePanel;
import com.htilssu.screens.MenuScreen;
import com.htilssu.settings.GameSetting;

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
     * Panel chứa game
     */
    GamePanel panel;
    MenuScreen menuScreen = new MenuScreen();
    /**
     * Biến đánh dấu có đang chạy hay không
     */
    private boolean running;

    private BattleShip() {
        setTitle("BattleShip");
        setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new GamePanel(this);
//        add(panel);
        add(menuScreen);
    }

    public static int getCurrentFPS() {
        return currentFPS;
    }

    public static void main(String[] args) {
        BattleShip battleShip = new BattleShip();
        battleShip.start();

    }

    public void start() {
        running = true;
        thread.start();
        setVisible(true);
    }

    public void stop() {
        running = false;
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

    private void render() {
        setTitle("BattleShip - FPS: " + currentFPS);
        panel.repaint();
    }

    private void updateData() {

    }
}
