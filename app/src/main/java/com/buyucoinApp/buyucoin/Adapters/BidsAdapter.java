package com.buyucoinApp.buyucoin.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pojos.Bids;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.BidsViewHolder> {

    private Context context;
    private List<Bids> bidsArrayList;

    public BidsAdapter(Context context, ArrayList<Bids> bids) {
        this.context = context;
        if (bids.size() > 10) {
            this.bidsArrayList = bids.subList(0,10);
        }else{
            this.bidsArrayList = bids;
        }
        Collections.reverse(this.bidsArrayList);

    }


    @NonNull
    @Override
    public BidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bids_item, parent, false);
        return new BidsAdapter.BidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidsViewHolder holder, int position) {

        DecimalFormat format2 = new DecimalFormat("0.####");
        DecimalFormat format = new DecimalFormat("0.########");

        holder.price.setText(String.valueOf(format2.format(bidsArrayList.get(position).getBid_price())));
        holder.value.setText(String.valueOf(format.format(Double.parseDouble(bidsArrayList.get(position).getBid_value()))));

        String v1 = format.format(bidsArrayList.get(position).getBid_volume());

        holder.vol.setText(String.valueOf(v1).trim());
//        if (position % 2 == 0) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
//        }

    }


    @Override
    public int getItemCount() {
        return (bidsArrayList.size()<=10)?bidsArrayList.size():10;
//        return bidsArrayList.size();
    }

    public class BidsViewHolder extends RecyclerView.ViewHolder {
        TextView price, value, vol;
        View progress;

        public BidsViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            value = itemView.findViewById(R.id.item_value);
            vol = itemView.findViewById(R.id.item_vol);
        }
    }
}
