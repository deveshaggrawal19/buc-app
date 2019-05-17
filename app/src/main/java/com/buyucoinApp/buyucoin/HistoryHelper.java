package com.buyucoinApp.buyucoin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryHelper {

    private static JSONObject j = null;

    public static JSONObject getHistory(String url, String Access_token, final ProgressBar progressBar, final FragmentActivity activity){
            OkHttpHandler.auth_get(url + "_history", Access_token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    j = null;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        assert response.body() != null;
                        try {
                        String s = response.body().string();
                            j = new JSONObject(s).getJSONObject("data");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.animate().alpha(0f).setDuration(progressBar.getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                                        public void onAnimationEnd(Animator animator) {
                                            progressBar.setVisibility(View.GONE);
                                            progressBar.setAlpha(1f);
                                        }
                                    });
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            });

            return j;

    }

    public static JSONObject getData(){
        return  j;
    }
}
