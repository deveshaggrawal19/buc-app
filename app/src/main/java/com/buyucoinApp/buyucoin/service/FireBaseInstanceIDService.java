package com.buyucoinApp.buyucoin.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FireBaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: "+s);
    }
}
