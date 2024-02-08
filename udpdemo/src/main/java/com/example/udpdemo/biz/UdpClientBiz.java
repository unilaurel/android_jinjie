package com.example.udpdemo.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClientBiz {
    private static final String TAG = "UdpClient";
    private String mServerIp = "192.168.1.49";
    private int mServerPort = 7777;
    private InetAddress mServerAddress;
    private DatagramSocket mSocket;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
//    private Handler mUIHandler = new Handler();

    public UdpClientBiz() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public interface onMsgReturnedListener {
        void onMsgReturned(String msg);

        void onError(Exception exception);
    }

    public void sendMsg(String msg, onMsgReturnedListener listener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    //send
                    byte[] clientMsgBytes = msg.getBytes();
                    DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                            clientMsgBytes.length, mServerAddress, mServerPort);
                    mSocket.send(clientPacket);

                    //receive
                    byte[] buf = new byte[1024];
                    DatagramPacket serverPacket = new DatagramPacket(buf, buf.length);
                    mSocket.receive(serverPacket);
                    String serverMsg = new String(serverPacket.getData(), 0, serverPacket.getLength());
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onMsgReturned(serverMsg);
                        }
                    });

                } catch (IOException e) {
                    listener.onError(e);
                }
            }
        }.start();

    }

    public void onDestroy() {
        if (mSocket != null) {
            mSocket.close();
        }
    }

}
