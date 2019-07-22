package com.buyucoinApp.buyucoin.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class P2P_PayBottomsheet extends BottomSheetDialogFragment {

    private int amount;
    private int min_amount;
    private String type = "deposit";
    private BuyucoinPref buyucoinPref;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_p2p_pay,container,false);
        buyucoinPref = new BuyucoinPref(view.getContext());

        Button make_p2p_request = view.findViewById(R.id.make_p2p_request);

        TextView pay_amt = view.findViewById(R.id.pay_amt);

        if(getArguments()!=null){
            Bundle b = getArguments();
            amount = b.getInt("amount",0);
            min_amount = b.getInt("min_amount",0);
            type = b.getString("type","deposit");

            pay_amt.setText(String.valueOf(amount));
        }


        make_p2p_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        return  view;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()), R.style.CoustomBottomSheet);
    }

    private void makeRequest(JSONObject order, String[] modes, final Context context) {
        OkHttpHandler.auth_post("create_peer", buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN), order.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getDialog()!=null){
                getDialog().dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                if(s!=null){
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
                else{
                     if(getActivity()!=null){
                        new CoustomToast(getContext(),"Something went wrong",CoustomToast.TYPE_DANGER).showToast();
                     }

                }



            }
        });
    }


}
