package com.imooc.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * 1. ネットワークでデータをリクエスト：パーミッションのリクエスト：ネットワークの許可、ストレージの許可
 * 2. レイアウトの設定
 * 3. ダウンロード前に何を行いますか UI
 * 4. ダウンロード中に何を行いますか データ
 * 5. ダウンロード後に何を行いますか UI
 */

public class AsyncTaskActivity2 extends AppCompatActivity {
    public static final int INIT_PROGRESS = 0;
    public static final String APK_URL = "https://img2.baidu.com/it/u=3154304441,881083203&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=666";
    public static final String FILENAME = "IMOOC.jpg";
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView mTextView;
    private static final String TAG = "AsyncTaskActivity";
    String mFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

//        new DownloadAsyncTask().execute("imooc", "good");
        //View を初期化する
        initViews();
        //Listener を設定する
        setListeners();
        //UI Dataを初期化する
        setData();


    }

    private void setListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // downloadを始める
                downloadFile();
            }
        });
    }

    private void downloadFile() {
        // 「非同期タスクを実行してファイルをダウンロードします」
        String downloadUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F25%2F20170825114626_c5mhn.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1709171258&t=8c385135b0f039b4fb8f6997ad7e09f9";
        String localPath = getExternalFilesDir(null).toString() + File.separator + (new Random().nextInt(100) + 100) + ".jpg"; // 替换为你的本地保存路径

        DownLoadHelper.download(downloadUrl, localPath, new DownLoadHelper.OnDownloadListener() {
            @Override
            public void onStart() {
                // download開始の操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //UIを操作できる,準備する
                        mButton.setText("下载中2");
                        mTextView.setText("下载中2");

                    }
                });
            }

            @Override
            public void onSuccess(int code, File file) {
                // download中の操作、UIを更新する
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: " + file.getAbsolutePath());
                        mTextView.setText("下载成功：" + code + file.getAbsolutePath());

                        mButton.setText("开始下载");

                    }
                });
            }

            @Override
            public void onFail(int code, File file, String message) {
                // download失敗の操作、UIを更新する
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(code + file.getAbsolutePath() + message);
                    }
                });
            }

            @Override
            public void onProgress(int progress) {
                // progressを更新する
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(progress);
                    }
                });
            }
        });
    }

    private void setData() {
        mProgressBar.setProgress(0);
        mButton.setText("点击下载");
        mTextView.setText("准备下载");
    }


    private void initViews() {
        mProgressBar = findViewById(R.id.asy_progressBar);
        mButton = findViewById(R.id.asy_button);
        mTextView = findViewById(R.id.asy_textView);
    }


}