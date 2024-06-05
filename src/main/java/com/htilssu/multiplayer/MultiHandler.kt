package com.htilssu.multiplayer

import com.htilssu.BattleShip
import com.htilssu.entity.component.Position
import com.htilssu.entity.player.Player
import com.htilssu.event.game.GameAction
import com.htilssu.event.player.PlayerJoinEvent
import com.htilssu.util.GameLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

open class MultiHandler(var battleShip: BattleShip) {


    companion object {
        const val PING = -1;
        const val PONG = -2;
    }

    private fun handle(message: String) {
        val messageParts = message.split("|")
        when (messageParts[0].toIntOrNull()) {
            GameAction.JOIN -> {
                if (messageParts.count() < 3) {
                    GameLogger.error("Message không hợp lệ (Player Join): $message")
                    return
                }
                val playerId = messageParts[1]
                val playerName = messageParts[2]
                val player = Player(playerId, playerName)
                battleShip.listenerManager.callEvent(PlayerJoinEvent(player), battleShip.gameManager)
            }

            GameAction.SHOOT -> {
                if (messageParts.count() < 3) {
                    GameLogger.error("Message không hợp lệ (Player Shoot): $message")
                    return
                }
                val pos = Position(
                    messageParts[1].toInt(),
                    messageParts[2].toInt()
                )
            }

            PING -> {
                if (this is Host) {
                    this.send(PONG.toString())
                }
            }

            GameAction.READY -> {
                if (this is Host) {
                    this.ready()
                }
            }

            GameAction.UNREADY -> {
                if (this is Host) {
                    this.unReady()
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

            while (socket.isConnected) {
                val message = bis.readLine() ?: break;
                handle(message)
            }
        } catch (_: IOException) {
            //empty
        }
    }

}
