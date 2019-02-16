package com.buyucoinApp.buyucoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
    Button send_otp,submit_otp;
    EditText mobile,otp;
    BuyucoinPref buyucoinPref;
    String ACCESS_TOKEN;
    static JSONObject finalObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);



        initView();

        buyucoinPref = new BuyucoinPref(getApplicationContext());
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);



        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_num = mobile.getText().toString();
                if(mobile_num.length()>=10){
                    getAuthKey(mobile_num);
                }
                else{
                    mobile.setError("Mobile number required");
                }
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

    }

    public void initView(){
        mobile_layout = findViewById(R.id.enter_number_layout);
        submit_otp = findViewById(R.id.submit_otp_btn);
        otp_layout = findViewById(R.id.enter_otp_layout);
        send_otp = findViewById(R.id.send_otp_btn);
        mobile = findViewById(R.id.enter_number_etv);
        otp = findViewById(R.id.enter_otp_etv);
    }

    public void getAuthKey(final String mobile){
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("we are securing your session..");
        progressDialog.show();
        OkHttpHandler.auth_get("add_mobile", ACCESS_TOKEN, new Callback() {
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
                        String message = res.getJSONArray("message").getJSONArray(0).getString(0);
                        if(res.getJSONObject("data").has("auth_key")){
                          String auth_key = res.getJSONObject("data").getString("auth_key");
                          JSONObject object = new JSONObject().put("auth_key",auth_key).put("mob",mobile);
                          getFreshOtp(object);

                        }else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    public void getFreshOtp(final JSONObject object){
        OkHttpHandler.auth_post("get_otp_fresh", ACCESS_TOKEN, object.toString(), new Callback() {
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
//                      String data = res.getJSONObject("data");
                        String message = res.getJSONArray("message").getJSONArray(0).getString(0);
                        String status = res.getString("status");

                        finalObject = object;
                        mobile_layout.setVisibility(View.GONE);
                        otp_layout.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void submitOtp(JSONObject object){
        OkHttpHandler.auth_post("confirm_mobile", ACCESS_TOKEN, object.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    public void verifyUser(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
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
                        boolean isVerified = res.getJSONObject("data").getBoolean("kyc_status");
                        if(isVerified){
                            Intent i = new Intent(VerifyUser.this,PassCodeActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
