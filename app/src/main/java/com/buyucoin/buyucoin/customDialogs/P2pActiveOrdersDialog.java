package com.buyucoin.buyucoin.customDialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapterDeposit;
import com.buyucoin.buyucoin.Adapters.P2PorderRecyclerViewAdapterWithdraw;
import com.buyucoin.buyucoin.Fragments.TriggerActiveOrder;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.ActiveP2pOrders;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class P2pActiveOrdersDialog extends DialogFragment {

    private BuyucoinPref preferences;
    private String ACCESS_TOKEN;
    RecyclerView recyclerView_d,recyclerView_w;
    ArrayList<ActiveP2pOrders> activeP2pOrderslist;
    TextView activeOrderType;
    String type = "";


    public static P2pActiveOrdersDialog newInstance(){
        return new P2pActiveOrdersDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        preferences = new BuyucoinPref(this.getContext());
        ACCESS_TOKEN = preferences.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        activeP2pOrderslist = new ArrayList<>();



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.p2p_active_order_dialog_layout,container,false);
        recyclerView_d = view.findViewById(R.id.p2p_active_orders_deposit);
        recyclerView_w = view.findViewById(R.id.p2p_active_orders_withdraw);
        recyclerView_d.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView_w.setLayoutManager(new LinearLayoutManager(this.getContext()));
        activeOrderType = view.findViewById(R.id.p2p_active_orders_type);

        getActiveOrders();
        return view;
    }

    private void getActiveOrders() {
        activeP2pOrderslist.clear();
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
                    JSONObject j = new JSONObject(s);
                    JSONObject active_deposite = j.getJSONObject("active_deposits");
                    JSONObject active_withdrawals = j.getJSONObject("active_withdrawals");

                    if(!active_deposite.toString().equals("{}")){
                        type = "Active Deposits";
                        crateOrderView(active_deposite);
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activeOrderType.setText(type);
                                P2PorderRecyclerViewAdapterDeposit adapter = new P2PorderRecyclerViewAdapterDeposit(getContext(),activeP2pOrderslist,getChildFragmentManager());
                                recyclerView_d.setAdapter(adapter);
                                recyclerView_d.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    if(!active_withdrawals.toString().equals("{}")){
                        type = "Active Withdrawals";
                        crateOrderView(active_withdrawals);
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activeOrderType.setText(type);
                                P2PorderRecyclerViewAdapterWithdraw adapter = new P2PorderRecyclerViewAdapterWithdraw(getContext(),activeP2pOrderslist,getChildFragmentManager(),getActivity());
                                recyclerView_w.setAdapter(adapter);
                                recyclerView_w.setVisibility(View.VISIBLE);
                            }
                        });

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void crateOrderView(JSONObject active_withdrawals) {
        try {
            for (Iterator<String> it = active_withdrawals.keys(); it.hasNext(); ) {
                String key = it.next();
                JSONObject o = active_withdrawals.getJSONObject(key).getJSONObject("peer");
                Log.d("fdgdhfjghdfhfgmf", o.toString());
                ActiveP2pOrders activeP2pOrders = new ActiveP2pOrders();
                JSONArray array = new JSONArray();

                activeP2pOrders.setAmount(o.getDouble("amount"));
                activeP2pOrders.setBoost(o.getDouble("boost"));
                activeP2pOrders.setCurrency(o.getInt("currency"));
                activeP2pOrders.setDuration(o.getInt("duration"));
                activeP2pOrders.setEnd_timestamp(o.getString("end_timestamp"));
                activeP2pOrders.setFee(o.getDouble("fee"));
                activeP2pOrders.setFilled(o.getDouble("filled"));
                activeP2pOrders.setFilled_by(o.getString("filled_by"));
                activeP2pOrders.setId(o.getInt("id"));
                activeP2pOrders.setMatched(o.getDouble("matched"));

                if (o.has("matched_by")) {
                    for (Iterator<String> it1 = o.getJSONObject("matched_by").keys(); it1.hasNext(); ) {
                        String s1 = it1.next();
                        array.put(o.getJSONObject("matched_by").
                                getJSONObject(s1).put("key",s1)
                                .put("id",o.getInt("id"))
                        );
                    }
                }


                activeP2pOrders.setMatched_by(array);

                activeP2pOrders.setMatches(o.get("matches"));

                activeP2pOrders.setMin_amount(o.getDouble("min_amount"));
                activeP2pOrders.setModes(o.getJSONArray("modes"));
                activeP2pOrders.setNote(o.getString("note"));

                activeP2pOrders.setRejected_matches(o.get("rejected_match"));

                activeP2pOrders.setStatus(o.getInt("status"));
                activeP2pOrders.setTimestamp(o.getString("timestamp"));
                activeP2pOrders.setTx_hash(o.getString("tx_hash"));
                activeP2pOrders.setType(o.getInt("type"));
                activeP2pOrders.setUpi_address(o.getString("note"));
                activeP2pOrders.setUser_id(o.getInt("user_id"));
                activeP2pOrders.setWallet_id(o.getInt("wallet_id"));
                activeP2pOrders.setWfee_amount(o.getInt("wfee_amount"));

                activeP2pOrderslist.add(activeP2pOrders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
