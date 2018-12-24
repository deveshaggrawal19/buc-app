package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.WalletCoinVertical;

import java.util.ArrayList;

class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

    private ArrayList<WalletCoinVertical> arrayList;

    VerticalAdapter(ArrayList<WalletCoinVertical> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VerticalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_single_row,viewGroup,false);
        return new VerticalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.coinname.setText(arrayList.get(i).getCoinname());
        myViewHolder.balance.setText(arrayList.get(i).getAmount());
        int r = (int) (Math.random() * 6);
        myViewHolder.itemView.getBackground().setLevel(r);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coinname,balance;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinname = itemView.findViewById(R.id.tvCurrency);
            balance = itemView.findViewById(R.id.balance);

        }
    }
}

