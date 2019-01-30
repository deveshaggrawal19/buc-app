package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.PeerHistoryWithdraw;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class P2P_Deposite_History_Fragment extends Fragment {
    LinearLayout layout;
    ProgressBar progressBar;
    ArrayList<PeerHistoryWithdraw> peerHistoryWithdrawArrayList;
    RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p2p_order_deposites_history,container,false);
        layout = view.findViewById(R.id.ll_empty);
        progressBar = view.findViewById(R.id.progress_circular_d);
        peerHistoryWithdrawArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.peerDepositRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getActiveOrders();
        return view;
    }

    private void getActiveOrders() {
        peerHistoryWithdrawArrayList.clear();
        OkHttpHandler.auth_get("peer", new BuyucoinPref(Objects.requireNonNull(getContext())).getPrefString(BuyucoinPref.ACCESS_TOKEN), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ERROR ON PEER =====>", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    assert response.body() != null;
                    String s = response.body().string();
                    JSONObject jsonObject = new JSONObject(s);
                    final JSONArray deposite = jsonObject.getJSONArray("deposits");
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(deposite.length()>0){
                                progressBar.setVisibility(View.GONE);
                                //                        layout.setVisibility(View.VISIBLE);

                                for(int i = 0;i<deposite.length();i++){
                                    try {
                                        JSONObject j = deposite.getJSONObject(i);
                                        if(j.getInt("status")>1) {
                                            PeerHistoryWithdraw p = new PeerHistoryWithdraw();
                                            p.setAmount(j.getInt("amount"));
                                            p.setBoost(j.getInt("boost"));
                                            p.setCurrency(j.getInt("currency"));
                                            p.setDuration(j.getInt("duration"));
                                            p.setEnd_timestamp(j.getString("end_timestamp"));
                                            p.setFee(j.getInt("fee"));
                                            p.setFilled(j.getInt("filled"));
                                            p.setFilled_by(j.getString("filled_by"));
                                            p.setId(j.getInt("id"));
                                            p.setMatched(j.getInt("matched"));
                                            p.setMatches(j.get("matches"));
                                            p.setMin_amount(j.getInt("min_amount"));
                                            p.setModes(j.getJSONArray("modes"));
                                            p.setNote(j.getString("note"));
                                            p.setPending(j.getBoolean("pending"));
                                            p.setRejected_match(j.get("rejected_match"));
                                            p.setStatus(j.getInt("status"));
                                            p.setTx_hash(j.getString("tx_hash"));
                                            p.setTimestamp(j.getString("timestamp"));
                                            p.setUser_id(j.getInt("user_id"));
                                            p.setWallet_id(j.getInt("wallet_id"));
                                            p.setWfee_amount(j.getInt("wfee_amount"));
                                            peerHistoryWithdrawArrayList.add(p);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                recyclerView.setAdapter(new PeerHistoryDepositAdapter(getContext(),peerHistoryWithdrawArrayList));
                                recyclerView.setVisibility(View.VISIBLE);
                                Log.d("======djnfu========>",String.valueOf(peerHistoryWithdrawArrayList.size()));

                            }else{
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }

                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private static class PeerHistoryDepositAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<PeerHistoryWithdraw> arrayList;
        private Context context;

        public PeerHistoryDepositAdapter(Context context,ArrayList<PeerHistoryWithdraw> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.peer_history_list_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.mCurrency.setText(String.valueOf(arrayList.get(position).getId()));
            holder.mAmount.setText(String.valueOf(arrayList.get(position).getAmount()));
            holder.mOpenTime.setText(arrayList.get(position).getTimestamp());
            holder.mStatus.setText(numToStatus(arrayList.get(position).getStatus()));


        }

        public String numToStatus(int s){
            switch (s){
                case 1:return "Pending";
                case 2:return "Processing";
                case 3:return "Success";
                case 4:return "Cancelled";
                case 5:return "Dispute";
                default:return "";
            }
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mAmount, mCurrency, mOpenTime,mStatus;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAmount = view.findViewById(R.id.tvHistoryAmount);
            mCurrency = view.findViewById(R.id.tvHistoryCurrency);
            mOpenTime = view.findViewById(R.id.tvHistoryOpenTime);
            mStatus = view.findViewById(R.id.tvHistoryStatus);
        }

    }
}
