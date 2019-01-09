package com.buyucoin.buyucoin.config;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.buyucoin.buyucoin.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Config extends Activity {

     String PRODUCTION_URL = "https://buyucoin.com/andrios/";
     String DEVELOPMENT_URL = "http://test.buyucoin.com/andrios/";
     private boolean isProduction = false;
    FirebaseApp PRODUCTION_APP;
     String PRODUCTION_DATABASE_NAME = "PRODUCTION_DATABASE";

    private FirebaseOptions FB_PRODUCTION = new FirebaseOptions.Builder()
            .setApplicationId("production app id")
            .setApiKey("production api key")
            .setDatabaseUrl("production db url")
            .build();

    public Config() {


    }

    public FirebaseDatabase getProductionFirebaseDatabase(Context context){


        if (isProduction){
            FirebaseApp.initializeApp(context,FB_PRODUCTION,PRODUCTION_DATABASE_NAME);
            PRODUCTION_APP = FirebaseApp.getInstance(PRODUCTION_DATABASE_NAME);
            return FirebaseDatabase.getInstance(PRODUCTION_APP);
        }
        return FirebaseDatabase.getInstance();
    }

    public String getAPI_BASE_URL(){
        if (isProduction){
            Log.d("PRODUCTION_URL",PRODUCTION_URL);
            return this.PRODUCTION_URL;
        }
        Log.d("DEVELOPMENT_URL",DEVELOPMENT_URL);

        return this.DEVELOPMENT_URL;
    }




}
