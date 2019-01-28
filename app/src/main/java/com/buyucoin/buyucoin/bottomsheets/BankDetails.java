package com.buyucoin.buyucoin.bottomsheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BankDetails extends BottomSheetDialogFragment {

    Bundle bundle;
    String account_no,bank_name,b_name,ifsc_code,mode,note = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            bundle = getArguments();

            account_no = nullSafety(bundle.getString("account_no"));
            bank_name = nullSafety(bundle.getString("bank_name"));
            b_name = nullSafety(bundle.getString("b_name"));
            ifsc_code = nullSafety(bundle.getString("ifsc_code"));
            mode = nullSafety(bundle.getString("mode"));
            note = nullSafety(bundle.getString("note"));
        }
    }


    public String nullSafety(String s){
        if(s!="") return s;
        else return "N/A";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bank_details_bottomsheet,container,false);

        TextView t1,t2,t3,t4,t5,t6;
        t1 = view.findViewById(R.id.textView2);
        t2 = view.findViewById(R.id.textView4);
        t3 = view.findViewById(R.id.textView6);
        t4 = view.findViewById(R.id.textView8);
        t5 = view.findViewById(R.id.textView10);
        t6 = view.findViewById(R.id.textView12);

        t1.setText(account_no);
        t2.setText(bank_name);
        t3.setText(b_name);
        t4.setText(ifsc_code);
        t5.setText(mode);
        t6.setText(note);


        return view;
    }
}
