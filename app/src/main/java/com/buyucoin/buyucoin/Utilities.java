package com.buyucoin.buyucoin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
            if(jsonObject.getString("status").equals("error") || jsonObject.getString("status").equals("redirect"))
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

    public static String getSuccessMessage(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getString("status").equals("success"))
                if(jsonObject.has("msg"))
                    return jsonObject.getString("msg");
                else
                    return jsonObject.getJSONArray("message").getJSONArray(0).getString(0);

            Log.d("UNPARSED RESPONSE gSM", s);
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

    public static void clearPrefs(Activity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences("BUYUCOIN_USER_PREFS", activity.MODE_PRIVATE).edit();
        editor.remove("access_token");
        editor.remove("refresh_token");
        editor.apply();
    }

    public static void addMobile(final Activity activity, final String ACCESS_TOKEN, final AlertDialog.Builder builder){

        OkHttpHandler.auth_get("add_mobile", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showToast(activity, "Error retrieving API");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                if(isSuccess(s)){
                    try {
                        getFreshOTP(activity, ACCESS_TOKEN, new JSONObject(s).getJSONObject("data").getString("auth_key"), builder);
                    }catch(Exception e){
                        e.printStackTrace();
                        showToast(activity, "An Error occcured.");
                    }
                }
                else
                    showToast(activity, "An Error occcured.");
            }
        });
    }

    public static void getFreshOTP(final Activity activity, final String ACCESS_TOKEN, final String auth_key, final AlertDialog.Builder builder){

        final EditText mobile = new EditText(activity.getApplicationContext());
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //otp.setLayoutParams(lp);

        builder.setView(mobile);
        builder.setMessage("Add Mobile");
        builder.setCancelable(false);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // showToast(activity, );
                        confirmGetFreshOTP(activity, ACCESS_TOKEN, auth_key, mobile.getText().toString(), builder);
                    }
                });
            }
        });


    }

    public static void confirmGetFreshOTP(final Activity activity, final String ACCESS_TOKEN, final String auth_key, final String mob, final AlertDialog.Builder builder){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("auth_key", auth_key).put("mob", mob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHandler.auth_post("get_otp_fresh", ACCESS_TOKEN, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d("/get_otp_fresh RESPONSE",s);
                if(isSuccess(s)) {
                    showToast(activity, getSuccessMessage(s));
                    getAuthKeyOTP(activity, ACCESS_TOKEN, auth_key, mob, builder);
                }
                else
                    showToast(activity, getErrorMessage(s));
            }
        });
    }

    public static void getAuthKeyOTP(final Activity activity, final String ACCESS_TOKEN, final String auth_key, final String mob, final AlertDialog.Builder builder){

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
                        // showToast(activity, )
                        confirmAddMobile(activity, ACCESS_TOKEN, auth_key, mob, otp.getText().toString(), dialog);
                    }
                });
            }
        });
    }

    public static void confirmAddMobile(final Activity activity, String ACCESS_TOKEN, String auth_key, String mob, String otp, final AlertDialog dialog){

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("auth_key", auth_key).put("mob", mob).put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String js = jsonObject.toString();
        OkHttpHandler.auth_post("add_mobile", ACCESS_TOKEN, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d("PAYLOAD", js);
                Log.d("/add_mobile RESPONSE", s);
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
