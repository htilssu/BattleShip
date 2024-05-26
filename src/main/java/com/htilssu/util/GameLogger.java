package com.htilssu.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogger {
    public static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger("GameLogger", null);

    public static void log(String message) {
        logger.info(ANSI_GREEN + message + ANSI_RESET);
    }

    public static void error(String s) {
        logger.warning(s);
    }
}
