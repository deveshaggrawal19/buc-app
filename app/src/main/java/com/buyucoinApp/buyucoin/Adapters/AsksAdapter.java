package com.buyucoinApp.buyucoin.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pojos.Ask;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AsksAdapter extends RecyclerView.Adapter<AsksAdapter.AskViewHolder> {
    private Context context;
    private ArrayList<Ask> askArrayList;

    public AsksAdapter(Context context, ArrayList<Ask> askArrayList) {
        this.context = context;
        this.askArrayList = askArrayList;
    }

    @NonNull
    @Override
    public AskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ask_item, parent, false);
        return new AsksAdapter.AskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AskViewHolder holder, int position) {
        DecimalFormat format2 = new DecimalFormat("0.####");
        DecimalFormat format = new DecimalFormat("0.########");

        Double price = askArrayList.get(position).getAsk_price();
        double value = Double.parseDouble(askArrayList.get(position).getAsk_value());
        String vol = format.format(askArrayList.get(position).getAsk_volume());

        holder.price.setText(String.valueOf(format2.format(price)));
        holder.value.setText((format.format(value)));
        holder.vol.setText(vol);


        if(position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
    }

    @Override
    public int getItemCount() {
        return (askArrayList.size()<=10)?askArrayList.size():10;
    }

    class AskViewHolder extends RecyclerView.ViewHolder {
        TextView price,value,vol;
        View progress;
        AskViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            value = itemView.findViewById(R.id.item_value);
            vol = itemView.findViewById(R.id.item_vol);

        }
    }
}
