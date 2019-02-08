package com.buyucoin.buyucoin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.bottomsheets.WithdrawDetails;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class P2pOrderMatchesAdpaterWithdraw extends RecyclerView.Adapter<P2pOrderMatchesAdpaterWithdraw.MyViewHolder> implements MatchedPeer {
    private BuyucoinPref pref;
    private AlertDialog.Builder progressDialog;
    private JSONArray activeP2pOrders;
    private FragmentManager fragmentManager;
    private android.content.Context context;


    public P2pOrderMatchesAdpaterWithdraw(JSONArray activeP2pOrders, FragmentManager fragmentManager, Context context) {
        this.activeP2pOrders = (activeP2pOrders.length()>0)?activeP2pOrders:null;
        this.fragmentManager = fragmentManager;
        this.context = context;
        pref = new BuyucoinPref(context);
        progressDialog = new ProgressDialog.Builder(context);
        progressDialog.setMessage("Processing");
        progressDialog.create();
    }
    public P2pOrderMatchesAdpaterWithdraw() {

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_withdraw_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (activeP2pOrders!=null) {
            try {
            final JSONObject data = activeP2pOrders.getJSONObject(position);
                Log.d("MATCH PEER DATA",data.toString());
                final Bundle bundle = new Bundle();
               if(data.has("tx_hash")){
                    final String tx_hash = data.getString("tx_hash");
                    bundle.putString("tx_hash",tx_hash);
                }
                else{
                    bundle.putString("tx_hash","");

                }


                final String did = String.valueOf(data.getInt("id"));
                final String wid = String.valueOf(data.getInt("key"));
                final String status = data.getString("status");



                holder.amount.setText(String.valueOf(data.getInt("vol")/10000));
                holder.id.setText(String.valueOf(data.getString("key")));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       DialogFragment withdrawDetails = new WithdrawDetails();


                       bundle.putString("did",did);
                       bundle.putString("wid",wid);
                       bundle.putInt("position",position);
                       bundle.putString("status",status);

                       withdrawDetails.setArguments(bundle);
                       withdrawDetails.show(fragmentManager,"fdsgfh");
                    }
                });

                if(data.getString("status").equals("WITHDRAW_COMPLETE")){
                    holder.action.setVisibility(View.GONE);
                    holder.dispute.setVisibility(View.GONE);
                    holder.successful.setVisibility(View.VISIBLE);
                    holder.details.setVisibility(View.GONE);
                    Log.d("DEPOSIT_ACCEPTED",data.getString("status"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("M-M-M-M-M-M-M-M", "FROM INSIDE WITHDRAW MATCH ADAPTER");


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
        TextView amount,id,details,action,dispute,successful;
         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_match_amount);
            id = itemView.findViewById(R.id.p2p_order_match_id);
            details = itemView.findViewById(R.id.p2p_order_match_details);
            action = itemView.findViewById(R.id.mark_complete);
            dispute = itemView.findViewById(R.id.mark_dispute);
            successful = itemView.findViewById(R.id.peer_success);
        }
    }



}
