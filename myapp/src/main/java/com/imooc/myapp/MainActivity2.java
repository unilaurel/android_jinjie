package com.imooc.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2-app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.d(TAG, "onCreate: "+getApplication()+","+this);
        setTitle("MainActivity2");

    }

    public void onClick(View v){
        if(v.getId() == R.id.btnGetUserName) {
            MyApp myApp= (MyApp) getApplication();
            String userName = myApp.getUserName();
            Log.d(TAG, "onClick: username "+userName);
            Toast.makeText(MainActivity2.this,myApp.getUserName(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+BusProvider.getBus());
        BusProvider.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: "+BusProvider.getBus());
        BusProvider.getBus().unregister(this);
    }
}