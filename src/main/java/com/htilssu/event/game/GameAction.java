package com.htilssu.event.game;

/** Lớp chứa các hành động mà người chơi có thể thực hiện */
public final class GameAction {
  /** Người chơi thực hiện hành động bắn đạn */
  public static final Integer SHOOT = 1;

  /** Người chơi thực hiện hành động tham gia trò chơi */
  public static final Integer JOIN = 2;

  /** Người chơi đã sẵn sàng tham gia trò chơi */
  public static final Integer READY = 3;

  public static final Integer UNREADY = 4;
  public static final Integer START_GAME = 5;
}
