package com.buyucoin.buyucoin.customDialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buyucoin.buyucoin.LoginActivity;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordDialog extends DialogFragment {

    private EditText old_password_et,new_password_et;
    private Button change_password_btn;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private String ACCESS_TOKEN;

    public static ChangePasswordDialog newInstance(){
        return new ChangePasswordDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        preferences = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS",Context.MODE_PRIVATE);
        ACCESS_TOKEN = preferences.getString("access_token", null);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.change_password_dialog,container,false);

        old_password_et = view.findViewById(R.id.old_password);
        new_password_et = view.findViewById(R.id.new_password);
        change_password_btn = view.findViewById(R.id.change_password_btn);

        progressBar = view.findViewById(R.id.change_password_pb);

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = old_password_et.getText().toString();
                String new_password = new_password_et.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                makeChangePasswordRequest(view.getContext(),old_password,new_password);

            }
        });
        return view;
    }

    private void makeChangePasswordRequest(final Context context, String old_password, String new_password) {




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject().put("old_password", old_password).put("new_password", new_password);
        }catch(Exception e){
            e.printStackTrace();
        }

        OkHttpHandler.auth_post("change_password",ACCESS_TOKEN, jsonObject.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    String s = response.body().string();
                    JSONObject jsonObject1 = new JSONObject(s);
                     Log.d("RESPONSE_______", s);
                    //Log.d("STRING___", jsonObject1.getString("status"));
                    if(jsonObject1.getString("status").equals("success")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("PASSWORD STATUS","PASSWORD CHANGED");
                    }
                    else{
//                        progressBar.setVisibility(View.INVISIBLE);
                       Log.d("PASSWORD STATUS","ERROR PASSWORD NOT MATCh");
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
