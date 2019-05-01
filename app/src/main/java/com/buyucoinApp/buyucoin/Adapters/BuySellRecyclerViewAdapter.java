package com.buyucoinApp.buyucoin.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.CurrencyActivity;
import com.buyucoinApp.buyucoin.MyResourcesClass;
import com.buyucoinApp.buyucoin.OTCActivity;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pojos.BuySellData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.ViewHolder> {

    private final List<BuySellData> mValues;


    public BuySellRecyclerViewAdapter(ArrayList<BuySellData> items) {
        this.mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_sell_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.coinname.setText(mValues.get(position).getName());
        try {
            holder.coinimg.setImageResource(MyResourcesClass.COIN_ICON.getInt(mValues.get(position).getCoin()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent currencyIntent = new Intent(holder.mView.getContext(), OTCActivity.class);
                currencyIntent.putExtra("currency", mValues.get(position).getCoin());
                holder.mView.getContext().startActivity(currencyIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView coinname;
        private final ImageView coinimg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            coinimg = view.findViewById(R.id.coinimg);
            coinname = view.findViewById(R.id.coin_name);
        }


    }
}

