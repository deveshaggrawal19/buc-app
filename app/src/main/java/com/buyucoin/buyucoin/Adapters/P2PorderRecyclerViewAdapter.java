package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.ActiveP2pOrders;
import com.buyucoin.buyucoin.pojos.WalletCoinHorizontal;
import com.buyucoin.buyucoin.pojos.WalletCoinVertical;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class P2PorderRecyclerViewAdapter extends RecyclerView.Adapter<P2PorderRecyclerViewAdapter.P2pOrderViewHolder> {

    private ArrayList<ActiveP2pOrders> arrayList ;


    private final int P2P_ORDERS = 1;
    private final int P2P_ORDER_MATCHES = 2;
    private Context context;


    public P2PorderRecyclerViewAdapter(Context context, ArrayList<ActiveP2pOrders> activeP2pOrderslist) {
        this.context = context;
        this.arrayList = activeP2pOrderslist;




    }

    @NonNull
    @Override
    public P2pOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.active_p2p_order_item,parent,false);

        return new P2pOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull P2pOrderViewHolder holder, final int position) {
        holder.amount.setText(String.valueOf(arrayList.get(position).getAmount()));
        P2pOrderMatchesAdpater p2pOrderMatchesAdpater = new P2pOrderMatchesAdpater(arrayList.get(position).getMatched_by());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(p2pOrderMatchesAdpater);


    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class P2pOrderViewHolder  extends RecyclerView.ViewHolder{
        TextView amount;
        RecyclerView recyclerView;
        public P2pOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_amount);
            recyclerView = itemView.findViewById(R.id.p2p_active_orders_rv);
        }
    }


}
