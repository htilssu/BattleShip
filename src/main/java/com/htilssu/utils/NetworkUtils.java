package com.htilssu.utils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class NetworkUtils {

    public static InetAddress find(int port) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }


                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    InetAddress inetAddress = address.getAddress();

                    if (address.getNetworkPrefixLength() != 24) continue;
                    if (inetAddress instanceof Inet4Address) {
                        InetAddress subnetMask = getSubnetMask(address.getNetworkPrefixLength());
                        List<InetAddress> aa = checkPort(getDefaultGateway(inetAddress, subnetMask), subnetMask, port);
                        for (InetAddress inetAddress1 : aa) {
                            System.out.println(inetAddress1.getHostAddress());

                        }
                    }
                }
            }
        } catch (SocketException ignored) {

        }
        return null;
    }


    private static InetAddress getSubnetMask(int prefixLength) {
        int value = 0xffffffff << (32 - prefixLength);
        byte[] bytes = new byte[]{(byte) (value >>> 24), (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff), (byte) (value & 0xff)};

        try {
            return InetAddress.getByAddress(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    private static List<InetAddress> checkPort(InetAddress defaultGateway, InetAddress subnetMask, int port) {
        byte[] address = defaultGateway.getAddress();
        byte[] mask = subnetMask.getAddress();
        boolean con = true;


        List<InetAddress> inetAddresses = new ArrayList<>();
        try (ExecutorService executorService = newFixedThreadPool(10)) {
            while (con) {
                executorService.submit(() -> {
                    try (Socket socket = new Socket(InetAddress.getByAddress(address), port)) {
                        if (socket.isConnected()) inetAddresses.add(socket.getInetAddress());
                    } catch (IOException ignored) {

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
                    }

                    count--;
                    if (count == 0) {
                        con = false;
                    }
                }
            }

            try {
                if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }


        return inetAddresses;
    }

}
