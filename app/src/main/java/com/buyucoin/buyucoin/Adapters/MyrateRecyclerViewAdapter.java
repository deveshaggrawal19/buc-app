package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Fragments.RateFragment.OnListFragmentInteractionListener;
import com.buyucoin.buyucoin.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyrateRecyclerViewAdapter extends RecyclerView.Adapter<MyrateRecyclerViewAdapter.ViewHolder> {

    private final JSONArray mValues;
    private final OnListFragmentInteractionListener mListener;
    Context mContext;

    public MyrateRecyclerViewAdapter(JSONArray items, OnListFragmentInteractionListener listener, Context context) {
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
            String s = mValues.getJSONObject(position).getString("currency").split("_")[0];

            holder.mItem = mValues.getJSONObject(position);
            holder.mCurrency.setText(s.toUpperCase());
            holder.mChange.setText("Change (24 Hrs): "+mValues.getJSONObject(position).getString("change"));
            holder.mLastTrade.setText("Last Trade: "+mValues.getJSONObject(position).getString("last_trade"));
            holder.mAsk.setText("Ask: "+mValues.getJSONObject(position).getString("ask"));
            holder.mBid.setText("Bid: "+mValues.getJSONObject(position).getString("bid"));

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
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBid, mChange, mLastTrade, mAsk, mCurrency;
        public final ImageView mImage;
        public JSONObject mItem;

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
