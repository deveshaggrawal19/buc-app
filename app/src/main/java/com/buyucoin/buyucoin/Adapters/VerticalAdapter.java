package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buyucoin.buyucoin.CoinDepositeWithdraw;
import com.buyucoin.buyucoin.DepositeWithdrawActivity;
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
    public void onBindViewHolder(@NonNull final VerticalAdapter.MyViewHolder myViewHolder, int i) {
        final Context context = myViewHolder.itemView.getContext();
        myViewHolder.coinname.setText(arrayList.get(i).getCoinname());
        myViewHolder.balance.setText(arrayList.get(i).getAmount());
        int r = (int) (Math.random() * 6);
        myViewHolder.itemView.getBackground().setLevel(r);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DepositeWithdrawActivity.class);
                myViewHolder.itemView.getContext().startActivity(intent);
            }
        });
        myViewHolder.deposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CoinDepositeWithdraw.class);
                i.putExtra("type","DEPOSITE");
                context.startActivity(i);
            }
        });
        myViewHolder.withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CoinDepositeWithdraw.class);
                i.putExtra("type","WITHDRAW");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coinname,balance;
        Button deposite,withdraw;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinname = itemView.findViewById(R.id.tvCurrency);
            balance = itemView.findViewById(R.id.balance);
            deposite = itemView.findViewById(R.id.vertical_deposite_btn);
            withdraw = itemView.findViewById(R.id.vertical_withdraw_btn);

        }
    }
}

