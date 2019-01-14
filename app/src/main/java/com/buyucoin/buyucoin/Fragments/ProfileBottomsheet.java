package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.ChangeNameDialog;
import com.buyucoin.buyucoin.customDialogs.ChangePasswordDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProfileBottomsheet extends BottomSheetDialogFragment {

    private Button password_btn,name_btn,mobile_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_profile,container,false);

        password_btn = view.findViewById(R.id.bottomsheet_update_password_btn);
        name_btn = view.findViewById(R.id.bottomsheet_update_name_btn);
        mobile_btn = view.findViewById(R.id.bottomsheet_update_mobile_btn);


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

        mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }
}
