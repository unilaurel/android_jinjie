package com.example.udpdemo;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpServer {
    private static final String TAG = "UdpServer";
    private InetAddress mInetAddress;
    private int mPort = 7777;//できるだけ5000以後使用する
    private DatagramSocket mSocket;

    private Scanner mScanner;

    public UdpServer() {
        try {
            mInetAddress = InetAddress.getLocalHost();
            mSocket = new DatagramSocket(mPort, mInetAddress);

            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(receivePacket);

                InetAddress address = receivePacket.getAddress();
                int port = receivePacket.getPort();
                byte[] data = receivePacket.getData();
//                String clientMsg = new String(data);
                String clientMsg = new String(data,0,receivePacket.getLength());


                System.out.println( "address=" + address + " port:" + port + " Msg:" + clientMsg);

                //return message
                String returnedMsg = mScanner.next();
                byte[] returnedMsgBytes = returnedMsg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(returnedMsgBytes,
                        returnedMsgBytes.length, receivePacket.getSocketAddress());
                mSocket.send(sendPacket);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UdpServer().start();
    }
}
