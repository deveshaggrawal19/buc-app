package com.buyucoin.buyucoin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Interfaces.MatchedPeer;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.bottomsheets.BankDetails;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.github.mikephil.charting.formatter.IFillFormatter;

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

public class P2pOrderMatchesAdpaterDeposit extends RecyclerView.Adapter<P2pOrderMatchesAdpaterDeposit.MyViewHolder> implements MatchedPeer {
    private BuyucoinPref pref;
    private AlertDialog.Builder progressDialog;
    private JSONArray activeP2pOrders;
    private FragmentManager fragmentManager;
    private android.content.Context context;


    public P2pOrderMatchesAdpaterDeposit(JSONArray activeP2pOrders, FragmentManager fragmentManager, Context context) {
        this.activeP2pOrders = activeP2pOrders;
        this.fragmentManager = fragmentManager;
        this.context = context;
        pref = new BuyucoinPref(context);
        progressDialog = new ProgressDialog.Builder(context);
        progressDialog.setMessage("Processing");
        progressDialog.create();
    }

    public P2pOrderMatchesAdpaterDeposit() {

    }

    public static String status(String s) {
        switch (s) {
            case "DEPOSIT_ACCEPTED":
                return "Accepted";
            case "WITHDRAW_ACCEPTED":
                return "Accepted";
            case "PENDING":
                return "Pending";
            case "DISPUTE":
                return "Dispute";
            default:
                return s;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_p2p_order_matches_deposit_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {
            final JSONObject data = activeP2pOrders.getJSONObject(position);
            final Bundle bundle = new Bundle();

            if(data.getString("status").equals("WITHDRAW_COMPLETE")){
                holder.peer_success.setVisibility(View.VISIBLE);
                holder.view_layout.setVisibility(View.GONE);
            }

            if (data.has("bank")) {
                JSONObject bank = data.getJSONObject("bank");
                final String account_no = bank.getString("account");
                final String bank_name = bank.getString("bank_name");
                final String b_name = bank.getString("beneficiary");
                final String ifsc_code = bank.getString("ifsc_code");
                bundle.putString("account_no", account_no);
                bundle.putString("bank_name", bank_name);
                bundle.putString("b_name", b_name);
                bundle.putString("ifsc_code", ifsc_code);
            }
            if (data.has("tx_hash")) {
                final String tx_hash = data.getString("tx_hash");
                bundle.putString("tx_hash", tx_hash);
            } else {
                bundle.putString("tx_hash", "");

            }
            if (data.has("mode")) {
                final String mode = data.getString("mode");
                bundle.putString("mode", mode);


            }
            if (data.has("note")) {
                final String note = data.getString("note");
                bundle.putString("note", note);


            }


            final String did = String.valueOf(data.getInt("id"));
            final String wid = String.valueOf(data.getInt("key"));
            final String status = data.getString("status");

            holder.amount.setText(String.valueOf(data.getInt("vol")/10000));
            holder.id.setText(String.valueOf(data.getString("key")));
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment bankDetails = new BankDetails();

                    bundle.putString("did", did);
                    bundle.putString("wid", wid);
                    bundle.putInt("position", position);
                    bundle.putString("status",status);


                    bankDetails.setArguments(bundle);
                    bankDetails.show(fragmentManager, "fdsgfh");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return activeP2pOrders.length();
    }

    @Override
    public void refreshMatch(int Position) {
        P2pOrderMatchesAdpaterDeposit.this.notifyItemChanged(Position);
        P2pOrderMatchesAdpaterDeposit.this.notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount, id,peer_success,view_layout;
        LinearLayout details;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.p2p_order_match_amount);
            id = itemView.findViewById(R.id.p2p_order_match_id);
            details = itemView.findViewById(R.id.p2p_order_match_details_layout);
            peer_success = itemView.findViewById(R.id.peer_success);
            view_layout = itemView.findViewById(R.id.view_layout_display);
        }
    }


}
