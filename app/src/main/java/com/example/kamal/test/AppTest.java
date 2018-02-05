package com.example.kamal.test;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by KAMAL on 2/5/2018.
 */

public class AppTest extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
