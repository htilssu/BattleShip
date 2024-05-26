package com.htilssu.multiplayer;

import com.htilssu.setting.GameSetting;
import com.htilssu.util.GameLogger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Host implements Runnable {
  ServerSocket serverSocket;
  Socket socket;
  Thread hostListenThread = new Thread(this);
  boolean canHost = true;

  public Host() {}

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
        InputStream ip = socket.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(ip));

        while (true) {
          String message = bis.readLine();
          if (message == null) {
            break;
          }

          MultiHandler.handle(message);
        }

      } catch (IOException e) {
        GameLogger.log("Error while connecting to client");
      }
    }
  }

  public void stop() {
    canHost = false;
  }
}
