package com.imooc.demo;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 1. download メソッド　localPath Listener
 * 2. listener:start,success,fail,progress
 * 3. asyntaskを使用して，カプセル化
 */
public class DownLoadHelper {

    public static void download(String url, String localPath, OnDownloadListener listener){
        DownloadAsyncTask task = new DownloadAsyncTask(url,localPath,listener);
        task.execute();
    }

    public static class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        String mUrl;
        String mFilePath;
        OnDownloadListener mListener;

        public DownloadAsyncTask(String url, String filePath, OnDownloadListener listener) {
            this.mUrl = url;
            this.mFilePath = filePath;
            this.mListener = listener;
        }

        /**
         * 非同期タスクの前、メインスレッド内
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIを操作できる,準備する
            if(mListener!=null){
                mListener.onStart();
            }


        }

        /**
         * 他のスレードで事件を処理する
         * @param params 入力パラマー
         * @return　結果
         */
        @Override
        protected Boolean doInBackground(String... params) {

                String apkUrl =mUrl;
                //URLを作成する
                try {
                    URL url = new URL(apkUrl);
                    URLConnection urlConnection = url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    int contentLength = urlConnection.getContentLength();//File total Length


                    File apkFile = new File(mFilePath);
                    if (apkFile.exists()) {
                        boolean result = apkFile.delete();
                        if (!result) {
                            if(mListener!=null){
                                mListener.onFail(-1,apkFile," 文件删除失败");
                            }
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
                    if(mListener!=null){
                        mListener.onFail(-2,new File(mFilePath),e.getMessage());
                    }
                    return false;
                }

            if(mListener!=null){
                mListener.onSuccess(0,new File(mFilePath));
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
           if(mListener!=null){
               if(result){
                   mListener.onSuccess(0,new File(mFilePath));
               }else{
                   mListener.onFail(-2,new File(mFilePath)," 下载失败");
               }
           }
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
                if(mListener!=null){
                    mListener.onProgress(values[0]);
                }
            }
        }
    }

    public interface OnDownloadListener{
        void onStart();

        void onSuccess(int code, File file);

        void onFail(int code,File file,String message);

        void onProgress(int progress);
    }
}
