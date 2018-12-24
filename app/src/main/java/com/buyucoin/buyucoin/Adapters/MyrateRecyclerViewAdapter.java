package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.DataClasses.Rates;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Fragments.RateFragment.OnListFragmentInteractionListener;
import com.buyucoin.buyucoin.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyrateRecyclerViewAdapter extends RecyclerView.Adapter<MyrateRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Rates> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context mContext;

    public MyrateRecyclerViewAdapter(ArrayList<Rates> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
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

            holder.mItem = mValues.get(position);
            holder.mCurrency.setText(s.toUpperCase());
            holder.mChange.setText("Change (24 Hrs): "+mValues.get(position).change);
            holder.mLastTrade.setText("Last Trade: "+mValues.get(position).last_trade);
            holder.mAsk.setText("Ask: "+mValues.get(position).ask);
            holder.mBid.setText("Bid: "+mValues.get(position).bid);

            holder.mImage.setImageDrawable(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(s, "drawable", mContext.getPackageName())));

        }catch(Exception e){
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    try {
                        mListener.onListFragmentInteraction(new JSONObject()
                                .put("bid", holder.mItem.bid)
                                .put("ask", holder.mItem.ask)
                                .put("change", holder.mItem.change)
                                .put("last_trade", holder.mItem.last_trade)
                                .put("currency", holder.mItem.currency)
                                .put("high_24", "")
                        );
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBid, mChange, mLastTrade, mAsk, mCurrency;
        public final ImageView mImage;
        public Rates mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBid = (TextView) view.findViewById(R.id.tvRateBid);
            mChange = (TextView) view.findViewById(R.id.tvRateChange);
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
