package com.buyucoinApp.buyucoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText, _passwordText;
    Button _loginButton;
    TextView _signupLink;
    ProgressDialog progressDialog;
    CheckBox show_password;
    BuyucoinPref buyucoinPref;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        buyucoinPref = new BuyucoinPref(getApplicationContext());
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        final int type = _passwordText.getInputType();
        show_password = findViewById(R.id.checkBox_show_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    _passwordText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    _passwordText.setInputType(type);

                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        String r = buyucoinPref.getPrefString(BuyucoinPref.REFRESH_TOKEN);
        if(s != null){
            Intent i = new Intent(this, Decide.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if(!Utilities.isOnline(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validate()) {
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject().put("email", email).put("password", password);
        }catch(Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
            onLoginFailed("Invalid credentials");
        }

        OkHttpHandler.post("login", jsonObject.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onLoginFailed("Error retrieving API" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    String s = response.body().string();
                    Log.d(TAG, "onResponse: loging response "+s);
                    JSONObject jsonObject1 = new JSONObject(s);
                    JSONObject data = (jsonObject1.has("data"))? jsonObject1.getJSONObject("data"):jsonObject1;
                    Log.d("LOGIN RESPONSE", s);
                    Log.d("LOGIN ACTIVITY RESPONSE", jsonObject1.getString("status"));
                    if(jsonObject1.getString("status").equals("success")) {

                        if(data.getBoolean("email_verified")) {
                            buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN, data.getString("access_token"));
                            buyucoinPref.setEditpref(BuyucoinPref.REFRESH_TOKEN, data.getString("refresh_token"));
                            buyucoinPref.setEditpref("bank_upload",data.getBoolean("bank_upload"));
                            buyucoinPref.setEditpref("email_verified", data.getBoolean("email_verified"));
                            buyucoinPref.setEditpref("kyc_upload",data.getBoolean("kyc_upload"));
                            buyucoinPref.setEditpref("kyc_status", data.getBoolean("kyc_verified"));
                            buyucoinPref.setEditpref("mob_verified", data.getBoolean("mob_verified"));
                            buyucoinPref.setEditpref("user_status",data.getString("user_status"));
                            buyucoinPref.setEditpref("wallet",data.getBoolean("wallet"));
                            onLoginSuccess(data.getBoolean("email_verified"));
                        }else {
                            onLoginSuccess(data.getBoolean("email_verified"));
                        }


                    }
                    else
                        onLoginFailed(jsonObject1.getJSONArray("message").getJSONArray(0).getString(0));
                }catch(Exception e) {
                    e.printStackTrace();
                    onLoginFailed("Error retreiving API " + e.getMessage());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(final boolean email) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(email) {
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                    new CoustomToast(getApplicationContext(), "Logged in",CoustomToast.TYPE_SUCCESS).showToast();
                    finish();
                    Intent intent = new Intent(LoginActivity.this, Decide.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    }else{
                        progressDialog.dismiss();
                        _loginButton.setEnabled(true);
                        new CoustomToast(getApplicationContext(), "Your email is not verified, \n verify your email first to login",CoustomToast.TYPE_PENDING).showToast();
                    }
                }
            });


    }

    public void onLoginFailed(final String error) {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                if(progressDialog != null)
                    progressDialog.dismiss();
                _loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Login failed: "+error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Enter your password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


}