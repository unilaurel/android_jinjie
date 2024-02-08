package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity0 extends AppCompatActivity {

    public static final String MY_ACTION = "com.example.action";
    public static final  String BROADCAST_CONTENT = "broadcast content";
    private ImoocBroadcastReceiver mBroadcastReceiver = null;
    private EditText mInputEditText;
    private Button mButton;
    private TextView mResultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        setTitle(getPackageName());



        mInputEditText = findViewById(R.id.inputEditText);
        mButton = findViewById(R.id.sendBroadcastButton);
        mResultTextView = findViewById(R.id.resultTextView);
        //ブロードキャスト レシーバーを作成する
        mBroadcastReceiver = new ImoocBroadcastReceiver(mResultTextView);
        //どのブロードキャストを受信するか
        IntentFilter intentFilter = new IntentFilter();
        //ブロードキャスト　actionを定義する
        intentFilter.addAction(MY_ACTION);
        //ブロードキャスト レシーバーの登録
        registerReceiver(mBroadcastReceiver, intentFilter,RECEIVER_EXPORTED);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //broadcastを作成する
                Intent intent = new Intent(MY_ACTION);
                //dataを導入する
                intent.putExtra(BROADCAST_CONTENT, mInputEditText.getText().toString());
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ブロードキャスト レシーバーの登録をキャンセル
        if(mBroadcastReceiver!=null){
            unregisterReceiver(mBroadcastReceiver);
        }
    }
}