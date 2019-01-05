package com.buyucoin.buyucoin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PassCodeActivity extends AppCompatActivity {
    static String pin ;
    static  String confirm_pin;
    View view;
    Intent ci;
    final int CONFIRM_INTENT_CODE = 1;
    LinearLayout enter_password,confirm_password;
    boolean isConfirmIntent;
    SharedPreferences.Editor pinpref;
    SharedPreferences viewPref;
    String PASSWORD;
    boolean DISABLE_PIN  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        viewPref = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE);
        DISABLE_PIN = viewPref.getBoolean("DISABLE_PASS_CODE",false);
        if(DISABLE_PIN){
            startActivity(new Intent(PassCodeActivity.this,Dashboard.class));
            finish();
        }
        pin = "";
        ci = getIntent();
        enter_password = findViewById(R.id.enter_passcode_heading);
        confirm_password = findViewById(R.id.confirm_passcode_heading);
        isConfirmIntent = ci.getBooleanExtra("isConfirm",false);


        PASSWORD = viewPref.getString("passcode","no");

        Log.d("PASSCODE ITEMS",isConfirmIntent+"  "+PASSWORD);

        if(isConfirmIntent){
            enter_password.setVisibility(View.GONE);
            confirm_password.setVisibility(View.VISIBLE);
        }else{
            confirm_password.setVisibility(View.GONE);
            enter_password.setVisibility(View.VISIBLE);
        }
    }

    public void updatepin(View view) {
        String s = ((TextView) view).getText().toString();
        pin = pin+s;
        if(isConfirmIntent){
            confirm_pin = confirm_pin+s;
        }
        if(pin.length()<=4){
            updatePinDote(pin.length(),1);
//            Toast.makeText(getApplicationContext(),pin,Toast.LENGTH_SHORT).show();
            if(pin.length()==4){
                if(pin.equals(PASSWORD)){
                    startActivity(new Intent(PassCodeActivity.this,Dashboard.class));
                    finish();
                }else{
                    if(PASSWORD.equals("no")){
                        if(isConfirmIntent){
                            Intent resultintent = getIntent();
                            resultintent.putExtra("confirmpin",confirm_pin);
                            setResult(CONFIRM_INTENT_CODE,resultintent);
                            finish();
                            confirm_pin ="";

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            Intent confirmIntent = new Intent(PassCodeActivity.this, PassCodeActivity.class);
                            confirmIntent.putExtra("isConfirm", true);
                            startActivityForResult(confirmIntent, CONFIRM_INTENT_CODE);
                        }
                    }
                    else{
                        clearPinsData();

                    }


                }


            }
        }else{
            Toast.makeText(getApplicationContext(),"pin exceed",Toast.LENGTH_LONG).show();

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
        Toast.makeText(getApplicationContext(), "Wrong pin", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PASSCODE ITEMS",isConfirmIntent+"  "+PASSWORD+" "+pin+" "+confirm_pin);

        if(requestCode==CONFIRM_INTENT_CODE){
            String cpin = null;
            if (data != null) {
                cpin = data.getStringExtra("confirmpin");
            }
            if(pin.equals(cpin)){
                pinpref = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE).edit();
                pinpref.putString("passcode",pin).apply();
                startActivity(new Intent(PassCodeActivity.this,Dashboard.class));
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"PASSCODE NOT MATCH",Toast.LENGTH_SHORT).show();
                clearPinsData();
            }
        }
    }
}
