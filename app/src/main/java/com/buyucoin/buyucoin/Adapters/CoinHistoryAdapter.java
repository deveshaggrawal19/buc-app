package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoinHistoryAdapter extends RecyclerView.Adapter<CoinHistoryAdapter.HistoryViewHolder> {

    private Context context;

    public CoinHistoryAdapter(Context applicationContext) {
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item,parent,false);
        return new CoinHistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView coin,date,amount;
        ImageView img;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            coin = itemView.findViewById(R.id.historyCoin);
            date = itemView.findViewById(R.id.historyDate);
            amount = itemView.findViewById(R.id.historyAmount);
            img = itemView.findViewById(R.id.historyImg);

        }
    }
}
