package com.buyucoin.buyucoin.Adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.bottomsheets.BankDetails;
import com.google.firebase.database.core.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class P2pOrderMatchesAdpater extends RecyclerView.Adapter<P2pOrderMatchesAdpater.MyViewHolder> {
    private JSONArray activeP2pOrders;
    private FragmentManager fragmentManager;

    public P2pOrderMatchesAdpater(JSONArray activeP2pOrders, FragmentManager fragmentManager) {
        this.activeP2pOrders = activeP2pOrders;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        try {
        JSONObject data = activeP2pOrders.getJSONObject(position);
            holder.amount.setText(String.valueOf(data.getInt("vol")));
            holder.id.setText(String.valueOf(data.getString("key")));
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   DialogFragment bankDetails = new BankDetails();
                   bankDetails.show(fragmentManager,"fdsgfh");
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

    @Override
    public int getItemCount() {
        return activeP2pOrders.length();
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
