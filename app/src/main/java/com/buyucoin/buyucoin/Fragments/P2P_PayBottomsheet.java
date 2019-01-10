package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class P2P_PayBottomsheet extends BottomSheetDialogFragment {

    private CheckBox upi_checkobx;
    private EditText upi;
    SeekBar boost_seekbar;
    TextView boost_value_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_p2p_pay,container,false);
        upi = view.findViewById(R.id.etP2PUpiId);
        upi.setVisibility(View.GONE);
        upi_checkobx = view.findViewById(R.id.checkBox2);
        boost_seekbar = view.findViewById(R.id.seekBar);
        boost_value_text = view.findViewById(R.id.boost_value_text);


        upi_checkobx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upi_checkobx.isChecked()){
                    upi.setVisibility(View.VISIBLE);
                }else{
                    upi.setVisibility(View.GONE);
                }
            }
        });
        boost_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String val = "Boost Amount "+getString(R.string.rupees)+" "+progress;
                boost_value_text.setText(val);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return  view;
    }
}
