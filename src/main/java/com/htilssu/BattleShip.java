package com.htilssu;


import com.htilssu.settings.GameSetting;

import javax.swing.*;


public class BattleShip extends JFrame implements Runnable {

    static int currentFPS = 0;
    private final Thread thread = new Thread(this);
    private boolean running;

    private BattleShip() {
        setTitle("BattleShip");
        setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
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
        while (running) {
            updateData();
            render();
        }
    }

    private void render() {
    }

    private void updateData() {

    }
}
