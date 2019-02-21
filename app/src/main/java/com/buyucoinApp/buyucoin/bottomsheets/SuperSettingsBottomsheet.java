package com.buyucoinApp.buyucoin.bottomsheets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.buyucoinApp.buyucoin.Fragments.HistoryFragment;
import com.buyucoinApp.buyucoin.LoginActivity;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SuperSettingsBottomsheet extends BottomSheetDialogFragment {
    RelativeLayout history,about,policy,logout;
    private BuyucoinPref buyucoinPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_supersettings,container,false);
        history = view.findViewById(R.id.supter_setting_history);
        about = view.findViewById(R.id.supter_setting_about);
        policy = view.findViewById(R.id.supter_setting_policy);
        logout = view.findViewById(R.id.super_setting_logout);
        buyucoinPref = new BuyucoinPref(view.getContext());

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment d = new HistoryFragment();
                d.show(getChildFragmentManager(),"");


            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buyucoin.com/about"));
                startActivity(intent);
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buyucoin.com/privacy-policy"));
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyucoinPref!=null){
                    buyucoinPref.removeAllPref();
                }
                new CoustomToast(getContext(),"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }
}
