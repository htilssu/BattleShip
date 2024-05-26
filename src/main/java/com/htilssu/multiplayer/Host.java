package com.htilssu.multiplayer;

import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    private static Host instance;
    ServerSocket serverSocket;
    Socket socket;
    Thread hostListenThread = new Thread(this);
    boolean canHost = true;

    public Host() {
        instance = this;
    }

    @NotNull
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

    public boolean isCanHost() {
        return canHost;
    }

    @Override
    public void run() {
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            GameLogger.error("Có lỗi khi chấp nhận kết nối từ client");
        }

        while (canHost) {
            if (socket != null) {
                MultiHandler.getInstance().readData(socket);
            }
        }
    }

    public void send(String message) {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println(message);
        } catch (IOException e) {
            GameLogger.error("Có lỗi khi gửi dữ liệu tới client");
        }
    }

    public void stop() {
        canHost = false;
    }
}
