package com.imooc.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 1. ネットワークでデータをリクエスト：パーミッションのリクエスト：ネットワークの許可、ストレージの許可
 * 2. レイアウトの設定
 * 3. ダウンロード前に何を行いますか UI
 * 4. ダウンロード中に何を行いますか データ
 * 5. ダウンロード後に何を行いますか UI
 */

public class AsyncTaskActivity extends AppCompatActivity {
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

DownLoadHelper.download(APK_URL, "", new DownLoadHelper.OnDownloadListener() {
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(int code, File file) {

    }

    @Override
    public void onFail(int code, File file, String message) {

    }

    @Override
    public void onProgress(int progress) {

    }
});
    }

    private void setData() {
        mProgressBar.setProgress(0);
        mButton.setText("点击下载");
        mTextView.setText("准备下载");
    }

    private void setListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO: 2024/01/29  download Task
                DownloadAsyncTask asyncTask = new DownloadAsyncTask();
                asyncTask.execute(APK_URL);
            }
        });
    }


    private void initViews() {
        mProgressBar = findViewById(R.id.asy_progressBar);
        mButton = findViewById(R.id.asy_button);
        mTextView = findViewById(R.id.asy_textView);
    }


    /**
     * string： input para，
     * integer ：progress，
     * boolean：result
     */
    public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        /**
         * 非同期タスクの前、メインスレッド内
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIを操作できる,準備する
            mButton.setText("下载中");
            mTextView.setText("下载中");
            mProgressBar.setProgress(INIT_PROGRESS);
        }

        /**
         * 他のスレードで事件を処理する
         * @param params 入力パラマー
         * @return　結果
         */
        @Override
        protected Boolean doInBackground(String... params) {
            if (params != null && params.length > 0) {
                String apkUrl = params[0];
                //URLを作成する
                try {
                    URL url = new URL(apkUrl);
                    URLConnection urlConnection = url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    int contentLength = urlConnection.getContentLength();//File total Length

                    mFilePath = getExternalCacheDir() + File.separator + FILENAME;

                    File apkFile = new File(mFilePath);
                    if (apkFile.exists()) {
                        boolean result = apkFile.delete();
                        if (!result) {
                            return false;
                        }
                    }

                    //downloaded size
                    int downloadSize = 0;
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    OutputStream os = new FileOutputStream(apkFile);
                    while ((length = is.read(bytes)) != -1) {
                        os.write(bytes, 0, length);
                        downloadSize += length;  //downloaded size の増加
                        //processを発送する
                        publishProgress(downloadSize * 100 / contentLength);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }

        /**
         * メインスレッド内，，結果の処理ができる
         *
         */
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mButton.setText(result? getString(R.string.download_finish): getString(R.string.download_fail));
            mTextView.setText(result? getString(R.string.download_finish)+mFilePath: getString(R.string.download_fail));


        }

        /**
         * メインスレッド内，progressを取得する
         *
         * @param values The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values!=null && values.length>0){
                mProgressBar.setProgress(values[0]);
            }
        }
    }
}