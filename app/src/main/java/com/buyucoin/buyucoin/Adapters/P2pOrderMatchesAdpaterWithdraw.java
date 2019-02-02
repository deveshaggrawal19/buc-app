package com.buyucoin.buyucoin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.bottomsheets.BankDetails;
import com.buyucoin.buyucoin.bottomsheets.WithdrawDetails;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapterDeposit.issuccess;

public class P2pOrderMatchesAdpaterWithdraw extends RecyclerView.Adapter<P2pOrderMatchesAdpaterWithdraw.MyViewHolder> implements MatchedPeer {
    private BuyucoinPref pref;
    private AlertDialog.Builder progressDialog;
    private JSONArray activeP2pOrders;
    private FragmentManager fragmentManager;
    private Context context;
    private Activity activity;
    private static String mainmsg = "";


    public P2pOrderMatchesAdpaterWithdraw(JSONArray activeP2pOrders, FragmentManager fragmentManager, Context context,Activity activity) {
        this.activeP2pOrders = activeP2pOrders;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.activity = activity;
        pref = new BuyucoinPref(context);
        progressDialog = new ProgressDialog.Builder(context);
        progressDialog.setMessage("Processing");
        progressDialog.create();
    }
    public P2pOrderMatchesAdpaterWithdraw() {

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_withdraw_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {
        final JSONObject data = activeP2pOrders.getJSONObject(position);
            Log.d("MATCH PEER DATA",data.toString());
            final Bundle bundle = new Bundle();

//            if(data.has("bank")){
//                JSONObject bank = data.getJSONObject("bank");
//                final String account_no = bank.getString("account");
//                final String bank_name = bank.getString("bank_name");
//                final String b_name = bank.getString("beneficiary");
//                final String ifsc_code = bank.getString("ifsc_code");
//                bundle.putString("account_no",account_no);
//                bundle.putString("bank_name",bank_name);
//                bundle.putString("b_name",b_name);
//                bundle.putString("ifsc_code",ifsc_code);
//            }
            if(data.has("tx_hash")){
                final String tx_hash = data.getString("tx_hash");
                bundle.putString("tx_hash",tx_hash);
            }
            else{
                bundle.putString("tx_hash","");

            }
//            if(data.has("mode")){
//                final String mode = data.getString("mode");
//                bundle.putString("mode",mode);
//
//
//            }
//            if(data.has("note")){
//                final String note = data.getString("note");
//                bundle.putString("note",note);
//
//
//            }


            final String did = String.valueOf(data.getInt("id"));
            final String wid = String.valueOf(data.getInt("key"));
            final String status = data.getString("status");



            holder.amount.setText(String.valueOf(data.getInt("vol")/10000));
            holder.id.setText(String.valueOf(data.getString("key")));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   DialogFragment withdrawDetails = new WithdrawDetails();


                   bundle.putString("did",did);
                   bundle.putString("wid",wid);
                   bundle.putInt("position",position);
                   bundle.putString("status",status);

                   withdrawDetails.setArguments(bundle);
                   withdrawDetails.show(fragmentManager,"fdsgfh");
                }
            });

//            if(data.getString("status").equals("DEPOSIT_ACCEPTED")){
//                holder.action.setVisibility(View.VISIBLE);
//                holder.dispute.setVisibility(View.VISIBLE);
//                Log.d("DEPOSIT_ACCEPTED",data.getString("status"));
//
//            }
//            if(data.getString("status").equals("DISPUTE")){
//                holder.action.setVisibility(View.VISIBLE);
//                holder.dispute.setVisibility(View.GONE);
//                Log.d("DEPOSIT_ACCEPTED",data.getString("status"));
//
//            }
            if(data.getString("status").equals("WITHDRAW_COMPLETE")){
                holder.action.setVisibility(View.GONE);
                holder.dispute.setVisibility(View.GONE);
                holder.successful.setVisibility(View.VISIBLE);
                holder.details.setVisibility(View.GONE);
                Log.d("DEPOSIT_ACCEPTED",data.getString("status"));

            }
//            holder.action.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   completeAction("peer_withdraw_complete",wid,did,position,"mark as complete");
//                }
//            });
//            holder.dispute.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    completeAction("peer_withdraw_dispute",wid,did,position,"raise dispute");
//                }
//            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void completeAction(String action, String wid, String did, final int position,String msg){
//
//        try {
//            Toast.makeText(context,did+" "+wid, Toast.LENGTH_SHORT).show();
//            final JSONObject object = new JSONObject();
//            object.put("method", action)
//                    .put("deposit_id", wid).put("withdraw_id", did);
//
//            Log.d("ooooooooooooooooooooo", "onClick: "+object.toString());
//            new AlertDialog.Builder(context).setMessage("Are you sure to "+msg)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ProgressDialog p = new ProgressDialog(context);
//                            p.setMessage("processing");
//                            p.show();
//                            boolean b = peerAction(object.toString());
//                            if(b){
//
//                                activeP2pOrders.remove(position);
//                                notifyItemRemoved(position);
//                                notifyDataSetChanged();
//                                p.dismiss();
//                                dialog.dismiss();
//                            }else{
//                                new CoustomToast(context,activity,mainmsg,CoustomToast.TYPE_DANGER).showToast();
//                                dialog.dismiss();
//                            }
//                        }
//                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).create().show();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    public static String status(String s){
        switch (s){
            case "DEPOSIT_ACCEPTED":return "Accepted";
            case "WITHDRAW_ACCEPTED":return "Accepted";
            case "PENDING":return "Pending";
            case "DISPUTE":return "Dispute";
            default: return s;
        }
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
                assert response.body() != null;
                String s = response.body().string();
                Log.d("ssssssssssssssssss", "onResponse: "+s);

                try {
                    JSONObject j = new JSONObject(s);
                    final String _mainmsg = j.getJSONArray("message").getJSONArray(0).getString(0);

                    Log.d(context.getPackageName()+"===>", "onResponse:"+j.toString());
                    if(j.has("status")){
                    final String status = j.getString("status");
                        switch (status){
                            case "success":
                                issuccess=true;mainmsg  = _mainmsg;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new CoustomToast(context, activity,_mainmsg,CoustomToast.TYPE_SUCCESS).showToast();
                                    }
                                });
                                break;
                            case "error":issuccess=false;
                            mainmsg  = _mainmsg;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new CoustomToast(context, activity,_mainmsg,CoustomToast.TYPE_DANGER).showToast();
                                    }
                                });
                            break;
                        }
                    }
                    if(j.has("success")){
                        if(j.getBoolean("success"))issuccess=true;mainmsg  = _mainmsg;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CoustomToast(context, activity,mainmsg,CoustomToast.TYPE_SUCCESS).showToast();
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return issuccess;
    }

    @Override
    public int getItemCount() {
        return activeP2pOrders.length();
    }

    @Override
    public void refreshMatch(int Position) {
        if(activeP2pOrders!=null){
        activeP2pOrders.remove(Position);
        notifyItemRemoved(Position);
        notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount,id,details,action,dispute,successful;
         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_match_amount);
            id = itemView.findViewById(R.id.p2p_order_match_id);
            details = itemView.findViewById(R.id.p2p_order_match_details);
            action = itemView.findViewById(R.id.mark_complete);
            dispute = itemView.findViewById(R.id.mark_dispute);
            successful = itemView.findViewById(R.id.peer_success);
        }
    }



}
