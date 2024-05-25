package com.htilssu.multiplayer;

import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    ServerSocket serverSocket;
    Socket socket;
    Thread hostListenThread = new Thread(this);
    boolean canHost = true;


    public Host() {

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
        if (canHost) {
            try {
                while (canHost) {
                    socket = serverSocket.accept();
                    InputStream ip = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(ip));
                    OutputStream op = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(op, true);
                    while (canHost) {
                        String message = reader.readLine();
                        if (message == null) {
                            break;
                        }
                        MultiHandler.handle(message);

                    }
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void stop() {
        canHost = false;
    }
}
