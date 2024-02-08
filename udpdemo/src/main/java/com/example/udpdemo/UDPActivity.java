package com.example.udpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.udpdemo.biz.UdpClientBiz;

public class UDPActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;

    private UdpClientBiz mUdpClientBiz=new UdpClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    return;
                }
                appendMsgToContent("client: "+msg);
                mTvContent.setText("");
                mUdpClientBiz.sendMsg(msg, new UdpClientBiz.onMsgReturnedListener() {
                    @Override
                    public void onMsgReturned(String msg) {
                        appendMsgToContent("server: "+msg);
                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        });
    }

    private void appendMsgToContent(String msg) {
        mTvContent.append(msg+"\n");
    }

    private void initViews() {
        mEtMsg = findViewById(R.id.id_et_msg);
        mBtnSend = findViewById(R.id.id_btn_send);
        mTvContent = findViewById(R.id.id_tv_content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUdpClientBiz.onDestroy();
    }
}