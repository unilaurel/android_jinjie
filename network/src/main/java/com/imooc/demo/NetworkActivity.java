package com.imooc.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//URL https://www.imooc.com/api/teacher?type=2&page=1
public class NetworkActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NetworkActivity";
    public static final String GET_URL = "https://www.imooc.com/api/teacher?type=2&page=1";
    public static final String POST_URL = "https://www.imooc.com/api/teacher";
    Button mGetButton;
    Button mParseDataButton;
    TextView mTextView;
    private String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
    }

    private void setListeners() {
        mGetButton.setOnClickListener(this);
        mParseDataButton.setOnClickListener(this);
    }

    private void findViews() {
        mGetButton = findViewById(R.id.getButton);
        mParseDataButton = findViewById(R.id.parseDataButton);
        mTextView = findViewById(R.id.textView);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.getButton) {
            requestDataByGet();

        }else if(v.getId()==R.id.parseDataButton){
            handleJsonData(mResult);
        }
    }

    private void handleJsonData(String result) {
        try {
            LessonResult lessonResult = new LessonResult();
            List<LessonResult.Lesson> data = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(result);
            int status = jsonObject.getInt("status");
            lessonResult.setmStatus(status);
            JSONArray lessons = jsonObject.getJSONArray("data");
            if(lessons!=null && lessons.length()>0){
                for (int i = 0; i < lessons.length(); i++) {
                    JSONObject lesson = lessons.getJSONObject(i);
                    int id = lesson.getInt("id");
                    int learner = lesson.getInt("learner");
                    String name = lesson.getString("name");
                    String picSmall = lesson.getString("picSmall");
                    String picBig = lesson.getString("picBig");
                    String description = lesson.getString("description");
                    LessonResult.Lesson lesson1=new LessonResult.Lesson();
                    lesson1.setmId(id);
                    lesson1.setmLearnerNum(learner);
                    lesson1.setmSmallPictureUrl(picSmall);
                    lesson1.setmBigPictureUrl(picBig);
                    lesson1.setmDescription(description);
                    data.add(lesson1);

                }

                lessonResult.setmLessons(data);
            }
            mTextView.setText(lessonResult.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestDataByGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GET_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30*1000);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("Accept-Charset","UTF-8");
                    connection.setRequestProperty("Charset","UTF-8");
//                connection.connect();
                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();
                    if(responseCode==HttpURLConnection.HTTP_OK){
                        InputStream is = connection.getInputStream();
                        mResult = streamToString(is);
                        Log.d(TAG, "run: result:"+ mResult);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText(mResult);
                            }
                        });
                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                mResult=decode(mResult);
                                mTextView.setText(mResult);
                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void requestDataByPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(POST_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30*1000);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("Accept-Charset","UTF-8");
                    connection.setRequestProperty("Charset","UTF-8");

                    connection.setDoInput(true);//server から取得,post
                    connection.setDoOutput(true);//server へ送信,post
                    connection.setUseCaches(false);
//                connection.connect();

                    String data = "username="+getEncodeValue("imooc")+"&number="+getEncodeValue("15088886666");
                    OutputStream os = connection.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();
                    if(responseCode==HttpURLConnection.HTTP_OK){
                        InputStream is = connection.getInputStream();
                        mResult = streamToString(is);
                        Log.d(TAG, "run: result:"+ mResult);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText(mResult);
                            }
                        });
                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                mResult=decode(mResult);
                                mTextView.setText(mResult);
                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getEncodeValue(String imooc) {
        String encode=null;
        try {
            encode = URLEncoder.encode(imooc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encode;
    }

    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }


    /**
     * 将Unicode字符转换为UTF-8类型字符串
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
}