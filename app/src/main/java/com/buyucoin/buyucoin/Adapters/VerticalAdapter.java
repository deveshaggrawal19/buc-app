package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buyucoin.buyucoin.CoinDepositeWithdraw;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.DepositeWithdrawActivity;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.myinterfaces.InrToP2P;
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
    public void onBindViewHolder(@NonNull final VerticalAdapter.MyViewHolder myViewHolder, final int i) {

        final Context context = myViewHolder.itemView.getContext();

        final String coin_name,availabel,address,description,tag,full_coin_name,porfolio,pending;

        coin_name = arrayList.get(i).getCoinname();
        availabel =  arrayList.get(i).getAvailabel();
        address =  arrayList.get(i).getAddress();
        description =  arrayList.get(i).getDescription();
        tag =   arrayList.get(i).getTag();
        full_coin_name =  arrayList.get(i).getFull_coin_name();
        porfolio = arrayList.get(i).getPortfolio();
        pending = arrayList.get(i).getPending();


        myViewHolder.coinname.setText(coin_name.toUpperCase());
        myViewHolder.balance.setText(availabel);
        myViewHolder.portfolio.setText(porfolio);
        myViewHolder.pending.setText(pending);

        int r = (int) (Math.random() * 6);

        myViewHolder.itemView.getBackground().setLevel(r);



        if(coin_name.equals("inr")){
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,DepositeWithdrawActivity.class);

                    intent.putExtra("coin_name",coin_name);
                    intent.putExtra("available",availabel);
                    intent.putExtra("address",address);
                    intent.putExtra("description",description);
                    intent.putExtra("tag",tag);
                    intent.putExtra("full_coin_name",full_coin_name);

                    myViewHolder.itemView.getContext().startActivity(intent);
                }
            });
        }

        if(coin_name.equals("inr")){
            myViewHolder.deposite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else{
            myViewHolder.deposite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CoinDepositeWithdraw.class);
                    intent.putExtra("type","DEPOSITE");
                    intent.putExtra("coin_name",coin_name);
                    intent.putExtra("available",availabel);
                    intent.putExtra("address",address);
                    intent.putExtra("description",description);
                    intent.putExtra("tag",tag);
                    intent.putExtra("full_coin_name",full_coin_name);
                    context.startActivity(intent);
                }
            });

        }


        if(coin_name.equals("inr")){
            myViewHolder.withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else{
            myViewHolder.withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CoinDepositeWithdraw.class);
                    intent.putExtra("type","WITHDRAW");
                    intent.putExtra("coin_name",coin_name);
                    intent.putExtra("available",availabel);
                    intent.putExtra("address",address);
                    intent.putExtra("description",description);
                    intent.putExtra("tag",tag);
                    intent.putExtra("full_coin_name",full_coin_name);
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView coinname,balance,portfolio,pending;
        Button deposite,withdraw;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinname = itemView.findViewById(R.id.tvCurrency);
            balance = itemView.findViewById(R.id.balance);
            pending = itemView.findViewById(R.id.wallet_card_pending);
            portfolio = itemView.findViewById(R.id.wallet_card_portfolio);

            deposite = itemView.findViewById(R.id.vertical_deposite_btn);
            withdraw = itemView.findViewById(R.id.vertical_withdraw_btn);

        }
    }
}

