package com.buyucoinApp.buyucoin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.CurrencyActivity;
import com.buyucoinApp.buyucoin.DataClasses.Rates;
import com.buyucoinApp.buyucoin.MyResourcesClass;
import com.buyucoinApp.buyucoin.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class MyrateRecyclerViewAdapter extends RecyclerView.Adapter<MyrateRecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Rates> mValues;
    private ArrayList<Rates> filtered;

    Context mContext;

    public MyrateRecyclerViewAdapter(ArrayList<Rates> items, Context context) {
        filtered = items;
        mContext = context;
        mValues = filtered;
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



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString();
                if(searchString.isEmpty()){
                    mValues = filtered;
                }
                else{
                    ArrayList<Rates> resultList = new ArrayList<>();
                    for(Rates item : filtered){
                        try {
                            String full_currency_name = MyResourcesClass.COIN_FULL_NAME.getString(item.currency).toLowerCase();
                            String currency = item.currency.toLowerCase();
                            if (currency.toLowerCase().contains(searchString.toLowerCase()) || full_currency_name.contains(searchString.toLowerCase()) ){
                                resultList.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mValues = resultList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mValues;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mValues = (ArrayList<Rates>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBid, mLastTrade, mAsk, mCurrency;
        public final ImageView mImage;
        public Rates mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBid = view.findViewById(R.id.tvRateBid);

            mLastTrade = view.findViewById(R.id.tvRateLastTrade);
            mAsk = view.findViewById(R.id.tvRateAsk);
            mCurrency = view.findViewById(R.id.tvRateCurrency);
            mImage = view.findViewById(R.id.ivRate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
