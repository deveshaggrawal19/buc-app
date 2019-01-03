package com.buyucoin.buyucoin;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        String s = prefs.getString("access_token", null);
        String r = prefs.getString("refresh_token", null);
        if(s != null){
            Intent i = new Intent(this, PassCodeActivity.class);
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
                    JSONObject jsonObject1 = new JSONObject(s);
                   // Log.d("RESPONSE_______", s);
                    //Log.d("STRING___", jsonObject1.getString("status"));
                    if(jsonObject1.getString("status").equals("success")) {

                        SharedPreferences.Editor editor = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
                        editor.putString("access_token", jsonObject1.getJSONObject("data").getString("access_token"));
                        editor.putString("refresh_token", jsonObject1.getJSONObject("data").getString("refresh_token"));
                        editor.apply();
                        onLoginSuccess(s);
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

    public void onLoginSuccess(final String response) {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                progressDialog.dismiss();
                _loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
            }
        });
        finish();
        Intent intent = new Intent(this, PassCodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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