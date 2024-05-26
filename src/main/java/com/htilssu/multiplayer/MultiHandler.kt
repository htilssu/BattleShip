package com.htilssu.multiplayer

import com.htilssu.util.GameLogger

class MultiHandler {
    companion object {
        @JvmStatic
        fun handle(message: String) {
            val messageParts = message.split("|")
            when (messageParts[0]) {
                "JOIN" -> {
                    GameLogger.log("Player ${messageParts[1]} joined the game")
                }

                "LEAVE" -> {
                    GameLogger.log("Player ${messageParts[1]} left the game")
                }

                "MOVE" -> {
                    GameLogger.log("Player ${messageParts[1]} moved to ${messageParts[2]}")
                }

                else -> {
                    GameLogger.log("Unknown message: $message")
                }
            }
        }
    }

}
