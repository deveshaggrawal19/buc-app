package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.Bids;

import java.util.ArrayList;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.BidsViewHolder> {

    private Context context;
    private ArrayList<Bids> bidsArrayList;

    public BidsAdapter(Context context, ArrayList<Bids> bidsArrayList) {
        this.context = context;
        this.bidsArrayList = bidsArrayList;
    }



    @NonNull
    @Override
    public BidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bids_item,parent,false);
        return new BidsAdapter.BidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidsViewHolder holder, int position) {
        holder.price.setText(String.valueOf(bidsArrayList.get(position).getBid_price()));
        holder.value.setText(String.valueOf(bidsArrayList.get(position).getBid_value()));
        holder.vol.setText(String.valueOf(bidsArrayList.get(position).getBid_volume()));

    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public class BidsViewHolder extends RecyclerView.ViewHolder {
        TextView price,value,vol;
        View progress;
        public BidsViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.item_price);
            value = itemView.findViewById(R.id.item_value);
            vol = itemView.findViewById(R.id.item_vol);
        }
    }
}
