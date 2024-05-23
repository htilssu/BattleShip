package com.htilssu.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Hoster {
    ServerSocket serverSocket;
    boolean canHost = true;

    {
        try {
            serverSocket = new ServerSocket(6789);
        } catch (IOException e) {
            canHost = false;
        }
    }

    public Hoster() {
        InetAddress inetAddress = serverSocket.getInetAddress();
        System.out.println(inetAddress);
    }

    public boolean isCanHost() {
        return canHost;
    }

}
