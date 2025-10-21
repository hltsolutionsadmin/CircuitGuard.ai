package com.skillrat.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {

    public static boolean ipMatches(String ip, String cidr) {
        String[] parts = cidr.split("/");
        String network = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            InetAddress networkAddress = InetAddress.getByName(network);

            byte[] ipBytes = ipAddress.getAddress();
            byte[] networkBytes = networkAddress.getAddress();

            int bytesToCheck = prefixLength / 8;
            int bitsToCheck = prefixLength % 8;

            for (int i = 0; i < bytesToCheck; i++) {
                if (ipBytes[i] != networkBytes[i]) {
                    return false;
                }
            }

            if (bitsToCheck > 0) {
                int mask = 0xFF << (8 - bitsToCheck);
                return (ipBytes[bytesToCheck] & mask) == (networkBytes[bytesToCheck] & mask);
            }

            return true;
        } catch (UnknownHostException e) {
            throw new RuntimeException("Invalid IP address or CIDR notation", e);
        }
    }
}

