package com.htilssu.manager;

import com.htilssu.BattleShip;
import com.htilssu.screen.PickScreen;
import com.htilssu.screen.PlayScreen;
import com.htilssu.screen.MenuScreen;
import com.htilssu.screen.SettingScreen;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/** Lớp quản lý các màn hình trong game */
public class ScreenManager {
    /** Màn hình bắt đầu - màn hình chọn thể loại, setting */
    public static final int MENU_SCREEN = 1;

    /** Màn hình chơi game */
    public static final int GAME_SCREEN = 2;
    public static final int SETTING_SCREEN = 3;
    public static final int PICK_SCREEN = 4;

    private final Map<Integer, JPanel> screenMap = new HashMap<>();

    /***
     * Màn hình hiện tại
     */
    int currentScreen = MENU_SCREEN;

    public ScreenManager(BattleShip battleShip) {
        screenMap.put(1, new MenuScreen(battleShip));
        screenMap.put(2, new PlayScreen(battleShip));
        screenMap.put(3, new SettingScreen(battleShip));
        screenMap.put(4, new PickScreen(battleShip));
    }

    /**
     * Lấy màn hình theo loại screen
     *
     * <p>Nếu tham số {@code update} là {@code true} thì sẽ cập nhật lại màn hình hiện tại được đánh
     * dấu khi lấy, ngược lại là {@code false}
     *
     * @param startScreen số nguyên biểu diễn loại màn hình
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
     * Đặt màn hình hiện tại ex: {@link #GAME_SCREEN} là màn hình chơi game hoặc {@link
     * #MENU_SCREEN} là màn hình chính
     *
     * @param currentScreen số nguyên biểu diễn loại màn hình
     */
    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }
}
