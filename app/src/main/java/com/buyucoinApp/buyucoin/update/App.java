package com.buyucoinApp.buyucoin.update;

import android.app.Application;
import android.util.Log;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

                Map<String,Object> defaultValue = new HashMap<>();
                defaultValue.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED,false);
                defaultValue.put(ForceUpdateChecker.KEY_CURRENT_VERSION,"1.0");
                defaultValue.put(ForceUpdateChecker.KEY_UPDATE_NAME,"test name");
                defaultValue.put(ForceUpdateChecker.KEY_UPDATE_URL,"market://details?id="+getPackageName());

                remoteConfig.setDefaults(defaultValue);

        Log.e(TAG, "onCreate: checking update...");
                remoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "onComplete: completed");
                        Log.e(TAG, "onComplete: "+task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.e(TAG, "onComplete: "+remoteConfig.getString(ForceUpdateChecker.KEY_CURRENT_VERSION));
                            remoteConfig.activateFetched();
                            new CoustomToast(getApplicationContext(),remoteConfig.getString(ForceUpdateChecker.KEY_CURRENT_VERSION),CoustomToast.TYPE_SUCCESS).showToast();
                        }
                    }
                });

            }
    }
