package com.buyucoinApp.buyucoin.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoinApp.buyucoin.Fragments.AccountFragment;
import com.buyucoinApp.buyucoin.MyResourcesClass;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.bottomsheets.HistoryBottomsheet;
import com.buyucoinApp.buyucoin.pojos.History;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyHistoryWithdrawRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryWithdrawRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<History> mValues;
    Context context;
    private FragmentManager fragmentManager;

    public MyHistoryWithdrawRecyclerViewAdapter(ArrayList<History> items, Context context, FragmentManager fragmentManager) {
        mValues = items;
        this.context = context;
        this.fragmentManager = fragmentManager;
        Collections.reverse(mValues);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            holder.mItem = mValues.get(position);



            holder.mCurrency.setText(holder.mItem.getCurr().toUpperCase());
            holder.mAmount.setText(String.valueOf(holder.mItem.getAmount()));
            if(!holder.mItem.getOpen().equals("")){

                holder.mOpenTime.setText(holder.mItem.getOpen());
            }else{

                holder.mOpenTime.setText(holder.mItem.getOpen_time());
            }
            holder.mTxHash.setText(holder.mItem.getTx_hash());

            holder.mType.setImageResource(R.drawable.history_withdraw_icon);


            switch (holder.mItem.getStatus()){
                case "Pending":
                    holder.mStatus.setVisibility(View.GONE);
                    holder.cancel_btn.setVisibility(View.VISIBLE);
                    break;
                case "Success":
                    holder.mStatus.setText(holder.mItem.getStatus());
                    holder.mStatus.setTextColor(context.getResources().getColor(R.color.kyc_color));

                    break;
                case "Cancelled":
                    holder.mStatus.setText(holder.mItem.getStatus());
                    holder.mStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
                    break;
                case "Failed":
                    holder.cancel_btn.setVisibility(View.GONE);
                    holder.mStatus.setVisibility(View.VISIBLE);
                    holder.mStatus.setText(holder.mItem.getStatus());
                    holder.mStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
                    break;
                default:
                    holder.mStatus.setVisibility(View.GONE);
                    holder.cancel_btn.setVisibility(View.VISIBLE);

            }

            holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,""+holder.mItem.getId(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            holder.mCurrency.setText("N/A");
            holder.mAmount.setText("N/A");
            holder.mOpenTime.setText("N/A");
            holder.mTxHash.setText("N/A");
        }

        try {
            holder.mImage.setImageResource(MyResourcesClass.COIN_ICON.getInt(holder.mItem.getCurr()));

        } catch (Exception e) {
            holder.mImage.setVisibility(View.GONE);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
//                        mListener.onListFragmentInteraction(holder.mItem);
                HistoryBottomsheet historyBottomsheet = new HistoryBottomsheet();
                Bundle b = new Bundle();
                b.putString("history_amount",String.valueOf(holder.mItem.getAmount()));
                b.putString("history_currency",holder.mItem.getCurr());
                if(!holder.mItem.getOpen().equals("")){

                    b.putString("history_time",holder.mItem.getOpen());
                }else{

                    b.putString("history_time",holder.mItem.getOpen_time());
                }
                b.putString("history_status",holder.mItem.getStatus());
                b.putString("history_tx_hash",holder.mItem.getTx_hash());
                b.putString("history_address",holder.mItem.getAddress());
                b.putString("history_fees",String.valueOf(holder.mItem.getFee()));
                b.putString("history_filled",String.valueOf(holder.mItem.getFilled()));
                b.putString("history_price",String.valueOf(holder.mItem.getPrice()));
                b.putString("history_type",String.valueOf(holder.mItem.getType()));
                b.putString("history_value",String.valueOf(holder.mItem.getValue()));

                historyBottomsheet.setArguments(b);

                historyBottomsheet.show(fragmentManager, "HISTORY");
                AccountFragment.makeViewDisable(holder.mView);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mAmount;
        private final TextView mCurrency;
        private final TextView mOpenTime;
        private final TextView mTxHash;
        private final TextView mStatus;
        private final ImageView mImage,mType;
        private History mItem;
        private Button cancel_btn;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAmount = view.findViewById(R.id.tvHistoryAmount);
            mCurrency = view.findViewById(R.id.tvHistoryCurrency);
            mOpenTime = view.findViewById(R.id.tvHistoryOpenTime);
            mTxHash = view.findViewById(R.id.tvHistoryTxHash);
            mStatus = view.findViewById(R.id.tvHistoryStatus);
            mImage = view.findViewById(R.id.ivHistory);
            mType = view.findViewById(R.id.history_type_image);
            cancel_btn = view.findViewById(R.id.cancel_pending_order);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}

