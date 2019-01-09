package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
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
        amount.setText(b.getString("history_amount"));
        curr.setText(b.getString("history_currency"));
        time.setText(b.getString("history_time"));
        status.setText(b.getString("history_status"));
        tx_hash.setText(b.getString("history_tx_hash"));
        address.setText(b.getString("history_address"));
        fee.setText(b.getString("history_fees"));
        filled.setText(b.getString("history_filled"));
        price.setText(b.getString("history_price"));
        type.setText(b.getString("history_type"));
        value.setText(b.getString("history_value"));
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
