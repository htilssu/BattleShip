package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.game.GameStartEvent;
import com.htilssu.manager.ScreenManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Host extends MultiHandler implements Runnable {

    private static Host instance;
    final String id = UUID.randomUUID().toString();
    ServerSocket serverSocket;
    Socket socket;
    Thread hostListenThread = new Thread(this);
    int ready = 0;
    private String hostName;
    private boolean isRunning;

    public Host(BattleShip battleShip) {
        super(battleShip);
        instance = this;
        try {
            serverSocket = new ServerSocket(GameSetting.DEFAULT_PORT);
        } catch (IOException e) {
            GameLogger.log(e.getMessage());
        }
    }

    public static Host getInstance() {
        return instance;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            hostListenThread.start();
        }
    }

    private boolean isConnected() {
        return socket != null && isRunning;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                socket = serverSocket != null ? serverSocket.accept() : null;
            } catch (IOException e) {
                GameLogger.error(e.getMessage());
            }

            if (socket != null) {
                readData(socket);
            }
        }
    }

    public void unReady() {
        ready -= 1;
        if (ready < 0) {
            ready = 0;
        }
    }

    public void ready() {
        ready += 1;

        if (ready == 2) {
            GamePlay gamePlay = battleShip.getGameManager().getCurrentGamePlay();
            battleShip.getListenerManager().callEvent(new GameStartEvent(gamePlay, battleShip));
            gamePlay.setGameMode(GamePlay.PLAY_MODE);
            battleShip.getScreenManager().getScreen(ScreenManager.PLAY_SCREEN).repaint();
            send(GameAction.START_GAME);
        }
    }

    public void send(Object... obj) {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            StringBuilder sb = new StringBuilder();
            for (Object o : obj) {
                sb.append(o).append("|");
            }
            pw.println(sb);
        } catch (IOException e) {
            GameLogger.error("Có lỗi khi gửi dữ liệu tới client");
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            GameLogger.error(e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
