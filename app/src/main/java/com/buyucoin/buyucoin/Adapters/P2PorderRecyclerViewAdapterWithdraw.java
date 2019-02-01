package com.buyucoin.buyucoin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pojos.ActiveP2pOrders;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class P2PorderRecyclerViewAdapterWithdraw extends RecyclerView.Adapter<P2PorderRecyclerViewAdapterWithdraw.P2pOrderViewHolder> implements MatchedPeer {

    private ArrayList<ActiveP2pOrders> arrayList ;
    private Context context;
    private FragmentManager fragmentManager;
    BuyucoinPref pref;
    static boolean issuccess = true;
    AlertDialog.Builder progressDialog;
    Activity activity;


    public P2PorderRecyclerViewAdapterWithdraw(Context context, ArrayList<ActiveP2pOrders> activeP2pOrderslist, FragmentManager childFragmentManager,Activity activity) {
        this.context = context;
        this.arrayList = activeP2pOrderslist;
        this.activity = activity;
        fragmentManager = childFragmentManager;
        pref = new BuyucoinPref(context);
        progressDialog = new ProgressDialog.Builder(context);
        progressDialog.setMessage("Processing");
        progressDialog.create();

    }

    public P2PorderRecyclerViewAdapterWithdraw() {
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
    public void onBindViewHolder(@NonNull final P2pOrderViewHolder holder, final int position) {
        final String id = String.valueOf(arrayList.get(position).getId());
        holder.amount.setText(String.valueOf(arrayList.get(position).getAmount()));
        holder.peer_order_id.setText(id);
        P2pOrderMatchesAdpaterWithdraw p2pOrderMatchesAdpaterWithdraw = new P2pOrderMatchesAdpaterWithdraw(arrayList.get(position).getMatched_by(),fragmentManager,context,activity);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(p2pOrderMatchesAdpaterWithdraw);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.p2p_active_orders_layout.getVisibility()==View.GONE){
                    holder.p2p_active_orders_layout.setVisibility(View.VISIBLE);
                }else{
                    holder.p2p_active_orders_layout.setVisibility(View.GONE);
                }
            }
        });
        holder.cancel_peer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject object = new JSONObject();
                try {
                    object.put("method","peer_withdraw_cancel")
                            .put("withdraw_id",id);
                    new AlertDialog.Builder(context).setMessage("Do you want to delete this peer")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean b = peerAction(object.toString());
                                    if(b){
                                        new CoustomToast(context,(Dashboard)context,"Deleted Successfully",CoustomToast.TYPE_SUCCESS).showToast();
                                        dialog.dismiss();
                                        arrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                    }else{
                                        new CoustomToast(context,(Dashboard)context,"Error While Deleting...",CoustomToast.TYPE_DANGER).showToast();
                                        dialog.dismiss();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public boolean peerAction(String s){

        OkHttpHandler.auth_post("peer_action", pref.getPrefString(BuyucoinPref.ACCESS_TOKEN), s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("PEER ACTION RESPONSE","FAILED");
                issuccess = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    assert response.body() != null;
                    String res = response.body().string();
                    JSONObject j = new JSONObject(res);
                    Log.d("PEER ACTION RESPONSE",j.toString());
                    if(j.getBoolean("success")){
                        issuccess = true;
                    }else{
                        issuccess = false;
                    }
                    issuccess = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return issuccess;

    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void refreshMatch(int position) {
//        if(arrayList!=null){
//            arrayList.get(position).getMatched_by().remove(position);
//            notifyItemRemoved(position);
//        }
        Log.d("POSITION",String.valueOf(position));

    }

    class P2pOrderViewHolder  extends RecyclerView.ViewHolder{
        TextView amount,peer_order_id,progress_text;
        RecyclerView recyclerView;
        LinearLayout p2p_active_orders_layout,progress_layout;
        Button cancel_peer_btn;
        public P2pOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_amount);
            recyclerView = itemView.findViewById(R.id.p2p_active_orders_rv);
            p2p_active_orders_layout = itemView.findViewById(R.id.p2p_active_orders_layout);
            cancel_peer_btn = itemView.findViewById(R.id.cancel_peer_order_btn);
            peer_order_id = itemView.findViewById(R.id.peer_order_id);
            progress_layout = itemView.findViewById(R.id.progress_layout);
            progress_text = itemView.findViewById(R.id.progress_text);

        }
    }



}
