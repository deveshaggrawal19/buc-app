package com.buyucoin.buyucoin.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.CustomDialogs;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WithdrawBottomsheet extends BottomSheetDialogFragment {
    Bundle bundle;
    private Double coin_amount;
    private String coin_tag,coin_address;
    private TextView amount,tag,address;
    private Button proceedToWithdraw;
    private BuyucoinPref buyucoinPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));

        if(bundle!=null){
        coin_amount = bundle.getDouble("coin_amount",0);
        coin_tag = bundle.getString("coin_tag","N/A");
        coin_address = bundle.getString("coin_address","N/A");

            Log.d("===========>",String.valueOf(coin_amount)+"  "+coin_tag);
        }
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottomsheet_withdraw,container,false);
        amount = view.findViewById(R.id.amount);
        tag = view.findViewById(R.id.tag);
        address = view.findViewById(R.id.address);
        proceedToWithdraw = view.findViewById(R.id.proceed_to_withdraw);

        proceedToWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Your Withdraw is Initiated",Toast.LENGTH_SHORT).show();
            }
        });

        if(bundle!=null){
            amount.setText("Amount : "+String.valueOf(coin_amount));
            tag.setText("Destination Tag : "+coin_tag);
            address.setText("Address : "+coin_address);
            if(!coin_tag.equals("")) tag.setVisibility(View.VISIBLE);

        }

        return view;
    }

    private void makeRequest(JSONObject order) {
        OkHttpHandler.auth_post("create_peer", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), order.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ORDER ERROR====>",e.getMessage());
                if(getDialog()!=null){
                    getDialog().dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("ORDER SUCCESS====>",response.toString());
                if(getDialog()!=null){
                    getDialog().dismiss();
                }



            }
        });
    }
}
