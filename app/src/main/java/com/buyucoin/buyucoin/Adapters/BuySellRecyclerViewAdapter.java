package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.Fragments.WalletFragment;
import com.buyucoin.buyucoin.MyCustomDialogBoxClass;
import com.buyucoin.buyucoin.R;

import org.json.JSONObject;

import java.util.List;

public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;
    private final WalletFragment.OnListFragmentInteractionListener mListener;

    public BuySellRecyclerViewAdapter(List<JSONObject> items, WalletFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        String s1, s2, s3, s4;
        try{
            s1 = mValues.get(position).getString("available").toUpperCase();
            s2 = "Minimum Withdrawl: " + mValues.get(position).getString("min_with");
            s3 = mValues.get(position).getString("currencyname");
            s4 = "Fees: "+ mValues.get(position).getString("fees");
            Log.d("DATA OF COIN",mValues.get(position).toString());
        }catch(Exception e){
            s1 = s2 = s3 = s4 = "N/A";
        }
        if(!s1.equals("")){
            holder.mAddress.setText(s1);
            holder.mCurrency.setText(s3.toUpperCase());
            final String finalS = s3;
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                        MyCustomDialogBoxClass.BuyDialog(v.getContext(), finalS,position);
                    }
                }
            });

        }else{
            holder.mView.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCurrency, mAddress;
        public JSONObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mFees = (TextView) view.findViewById(R.id.tvFees);
            mCurrency = (TextView) view.findViewById(R.id.tvCurrency);
            mAddress = (TextView) view.findViewById(R.id.tvAddress);
//            mMinWith = (TextView) view.findViewById(R.id.tvMinWith);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddress.getText() + "'";
        }
    }
    }

