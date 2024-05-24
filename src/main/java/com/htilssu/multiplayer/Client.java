package com.htilssu.multiplayer;

import java.net.Socket;

public class Client {
    Socket socket;
    HostScanner hostScanner = new HostScanner(this);
    private String status;

    public Client() {
        scanHosts();
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
}
