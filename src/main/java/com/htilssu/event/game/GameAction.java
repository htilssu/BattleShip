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

    /**
     * Người chơi hủy sẵn sàng
     */
    public static final int UNREADY = 4;
    /**
     * Bắt đầu trò chơi
     */
    public static final int START_GAME = 5;
    /**
     * Trả về thông tin sau khi gửi thông tin vị trí bắn đến đôi phương
     */
    public static final int RESPONSE_SHOOT = 6;
    /**
     * Kết thúc trò chơi, người nhận được sự kiện {@link #END_GAME} từ {@link #RESPONSE_SHOOT} sẽ là người chiến thắng
     */
    public static final int END_GAME = 7;
    public static final int END_TURN = 8;
    /**
     * Khi kết thúc game thông tin thuyền còn lại của đối thủ sẽ được hiện lên
     */
    public static final int SHIP_LEAK = 9;
}
