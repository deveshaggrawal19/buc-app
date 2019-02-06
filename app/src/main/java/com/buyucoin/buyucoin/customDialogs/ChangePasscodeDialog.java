package com.buyucoin.buyucoin.customDialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ChangePasscodeDialog extends DialogFragment {

    EditText old_passcode,new_passcode;
    Button change_passcode_btn;
    BuyucoinPref pref;
    private String password = "";





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        password = pref.getPrefString("passcode");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_passcode,container,false);
        old_passcode = view.findViewById(R.id.old_passcode);
        new_passcode = view.findViewById(R.id.new_passcode);
        change_passcode_btn = view.findViewById(R.id.change_passcode_btn);

        change_passcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String op = old_passcode.getText().toString();
                String np = new_passcode.getText().toString();

                if(!op.equals("") || np.equals("")){
                    if(op.equals(password)){
                        pref.setEditpref("passcode",np);
                        new CoustomToast(
                                getContext(),
                                Objects.requireNonNull(getActivity()),
                                np+" is your new pass code",
                                CoustomToast.TYPE_SUCCESS
                        ).showToast();
                        dismiss();
                    }
                    else{
                        old_passcode.setError("Wrong Passcode");
                    }

                }else {
                    new CoustomToast(
                            getContext(),
                            Objects.requireNonNull(getActivity()),
                            "Both Field Required",
                            CoustomToast.TYPE_DANGER
                    ).showToast();
                }


            }
        });

        return view;
    }
}
