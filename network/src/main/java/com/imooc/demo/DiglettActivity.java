package com.imooc.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

public class DiglettActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final int RANDOM_NUM = 1000;
    private static int CODE=123;
    private Button mButton;
    private TextView mTextView;
    private ImageView mImageView;
    private int mScreenWidth;
    private int mScreenHeight;

    private static final int MAX_COUNT = 10;

    private int[][] mPosition = new int[10][2];

    private int mTotalCount;
    private int mSuccessCount;

    private DigletHandler mHandler = new DigletHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diglett);

        initViews();
        setTitle("打地鼠");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWH();
            Random r = new Random();

            for (int i = 0; i < mPosition.length; i++) {
                int width = r.nextInt(mScreenWidth);
                int height = r.nextInt(mScreenHeight);
                mPosition[i][0] = width;
                mPosition[i][1] = height;
            }
        }
    }


    private void initViews() {
        mButton = findViewById(R.id.start_button);
        mTextView = findViewById(R.id.textView2);
        mImageView = findViewById(R.id.imageView);

        mButton.setOnClickListener(this);
        mImageView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_button) {
            start();
        }
    }

    private void start() {
        //messageを発送する
        next(0);
        mTextView.setText("开始啦");
        mButton.setText("游戏中");
        mButton.setEnabled(false);
    }

    public void next(int delayTime){
        int position = new Random().nextInt(mPosition.length);

        Message message = Message.obtain();
        message.what=CODE;
        message.arg1 = position;

        mHandler.sendMessageDelayed(message, delayTime);
        mTotalCount++;

    }

    public void getWH() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.setVisibility(View.GONE);
        mSuccessCount++;
        mTextView.setText("打到了"+mSuccessCount+"只，共"+MAX_COUNT+"只");
        return false;
    }

    public  static class DigletHandler extends Handler{

        public final WeakReference<DiglettActivity> mWeakRefrence;

        public DigletHandler(DiglettActivity activity) {
            this.mWeakRefrence = new WeakReference<DiglettActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DiglettActivity activity = mWeakRefrence.get();

            if(msg.what== CODE){
                if(activity.mTotalCount>MAX_COUNT){
                    activity.clear();
                    Toast.makeText(activity, "地鼠打完了", Toast.LENGTH_SHORT).show();
                    return;
                }
                int position = msg.arg1;
                activity.mImageView.setX(activity.mPosition[position][0]);
                activity.mImageView.setY(activity.mPosition[position][1]);
                activity.mImageView.setVisibility(View.VISIBLE);

                int randomTime=new Random().nextInt(RANDOM_NUM)+RANDOM_NUM;
                activity.next(randomTime);

            }

        }
    }

    private void clear() {
        mTotalCount=0;
        mSuccessCount=0;
        mImageView.setVisibility(View.GONE);
        mButton.setText("点击开始");
        mButton.setEnabled(true);
    }
}