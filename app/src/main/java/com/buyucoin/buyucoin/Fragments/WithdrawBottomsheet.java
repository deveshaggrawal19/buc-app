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

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.CustomDialogs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WithdrawBottomsheet extends BottomSheetDialogFragment {
    Bundle bundle;
    private Double coin_amount;
    private String coin_tag,coin_address;
    private TextView amount,tag,address;
    private Button proceedToWithdraw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
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
}
