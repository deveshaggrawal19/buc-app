package com.buyucoin.buyucoin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static Bundle toBundle(JSONObject item) {
        Bundle bundle = new Bundle();
        for(int i=0; i<item.names().length(); i++){
            try {
                bundle.putString(item.names().getString(i),item.getString(item.names().getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }

    public static void hideProgressBar(final ProgressBar pb){
        pb.animate().alpha(0f).setDuration(1).setListener(new AnimatorListenerAdapter(){
            public void onAnimationEnd(Animator animator) {
                pb.setVisibility(View.GONE);
                pb.setAlpha(1f);
            }
        });
    }

    public static void showProgressBar(final ProgressBar pb){
        pb.setVisibility(View.VISIBLE);
    }

    public static void showToast(final Activity activity, final String text){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
