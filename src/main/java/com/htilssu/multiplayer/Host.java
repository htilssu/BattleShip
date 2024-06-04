package com.htilssu.multiplayer;

import com.htilssu.BattleShip;
import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends MultiHandler implements Runnable {
  private static Host instance;
  ServerSocket serverSocket;
  Socket socket;
  Thread hostListenThread = new Thread(this);
  boolean canHost = true;
  public Host(BattleShip battleShip) {
    super(battleShip);
    instance = this;
  }

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
    while (canHost) {
      try {
        socket = serverSocket.accept();
        GameLogger.log(String.valueOf(socket.getInetAddress()));
      } catch (IOException e) {
        GameLogger.error("Có lỗi khi chấp nhận kết nối từ client");
      }

      if (socket != null) {
        readData(socket);
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
