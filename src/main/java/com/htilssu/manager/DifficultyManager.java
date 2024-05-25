package com.htilssu.manager;

import java.util.Map;

public class DifficultyManager {
    private static final int NORMAL = 1;
    private static final int HARD = 2;
    public static final Map<Integer, Integer> difficultyMap = Map.of(
            NORMAL, 10,
            HARD, 15
    );
    public static int DIFFICULTY = DifficultyManager.NORMAL;

    public static int getGameBoardSize() {
        return difficultyMap.get(DIFFICULTY);
    }
}
