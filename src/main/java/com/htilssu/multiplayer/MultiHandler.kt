package com.htilssu.multiplayer

import com.htilssu.BattleShip
import com.htilssu.entity.player.PlayerAction
import com.htilssu.util.GameLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

class MultiHandler(var battleShip: BattleShip) {


    companion object {
        private var instance: MultiHandler? = null
        const val PING = -1;
        const val PONG = -2;

        @JvmStatic
        fun getInstance(): MultiHandler {
            return instance!!
        }

        @JvmStatic
        fun createInstance(battleShip: BattleShip) {
            instance = MultiHandler(battleShip)
        }
    }

    private fun handle(message: String) {
        val messageParts = message.split("|")
        when (messageParts[0].toIntOrNull()) {
            PlayerAction.JOIN -> {
                GameLogger.log("Player ${messageParts[1]} joined the game")
            }

            PlayerAction.ATTACK -> {
                GameLogger.log("Player ${messageParts[1]} left the game")
            }

            PING -> {
                Host.getInstance().send(PONG.toString())
            }

            else -> {
                GameLogger.log("Unknown message: $message")
            }
        }
    }

    fun readData(socket: Socket) {
        try {
            val ip = socket.getInputStream()
            val bis = BufferedReader(InputStreamReader(ip))

            while (true) {
                val message = bis.readLine() ?: break
                handle(message)
            }
        } catch (e: IOException) {
            Client.getInstance().status = "Mất kết nối với máy chủ"
        }
    }

}
