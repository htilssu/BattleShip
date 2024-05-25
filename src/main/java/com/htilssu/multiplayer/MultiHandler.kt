package com.htilssu.multiplayer

import com.htilssu.util.GameLogger

class MultiHandler {
    companion object {
        @JvmStatic
        fun handle(message: String) {
            GameLogger.log("MultiHandler Handling message: $message")
        }
    }

}
