package com.buyucoinApp.buyucoin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
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

public class WithdrawBottomsheet extends BottomSheetDialogFragment {
    Bundle bundle;
    private Double coin_amount;
    private String coin_tag,coin_address,coin_name;
    private TextView amount,tag,address;
    private Button proceedToWithdraw;
    private BuyucoinPref buyucoinPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));

        if(bundle!=null){
        coin_amount = bundle.getDouble("coin_amount",0);
        coin_tag = bundle.getString("coin_tag","N/A");
        coin_address = bundle.getString("coin_address","N/A");
        coin_name = bundle.getString("coin_name","N/A");

            Log.d("===========>",String.valueOf(coin_amount)+"  "+coin_tag);
        }
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottomsheet_withdraw,container,false);
        amount = view.findViewById(R.id.amount);
        tag = view.findViewById(R.id.tag);
        address = view.findViewById(R.id.address);
        proceedToWithdraw = view.findViewById(R.id.proceed_to_withdraw);

        proceedToWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject order_ob = new JSONObject();
                try {
                    order_ob.put("address",coin_address)
                            .put("amount",coin_amount)
                            .put("currency",coin_name)
                            .put("tag",coin_tag);

                    makeRequest(order_ob.toString(),view.getContext());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        if(bundle!=null){
            amount.setText("Amount : "+String.valueOf(coin_amount));
            tag.setText("Destination Tag : "+coin_tag);
            address.setText("Address : "+coin_address);
            if(!coin_tag.equals("")) tag.setVisibility(View.VISIBLE);

        }

        return view;
    }

    private void makeRequest(String s,final Context context) {
        OkHttpHandler.auth_post("create_withdraw", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN),s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ORDER ERROR====>",e.getMessage());
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

                    final String tmsg = "STATUS "+status+" MESSAGE "+msg.getJSONArray(0).getString(0);

                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if(status.equals("success")){
                                Toast.makeText(context, tmsg, Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                            else{
                                Toast.makeText(context, tmsg, Toast.LENGTH_SHORT).show();
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
