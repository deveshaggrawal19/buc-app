package com.buyucoinApp.buyucoin.update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class ForceUpdateChecker {
    public static final String KEY_UPDATE_NAME = "name";
    private static final String TAG = ForceUpdateChecker.class.getSimpleName();
    public static final String KEY_UPDATE_REQUIRED = "isUpdate";
    public static final String KEY_CURRENT_VERSION = "version";
    public static final String KEY_UPDATE_URL = "url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    private ForceUpdateChecker(@NonNull Context context,
                               OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    private void check() {
        new CoustomToast(context,"Checking for Update",CoustomToast.TYPE_NORMAL).showToast();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference updateref = firebaseDatabase.getReference();

        updateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot update = dataSnapshot.child("update");
                boolean isUpdate = update.child(KEY_UPDATE_REQUIRED).getValue(Boolean.class);
                String currentVersion = getAppVersion(context);
                String appVersion = update.child(KEY_CURRENT_VERSION).getValue(String.class);
                String updateUrl = update.child(KEY_UPDATE_URL).getValue(String.class);

                Log.d(TAG, "ISUPDATE :"+isUpdate);
                Log.d(TAG, "CURRENT VERSION :"+currentVersion);
                Log.d(TAG, "APP VERSION :"+appVersion);
                Log.d(TAG, "URL :"+updateUrl);



                if (isUpdate) {
                    if (!TextUtils.equals(currentVersion, appVersion) && onUpdateNeededListener != null) {
                        onUpdateNeededListener.onUpdateNeeded(updateUrl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }
}
