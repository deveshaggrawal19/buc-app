package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class P2P_PayBottomsheet extends BottomSheetDialogFragment {

    private CheckBox upi_checkobx,imps_checkbox;
    private EditText upi;
    SeekBar boost_seekbar;
    TextView boost_value_text;
    Button make_p2p_request;
    int amount;
    int min_amount;
    String type = "deposite",modes = "imps";
    String upi_address;
    int boost = 0;


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
        imps_checkbox = view.findViewById(R.id.checkBox);
        upi_checkobx = view.findViewById(R.id.checkBox2);
        boost_seekbar = view.findViewById(R.id.seekBar);
        boost_value_text = view.findViewById(R.id.boost_value_text);
        make_p2p_request = view.findViewById(R.id.make_p2p_request);

        if(getArguments()!=null){
            Bundle b = getArguments();
            amount = b.getInt("amount",0);
            min_amount = b.getInt("min_amount",0);
        }

        imps_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(imps_checkbox.isChecked()){
                    modes = "imps";
                }else {

                }
            }
        });


        upi_checkobx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upi_checkobx.isChecked()){
                    modes = "imps/upi";
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
                boost = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        make_p2p_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upi_checkobx.isChecked()){
                    if(!upi.getText().toString().equals("")){
                        upi_address = upi.getText().toString();
                    }else {
                        Toast.makeText(getContext(), "Enter UPI Address", Toast.LENGTH_SHORT).show();
                    }
                }
                if(min_amount>0){
                    type = "deposit";
                }
                else{
                    type = "withdraw";
                }

                String msg = "Amount : "+amount+" Min_Amount : "+min_amount+" Type : "+type+" Modes : "+modes+" UPI Address : "+upi_address+" Boost : "+boost;
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        return  view;
    }
}
