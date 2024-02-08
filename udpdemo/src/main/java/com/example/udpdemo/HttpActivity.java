package com.example.udpdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.udpdemo.biz.TcpClientBiz;
import com.example.udpdemo.https.HttpUtils;

public class HttpActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_http);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    return;
                }

                Log.i(TAG, "onClick: "+url);
                HttpUtils.doGet(url, new HttpUtils.HttpListener() {
                    @Override
                    public void onSuccess(String content) {
                        mTvContent.append(content);
                    }

                    @Override
                    public void onFail(Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        });


    }


    private void initViews() {
        mEtMsg = findViewById(R.id.id_et_msg);
        mBtnSend = findViewById(R.id.id_btn_send);
        mTvContent = findViewById(R.id.id_tv_content);
    }


}