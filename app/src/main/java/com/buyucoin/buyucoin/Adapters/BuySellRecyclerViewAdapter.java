package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.BuySellActivity;
import com.buyucoin.buyucoin.Fragments.WalletFragment;
import com.buyucoin.buyucoin.MyCustomDialogBoxClass;
import com.buyucoin.buyucoin.R;

import org.json.JSONObject;

import java.util.List;

public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;
    private final WalletFragment.OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    TextView textView;

    public BuySellRecyclerViewAdapter(List<JSONObject> items, WalletFragment.OnListFragmentInteractionListener listener,RecyclerView recyclerView,TextView textView) {
        mValues = items;
        mListener = listener;
        this.recyclerView = recyclerView;
        this.textView = textView;
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

            final String finalS = s3;
        holder.coinname.setText(s3);
        if(position<34){
        holder.coinimg.setImageResource(MyCustomDialogBoxClass.arr[position]);
        }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {

                        Intent buysellIntent = new Intent(holder.mView.getContext(),BuySellActivity.class);
                        holder.mView.getContext().startActivity(buysellIntent);

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
        public final TextView coinname ;
        public  final ImageView coinimg;
        public JSONObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            coinimg = view.findViewById(R.id.coinimg);
            coinname = view.findViewById(R.id.coin_name);
        }


    }
    }

