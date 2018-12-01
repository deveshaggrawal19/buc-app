package com.buyucoin.buyucoin.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Fragments.WalletFragment.OnListFragmentInteractionListener;
import com.buyucoin.buyucoin.dummy.DummyContent.DummyItem;

import org.json.JSONObject;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<JSONObject> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String s1, s2, s3, s4;
        try{
            s1 = "Address: "+ mValues.get(position).getString("address").toUpperCase();
            s2 = "Minimum Withdrawl: " + mValues.get(position).getString("min_with");
            s3 = mValues.get(position).getString("currencyname");
            s4 = "Fees: "+ mValues.get(position).getString("fees");
        }catch(Exception e){
            s1 = s2 = s3 = s4 = "N/A";
        }
        holder.mAddress.setText(s1);
        holder.mMinWith.setText(s2);
        holder.mCurrency.setText(s3);
        holder.mFees.setText(s3);
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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCurrency, mFees, mAddress, mMinWith;
        public JSONObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFees = (TextView) view.findViewById(R.id.tvFees);
            mCurrency = (TextView) view.findViewById(R.id.tvCurrency);
            mAddress = (TextView) view.findViewById(R.id.tvAddress);
            mMinWith = (TextView) view.findViewById(R.id.tvMinWith);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddress.getText() + "'";
        }
    }
}
