package com.buyucoinApp.buyucoin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UploadKyc extends AppCompatActivity {

    Button kyc_upload_button,kyc_logout_button,kyc_pending_logout_button;
    BuyucoinPref buyucoinPref;
    LinearLayout kyc_layout,kyc_pending_layout;

    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_kyc);

        buyucoinPref = new BuyucoinPref(getApplicationContext());
        kyc_upload_button = findViewById(R.id.kyc_button);
        kyc_logout_button = findViewById(R.id.kyc_logout_button);
        kyc_layout = findViewById(R.id.kyc_layout);
        kyc_pending_layout = findViewById(R.id.kyc_pending_layout);
        kyc_pending_logout_button = findViewById(R.id.kyc_pending_logout_button);


        if(buyucoinPref.getPrefBoolean("kyc_upload") || buyucoinPref.getPrefBoolean("bank_upload")){
            kyc_pending_layout.setVisibility(View.VISIBLE);
        }else{
            kyc_layout.setVisibility(View.VISIBLE);
        }



        kyc_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isPermissionGranted()){
//
//                }else{
//                    requestStoragePermission();
//                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buyucoin.com/"));
                startActivity(intent);
            }
        });



        kyc_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        kyc_pending_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNonFreshToken(buyucoinPref.getPrefString(BuyucoinPref.REFRESH_TOKEN));
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("allow app read your external storage to upload KYC documents")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadKyc.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    public boolean isPermissionGranted(){
        return ContextCompat.checkSelfPermission(UploadKyc.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }

    public void logout(){
        buyucoinPref.removeAllPref();
        new CoustomToast(getApplicationContext(),"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
        Intent i = new Intent(UploadKyc.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{

            }
        }
    }

    public void showImageDialog(){

    }

    public void getNonFreshToken(String refresh_token) {

        final ProgressDialog dialog = new ProgressDialog(UploadKyc.this);
        dialog.setMessage("checking KYC status");
        dialog.setCancelable(false);
        dialog.show();

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
                        new CoustomToast(UploadKyc.this,"Error retrieving KYC status",CoustomToast.TYPE_DANGER).showToast();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {

                    final JSONObject data = new JSONObject(s);

                    Log.d("get non fresh token", "onResponse: "+data.toString());
                    buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN, data.getString("access_token"));
                    buyucoinPref.setEditpref("bank_upload",data.getBoolean("bank_upload"));
                    buyucoinPref.setEditpref("email_verified", data.getBoolean("email_verified"));
                    buyucoinPref.setEditpref("kyc_upload",data.getBoolean("kyc_upload"));
                    buyucoinPref.setEditpref("kyc_status", data.getBoolean("kyc_verified"));
                    buyucoinPref.setEditpref("mob_verified", data.getBoolean("mob_verified"));
                    buyucoinPref.setEditpref("user_status",data.getString("user_status"));
                    buyucoinPref.setEditpref("wallet",data.getBoolean("wallet"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if(buyucoinPref.getPrefBoolean("kyc_upload") && buyucoinPref.getPrefBoolean("bank_upload")){
                                    if(buyucoinPref.getPrefBoolean("kyc_status") && buyucoinPref.getPrefBoolean("wallet")){
                                        startActivity(new Intent(UploadKyc.this,PassCodeActivity.class));
                                    }else {
                                        startActivity(new Intent(UploadKyc.this,GenerateWallet.class));

                                    }
                                }

                                if(!buyucoinPref.getPrefBoolean("kyc_upload") || !buyucoinPref.getPrefBoolean("bank_upload") ){
                                   kyc_pending_layout.setVisibility(View.VISIBLE);
                                   dialog.dismiss();
                                }

                                if(!buyucoinPref.getPrefBoolean("kyc_upload") && !buyucoinPref.getPrefBoolean("bank_upload") ){
                                    kyc_layout.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }
                    });


                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CoustomToast(UploadKyc.this,e.getMessage(),CoustomToast.TYPE_NORMAL).showToast();
                            dialog.dismiss();
                        }
                    });


                }
            }
        });
    }
}
