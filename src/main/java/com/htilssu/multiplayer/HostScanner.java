package com.htilssu.multiplayer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class HostScanner {

    /**
     * Thời gian chờ mỗi lần kiểm tra (ms)
     */
    private static final int TIMEOUT = 200;
    /**
     * Cổng cần kiểm tra
     */
    private static final int PORT = 5555;

    Client client;

    public HostScanner(Client client) {
        this.client = client;
    }


}
