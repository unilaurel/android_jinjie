package com.imooc.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HandlerActivity extends AppCompatActivity {

    private static final String TAG = "HandlerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        TextView textView = findViewById(R.id.textView);

        //handler を作成す，匿名クラス
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "handleMessage: "+msg.what);
                if(msg.what==1001){
                    textView.setText("immooc");
                }else if(msg.what==1002){
                    Log.d(TAG, "handleMessage: "+msg.what+msg.arg1+msg.arg2+msg.obj);
                }
            }
        };

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       handler.sendEmptyMessage(1001);
                       Message message = Message.obtain();
                       message.what=1002;
                       message.arg1=1003;
                       message.arg2=1004;
                       message.obj=HandlerActivity.this;
                       handler.sendMessage(message);

                       Runnable runnable = new Runnable() {
                           @Override
                           public void run() {
                               int a = 1 + 2 + 3;
                           }
                       };
                       handler.post(runnable);
                       runnable.run();
                       handler.postDelayed(runnable, 2000);

                   }
               }).start();
            }
        });


    }
}