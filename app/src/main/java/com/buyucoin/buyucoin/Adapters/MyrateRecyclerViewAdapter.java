package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.CurrencyActivity;
import com.buyucoin.buyucoin.DataClasses.Rates;
import com.buyucoin.buyucoin.MyResourcesClass;
import com.buyucoin.buyucoin.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class MyrateRecyclerViewAdapter extends RecyclerView.Adapter<MyrateRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Rates> mValues;

    Context mContext;

    public MyrateRecyclerViewAdapter(ArrayList<Rates> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            String s = mValues.get(position).currency.split("_")[0];
            DecimalFormat format = new DecimalFormat("0.########");
            holder.mItem = mValues.get(position);
            holder.mCurrency.setText(s.toUpperCase());

            holder.mLastTrade.setText(format.format(Double.parseDouble(mValues.get(position).last_trade)));


            holder.mAsk.setText(format.format(Double.parseDouble(mValues.get(position).ask)));
            holder.mBid.setText(format.format(Double.parseDouble(mValues.get(position).bid)));

            holder.mImage.setImageResource(MyResourcesClass.COIN_ICON.getInt(s));

        }catch(Exception e){
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                        Bundle bundle = new Bundle();
                                bundle.putString("bid", holder.mItem.bid);
                                bundle.putString("ask", holder.mItem.ask);
                                bundle.putString("change", holder.mItem.change);
                                bundle.putString("last_trade", holder.mItem.last_trade);
                                bundle.putString("currency", holder.mItem.currency);
                                bundle.putString("high_24", "");
                Intent intent = new Intent(holder.mView.getContext(),CurrencyActivity.class);
                intent.putExtras(bundle);
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    public String removeZeros(String s){
        int dot = s.indexOf('.');
        int zero = s.indexOf('0',dot);
        return s.substring(0,zero+1);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBid, mLastTrade, mAsk, mCurrency;
        public final ImageView mImage;
        public Rates mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBid = (TextView) view.findViewById(R.id.tvRateBid);

            mLastTrade = (TextView) view.findViewById(R.id.tvRateLastTrade);
            mAsk = (TextView) view.findViewById(R.id.tvRateAsk);
            mCurrency = (TextView) view.findViewById(R.id.tvRateCurrency);
            mImage = (ImageView) view.findViewById(R.id.ivRate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
