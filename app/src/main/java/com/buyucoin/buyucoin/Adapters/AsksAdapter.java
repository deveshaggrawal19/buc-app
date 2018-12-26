package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.Ask;

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
        holder.price.setText(String.valueOf(askArrayList.get(position).getAsk_amount()));
        int view_width = (int) (Math.random()*150);
        holder.progress.setLayoutParams(new LinearLayout.LayoutParams(view_width,5));
        Log.d("WIDTH============>",String.valueOf(holder.itemView));

    }

    @Override
    public int getItemCount() {
        return askArrayList.size();
    }

    class AskViewHolder extends RecyclerView.ViewHolder {
        TextView price;
        View progress;
        AskViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            progress = itemView.findViewById(R.id.item_progress);
        }
    }
}
