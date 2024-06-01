package com.htilssu.multiplayer

import com.htilssu.BattleShip
import com.htilssu.component.Position
import com.htilssu.entity.player.Player
import com.htilssu.event.player.PlayerAction
import com.htilssu.event.player.PlayerJoinEvent
import com.htilssu.util.GameLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

open class MultiHandler(var battleShip: BattleShip) {


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
                if (messageParts.count() < 3) {
                    GameLogger.error("Message không hợp lệ (Player Join): $message")
                    return
                }
                val playerId = messageParts[1]
                val playerName = messageParts[2]
                val player = Player(playerId, playerName)
                battleShip.listenerManager.callEvent(PlayerJoinEvent(player), battleShip.gameManager)
            }

            PlayerAction.SHOOT -> {
                if (messageParts.count() < 3) {
                    GameLogger.error("Message không hợp lệ (Player Shoot): $message")
                    return
                }
                val pos = Position(messageParts[1].toInt(), messageParts[2].toInt())
            }

            PING -> {
                if (this is Host) {
                    this.send(PONG.toString())
                }
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
        } catch (_: IOException) {
        }
    }

}
