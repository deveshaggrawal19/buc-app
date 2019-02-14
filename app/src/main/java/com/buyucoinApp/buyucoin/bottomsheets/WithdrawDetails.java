package com.buyucoinApp.buyucoin.bottomsheets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WithdrawDetails extends BottomSheetDialogFragment {

    private String did,wid,tx_hash_string,status = "";
    private Context context;
    private static boolean issuccess = true;
    private BuyucoinPref pref;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();

            did = nullSafety(bundle.getString("did"));
            wid = nullSafety(bundle.getString("wid"));
            tx_hash_string = nullSafety(bundle.getString("tx_hash"));
            context = getContext();
            assert context != null;
            pref = new BuyucoinPref(context);
            position = bundle.getInt("position");
            status = bundle.getString("status");
        }
    }


    public String nullSafety(String s){
        if(s!="") return s;
        else return "N/A";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.withdraw_details_bottomsheet,container,false);

        TextView tx_hash_tv,dispute_tx_hash_tv;
        Button mark_complete,raise_dispute,mark_dispute_received;
        LinearLayout view_layout_display,tx_hash_layout_display,dispute_layout_display,success_layout_display;

        tx_hash_tv = view.findViewById(R.id.tx_hash_tv);
        dispute_tx_hash_tv = view.findViewById(R.id.dispute_tx_hash_tv);

        view_layout_display = view.findViewById(R.id.view_layout_display);
        tx_hash_layout_display = view.findViewById(R.id.tx_hash_layout_display);
        dispute_layout_display = view.findViewById(R.id.dispute_layout_display);
        success_layout_display = view.findViewById(R.id.success_layout_display);


        mark_complete = view.findViewById(R.id.mark_complete);
        raise_dispute = view.findViewById(R.id.raise_dispute);
        mark_dispute_received = view.findViewById(R.id.mark_received);


        switch (status){
            case "WITHDRAW_ACCEPTED":
                view_layout_display.setVisibility(View.VISIBLE);break;
            case "DEPOSIT_ACCEPTED":
                tx_hash_layout_display.setVisibility(View.VISIBLE);break;
            case "WITHDRAW_COMPLETE":
                success_layout_display.setVisibility(View.VISIBLE);break;
            case "DISPUTE":
                dispute_layout_display.setVisibility(View.VISIBLE);break;
        }

        if(!tx_hash_string.equals("N/A")){
            tx_hash_tv.setText(tx_hash_string);
            dispute_tx_hash_tv.setText("TX HASH : "+tx_hash_string);

        }

        mark_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAction("peer_withdraw_complete",wid,did,"mark as complete");
            }
        });

        mark_dispute_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAction("peer_withdraw_complete",wid,did,"mark as complete");
            }
        });


        raise_dispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAction("peer_withdraw_dispute",wid,did,"raise dispute");
            }
        });

        return view;
    }

    public void completeAction(String action, String wid, String did,String msg){

        try {
            final JSONObject object = new JSONObject();
            object.put("method", action)
                    .put("deposit_id", wid).put("withdraw_id", did);

            Log.d("ooooooooooooooooooooo", "onClick: "+object.toString());
            new AlertDialog.Builder(context).setMessage("Are you sure to "+msg)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog p = new ProgressDialog(context);
                            p.setMessage("processing");
                            p.show();
                            boolean b = peerAction(object.toString());
                            if(b){
                                p.dismiss();
                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean peerAction(String s){

        OkHttpHandler.auth_post("peer_action", pref.getPrefString(BuyucoinPref.ACCESS_TOKEN), s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("PEER ACTION RESPONSE","FAILED");
                issuccess = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    assert response.body() != null;
                    String res = response.body().string();
                    JSONObject j = new JSONObject(res);
                    final String msg = j.getJSONArray("message").getJSONArray(0).getString(0);
                    Log.d("PEER ACTION RESPONSE",j.toString());
                    if(j.getBoolean("success")){
                        issuccess = true;
                    }else{
                        issuccess = false;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(issuccess){
                                new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),msg,CoustomToast.TYPE_SUCCESS).showToast();

                            }else{
                                new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),msg,CoustomToast.TYPE_DANGER).showToast();

                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return issuccess;

    }
}
