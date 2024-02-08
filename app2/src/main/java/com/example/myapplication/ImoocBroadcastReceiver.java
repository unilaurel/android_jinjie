package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class ImoocBroadcastReceiver extends BroadcastReceiver {

    TextView mTextView;

    public ImoocBroadcastReceiver(TextView mTextView) {
        this.mTextView = mTextView;
    }

    public ImoocBroadcastReceiver() {
    }

    private static final String TAG = "ImoocBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //ブロードギャストを受信する
        if (intent != null) {
            //どんなブロードギャストを受信するか
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);

            //自分で発信したbroadcast
            if (TextUtils.equals(action, MainActivity0.MY_ACTION)) {
                if (mTextView != null) {
                    String content = intent.getStringExtra(MainActivity0.BROADCAST_CONTENT);
                    mTextView.setText("接收到的action是：" + action + ",\n接收到的内容是：" + content);
                }
            }
        }
    }
}
