package com.example.udpdemo.tcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class MsgPool {
    private static MsgPool sInstance = new MsgPool();
    private LinkedBlockingDeque<String> mQueue = new LinkedBlockingDeque<>();

    public static MsgPool getInstance() {
        return sInstance;
    }

    private MsgPool() {
    }
    
    public void  start(){
        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        String msg = mQueue.take();
                        notifyMsgComing(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void notifyMsgComing(String msg) {
        for (MsgComingListener listener : mListeners) {
            listener.onMsgComing(msg);
        }
    }

    public interface MsgComingListener {
        void onMsgComing(String msg);
    }

    private List<MsgComingListener> mListeners = new ArrayList<>();

    public void addMsgComingListener(MsgComingListener listener) {
        mListeners.add(listener);
    }

    public void sendMsg(String msg) {
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
