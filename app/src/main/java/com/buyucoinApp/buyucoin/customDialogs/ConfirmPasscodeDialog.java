package com.buyucoinApp.buyucoin.customDialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmPasscodeDialog extends DialogFragment {

    EditText pin_password;
    Button pin_confirm_btn;
    BuyucoinPref pref;

    private String password = "";





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        pref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        password = pref.getPrefString("passcode");
        boolean isDisabled = pref.getPrefBoolean("DISABLE_PASS_CODE");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_pin,container,false);

        pin_password = view.findViewById(R.id.pin_password);
        pin_confirm_btn = view.findViewById(R.id.pin_confirm_btn);


        pin_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pin_password.getText().toString();
                if(!pin.equals("") && pin.equals(password)){
                   dismiss();
                }
                else{
                    new CoustomToast(getContext(), "Wrong Password",CoustomToast.TYPE_DANGER).showToast();
                }
            }
        });

        return view;
    }
}
