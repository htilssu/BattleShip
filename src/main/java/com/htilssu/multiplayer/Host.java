package com.htilssu.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
            serverSocket = new ServerSocket(5555);
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
                socket = serverSocket.accept();
                InputStream ip = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(ip));


                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
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
