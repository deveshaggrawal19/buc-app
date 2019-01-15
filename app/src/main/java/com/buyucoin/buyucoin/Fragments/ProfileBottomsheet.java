package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.ChangeNameDialog;
import com.buyucoin.buyucoin.customDialogs.ChangePasswordDialog;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProfileBottomsheet extends BottomSheetDialogFragment {

    private Button password_btn,name_btn;
    private TextView profile_name,profile_phone,profile_email,profile_kyc;
    BuyucoinPref buyucoinPref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_profile,container,false);

        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));



        password_btn = view.findViewById(R.id.bottomsheet_update_password_btn);
        name_btn = view.findViewById(R.id.bottomsheet_update_name_btn);


        profile_name = view.findViewById(R.id.profile_name);
        profile_phone = view.findViewById(R.id.profile_phone);
        profile_kyc = view.findViewById(R.id.profile_kyc);
        profile_email = view.findViewById(R.id.profile_email);

        profile_name.setText(String.valueOf(buyucoinPref.getPrefString("name")));
        profile_phone.setText(String.valueOf(buyucoinPref.getPrefString("mob")));
        profile_email.setText(String.valueOf(buyucoinPref.getPrefString("email")));
        String s = "KYC Verified : "+buyucoinPref.getPrefBoolean("kyc_status");
        profile_kyc.setText(s);




        password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = ChangePasswordDialog.newInstance();
                dialogFragment.show(getChildFragmentManager(),"UPDATE PASSWORD");

            }
        });

        name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = ChangeNameDialog.newInstance();
                dialogFragment.show(getChildFragmentManager(),"UPDATE NAME");
            }
        });




        return view;
    }
}
