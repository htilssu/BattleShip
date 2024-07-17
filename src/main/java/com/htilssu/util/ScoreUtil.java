package com.htilssu.util;

import com.htilssu.manager.GameManager;

public class ScoreUtil {

    private static final int maxScore = 20;

    public static int calculateScore(int remainingTime) {
        int percent = remainingTime / (GameManager.TIME_PER_TURN * 200);
        return percent * maxScore;
    }
}
