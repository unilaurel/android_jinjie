package com.example.udpdemo.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;



public class UdpServer {

    private InetAddress mInetAddress;
    private int mPort = 7777;
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
                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(receivedPacket);

                InetAddress address = receivedPacket.getAddress();
                int port = receivedPacket.getPort();
                String clientMsg = new String(receivedPacket.getData(),
                        0, receivedPacket.getLength());
                System.out.println("address = " + address
                        + " , port = " + port + " , msg = " + clientMsg);

                String returnedMsg = mScanner.next();
                byte[] returnedMsgBytes = returnedMsg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(returnedMsgBytes,
                        returnedMsgBytes.length, receivedPacket.getSocketAddress());
                mSocket.send(sendPacket);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args){
        new UdpServer().start();
    }


}
