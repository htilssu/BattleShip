package com.htilssu.event.game;

/**
 * Lớp chứa các hành động mà người chơi có thể thực hiện
 */
public final class GameAction {
    /**
     * Người chơi thực hiện hành động bắn đạn
     */
    public static final int SHOOT = 1;

    /**
     * Người chơi thực hiện hành động tham gia trò chơi
     */
    public static final int JOIN = 2;

    /**
     * Người chơi đã sẵn sàng tham gia trò chơi
     */
    public static final int READY = 3;

    public static final int UNREADY = 4;
    public static final int START_GAME = 5;
    public static final int RESPONSE_SHOOT = 6;
}
