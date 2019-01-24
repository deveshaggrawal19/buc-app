package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.CurrencyActivity;
import com.buyucoin.buyucoin.MyResourcesClass;
import com.buyucoin.buyucoin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;

    private ArrayList<String> buysell_list = new ArrayList<>();

    public BuySellRecyclerViewAdapter(List<JSONObject> items) {
        mValues = items;
        try {
            for (JSONObject j : mValues) {
                buysell_list.add(j.getString("currencyname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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


        holder.coinname.setText(buysell_list.get(position).toUpperCase());
        try {
            holder.coinimg.setImageResource(MyResourcesClass.COIN_ICON.getInt(buysell_list.get(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent currencyIntent = new Intent(holder.mView.getContext(), CurrencyActivity.class);
                currencyIntent.putExtra("currency", buysell_list.get(position));
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
        public final TextView coinname;
        public final ImageView coinimg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            coinimg = view.findViewById(R.id.coinimg);
            coinname = view.findViewById(R.id.coin_name);
        }


    }
}

