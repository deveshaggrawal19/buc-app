package com.buyucoinApp.buyucoin.bottomsheets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OtcOrderConfirm extends BottomSheetDialogFragment {

    private JSONObject jsonObject;
    private TextView type,coin,amount,price,total;
    private LinearLayout cancel_order,confirm_order;
    private ProgressDialog p;
    private String ACCESS_TOKEN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            try {
                jsonObject = new JSONObject(getArguments().getString("object"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        BuyucoinPref prefs = new BuyucoinPref(Objects.requireNonNull(getContext()));

        ACCESS_TOKEN = prefs.getPrefString(BuyucoinPref.ACCESS_TOKEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final  View view = inflater.inflate(R.layout.otc_order_confirm,null,false);

        type = view.findViewById(R.id.order_type);
        coin = view.findViewById(R.id.order_coin);
        amount = view.findViewById(R.id.order_amount);
        price = view.findViewById(R.id.order_price);
        total = view.findViewById(R.id.order_total);
        cancel_order = view.findViewById(R.id.otc_order_cancel);
        confirm_order = view.findViewById(R.id.otc_order_confirm);

        p = new ProgressDialog(getActivity());
        p.setTitle("Placing Order");
        p.setMessage("please wait while placing your OTC order");
        p.create();

        try {

//            Toast.makeText(getContext(), ""+jsonObject.toString(), Toast.LENGTH_LONG).show();
            type.setText(jsonObject.get("type").toString());
            coin.setText(jsonObject.get("coin").toString());
            amount.setText(jsonObject.get("amount").toString());
            price.setText(jsonObject.get("rate").toString());
            total.setText(String.valueOf(jsonObject.getDouble("total")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOTCOrder(jsonObject.toString());

            }
        });

        return view;
    }

    private void placeOTCOrder(String s) {

        p.show();
        OkHttpHandler.auth_post("get_notifications", ACCESS_TOKEN, s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    p.dismiss();
                                    Objects.requireNonNull(getDialog()).dismiss();
                                    new CoustomToast(getContext(),"Order Placed Sucessfully",CoustomToast.TYPE_SUCCESS).showToast();
                                }
                            },2000);

                        }
                    });

                }
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()),R.style.CoustomBottomSheet);
    }
}
