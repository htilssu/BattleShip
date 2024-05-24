package com.htilssu.multiplayer;

import com.htilssu.utils.NetworkUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static Client instance;
    Socket socket;
    private List<InetAddress> hostList = new ArrayList<>();
    private String status;

    public Client() {
        scanHosts();
        instance = this;
    }

    public static Client getInstance() {
        return instance;
    }

    private void scanHosts() {
//        hostScanner.scan();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean connect(InetAddress ip, short port) {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            status = "Không thể kết nối đến máy chủ";
            return false;
        }

        return true;
    }

    public void scanHost() {
        hostList = NetworkUtils.find(NetworkUtils.DEFAULT_PORT);
    }

    public List<InetAddress> getHostList() {
        return hostList;
    }
}
