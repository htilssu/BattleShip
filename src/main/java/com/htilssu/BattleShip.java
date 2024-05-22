package com.htilssu;


import com.htilssu.screens.GamePanel;
import com.htilssu.screens.MenuScreen;
import com.htilssu.settings.GameSetting;

import javax.swing.*;
import java.awt.*;


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
        add(panel);
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
        float lastFrameTime = System.nanoTime();
        float lastTime = System.nanoTime();
        int fpsCount = 0;
        float lastTickTime = System.nanoTime();
        float nsFramePerSecond = 1e9f / GameSetting.FPS;
        float nsTickPerSecond = 1e9f / GameSetting.TPS;

        while (running) {
            float now = System.nanoTime();

            if (now - lastFrameTime > nsFramePerSecond) {
                lastFrameTime = now;
                render();
                fpsCount++;
            }

            if (now - lastTickTime > nsTickPerSecond) {
                lastTickTime = now;
                updateData();
            }


            if (now - lastTime > 1e9f) {
                lastTime = now;
                currentFPS = fpsCount;
                fpsCount = 0;
            }
        }
    }

    private void render() {
        setTitle("BattleShip - FPS: " + currentFPS);
    }

    private void updateData() {

    }
}
