package com.htilssu.setting;

public class GameSetting {
    /** Kích thước của 1 TILE */
    public static final int TILE_SIZE = 32;

    /** S&#x1ED1; l&#x1B0;&#x1EE3;ng TILE trong chi&#x1EC1;u r&#x1ED9;ng */
    public static final int TILE_IN_WIDTH = 28;

    /** S&#x1ED1; l&#x1B0;&#x1EE3;ng TILE trong chi&#x1EC1;u cao */
    public static final int TILE_IN_HEIGHT = 20;

    /** */
    public static final short DEFAULT_PORT = 5555;

    /**
     * &#x110;&#x1ED9; t&#x103;ng tr&#x1B0;&#x1EDF;ng c&#x1EE7;a game (g&#x1EA5;p m&#x1EA5;y
     * l&#x1EA7;n k&iacute;ch th&#x1B0;&#x1EDB;c ban &#x111;&#x1EA7;u)
     */
    public static float SCALE = 1;

    /**
     * Chi&#x1EC1;u r&#x1ED9;ng c&#x1EE7;a c&#x1EED;a s&#x1ED5; game
     *
     * <p>&#x110;&#x1B0;&#x1EE3;c t&iacute;nh theo c&ocirc;ng th&#x1EE9;c: TILE_SIZE * TILE_IN_WIDTH
     * * SCALE
     */
    public static final int WIDTH = (int) (TILE_SIZE * TILE_IN_WIDTH * SCALE);

    /**
     * Chi&#x1EC1;u cao c&#x1EE7;a c&#x1EED;a s&#x1ED5; game
     *
     * <p>&#x110;&#x1B0;&#x1EE3;c t&iacute;nh theo c&ocirc;ng th&#x1EE9;c: TILE_SIZE *
     * TILE_IN_HEIGHT * SCALE
     */
    public static final int HEIGHT = (int) (TILE_SIZE * TILE_IN_HEIGHT * SCALE);

    /** Số lượng frame mỗi giây */
    public static int FPS = 60;

    /** Số lượng tick mỗi giây */
    public static int TPS = 120;
}
