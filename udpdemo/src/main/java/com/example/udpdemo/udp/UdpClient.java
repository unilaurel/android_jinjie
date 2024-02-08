package com.example.udpdemo.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;



public class UdpClient {

    private String mServerIp = "192.168.1.49";
    private InetAddress mServerAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;
    private Scanner mScanner;


    public UdpClient() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {

            try {
                String clientMsg = mScanner.next();
                byte[] clientMsgBytes = clientMsg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                        clientMsgBytes.length, mServerAddress, mServerPort);
                mSocket.send(clientPacket);

                byte[] buf = new byte[1024];
                DatagramPacket serverMsgPacket = new DatagramPacket(buf ,buf.length);
                mSocket.receive(serverMsgPacket);

                String serverMsg = new String(serverMsgPacket.getData(),
                        0, serverMsgPacket.getLength());
                System.out.println("msg = " + serverMsg);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args){
        new UdpClient().start();
    }

}
