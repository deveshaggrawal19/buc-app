package com.buyucoin.buyucoin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    public static boolean isSuccess(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getString("status").equals("success")){
                return true;
            }
            Log.d("UNPARSED RESPONSE iS", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean isSuccessRedirect(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getString("status").equals("redirect")){
                return jsonObject.getJSONArray("message").getJSONArray(0).getString(1).equals("success");
            }
            Log.d("UNPARSED RESPONSE iSR", s);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getErrorMessage(String s){

        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getString("status").equals("error"))
                if(jsonObject.has("msg"))
                    return jsonObject.getString("msg");
                else
                    return jsonObject.getJSONArray("message").getJSONArray(0).getString(0);

            Log.d("UNPARSED RESPONSE gEM", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getOTP(final Activity activity, final String ACCESS_TOKEN, final AlertDialog.Builder builder){
        OkHttpHandler.auth_get("get_otp", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                if(isSuccess(s)){
                    getOTPFromUser(activity, ACCESS_TOKEN, builder);
                }else{
                    getOTPFromUser(activity, ACCESS_TOKEN, builder);
                }
            }
        });
    }

    public static void getOTPFromUser(final Activity activity, final String ACCESS_TOKEN, final AlertDialog.Builder builder){
        final EditText otp = new EditText(activity.getApplicationContext());
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //otp.setLayoutParams(lp);
        builder.setView(otp);
        builder.setMessage("Enter OTP");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        submitNewOTP(otp.getText().toString(), ACCESS_TOKEN, activity, dialog);
                    }
                });
            }
        });


    }

    public static void submitNewOTP(String s, String ACCESS_TOKEN, final Activity activity, final AlertDialog dialog){
        JSONObject jo = new JSONObject();
        try {
            jo.put("otp", s);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //submitOTP
        OkHttpHandler.auth_post("otp_needed", ACCESS_TOKEN, jo.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), "Error retreiving API", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                if(isSuccess(s) || isSuccessRedirect(s)){
                    showToast(activity, "OTP registered");
                    dialog.dismiss();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.recreate();
                        }
                    });
                }else {
                    showToast(activity, getErrorMessage(s));
                }

            }
        });

    }
}
