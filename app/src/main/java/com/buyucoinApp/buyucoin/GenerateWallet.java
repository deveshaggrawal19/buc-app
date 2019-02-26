package com.buyucoinApp.buyucoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GenerateWallet extends AppCompatActivity {

    BuyucoinPref buyucoinPref;
    Button genrate_wallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_wallet);

        buyucoinPref = new BuyucoinPref(getApplicationContext());

        genrate_wallet = findViewById(R.id.generate_wallet);

        genrate_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateWallet();
            }
        });


    }

    private void generateWallet() {
        final ProgressDialog progressDialog = new ProgressDialog(GenerateWallet.this);
        progressDialog.setMessage("Creating wallet");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpHandler.auth_post("intialize_wallet", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN),"", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CoustomToast(GenerateWallet.this,"Problem in creating wallet",CoustomToast.TYPE_DANGER).showToast();
                            progressDialog.dismiss();
                        }
                    });
                }catch (Exception err){
                    err.printStackTrace();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    assert response.body() != null;
                    String res = response.body().string();
                    JSONObject object = new JSONObject(res);
                    Log.d("generate wallet", "onResponse: "+object.toString());
                    if(object.getString("status").equals("redirect")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CoustomToast(GenerateWallet.this,"Wallet Created Successfully",CoustomToast.TYPE_SUCCESS).showToast();
                                buyucoinPref.setEditpref("wallet",true);
                                progressDialog.dismiss();
                                startActivity(new Intent(GenerateWallet.this,Dashboard.class));
                                finish();
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CoustomToast(GenerateWallet.this,"Problem in creating wallet",CoustomToast.TYPE_DANGER).showToast();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
