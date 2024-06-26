package com.htilssu;

import com.htilssu.entity.game.GamePlay;
import com.htilssu.listener.GameStartListener;
import com.htilssu.listener.PlayerListener;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.ListenerManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.multiplayer.Host;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BattleShip extends JFrame implements Runnable, KeyListener, ComponentListener {

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
    private final Host host = new Host(this);
    /**
     * Số frame mỗi giây hiện tại
     */
    int currentFPS = 0;
    /**
     * Quản lý các màn hình trong game
     */
    ScreenManager screenManager = new ScreenManager(this);
    /**
     * Quản lý game, chứa thông tin về người chơi, trạng thái game, ...
     */
    GameManager gameManager = new GameManager(this);
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

        setUp();


        // Đăng ký các listener
        registerListener();
    }

    /**
     * Cài đặt các sự kiện cho frame
     */
    private void setUp() {
        setTitle("BattleShip");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        add(screenManager.getCurrentScreen());
        setIconImage(AssetUtils.loadImage("/images/game_icon.png"));
        addKeyListener(this);
        setFocusable(true);
        setResizable(false);
        addComponentListener(this);
        setExtendedState(Frame.MAXIMIZED_BOTH);
//        gameManager.createTestGamePlay();
//        gameManager.getCurrentGamePlay().setGameMode(GamePlay.PLAY_MODE);

    }

    /**
     * Hàm cài đặt các sự kiện
     */
    private void registerListener() {
        listenerManager.registerListener(PlayerListener.class);
        listenerManager.registerListener(GameStartListener.class);
    }

    public static void main(String[] args) {
        BattleShip battleShip = new BattleShip();
        battleShip.start();
    }

    /**
     * Gọi hàm này để bắt đầu chạy app
     */
    public void start() {
        setVisible(true);

        running = true;
        //        thread.start();
    }

    public Host getHost() {
        return host;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
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
     * Hàm update dữ liệu mỗi tick
     */
    private void updateData() {
        // empty
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F11:
                //                toggleFullScreen();
                break;
            case KeyEvent.VK_ESCAPE:
                changeScreen(ScreenManager.MENU_SCREEN);
                break;
            case KeyEvent.VK_R:
                GamePlay cP = gameManager.getCurrentGamePlay();
                cP.changeDirection();
                repaint();
                break;
            default:
        }
    }

    /**
     * Thay đổi màn hình hiện tại
     *
     * @param screen màn hình cần chuyển đến
     */
    public synchronized void changeScreen(int screen) {
        JPanel targetScreen = screenManager.getScreen(screen);
        JPanel currentScreen = screenManager.getCurrentScreen();
        if (targetScreen == currentScreen) return;
        remove(currentScreen);

        screenManager.setCurrentScreen(screen);
        add(screenManager.getCurrentScreen());
        screenManager.getCurrentScreen().requestFocusInWindow();

        pack();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public Client getClient() {
        return client;
    }

    /**
     * Hàm chuyển đổi giữa chế độ full screen và window
     */
    public void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        dispose();
        setUndecorated(isFullScreen);

        if (isFullScreen) {
            setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        else {
            setExtendedState(Frame.NORMAL);
            setSize(GameSetting.WIDTH, GameSetting.HEIGHT);
            GameSetting.SCALE = 1;
        }

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        GameSetting.SCALE = getWidth() / (float) GameSetting.WIDTH;
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
