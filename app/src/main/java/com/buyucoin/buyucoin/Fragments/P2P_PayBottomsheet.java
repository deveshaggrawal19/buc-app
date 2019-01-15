package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class P2P_PayBottomsheet extends BottomSheetDialogFragment {

    private CheckBox upi_checkobx,imps_checkbox;
    private EditText upi;
    SeekBar boost_seekbar;
    TextView boost_value_text,pay_amt,pay_min_amt,pay_boost_amt;
    Button make_p2p_request;
    int amount;
    int min_amount;
    String type = "deposit",modes = "imps";
    String upi_address;
    int boost = 0;
    BuyucoinPref buyucoinPref;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_p2p_pay,container,false);
        buyucoinPref = new BuyucoinPref(view.getContext());

        upi = view.findViewById(R.id.etP2PUpiId);
        upi.setVisibility(View.GONE);
        imps_checkbox = view.findViewById(R.id.checkBox);
        upi_checkobx = view.findViewById(R.id.checkBox2);
        boost_seekbar = view.findViewById(R.id.seekBar);
        boost_value_text = view.findViewById(R.id.boost_value_text);
        make_p2p_request = view.findViewById(R.id.make_p2p_request);

        pay_amt = view.findViewById(R.id.pay_amt);
        pay_min_amt = view.findViewById(R.id.pay_min_amt);
        pay_boost_amt = view.findViewById(R.id.pay_boost_amt);

        if(getArguments()!=null){
            Bundle b = getArguments();
            amount = b.getInt("amount",0);
            min_amount = b.getInt("min_amount",0);
            type = b.getString("type","deposit");

            pay_amt.setText(String.valueOf(amount));
            pay_min_amt.setText(String.valueOf(min_amount));
            pay_boost_amt.setText(String.valueOf(boost));
        }

        imps_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(imps_checkbox.isChecked()){
                    modes = "imps";
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
                pay_boost_amt.setText(String.valueOf(progress));
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

                String msg = "Amount : "+amount+" Min_Amount : "+min_amount+" Type : "+type+" Modes : "+modes+" UPI Address : "+upi_address+" Boost : "+boost;
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                JSONObject order = new JSONObject();
                try {
                    order.put("amount",amount)
                            .put("min_amount",min_amount)
                            .put("type",type)
                            .put("modes",modes)
                            .put("note",upi_address)
                            .put("boost",boost);
                    makeRequest(order);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return  view;


    }

    private void makeRequest(JSONObject order) {
        OkHttpHandler.auth_post("create_peer", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), order.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ORDER ERROR====>",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("ORDER SUCCESS====>",response.toString());

            }
        });
    }


}
