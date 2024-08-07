package com.htilssu.manager;

import com.htilssu.BattleShip;
import com.htilssu.ui.screen.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp quản lý các màn hình trong game
 */
public final class ScreenManager {

    /**
     * Màn hình bắt đầu - màn hình chọn thể loại, setting
     */
    public static final int MENU_SCREEN = 1;

    /**
     * Màn hình chơi game
     */
    public static final int PLAY_SCREEN = 2;

    /**
     * Màn hình cài đặt
     */
    public static final int SETTING_SCREEN = 3;
    /**
     * Màn hình chọn chế độ
     */
    public static final int PICK_SCREEN = 4;
    /**
     * Màn hình chọn host cho chế độ chơi online
     */
    public static final int NETWORK_SCREEN = 5;

    public static final int INTRODUCTION_SCREEN = 8;

    public static final int START2_PLAYER_SCREEN = 6;   ///Game Tuan
    public static final int END_GAME_SCREEN = 7;

    private final Map<Integer, JPanel> screenMap = new HashMap<>();
    private final BattleShip battleShip;

    /***
     * Màn hình hiện tại
     */
    int currentScreen = MENU_SCREEN;

    public ScreenManager(BattleShip battleShip) {
        this.battleShip = battleShip;
        screenMap.put(MENU_SCREEN, new MenuScreen(battleShip));
        screenMap.put(PLAY_SCREEN, new PlayScreen(battleShip));
        screenMap.put(SETTING_SCREEN, new SettingScreen(battleShip));
        screenMap.put(NETWORK_SCREEN, new NetworkScreen(battleShip));
        screenMap.put(INTRODUCTION_SCREEN, new IntroductionScreen(battleShip));  //gioi thieu game
        screenMap.put(PICK_SCREEN, new PickScreen(battleShip));
        screenMap.put(START2_PLAYER_SCREEN, new Start2Player(battleShip));   ///test
        screenMap.put(END_GAME_SCREEN, new EndGameScreen(battleShip));
    }

    public BattleShip getBattleShip() {
        return battleShip;
    }

    /**
     * Lấy màn hình theo loại screen
     *
     * @param screen số nguyên biểu diễn loại màn hình
     *
     * @return Màn hình được kế thừa từ {@link JPanel} hoặc {@code null} nếu không tìm thấy
     */
    public JPanel getScreen(int screen) {
        return screenMap.get(screen);
    }

    /**
     * Lấy màn hình hiện tại
     *
     * @return Màn hình hiện tại
     */
    public JPanel getCurrentScreen() {
        return screenMap.get(currentScreen);
    }

    /**
     * Đặt màn hình hiện tại ex: {@link #PLAY_SCREEN} là màn hình chơi game hoặc
     * {@link #MENU_SCREEN}
     * là màn hình chính
     *
     * @param currentScreen số nguyên biểu diễn loại màn hình
     */
    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
        getCurrentScreen().setPreferredSize(
                new Dimension(battleShip.getWidth(), battleShip.getHeight()));

        battleShip.repaint();
        getCurrentScreen().requestFocus();
    }
}
