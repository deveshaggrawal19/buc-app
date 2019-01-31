package com.buyucoin.buyucoin.bottomsheets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapterDeposit;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Fragments.P2PFragment;
import com.buyucoin.buyucoin.Fragments.TriggerActiveOrder;
import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.customDialogs.P2pActiveOrdersDialog;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BankDetails extends BottomSheetDialogFragment {

    private String account_no,bank_name,b_name,ifsc_code,mode,note,did,wid,tx_hash_string,tx_hash_time = "";
    private Context context;
    private static boolean issuccess = true;
    private BuyucoinPref pref;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            account_no = nullSafety(bundle.getString("account_no"));
            bank_name = nullSafety(bundle.getString("bank_name"));
            b_name = nullSafety(bundle.getString("b_name"));
            ifsc_code = nullSafety(bundle.getString("ifsc_code"));
            mode = nullSafety(bundle.getString("mode"));
            note = nullSafety(bundle.getString("note"));
            did = nullSafety(bundle.getString("did"));
            wid = nullSafety(bundle.getString("wid"));
            tx_hash_string = nullSafety(bundle.getString("tx_hash"));
            tx_hash_time = nullSafety(bundle.getString("time"));
            context = getContext();
            assert context != null;
            pref = new BuyucoinPref(context);
            position = bundle.getInt("position");
        }
    }


    public String nullSafety(String s){
        if(s!="") return s;
        else return "N/A";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bank_details_bottomsheet,container,false);

        TextView t1,t2,t3,t4,t5,t6,time;
        final Button remove_request,cancel,mark_complete,submit_hash,see_bank_details,raise_dispute;
        final EditText tx_hash;
        final LinearLayout action_layout,tx_hash_layout,progress_action_layout,tx_hash_layout_success,bank_layout;
        t1 = view.findViewById(R.id.textView2);
        t2 = view.findViewById(R.id.textView4);
        t3 = view.findViewById(R.id.textView6);
        t4 = view.findViewById(R.id.textView8);
        t5 = view.findViewById(R.id.textView10);
        t6 = view.findViewById(R.id.textView12);
        time = view.findViewById(R.id.tx_hash_time_tv);
        remove_request = view.findViewById(R.id.remove_request);
        mark_complete = view.findViewById(R.id.mark_complete);
        cancel = view.findViewById(R.id.cancel_request);
        submit_hash = view.findViewById(R.id.submit_tx_hash);
        tx_hash = view.findViewById(R.id.transition_hash);
        action_layout = view.findViewById(R.id.action_layout);
        tx_hash_layout = view.findViewById(R.id.tx_hash_layout);
        progress_action_layout = view.findViewById(R.id.progress_action_layout);
        see_bank_details = view.findViewById(R.id.see_bank_details);
        raise_dispute = view.findViewById(R.id.raise_dispute);
        tx_hash_layout_success = view.findViewById(R.id.tx_action_layout);
        bank_layout = view.findViewById(R.id.bank_details_layout);

        long minutes = new Date(tx_hash_time).getTime()*60;
        time.setText(String.valueOf(minutes));


        if(!tx_hash_string.equals("N/A")){
            bank_layout.setVisibility(View.GONE);
            tx_hash_layout.setVisibility(View.GONE);
            action_layout.setVisibility(View.GONE);

        }
        else {
            tx_hash_layout_success.setVisibility(View.GONE);
        }

        see_bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = (bank_layout.getVisibility()==View.VISIBLE)?View.GONE:View.VISIBLE;
                if(see_bank_details.getText().equals("Hide Details"))see_bank_details.setText(R.string.see_bank_details);
                else see_bank_details.setText(getString(R.string.hide_details));
                bank_layout.setVisibility(vis);
            }
        });


        t1.setText(account_no);
        t2.setText(bank_name);
        t3.setText(b_name);
        t4.setText(ifsc_code);
        t5.setText(mode);
        t6.setText(note);
        remove_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final JSONObject object = new JSONObject();
                    object.put("method", "peer_deposit_cancel")
                            .put("deposit_id", did).put("withdraw_id", wid);
                    new AlertDialog.Builder(context).setMessage("Do you want to delete this peer")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean b = peerAction(object.toString());
                                    if (b) {
                                        new CoustomToast(context, (Dashboard) context, "Deleted Successfully", CoustomToast.TYPE_SUCCESS).showToast();
                                        dialog.dismiss();
                                        MatchedPeer peer = new P2PorderRecyclerViewAdapterDeposit();
                                        peer.refreshMatch(position);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                               dismiss();
                                            }
                                        },1000);
                                    } else {
                                        new CoustomToast(context, (Dashboard) context, "Error While Deleting...", CoustomToast.TYPE_DANGER).showToast();
                                        dialog.dismiss();
                                        dismiss();
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
        });

        mark_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_layout.setVisibility(View.GONE);
                tx_hash_layout.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_layout.setVisibility(View.VISIBLE);
                tx_hash_layout.setVisibility(View.GONE);
            }
        });

        submit_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tx_hash_string = tx_hash.getText().toString();
                if(!tx_hash_string.equals("")){
                    JSONObject j = new JSONObject();
                    try {
                        j.put("deposit_id",did)
                                .put("withdraw_id",wid)
                                .put("tx_hash",tx_hash_string)
                                .put("method", "peer_deposit_complete");
                        progress_action_layout.setVisibility(View.VISIBLE);
                        tx_hash_layout.setVisibility(View.GONE);
                        boolean b = peerAction(j.toString());

                        if(b){
                            new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),"Successful",CoustomToast.TYPE_SUCCESS).showToast();
                            TriggerActiveOrder t = new P2PFragment();
                            t.triggerOrder();
                            dismiss();
                        }else{
                            new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),"Error happen",CoustomToast.TYPE_DANGER).showToast();
                            progress_action_layout.setVisibility(View.GONE);
                            tx_hash_layout.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),"Enter transection hash !!",CoustomToast.TYPE_DANGER).showToast();
                }
            }
        });




        return view;
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
                    Log.d("PEER ACTION RESPONSE",j.toString());
                    if(j.getBoolean("success")){
                        issuccess = true;
                    }else{
                        issuccess = false;
                    }
                    issuccess = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return issuccess;

    }
}
