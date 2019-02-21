package com.buyucoinApp.buyucoin.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.Fragments.AccountFragment;
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

public class BuySellConfirm extends BottomSheetDialogFragment {

    private String dialog_title;
    private String quantity;
    private String price;
    private String fees;
    private String total;
    private String type;
    private String coin;

    public static BuySellConfirm newInstance(){
        return new BuySellConfirm();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.buy_dialog_layout,container,false);

        TextView title,tv_quantity,tv_price,tv_fees,tv_total;
        final Button agree,cancel;
        title = view.findViewById(R.id.dialog_title);
        tv_quantity = view.findViewById(R.id.dialog_quantity);
        tv_price = view.findViewById(R.id.dialog_price);
        tv_fees = view.findViewById(R.id.dialog_fees);
        tv_total = view.findViewById(R.id.dialog_total);

        agree = view.findViewById(R.id.agree_action_btn);
        cancel = view.findViewById(R.id.cancel_action_btn);



        if(getArguments()!=null){
            Bundle bundle = getArguments();
            quantity = bundle.getString("quantity");
            price = bundle.getString("price");
            fees = bundle.getString("fees");
            total = bundle.getString("total");
            type = bundle.getString("type");
            coin = bundle.getString("coin");
            dialog_title = type.toUpperCase()+" ORDER";
        }

        title.setText(dialog_title);
        tv_quantity.setText(quantity);
        tv_price.setText(price);
        tv_fees.setText(fees);
        tv_total.setText(total);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!quantity.equals("") && !price.equals("")){
                final JSONObject order = new JSONObject();
                try {
                    order.put("amount",Double.parseDouble(quantity))
                            .put("rate",Double.parseDouble(price))
                            .put("type",type);

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            AccountFragment.makeViewDisable(agree);
                            PlaceOrder(order.toString(),coin,view.getContext());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }else{
                    String msg = (quantity.equals(""))?"Amount":(price.equals(""))?"Price":"Amount and Price";
                    new CoustomToast(getContext(),"Enter "+msg,CoustomToast.TYPE_DANGER).showToast();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;


    }





    private void PlaceOrder(String s,String coin,final Context context){
        OkHttpHandler.auth_post("create_order?currency="+coin, new BuyucoinPref(context).getPrefString(BuyucoinPref.ACCESS_TOKEN), s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ORDER FAILED =====>",e.getMessage());
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
                            new CoustomToast(getContext(),tmsg,CoustomToast.TYPE_PENDING).showToast();

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
