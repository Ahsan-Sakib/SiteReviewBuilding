package com.example.buildingconstraction.is229443.App;

import android.app.Application;

import com.example.buildingconstraction.is229443.sharedPreference.AppSharedPref;

public class StartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppSharedPref.init(getApplicationContext());
    }
}
