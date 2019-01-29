package com.buyucoin.buyucoin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.bottomsheets.BankDetails;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapter.issuccess;

public class P2pOrderMatchesAdpater extends RecyclerView.Adapter<P2pOrderMatchesAdpater.MyViewHolder> implements MatchedPeer {
    private BuyucoinPref pref;
    private AlertDialog.Builder progressDialog;
    private JSONArray activeP2pOrders;
    private FragmentManager fragmentManager;
    private android.content.Context context;


    public P2pOrderMatchesAdpater(JSONArray activeP2pOrders, FragmentManager fragmentManager, Context context) {
        this.activeP2pOrders = activeP2pOrders;
        this.fragmentManager = fragmentManager;
        this.context = context;
        pref = new BuyucoinPref(context);
        progressDialog = new ProgressDialog.Builder(context);
        progressDialog.setMessage("Processing");
        progressDialog.create();
    }
    public P2pOrderMatchesAdpater() {

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {
        final JSONObject data = activeP2pOrders.getJSONObject(position);
            JSONObject bank = data.getJSONObject("bank");
            Log.d("MATCH PEER DATA",data.toString());

            final String did = String.valueOf(data.getInt("id"));
            final String wid = String.valueOf(data.getInt("key"));
            final String account_no = bank.getString("account");
            final String bank_name = bank.getString("bank_name");
            final String b_name = bank.getString("beneficiary");
            final String ifsc_code = bank.getString("ifsc_code");
            final String mode = data.getString("mode");
            final String note = data.getString("note");



            holder.amount.setText(String.valueOf(data.getInt("vol")));
            holder.id.setText(String.valueOf(data.getString("key")));
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   DialogFragment bankDetails = new BankDetails();
                   Bundle bundle = new Bundle();
                    bundle.putString("account_no",account_no);
                    bundle.putString("bank_name",bank_name);
                    bundle.putString("b_name",b_name);
                    bundle.putString("ifsc_code",ifsc_code);
                    bundle.putString("mode",mode);
                    bundle.putString("note",note);
                   bundle.putString("did",did);
                   bundle.putString("wid",wid);
                   bundle.putInt("position",position);

                   bankDetails.setArguments(bundle);
                   bankDetails.show(fragmentManager,"fdsgfh");
                }
            });
            holder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(holder.itemView.getContext(),did+" "+wid, Toast.LENGTH_SHORT).show();
                        final JSONObject object = new JSONObject();
                            object.put("method", "peer_deposit_cancel")
                                    .put("deposit_id", did).put("withdraw_id", wid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String status(String s){
        switch (s){
            case "DEPOSIT_ACCEPTED":return "Accepted";
            case "WITHDRAW_ACCEPTED":return "Accepted";
            case "PENDING":return "Pending";
            case "DISPUTE":return "Dispute";
            default: return s;
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

    @Override
    public int getItemCount() {
        return activeP2pOrders.length();
    }

    @Override
    public void refreshMatch(int Position) {
        if(activeP2pOrders!=null){
        activeP2pOrders.remove(Position);
        notifyItemRemoved(Position);
        notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount,id,details,action;
         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_match_amount);
            id = itemView.findViewById(R.id.p2p_order_match_id);
            details = itemView.findViewById(R.id.p2p_order_match_details);
            action = itemView.findViewById(R.id.p2p_order_match_action);

        }
    }



}
