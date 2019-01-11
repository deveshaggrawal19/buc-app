package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.Ask;
import com.buyucoin.buyucoin.pojos.MarketHistory;
import com.google.firebase.database.android.AndroidPlatform;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarketHistoryAdapter extends RecyclerView.Adapter<MarketHistoryAdapter.MarketHistoryViewHolder> {
    private Context context;
    private ArrayList<MarketHistory> historyArrayList;

    public MarketHistoryAdapter(Context context, ArrayList<MarketHistory> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public MarketHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.market_history_item, parent, false);
        return new MarketHistoryAdapter.MarketHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketHistoryViewHolder holder, int position) {

        holder.price.setText(String.valueOf(historyArrayList.get(position).getPrice()));
        holder.value.setText(String.valueOf(historyArrayList.get(position).getValue()));
        holder.amount.setText(String.valueOf(historyArrayList.get(position).getAmount()));
        holder.type.setText(historyArrayList.get(position).getType());

        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        if(historyArrayList.get(position).getType().equals("Buy")){
            holder.type_img.setImageResource(R.drawable.history_deposite_icon);
            holder.type_img.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.kyc_color)));
            holder.price.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.value.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.amount.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.type.setTextColor(context.getResources().getColor(R.color.kyc_color));
        }else{
            holder.type_img.setImageResource(R.drawable.history_withdraw_icon);
            holder.type_img.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRed)));
            holder.price.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.value.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.type.setTextColor(context.getResources().getColor(R.color.colorRed));
        }


        Log.d("WIDTH============>",String.valueOf(holder.itemView));

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    class MarketHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView price,value,amount,type;
        ImageView type_img;
        View progress;
        MarketHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            value = itemView.findViewById(R.id.item_value);
            amount = itemView.findViewById(R.id.item_amount);
            type = itemView.findViewById(R.id.item_type);
            type_img = itemView.findViewById(R.id.item_img_type);

        }
    }
}
