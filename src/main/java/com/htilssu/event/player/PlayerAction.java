package com.htilssu.event.player;

/** Lớp chứa các hành động mà người chơi có thể thực hiện */
public final class PlayerAction {
  /** Người chơi thực hiện hành động bắn đạn */
  public static final int SHOOT = 1;

  /** Người chơi thực hiện hành động tham gia trò chơi */
  public static final int JOIN = 2;

  /** Người chơi đã sẵn sàng tham gia trò chơi */
  public static final int READY = 3;

  public static final int UNREADY = 4;
}
