package com.buyucoinApp.buyucoin.customDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.buyucoinApp.buyucoin.LoginActivity;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.buyucoinApp.buyucoin.Utilities.getErrorMessage;
import static com.buyucoinApp.buyucoin.Utilities.isSuccess;
import static com.buyucoinApp.buyucoin.Utilities.isSuccessRedirect;
import static com.buyucoinApp.buyucoin.Utilities.showToast;

public class OtpDialog extends DialogFragment {

    private String ACCESS_TOKEN;

    public static ChangeNameDialog newInstance(){
        return new ChangeNameDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyFullScreenDialog);
        BuyucoinPref buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.otp_layout,container,false);
        final EditText otp = view.findViewById(R.id.otp_edittext);
        final Button submit_otp = view.findViewById(R.id.submit_otp_btn);
        final Button logout_otp_btn = view.findViewById(R.id.logout_otp_btn);

        submit_otp.setOnClickListener(v -> submitNewOTP(otp.getText().toString(), ACCESS_TOKEN,getActivity(),getDialog()));
        logout_otp_btn.setOnClickListener(v -> {
            new CoustomToast(Objects.requireNonNull(getActivity()).getApplicationContext(),"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
            new BuyucoinPref(Objects.requireNonNull(getContext())).removeAllPref();
            Intent i = new Intent(getContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(i);
            getActivity().finish();
        });





        return view;
    }



    private static void submitNewOTP(String s, String ACCESS_TOKEN, final Activity activity, final Dialog dialog){
        JSONObject jo = new JSONObject();
        try {
            jo.put("otp", s);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //submitOTP
        OkHttpHandler.auth_post("otp_needed", ACCESS_TOKEN, jo.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> new CoustomToast(activity.getApplicationContext(), "Error retreiving API",CoustomToast.TYPE_DANGER).showToast());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                try {
                    JSONObject j = new JSONObject(s);
                    Log.d("OTP DIALOG", "onResponse: "+j.toString());
                    if(j.has("status")){
                        if(isSuccess(s) || isSuccessRedirect(s)){
                            showToast(activity, "OTP registered");
                            dialog.dismiss();
                            activity.runOnUiThread(activity::recreate);
                        }else {
                            showToast(activity, getErrorMessage(s));
                        }
                    }else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CoustomToast(activity.getApplicationContext(),"Session Expired log in Again",CoustomToast.TYPE_NORMAL).showToast();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }



}
