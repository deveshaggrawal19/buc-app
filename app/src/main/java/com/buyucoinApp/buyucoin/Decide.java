package com.buyucoinApp.buyucoin;

import android.content.Intent;
import android.os.Bundle;

import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import androidx.appcompat.app.AppCompatActivity;

public class Decide extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_decide);

        BuyucoinPref buyucoinPref = new BuyucoinPref(getApplicationContext());

        if(!buyucoinPref.getPrefBoolean("kyc_upload") || !buyucoinPref.getPrefBoolean("bank_upload") ){
            startActivity(new Intent(Decide.this,UploadKyc.class));
        }

        if(!buyucoinPref.getPrefBoolean("mob_verified")){
            startActivity(new Intent(Decide.this,VerifyUser.class));
        }
//        if(buyucoinPref.getPrefBoolean("kyc_status") && buyucoinPref.getPrefBoolean("mob_verified") && buyucoinPref.getPrefBoolean("wallet")){
//            startActivity(new Intent(Decide.this,PassCodeActivity.class));
//        }

        if(buyucoinPref.getPrefBoolean("kyc_upload") && buyucoinPref.getPrefBoolean("bank_upload")){
            if(buyucoinPref.getPrefBoolean("kyc_status") && buyucoinPref.getPrefBoolean("wallet")){
                startActivity(new Intent(Decide.this,PassCodeActivity.class));
            }else {
                startActivity(new Intent(Decide.this,GenerateWallet.class));

            }
        }

        finish();
    }
}
