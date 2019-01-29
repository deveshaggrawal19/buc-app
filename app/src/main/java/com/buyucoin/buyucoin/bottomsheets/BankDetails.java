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
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapter;
import com.buyucoin.buyucoin.Adapters.P2pOrderMatchesAdpater;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
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


public class BankDetails extends BottomSheetDialogFragment {

    private String account_no,bank_name,b_name,ifsc_code,mode,note,did,wid = "";
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
        View view = inflater.inflate(R.layout.bank_details_bottomsheet,container,false);

        TextView t1,t2,t3,t4,t5,t6;
        Button button;
        t1 = view.findViewById(R.id.textView2);
        t2 = view.findViewById(R.id.textView4);
        t3 = view.findViewById(R.id.textView6);
        t4 = view.findViewById(R.id.textView8);
        t5 = view.findViewById(R.id.textView10);
        t6 = view.findViewById(R.id.textView12);
        button = view.findViewById(R.id.button2);

        t1.setText(account_no);
        t2.setText(bank_name);
        t3.setText(b_name);
        t4.setText(ifsc_code);
        t5.setText(mode);
        t6.setText(note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(context,did+" "+wid, Toast.LENGTH_SHORT).show();
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
                                        MatchedPeer peer = new P2PorderRecyclerViewAdapter();
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
