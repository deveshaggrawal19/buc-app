package com.buyucoinApp.buyucoin.customDialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.buyucoinApp.buyucoin.LoginActivity;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordDialog extends DialogFragment {

    private EditText old_password_et,new_password_et,confirm_password;
    private Button change_password_btn;
    private ProgressBar progressBar;
    private BuyucoinPref buyucoinPref;
    private String ACCESS_TOKEN;
    private ImageView goback;

    public static ChangePasswordDialog newInstance(){
        return new ChangePasswordDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.change_password_dialog,container,false);

        goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        
        
        
        old_password_et = view.findViewById(R.id.old_password);
        new_password_et = view.findViewById(R.id.new_password);
        confirm_password = view.findViewById(R.id.confirm_password);
        change_password_btn = view.findViewById(R.id.change_password_btn);

        change_password_btn.setEnabled(false);


        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();
                if(val.equals(new_password_et.getText().toString())){
                    change_password_btn.setEnabled(true);
                }else {
                    change_password_btn.setEnabled(false);
                }

            }
        });


        progressBar = view.findViewById(R.id.change_password_pb);

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = old_password_et.getText().toString();
                String new_password = new_password_et.getText().toString();
                String conifrm_password = confirm_password.getText().toString();
                if(conifrm_password.equals(new_password)){
                progressBar.setVisibility(View.VISIBLE);
                makeChangePasswordRequest(view.getContext(),old_password,new_password,conifrm_password);
                }else {
                    confirm_password.setError("Confirm Password not matched");
                }

            }
        });
        return view;
    }

    private void makeChangePasswordRequest(final Context context, String old_password, String new_password, String conifrm_password) {




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject()
                    .put("old_password", old_password)
                    .put("new_password", new_password)
                    .put("re_new_password",conifrm_password);
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
                    String message = "";
                    final String status = jsonObject1.getString("status");
                    message = jsonObject1.getJSONArray("message").getJSONArray(0).getString(0);

                     Log.d("RESPONSE_______", s);
                    //Log.d("STRING___", jsonObject1.getString("status"));
                    if(jsonObject1.getString("status").equals("success")) {

                        Log.d("PASSWORD STATUS","PASSWORD CHANGED");
                        final String finalMessage = message;
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                if(status.equals("success")){
                                    new CoustomToast(getContext(),finalMessage,CoustomToast.TYPE_SUCCESS).showToast();
                                    new BuyucoinPref(context).removePref(BuyucoinPref.ACCESS_TOKEN).remove(BuyucoinPref.REFRESH_TOKEN).apply();
                                    Objects.requireNonNull(getContext()).startActivity(new Intent(getActivity(),LoginActivity.class));
                                }
                                if(status.equals("error")){
                                    new CoustomToast(getContext(),finalMessage,CoustomToast.TYPE_DANGER).showToast();

                                }
//                                dismiss();
                            }
                        });
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
