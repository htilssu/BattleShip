package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;
import com.htilssu.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Lớp quản lý kết nối giữa client với server */
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
    sendData(MultiHandler.PING + "");
    return (int) (System.currentTimeMillis() - time);
  }

  public void sendData(Object... obj) {
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

  public void scanHost() {
    hostList = NetworkUtils.find(GameSetting.DEFAULT_PORT);
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
}
