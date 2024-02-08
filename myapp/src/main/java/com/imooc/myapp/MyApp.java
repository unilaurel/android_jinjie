package com.imooc.myapp;

import android.app.Application;
import android.content.res.Configuration;
import android.text.style.BulletSpan;
import android.util.Log;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;

public class MyApp extends Application {
    private static final String TAG = "MyApp-app";

    private String userName;
    private Bus bus;

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+this);
        Log.d(TAG, "onCreate: "+Thread.currentThread());
        bus = new Bus();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: "+newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory: ");
    }
}
