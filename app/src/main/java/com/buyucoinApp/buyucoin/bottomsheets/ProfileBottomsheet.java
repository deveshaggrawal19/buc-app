package com.buyucoinApp.buyucoin.bottomsheets;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.ChangeNameDialog;
import com.buyucoinApp.buyucoin.customDialogs.ChangePasswordDialog;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class ProfileBottomsheet extends BottomSheetDialogFragment {

    BuyucoinPref buyucoinPref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_profile,container,false);

        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));


        Button password_btn = view.findViewById(R.id.bottomsheet_update_password_btn);
        Button name_btn = view.findViewById(R.id.bottomsheet_update_name_btn);


        TextView profile_name = view.findViewById(R.id.profile_name);
        TextView profile_phone = view.findViewById(R.id.profile_phone);
        TextView profile_kyc = view.findViewById(R.id.profile_kyc);
        TextView profile_email = view.findViewById(R.id.profile_email);

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()), R.style.CoustomBottomSheet);
    }
}
