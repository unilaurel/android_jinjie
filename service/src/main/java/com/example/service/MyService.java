package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.health.connect.changelog.ChangeLogTokenRequest;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;

public class MyService extends Service {

    private static final String TAG = "MyService";
    private int i;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        //新しいスレッドを作成し、時間がかかるタスクを模擬する
        new Thread() {
            @Override
            public void run() {
                for (i = 0; i < 100; i++) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }


// onBindメソッドに関しては、iBinderオブジェクトを返す必要があります。
// 実際には、私たちはBindクラスを継承した内部クラスを自分で定義することが一般的です。
    class MyBinder extends Binder {
        public int getProgress() {
            return i;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind: ");
//        return new MyBinder();
        return new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public void showProgress() throws RemoteException {
                Log.d(TAG, "showProgress: 当前进度是："+i);
            }
        };
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}