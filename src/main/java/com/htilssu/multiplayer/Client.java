package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.screen.NetworkScreen;
import com.htilssu.util.GameLogger;
import com.htilssu.util.NetworkUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.htilssu.manager.ScreenManager.NETWORK_SCREEN;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Lớp quản lý kết nối giữa client với server
 */
public class Client extends MultiHandler implements Runnable {
    static Client instance;
    Socket socket;
    private List<InetAddress> hostList = new ArrayList<>();
    private String status;

    public Client(BattleShip battleShip) {
        super(battleShip);
        instance = this;
    }

    public static Client getInstance() {
        return instance;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Đặt trạng thái kết nối của client với host
     *
     * @param status Trạng thái kết nối
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void connect(InetAddress ip, short port) {
        try {
            socket = new Socket(ip, port);
            new Thread(this).start();
            status = "Đã kết nối đến máy chủ";
        } catch (IOException e) {
            status = "Không thể kết nối đến máy chủ";
        }
    }

    public int getPing() {
        long time = System.currentTimeMillis();
        send(MultiHandler.PING + "");
        return (int) (System.currentTimeMillis() - time);
    }

    public void send(Object... obj) {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            StringBuilder data = new StringBuilder();
            for (Object o : obj) {
                data.append(o.toString()).append("|");
            }
            pw.println(data);
        } catch (IOException e) {
            GameLogger.error("Có lỗi khi gửi dữ liệu");
        }
    }

    public synchronized void scanHost() {
        new Thread(() -> {
            hostList.clear();
            hostList.addAll(NetworkUtils.find(GameSetting.DEFAULT_PORT));

            var networkScreen = (NetworkScreen) battleShip.getScreenManager().getScreen(NETWORK_SCREEN);
            networkScreen.updateListHost(getHostList());
            networkScreen.repaint();
        }).start();
    }

    public List<InetAddress> getHostList() {
        return hostList;
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            readData(socket);
        }
    }

    public boolean isConnected() {
        return socket != null;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            GameLogger.log("Không thể ngắt kết nối");
        }
        socket = null;
    }
}
