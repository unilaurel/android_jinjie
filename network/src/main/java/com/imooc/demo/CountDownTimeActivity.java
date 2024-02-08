package com.imooc.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class CountDownTimeActivity extends AppCompatActivity {

    public static final int COUNTDOWN_INT_MODE = 119;
    public static final int DELAY_MILLIS = 1000;
    public static final int MAX = 10;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_time);

        mTextView = findViewById(R.id.countdownTimeTextView);

        CountdownTimeHandler handler = new CountdownTimeHandler(this);
        Message message = Message.obtain();
        message.what = COUNTDOWN_INT_MODE;
        message.arg1 = MAX;

        handler.sendMessageDelayed(message, DELAY_MILLIS);
    }

    public static class CountdownTimeHandler extends Handler {
        final WeakReference<CountDownTimeActivity> mWeakRefrence;

        public CountdownTimeHandler(CountDownTimeActivity activity) {
            this.mWeakRefrence = new WeakReference<CountDownTimeActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            super.handleMessage(msg);
            CountDownTimeActivity activity = mWeakRefrence.get();//弱引用でオブジェクトを取得する
            if (msg.what == COUNTDOWN_INT_MODE) {
                int value = msg.arg1;
                activity.mTextView.setText(String.valueOf(value--));

                if (value >=0) {
                    Message message = Message.obtain();
                    message.what = COUNTDOWN_INT_MODE;
                    message.arg1 = value;
                    sendMessageDelayed(message, DELAY_MILLIS);
                } else {
                }

            }
        }
    }

}