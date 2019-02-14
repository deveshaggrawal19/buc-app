package com.buyucoinApp.buyucoin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.crashlytics.android.Crashlytics;

import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;

public class ConfirmCodeActivity extends AppCompatActivity {
    static String pin = "";
    String confirm_pin;
    View view;
    BuyucoinPref buyucoinPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        buyucoinPref = new BuyucoinPref(this);
        Intent ci = getIntent();
        confirm_pin = ci.getStringExtra("check_pin");

    }

    public void updatepin(View view) {
        String s = ((TextView) view).getText().toString();
        pin = pin+s;
        if(pin.length()<=4){
            updatePinDote(pin.length(),1);
            if(pin.length()==4){
                if(pin.equals(confirm_pin)){
                    buyucoinPref.setEditpref("passcode",pin);
                    startActivity(new Intent(ConfirmCodeActivity.this,Dashboard.class));
                    finish();
                }else{
                    new CoustomToast(getApplicationContext(), ConfirmCodeActivity.this,"Wrong pin",CoustomToast.TYPE_NORMAL).showToast();
                    clearPinsData();
                }
            }
        }else{
            new CoustomToast(getApplicationContext(), ConfirmCodeActivity.this,"pin exceed",CoustomToast.TYPE_NORMAL).showToast();
        }
    }

    public void clearPinsData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=1;i<=4;i++){
                    updatePinDote(i,0);
                }
            }
        },100);
        pin = "";
        new CoustomToast(getApplicationContext(), ConfirmCodeActivity.this, "Wrong pin", CoustomToast.TYPE_DANGER).showToast();
    }

    public void updatePinDote(int i,int level){
        switch (i){
            case 1:
                view = findViewById(R.id.pinview1);
                break;
            case 2:
                view = findViewById(R.id.pinview2);
                break;
            case 3:
                view = findViewById(R.id.pinview3);
                break;
            case 4:
                view = findViewById(R.id.pinview4);
                break;
                default:
                    view = findViewById(R.id.pinview1);


        }
        view.getBackground().setLevel(level);
    }


    public void clearPinByOne(View view) {
        if(pin.length()>0){
            updatePinDote(pin.length(),0);
            pin = pin.substring(0,pin.length()-1);
        }

    }
}
