package com.htilssu.settings;

public class GameSetting {
    /**
     * Kích thước của 1 TILE
     */
    public static final int TILE_SIZE = 64;

    /**
     * Độ tăng trưởng của game (gấp mấy lần kích thước ban đầu)
     */
    public static final int SCALE = 1;
    /**
     * Số lượng TILE trong chiều rộng
     */
    public static final int TILE_IN_WIDTH = 20;
    /**
     * Số lượng TILE trong chiều cao
     */
    public static final int TILE_IN_HEIGHT = 10;
    /**
     * Chiều rộng của cửa sổ game
     * <p>
     * Được tính theo công thức: TILE_SIZE * TILE_IN_WIDTH * SCALE
     */
    public static final int WIDTH = TILE_SIZE * TILE_IN_WIDTH * SCALE;
    /**
     * Chiều cao của cửa sổ game
     * <p>
     * Được tính theo công thức: TILE_SIZE * TILE_IN_HEIGHT * SCALE
     * </p>
     */
    public static final int HEIGHT = TILE_SIZE * TILE_IN_HEIGHT * SCALE;

    /**
     * Số lượng frame mỗi giây
     */
    public static int FPS = 60;
    /**
     * Số lượng tick mỗi giây
     */
    public static int TPS = 120;
}
