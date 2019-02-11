package com.buyucoin.buyucoin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.crashlytics.android.Crashlytics;

import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;

public class PassCodeActivity extends AppCompatActivity {
    static String pin;
    View view;
    SharedPreferences viewPref;
    String PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        viewPref = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE);
        PASSWORD = viewPref.getString("passcode","no");
        pin = "";

    }

    public void updatepin(View view) {
        String s = ((TextView) view).getText().toString();
        pin = pin+s;
        if(pin.length()<=4){
            updatePinDote(pin.length(),1);
            if(pin.length()==4){
                if(pin.equals(PASSWORD)){
                    startActivity(new Intent(PassCodeActivity.this,Dashboard.class));
                    finish();
                }else{
                    if(PASSWORD.equals("no")){
                            Intent resultintent = new Intent(PassCodeActivity.this,ConfirmCodeActivity.class);
                            resultintent.putExtra("check_pin",pin);
                            startActivity(resultintent);
                            finish();
                    }
                    else{
                        clearPinsData();

                    }


                }


            }
        }else{
            new CoustomToast(getApplicationContext(),PassCodeActivity.this,"pin exceed",CoustomToast.TYPE_NORMAL).showToast();
        }
        Log.d("PIN=======>","pin = "+pin+" pin length="+String.valueOf(pin.length()));




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
        new CoustomToast(getApplicationContext(),PassCodeActivity.this, "Wrong pin", CoustomToast.TYPE_DANGER).showToast();
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
