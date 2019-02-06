package com.buyucoin.buyucoin.config;

import android.app.Activity;

import com.buyucoin.buyucoin.BuildConfig;
import com.google.firebase.database.FirebaseDatabase;

public class Config extends Activity {

    String BASE_URL = BuildConfig.BASE_URL;
    public Config() { }
    public FirebaseDatabase getProductionFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }
    public String getAPI_BASE_URL(){
        return this.BASE_URL;
    }




}
