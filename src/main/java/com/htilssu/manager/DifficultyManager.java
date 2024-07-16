package com.htilssu.manager;

import java.util.Map;

public final class DifficultyManager {

    public static final int NORMAL = 1;
    public static final int HARD = 2;
    public static final Map<Integer, Integer> difficultyMap =
            Map.of(
                    NORMAL, 10,
                    HARD, 15);
    public static int difficulty = DifficultyManager.NORMAL;

    /**
     * Lấy kích thước bảng hiện tại của game dựa vào {@link #difficulty}
     *
     * @return kích thước bảng game
     */
    public static int getGameBoardSize() {
        return difficultyMap.get(difficulty);
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
