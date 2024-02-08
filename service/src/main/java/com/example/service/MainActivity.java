package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        // クライアントがサービスに正常に接続している場合、サービスのバインド操作が呼び出されます。
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d(TAG, "onServiceConnected:muke ");
//            MyService.MyBinder mb= (MyService.MyBinder) service;
//            int progress = mb.getProgress();
//            Log.d(TAG, "onServiceConnected: 当前进度是："+progress);

            IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(service);
            try {
                imai.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // クライアントとサービスの接続が失われた場合
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: muk2");
        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operate(View v) {
        if (v.getId() == R.id.start) {
            Intent it1 = new Intent(this, MyService.class);
            startService(it1);
        } else if (v.getId() == R.id.stop) {
            Intent it2 = new Intent(this, MyService.class);
            stopService(it2);
        } else if (v.getId() == R.id.bind) {
            Intent it3 = new Intent(this, MyService.class);
            bindService(it3, conn, BIND_AUTO_CREATE);
        } else if (v.getId() == R.id.unbind) {
            unbindService(conn);
        }
    }
}