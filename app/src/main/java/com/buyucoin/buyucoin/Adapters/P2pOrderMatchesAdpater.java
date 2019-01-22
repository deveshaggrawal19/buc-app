package com.buyucoin.buyucoin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class P2pOrderMatchesAdpater extends RecyclerView.Adapter<P2pOrderMatchesAdpater.MyViewHolder> {
    private JSONArray activeP2pOrders;

    public P2pOrderMatchesAdpater(JSONArray activeP2pOrders) {
        this.activeP2pOrders = activeP2pOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.amount.setText(String.valueOf(activeP2pOrders.getJSONObject(position).getInt("vol")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return activeP2pOrders.length();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_match_amount);
        }
    }
}
