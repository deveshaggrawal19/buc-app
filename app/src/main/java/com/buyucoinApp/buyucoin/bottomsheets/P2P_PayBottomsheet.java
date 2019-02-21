package com.buyucoinApp.buyucoin.bottomsheets;

import android.content.Context;
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

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
    String type = "deposit";
    private String[] IMPS_UPI = new String[]{"imps","upi"};
    private String[] IMPS_ONLY = new String[]{"imps"};
    String[] modes = IMPS_ONLY;
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
        final View view = inflater.inflate(R.layout.bottom_sheet_p2p_pay,container,false);
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
                    modes = IMPS_ONLY;
                }
            }
        });


        upi_checkobx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upi_checkobx.isChecked()){
                    modes = IMPS_UPI;
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
                upi_address = upi.getText().toString();
                if(upi_checkobx.isChecked()){
                    if(upi_address.equals("")){
                        new CoustomToast(getContext(),"Enter UPI Address",CoustomToast.TYPE_DANGER).showToast();
                        return;
                    }
                }
                if(imps_checkbox.isChecked()){
                    JSONObject order = new JSONObject();
                    try {
                        order.put("amount",amount)
                                .put("min_amount",min_amount)
                                .put("type",type)
                                .put("modes",new JSONArray(modes))
                                .put("boost",boost);

                        if(!upi_address.equals("")){
                            order.put("note",upi_address);
                        }
                        makeRequest(order,modes,view.getContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(!imps_checkbox.isChecked() &&  !upi_checkobx.isChecked()){
                    new CoustomToast(getContext(),"Choose at least one mode of payment",CoustomToast.TYPE_DANGER).showToast();
                }


            }
        });
        return  view;


    }

    private void makeRequest(JSONObject order,String[] modes , final Context context) {
        OkHttpHandler.auth_post("create_peer", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), order.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getDialog()!=null){
                getDialog().dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {
                    final JSONObject jsonObject1 = new JSONObject(s);
                    final JSONArray msg = jsonObject1.getJSONArray("message");
                    final String status = jsonObject1.getString("status");

                    final String tmsg = msg.getJSONArray(0).getString(0);

                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if(status.equals("success")){
                                new CoustomToast(getContext(),tmsg,CoustomToast.TYPE_SUCCESS).showToast();
                                dismiss();
                            }
                            else{
                                new CoustomToast(getContext(),tmsg,CoustomToast.TYPE_DANGER).showToast();
                            }

                        }
                    });
//                dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }


}
