package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.entity.game.GamePlay;
import com.htilssu.event.game.GameAction;
import com.htilssu.event.game.GameStartEvent;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends MultiHandler implements Runnable {

    private static Host instance;
    ServerSocket serverSocket;
    Socket socket;
    Thread hostListenThread = new Thread(this);
    boolean canHost = true;
    int ready = 0;

    public Host() {
        super();
    }

    public Host(BattleShip battleShip) {
        super(battleShip);
        instance = this;
    }

    public static Host getInstance() {
        return instance;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(GameSetting.DEFAULT_PORT);
            serverSocket.setSoTimeout(0);
        } catch (IOException e) {
            canHost = false;
        }
        hostListenThread.start();
    }


    @Override
    public void run() {
        while (canHost) {
            try {
                socket = serverSocket.accept();
                setHost(true);
            } catch (IOException e) {
                GameLogger.error("Có lỗi khi chấp nhận kết nối từ client");
            }

            if (socket != null) {
                readData(socket);
            }
            setHost(false);
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

    public void unReady() {
        ready -= 1;
        if (ready < 0) {
            ready = 0;
        }
    }

    public void ready() {
        ready += 1;
        if (ready > 2) {
            ready = 2;
        }

        if (ready == 2) {
            GamePlay gamePlay = battleShip.getGameManager().getCurrentGamePlay();
            battleShip.getListenerManager().callEvent(new GameStartEvent(gamePlay, battleShip));
            gamePlay.setGameMode(GamePlay.PLAY_MODE);
            send(GameAction.START_GAME);
        }
    }

    public void stop() {
        canHost = false;
    }

}
