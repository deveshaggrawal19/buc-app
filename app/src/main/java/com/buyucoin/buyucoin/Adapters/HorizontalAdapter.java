package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.WalletCoinHorizontal;

import java.util.ArrayList;

class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private ArrayList<WalletCoinHorizontal> arrayList;

    public HorizontalAdapter(ArrayList<WalletCoinHorizontal> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_single_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.coinname.setText(arrayList.get(i).getCoinname().toUpperCase());
        myViewHolder.portfolio.setText(arrayList.get(i).getPortfolio());
        int r = (int) (Math.random() * 6);
        myViewHolder.itemView.getBackground().setLevel(r);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coinname,portfolio;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinname = itemView.findViewById(R.id.tvCurrency);
            portfolio = itemView.findViewById(R.id.wallet_card_portfolio);
        }
    }
}
