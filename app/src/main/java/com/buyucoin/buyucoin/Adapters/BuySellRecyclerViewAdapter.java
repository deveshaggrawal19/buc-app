package com.buyucoin.buyucoin.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.BuySellActivity;
import com.buyucoin.buyucoin.CurrencyActivity;
import com.buyucoin.buyucoin.MyCustomDialogBoxClass;
import com.buyucoin.buyucoin.R;

import org.json.JSONObject;

import java.util.List;

public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;
    RecyclerView recyclerView;
    TextView textView;
    String s1, s2, s3, s4;
    private String availabel,min_with,currencyname,fees;

    public BuySellRecyclerViewAdapter(List<JSONObject> items, RecyclerView recyclerView, TextView textView) {
        mValues = items;
        this.recyclerView = recyclerView;
        this.textView = textView;
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
        holder.mItem = mValues.get(position);


        try{
            availabel = mValues.get(position).getString("available").toUpperCase();
            min_with = mValues.get(position).getString("min_with");
            currencyname = mValues.get(position).getString("currencyname");
            fees = mValues.get(position).getString("fees");
            Log.d("DATA OF COIN",mValues.get(position).toString());
        }catch(Exception e){
            availabel = min_with = currencyname = fees = "";
        }

        holder.coinname.setText(currencyname.toUpperCase());
        if(position<34){
        holder.coinimg.setImageResource(MyCustomDialogBoxClass.arr[position]);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        Intent currencyIntent = new Intent(holder.mView.getContext(), CurrencyActivity.class);
                        currencyIntent.putExtra("currency", currencyname);
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

