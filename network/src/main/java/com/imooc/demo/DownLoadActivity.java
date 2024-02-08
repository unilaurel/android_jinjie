package com.imooc.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


//http://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk
public class DownLoadActivity extends AppCompatActivity {

    public static final int DOWNLOAD_MESSAGE_CODE = 10001;
    private static final int DOWNLOAD_MESSAGE_FAILD_CODE = 10002;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
/**
 * メインスレッド--開始
 * ボタンをクリックして、ダウンロードを開始します
 * サブスレッドを開始してダウンロードを実行します
 * ダウンロードが完了したらメインスレッドに通知します
 * メインスレッドで進捗バーを更新します
 */

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download("https://img0.baidu.com/it/u=2460073190,3052311705&fm=253&fmt=auto&app=138&f=JPEG?w=890&h=500");
                    }
                }).start();
            }
        });

        ProgressBar pb = findViewById(R.id.progressBar);

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == DOWNLOAD_MESSAGE_CODE) {
                    pb.setProgress((Integer) msg.obj);
                }
            }
        };
    }

    private void download(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(300*1000);
            InputStream inputStream = urlConnection.getInputStream();
            //file lengthを取得する
            int contentLength = urlConnection.getContentLength();
            String downloadFileName = getExternalFilesDir(null) + File.separator + "imooc" + File.separator;
            File file = new File(downloadFileName);
            if (!file.exists()) {
                file.mkdir();
            }
            String fileName = downloadFileName + "imooc.jpg";
            File apkFile = new File(fileName);

            int downloadSize = 0;
            byte[] bytes = new byte[1024];
            int length = 0;
            OutputStream os = new FileOutputStream(fileName);
            while ((length = inputStream.read(bytes)) != -1) {
                os.write(bytes, 0, length);
                downloadSize += length;

                Message message = Message.obtain();
                message.obj = downloadSize * 100 / contentLength;
                message.what = DOWNLOAD_MESSAGE_CODE;
                mHandler.sendMessage(message);
            }

            inputStream.close();
            os.close();

        } catch (MalformedURLException e) {
            sendMessageFailed();
            e.printStackTrace();

        } catch (IOException e) {
            sendMessageFailed();
            e.printStackTrace();
        }
    }

    private void sendMessageFailed() {
        Message message = Message.obtain();
        message.what = DOWNLOAD_MESSAGE_FAILD_CODE;
        mHandler.sendMessage(message);
    }
}