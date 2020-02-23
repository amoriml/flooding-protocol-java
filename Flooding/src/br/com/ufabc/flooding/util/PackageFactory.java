package br.com.ufabc.flooding.util;

import java.io.IOException;
import java.net.*;

public abstract class PackageFactory {

    public static DatagramPacket createPackage(String message, String destinyHost, int destinyPort)
            throws UnknownHostException {
        return new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(destinyHost),
                destinyPort);
    }

    public static DatagramPacket createPackage(String message) {
        return new DatagramPacket(message.getBytes(), message.getBytes().length);
    }

    public static DatagramPacket createPackage(byte[] buffer) {
        return new DatagramPacket(buffer, buffer.length);
    }

    public static DatagramSocket createSocket() throws SocketException {
        return new DatagramSocket();
    }

    public static DatagramSocket createSocket(int port) throws SocketException {
        return new DatagramSocket(port);
    }

    public static ServerSocket createServerSocket() throws IOException{
        return new ServerSocket();
    }
}
