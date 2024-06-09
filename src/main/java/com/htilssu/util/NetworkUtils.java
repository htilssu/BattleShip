package com.htilssu.util;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class NetworkUtils {

  private static final int SOCKET_TIMEOUT = 200;
  private static final int THREAD_POOL_SIZE = 50;

  public static List<InetAddress> find(int port) {
    List<InetAddress> inetAddresses;
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      inetAddresses = new ArrayList<>();
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();

        if (!networkInterface.isUp() || networkInterface.isLoopback()) {
          continue;
        }

        for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
          InetAddress inetAddress = address.getAddress();

          if (inetAddress instanceof Inet4Address) {
            InetAddress subnetMask = getSubnetMask(address.getNetworkPrefixLength());
            if (subnetMask != null) {
              inetAddresses.addAll(
                  checkPort(getDefaultGateway(inetAddress, subnetMask), subnetMask, port));
            }
          }
        }
      }
    } catch (SocketException ignored) {
      return Collections.emptyList();
    }

    return inetAddresses;
  }

  private static InetAddress getSubnetMask(int prefixLength) {
    int value = 0xffffffff << (32 - prefixLength);
    byte[] bytes =
        new byte[] {
          (byte) (value >>> 24),
          (byte) (value >> 16 & 0xff),
          (byte) (value >> 8 & 0xff),
          (byte) (value & 0xff)
        };

    try {
      return InetAddress.getByAddress(bytes);
    } catch (Exception e) {
      return null;
    }
  }

  private static InetAddress getDefaultGateway(InetAddress inetAddress, InetAddress subnetMask) {
    byte[] address = inetAddress.getAddress();
    byte[] mask = subnetMask.getAddress();

    for (int i = 0; i < address.length; i++) {
      address[i] = (byte) (address[i] & mask[i]);
    }

    try {
      return InetAddress.getByAddress(address);
    } catch (UnknownHostException e) {
      return null;
    }
  }

  private static List<InetAddress> checkPort(
      InetAddress defaultGateway, InetAddress subnetMask, int port) {
    byte[] address = defaultGateway.getAddress();
    byte[] mask = subnetMask.getAddress();
    boolean con = true;

    ConcurrentLinkedQueue<InetAddress> inetAddresses = new ConcurrentLinkedQueue<>();
    try (ExecutorService executorService = newFixedThreadPool(THREAD_POOL_SIZE)) {
      while (con) {
        byte[] currentAddress = Arrays.copyOf(address, address.length);
        executorService.submit(
            () -> {
              try (Socket socket = new Socket()) {
                socket.connect(
                    new InetSocketAddress(InetAddress.getByAddress(currentAddress), port),
                    SOCKET_TIMEOUT);
                socket.close();
                inetAddresses.add(socket.getInetAddress());
              } catch (IOException e) {
                System.out.println(e.getMessage());
              }
            });

        int count = mask.length;
        for (int i = address.length - 1; i >= 0; i--) {
          if (((address[i] | mask[i]) ^ (byte) 0xff) != 0) {
            address[i]++;
            break;
          }
          if (i - 1 >= 0 && (address[i - 1] | mask[i - 1]) != (byte) 0xff) {
            address[i - 1]++;
            address[i] = 0;
            break;
          }

          count--;
          if (count == 0) {
            con = false;
          }
        }
      }
      executorService.shutdown();
      try {
        if (!executorService.awaitTermination(2, TimeUnit.MINUTES)) {
          executorService.shutdownNow();
        }
      } catch (InterruptedException e) {
        executorService.shutdownNow();
      }
    } catch (Exception ignored) {
    }

    return new ArrayList<>(inetAddresses);
  }
}
