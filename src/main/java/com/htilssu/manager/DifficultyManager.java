package com.htilssu.manager;

import java.util.Map;

public class DifficultyManager {
  private static final int NORMAL = 1;
  private static final int HARD = 2;
  public static final Map<Integer, Integer> difficultyMap =
      Map.of(
          NORMAL, 10,
          HARD, 15);
  public static int DIFFICULTY = DifficultyManager.NORMAL;

  /**
   * Lấy kích thước bảng hiện tại của game dựa vào {@link #DIFFICULTY}
   *
   * @return kích thước bảng game
   */
  public static int getGameBoardSize() {
    return difficultyMap.get(DIFFICULTY);
  }

  /**
   * Lấy kích thước bảng game dựa vào độ khó
   *
   * @param difficulty độ khó của game
   * @return kích thước bảng game
   */
  public static int getGameBoardSize(int difficulty) {
    return difficultyMap.get(difficulty);
  }
}
