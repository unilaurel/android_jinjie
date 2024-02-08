package com.example.udpdemo.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class TcpClientBiz {
    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;
    private OnMsgComingListener mListener;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public void setOnMsgComingListener(OnMsgComingListener listener) {
        mListener = listener;
    }

    public interface OnMsgComingListener {
        void onMsgComing(String msg);

        void onError(Exception exception);
    }

    public TcpClientBiz() {
        new Thread() {
            @Override
            public void run() {

                try {
                    mSocket = new Socket("192.168.1.49", 9090);
                    mIs = mSocket.getInputStream();
                    mOs = mSocket.getOutputStream();
                    readServerMsg();
                } catch (IOException e) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onError(e);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void readServerMsg() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(mIs));
        String line = null;
        while (((line = br.readLine()) != null)) {
            String finalLine = line;
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onMsgComing(finalLine);
                    }
                }
            });
        }
    }

    public void sendMsg(String msg) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mOs));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void onDestroy() {
        //各解放処理を独立の try-catch ブロックで囲むことで、一つの例外が他の解放処理に影響を与えないようにしています。
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mIs != null) {
            try {
                mIs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mOs != null) {
            try {
                mOs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

