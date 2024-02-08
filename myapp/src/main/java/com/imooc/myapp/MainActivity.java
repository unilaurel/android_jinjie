package com.imooc.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Bus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + getApplication()+","+this);
        setTitle("MainActivity");

        Button btn = findViewById(R.id.btnGotoActivity2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp myApp = (MyApp) getApplication();
        Bus bus = myApp.getBus();
        Log.d(TAG, "onResume: "+bus);
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp myApp = (MyApp) getApplication();
        Bus bus = myApp.getBus();
        Log.d(TAG, "onPause: "+bus);
        bus.unregister(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnGotoActivity2) {
            startActivity(new Intent(this, MainActivity2.class));
        }else if(v.getId() == R.id.btnGotoService) {
            startService(new Intent(this, MyService.class));
        }else if(v.getId() == R.id.btnSetUserName) {
            MyApp myApp= (MyApp) getApplication();
            myApp.setUserName("fafa");
            Toast.makeText(MainActivity.this,myApp.getUserName(),Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.btnGetUserName) {
            MyApp myApp= (MyApp) getApplication();
            String userName = myApp.getUserName();
            Log.d(TAG, "onClick: username "+userName);
            Toast.makeText(MainActivity.this,myApp.getUserName(),Toast.LENGTH_SHORT).show();
        }
    }
}