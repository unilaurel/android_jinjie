package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private UninstallReceiver uninstallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 注册卸载广播接收器
        uninstallReceiver = new UninstallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(uninstallReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 取消注册卸载广播接收器
        if (uninstallReceiver != null) {
            unregisterReceiver(uninstallReceiver);
        }
    }



    public class UninstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                // 获取卸载应用的包名
                String packageName = intent.getData().getSchemeSpecificPart();

                // 处理卸载广播
                Log.d("UninstallReceiver", "Package removed: " + packageName);
            }
        }
    }

}

