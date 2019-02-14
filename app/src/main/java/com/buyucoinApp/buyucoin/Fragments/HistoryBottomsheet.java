package com.buyucoinApp.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryBottomsheet extends BottomSheetDialogFragment {
    private TextView  amount,curr,time,status,tx_hash,address,fee,filled,price,type,value;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_history,container,false);
        intializeViews(view);

        Bundle b = getArguments();
        setTextToViews(b);
        return view;
    }

    private void setTextToViews(Bundle b) {
        nullSafeView(amount,b.getString("history_amount"));
        nullSafeView(curr,b.getString("history_currency"));
        nullSafeView(time,b.getString("history_time"));
        nullSafeView(status,b.getString("history_status"));
        nullSafeView(tx_hash,b.getString("history_tx_hash"));
        nullSafeView(address,b.getString("history_address"));
        nullSafeView(fee,b.getString("history_fees"));
        nullSafeView(filled,b.getString("history_filled"));
        nullSafeView(price,b.getString("history_price"));
        nullSafeView(type,b.getString("history_type"));
        nullSafeView(value,b.getString("history_value"));
    }

    public void nullSafeView(TextView view,String s){
        if(!s.equals(""))view.setText(s);
        else{
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void intializeViews(View view) {
        amount = view.findViewById(R.id.history_amount);
        curr= view.findViewById(R.id.history_currency);
        time= view.findViewById(R.id.history_time);
        status= view.findViewById(R.id.history_status);
        tx_hash= view.findViewById(R.id.history_tx_hash);
        address= view.findViewById(R.id.history_address);
        fee= view.findViewById(R.id.history_fees);
        filled= view.findViewById(R.id.history_filled);
        price= view.findViewById(R.id.history_price);
        type= view.findViewById(R.id.history_type);
        value= view.findViewById(R.id.history_value);

    }
}
