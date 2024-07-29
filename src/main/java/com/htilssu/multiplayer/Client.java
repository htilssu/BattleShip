package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.event.game.GameAction;
import com.htilssu.manager.GameManager;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;
import com.htilssu.util.NetworkUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp quản lý kết nối giữa client với server
 */
public class Client extends MultiHandler implements Runnable {

    static Client instance;
    private final List<InetAddress> hostList = new ArrayList<>();
    Socket socket;
    private String status;
    private Thread scanThread;

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

    /**
     * Gửi yêu cầu tham gia vào game
     */
    public void join() {
        send(GameAction.JOIN, GameManager.gamePlayer.getId(), GameManager.gamePlayer.getName());
    }

    /**
     * Gửi dữ liệu đến host
     *
     * @param obj Danh sách object cần gửi
     */
    public void send(Object... obj) {
        if (isConnected()) {
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
    }

    /**
     * Kiểm tra xem client có đang kết nối với host không
     *
     * @return Trả về {@code true} nếu client đang kết nối với host, ngược lại trả về {@code false}
     */
    public boolean isConnected() {
        return socket != null;
    }

    public synchronized void scanHost() {

        if (scanThread != null && scanThread.isAlive()) scanThread.interrupt();

        scanThread = new Thread(() -> {
            hostList.clear();

            for (InetAddress inetAddress : NetworkUtils.find(GameSetting.DEFAULT_PORT)) {
                connect(inetAddress, GameSetting.DEFAULT_PORT);
                send(PING);
            }

        });

        scanThread.start();
    }

    /**
     * Kết nối đến host với port và địa chỉ IP
     * phương thức này sẽ không tạo thread mới để chạy
     *
     * @param ip   Địa chỉ IP của host
     * @param port Cổng kết nối
     */
    public void connect(InetAddress ip, short port) {
        if (!isConnected()) {
            try {
                socket = new Socket(ip, port);
                new Thread(this).start();
                status = "Đã kết nối đến máy chủ";
            } catch (IOException e) {
                disconnect();
                status = "Không thể kết nối đến máy chủ";
            }
        }
    }

    /**
     * Ngắt kết nối giữa client và host
     */
    public synchronized void disconnect() {
        try {
            if (isConnected()) socket.close();
        } catch (IOException e) {
            socket = null;
            GameLogger.log("Không thể ngắt kết nối");
        }
        socket = null;
    }

    public void stopScan() {

    }

    public List<InetAddress> getHostList() {
        return hostList;
    }

    @Override
    public void run() {
        while (isConnected()) {
            readData(socket);
            disconnect();
        }
    }
}
