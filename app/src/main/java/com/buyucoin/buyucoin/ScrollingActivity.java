package com.buyucoin.buyucoin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScrollingActivity extends AppCompatActivity {

    String ACCESS_TOKEN = null;
    Toolbar toolbar;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv = findViewById(R.id.tv);

        SharedPreferences prefs = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, ACCESS_TOKEN, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadProfile();
    }

    public void loadProfile(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("Error retrieving profile.");
                e.printStackTrace();
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d("RESPONSE_____", s);
                try{
                    JSONObject jsonObject1 = new JSONObject(s);
                    tv.setText(jsonObject1.getJSONObject("data").getString("email"));
                    toolbar.setTitle(jsonObject1.getJSONObject("data").getString("name"));
                    toolbar.setSubtitle(jsonObject1.getJSONObject("data").getString("email"));
                }catch(Exception e){
                    e.printStackTrace();
                    showToast("Error retreiving profile");
                    finish();
                }
            }
        });
    }

    public void showToast(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
