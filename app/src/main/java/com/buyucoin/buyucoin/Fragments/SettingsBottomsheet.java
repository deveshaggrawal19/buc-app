package com.buyucoin.buyucoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.buyucoin.buyucoin.LoginActivity;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.ChangePasscodeDialog;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsBottomsheet extends BottomSheetDialogFragment {

    private BuyucoinPref buyucoinPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_settings,container,false);

        buyucoinPref = new BuyucoinPref(view.getContext());

        RelativeLayout change_passcode_layout = view.findViewById(R.id.change_passcode_layout);
        RelativeLayout logout_layout = view.findViewById(R.id.logout_layout);

        change_passcode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment changePassocde = new ChangePasscodeDialog();
                changePassocde.setCancelable(true);
                changePassocde.show(getChildFragmentManager(),"CHANGE PASSCODE");
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyucoinPref!=null){
                    buyucoinPref.removeAllPref();
                }
                new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        return view;
    }
}
