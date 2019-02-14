package com.buyucoinApp.buyucoin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    EditText _nameText, _emailText, _passwordText;
    Button _signupButton;
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = (EditText)findViewById(R.id.input_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _signupButton = (Button)findViewById(R.id.btn_signup);
        _loginLink = (TextView)findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject().put("email", email).put("password", password).put("name", name);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            onSignupFailed("Invalid credentials");
        }
        OkHttpHandler.post("signup", jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onSignupFailed("Error retrieving API" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                progressDialog.dismiss();
                try {
                    String s = response.body().string();
                    JSONObject jsonObject1 = new JSONObject(s);
                    Log.d("RESPONSE_______", s);
                    if (jsonObject1.getString("status").equals("success")) {
//                        SharedPreferences.Editor editor = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
//                        editor.putString("access_token", jsonObject1.getJSONObject("data").getString("access_token"));
//                        editor.putString("refresh_token", jsonObject1.getJSONObject("data").getString("refresh_token"));
//                        editor.apply();

                        onSignupSuccess(jsonObject1.getJSONArray("message").getJSONArray(0).getString(0));
                    }
                    else if(jsonObject1.getString("status").equals("error"))
                        onSignupFailed(jsonObject1.getJSONArray("message").getJSONArray(0).getString(0));
                    else
                        onSignupSuccess(jsonObject1.getJSONArray("message").getJSONArray(0).getString(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    onSignupFailed("Error retreiving API: " + e.getMessage());
                }
            }
        });                        // onSignupFailed()
    }


    public void onSignupSuccess(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Account created. Verify your E-mail to login", Toast.LENGTH_LONG).show();
                _signupButton.setEnabled(true);

            }
        });
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Signup error: " + msg, Toast.LENGTH_LONG).show();
                _signupButton.setEnabled(true);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Password should be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
