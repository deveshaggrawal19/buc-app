package com.buyucoinApp.buyucoin.customDialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class ChangeNameDialog extends DialogFragment {

    private EditText new_name_et;
    private Button change_name_btn;
    private ProgressBar progressBar;
    private BuyucoinPref buyucoinPref;
    private String ACCESS_TOKEN;

    public static ChangeNameDialog newInstance(){
        return new ChangeNameDialog();
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
        final View view = inflater.inflate(R.layout.change_name_dialog,container,false);

        new_name_et = view.findViewById(R.id.new_name);
        change_name_btn = view.findViewById(R.id.change_name_btn);

        progressBar = view.findViewById(R.id.change_password_pb);

       change_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = new_name_et.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                makeChangePasswordRequest(view.getContext(),new_name);

            }
        });
        return view;
    }

    private void makeChangePasswordRequest(final Context context, String new_name) {




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject().put("name",new_name);
        }catch(Exception e){
            e.printStackTrace();
        }

        OkHttpHandler.auth_post("change_name",ACCESS_TOKEN, jsonObject.toString(), new Callback(){
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
                        Log.d("PASSWORD STATUS","NAME CHANGED");
                    }
                    else{
//                        progressBar.setVisibility(View.INVISIBLE);
                       Log.d("PASSWORD STATUS","ERROR NAME NOT CHANGED");
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
