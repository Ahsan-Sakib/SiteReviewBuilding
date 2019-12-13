package com.example.buildingconstraction.is229443.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.buildingconstraction.is229443.contants.AppContants;

public class AppSharedPref {
    static SharedPreferences sharedPreferences;

    public static void init(Context mContext){
        sharedPreferences = mContext.getSharedPreferences(AppContants.SharedPrefName,Context.MODE_PRIVATE);
    }
    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
