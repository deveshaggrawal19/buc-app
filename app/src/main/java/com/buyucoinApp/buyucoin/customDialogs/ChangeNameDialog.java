package com.buyucoinApp.buyucoin.customDialogs;

import android.content.Context;
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

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
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
    private ImageView goback;

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

        goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        
        
        new_name_et = view.findViewById(R.id.new_name);
        change_name_btn = view.findViewById(R.id.change_name_btn);
        change_name_btn.setEnabled(false);

        new_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();
                if(val.length()>3){
                    change_name_btn.setEnabled(true);
                }else{
                    change_name_btn.setEnabled(false);
                }
            }
        });

        progressBar = view.findViewById(R.id.change_password_pb);

       change_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = new_name_et.getText().toString();
                if(!new_name.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                change_name_btn.setVisibility(View.GONE);
                makeChangeNameRequest(view.getContext(),new_name);

                }else {
                    new_name_et.setError("Please Enter New Name");
                }

            }
        });
        return view;
    }

    private void makeChangeNameRequest(final Context context, String new_name) {




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
                    assert response.body() != null;
                    String s = response.body().string();
                    final JSONObject jsonObject1 = new JSONObject(s);
                     Log.d("RESPONSE_______", s);
                    //Log.d("STRING___", jsonObject1.getString("status"));
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(jsonObject1.getString("status").equals("success")) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    new CoustomToast(context,"Name Changed Successfully",CoustomToast.TYPE_SUCCESS).showToast();
                                    dismiss();
                                    Objects.requireNonNull(getActivity()).recreate();
                                }
                                else{
    //                        progressBar.setVisibility(View.INVISIBLE);
                                    Log.d("NAME STATUS","ERROR NAME NOT CHANGED");
                                    new CoustomToast(context,"Name Not Changed",CoustomToast.TYPE_DANGER).showToast();
                                    change_name_btn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
