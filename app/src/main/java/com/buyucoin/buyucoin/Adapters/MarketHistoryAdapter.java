package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.MarketHistory;

import java.text.DecimalFormat;
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


        DecimalFormat format = new DecimalFormat("0.####");
        String v1 = format.format(historyArrayList.get(position).getAmount());
        holder.amount.setText(String.valueOf(v1));
        holder.type.setText(historyArrayList.get(position).getType());
//        holder.type_img.setVisibility(View.GONE);

        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        if(historyArrayList.get(position).getType().equals("Buy")){
//            holder.type_img.setImageResource(R.drawable.history_deposite_icon);
//            holder.type_img.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.kyc_color)));
            holder.price.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.value.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.amount.setTextColor(context.getResources().getColor(R.color.kyc_color));
            holder.type.setTextColor(context.getResources().getColor(R.color.kyc_color));
        }else{
//            holder.type_img.setImageResource(R.drawable.history_withdraw_icon);
//            holder.type_img.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRed)));
            holder.price.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.value.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.type.setTextColor(context.getResources().getColor(R.color.colorRed));
        }



    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    class MarketHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView price,value,amount,type;
//        ImageView type_img;
        View progress;
        MarketHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            value = itemView.findViewById(R.id.item_value);
            amount = itemView.findViewById(R.id.item_amount);
            type = itemView.findViewById(R.id.item_type);
//            type_img = itemView.findViewById(R.id.item_img_type);

        }
    }
}
