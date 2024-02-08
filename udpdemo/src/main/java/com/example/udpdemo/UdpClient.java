package com.example.udpdemo;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClient {
    private static final String TAG = "UdpClient";
    private String mServerIp = "192.168.1.49";
    private int mServerPort = 7777;
    private InetAddress mServerAddress;
    private DatagramSocket mSocket;
    private Scanner mScanner;

    public UdpClient() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                //send
                String clientMsg = mScanner.next();
                byte[] clientMsgBytes = clientMsg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                        clientMsgBytes.length, mServerAddress, mServerPort);
                mSocket.send(clientPacket);

                //receive
                byte[] buf = new byte[1024];
                DatagramPacket serverPacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(serverPacket);

                InetAddress address = serverPacket.getAddress();
                int port = serverPacket.getPort();
//                byte[] data = serverPacket.getData();
//                String serverMsg = new String(data,0,data.length);
                String serverMsg = new String(serverPacket.getData(),0,serverPacket.getLength());

                System.out.println( "address=" + address + " port:" + port + " Msg:" + serverMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UdpClient().start();
    }
}
