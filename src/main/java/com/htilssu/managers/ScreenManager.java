package com.htilssu.managers;

import com.htilssu.BattleShip;
import com.htilssu.screens.PlayScreen;
import com.htilssu.screens.MenuScreen;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp quản lý các màn hình trong game
 */
public class ScreenManager {
    /**
     * Màn hình bắt đầu - màn hình chọn thể loại, setting
     */
    public static final int MENU_SCREEN = 1;

    /**
     * Màn hình chơi game
     */
    public static final int GAME_SCREEN = 2;
    private final Map<Integer, JPanel> screenMap = new HashMap<>();
    /***
     * Màn hình hiện tại
     */
    int currentScreen = MENU_SCREEN;

    public ScreenManager(BattleShip battleShip) {
        screenMap.put(2, new PlayScreen(battleShip));
        screenMap.put(1, new MenuScreen(battleShip));
    }

    /**
     * Lấy màn hình theo loại screen
     *
     * @param startScreen số nguyên biểu diễn loại màn hình
     * @return Màn hình được kế thừa từ {@link JPanel} hoặc {@code null} nếu không tìm thấy
     */
    public JPanel getScreen(int startScreen) {
        return screenMap.get(startScreen);
    }

    public JPanel getCurrentScreen() {
        return screenMap.get(currentScreen);
    }

    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }
}
