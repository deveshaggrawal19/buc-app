package com.buyucoinApp.buyucoin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VerifyUser extends AppCompatActivity {

    LinearLayout mobile_layout,otp_layout;
    Button send_otp,submit_otp,logout;
    EditText mobile,otp;
    BuyucoinPref buyucoinPref;
    static JSONObject finalObject;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
        progressDialog = new ProgressDialog(VerifyUser.this);
        progressDialog.setTitle("OTP sending..");
        progressDialog.setMessage("we are sending OTP , please wait for couple of minutes");
        buyucoinPref = new BuyucoinPref(this);
        initView();
        getNonFreshToken(buyucoinPref.getPrefString(BuyucoinPref.REFRESH_TOKEN));
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.addMobile(VerifyUser.this,buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN),new AlertDialog.Builder(VerifyUser.this));

//                String mobile_num = mobile.getText().toString();
//                if(mobile_num.length()>=10){
//
////                    getAuthKey(buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN),mobile_num);
//                }
//                else{
//                    mobile.setError("Mobile number required");
//                }
            }
        });

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp_ = otp.getText().toString();
                if(otp!=null && otp_.length()>=6){
                    try {
                        submitOtp(finalObject.put("otp",otp_));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    otp.setError("enter otp");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyucoinPref!=null){
                    buyucoinPref.removeAllPref();
                }
                new CoustomToast(getApplicationContext(),"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
                Intent i = new Intent(VerifyUser.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });



    }

    public void initView(){
        mobile_layout = findViewById(R.id.enter_number_layout);
        submit_otp = findViewById(R.id.submit_otp_btn);
        otp_layout = findViewById(R.id.enter_otp_layout);
        send_otp = findViewById(R.id.send_otp_btn);
        mobile = findViewById(R.id.enter_number_etv);
        logout = findViewById(R.id.logout);
        otp = findViewById(R.id.enter_otp_etv);
    }

    public void getAuthKey(final String token,final String mobile){
        Log.d("avi","token:"+token);
        progressDialog.show();
        OkHttpHandler.auth_get("add_mobile",token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                if(s!=null){
                    try {
                        Log.d("ON AUTH KEY RES  TOKEN", "onResponse: "+buyucoinPref.getPrefString(
                                BuyucoinPref.ACCESS_TOKEN
                        ));
                        JSONObject res = new JSONObject(s);

                        Log.d("GET AUTH KEY", res.toString());
                        String message = "";
                        if(res.has("data") && res.getJSONObject("data").has("auth_key")){
                            if(res.has("message")){
                                res.getJSONArray("message").getJSONArray(0).getString(0);
                            }
                            String auth_key = res.getJSONObject("data").getString("auth_key");
                          JSONObject object = new JSONObject().put("auth_key",auth_key).put("mob",mobile);
                          getFreshOtp(object);

                        }
                        else{
                            message = res.getString("msg");
                          if(message.equals("Fresh token required")){
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      progressDialog.dismiss();
                                      new CoustomToast(getApplicationContext(),"Session Expired, Login again.",CoustomToast.TYPE_NORMAL).showToast();
                                  }
                              });
                          }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    public void getFreshOtp(final JSONObject object){
        OkHttpHandler.auth_post("get_otp_fresh", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), object.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                if(s!=null){
                    try {
                        JSONObject res = new JSONObject(s);
                        Log.d("GET FRESH OTP", res.toString());

//                      String data = res.getJSONObject("data");
                        String message = res.getJSONArray("message").getJSONArray(0).getString(0);
                        String status = res.getString("status");

                        finalObject = object;
                        mobile_layout.setVisibility(View.GONE);
                        otp_layout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void submitOtp(JSONObject object){
        OkHttpHandler.auth_post("confirm_mobile", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), object.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                if(s!=null){
                    try {
                        JSONObject res = new JSONObject(s);
                        Log.d("GET SUBMIT OTP", res.toString());

//                      String data = res.getJSONObject("data");
                        String message = res.getJSONArray("message").getJSONArray(0).getString(0);
                        String status = res.getString("status");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    public void getNonFreshToken(String refresh_token) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("refresh_token", refresh_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpHandler.refresh_post("refresh", refresh_token, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new CoustomToast(VerifyUser.this,"Error retreiving API",CoustomToast.TYPE_DANGER).showToast();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                try {
                    final JSONObject jsonObject1 = new JSONObject(s);
                    buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN,jsonObject1.getString("access_token"));
                    Log.d("ON REFRESH TOKEN", "onResponse: "+buyucoinPref.getPrefString(
                            BuyucoinPref.ACCESS_TOKEN
                    ));
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        new CoustomToast(VerifyUser.this,e.getMessage(),CoustomToast.TYPE_NORMAL).showToast();
                        }
                    });


                }
            }
        });
    }
}
