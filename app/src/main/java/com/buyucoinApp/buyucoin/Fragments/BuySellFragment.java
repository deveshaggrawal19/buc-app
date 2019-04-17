package com.buyucoinApp.buyucoin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.buyucoinApp.buyucoin.Adapters.BuySellRecyclerViewAdapter;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.Utilities;
import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.pojos.BuySellData;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BuySellFragment extends Fragment {

    String ACCESS_TOKEN = null;
    private ArrayList<BuySellData> list;
    private RecyclerView recyclerView;
    private ProgressBar pb;
    //    TextView trade;
//    private String FRAGMENT_STATE = "BUYSELL";
    private DatabaseReference myRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BuyucoinPref buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        list = new ArrayList<>();
        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
        // Toast.makeText(getActivity(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buysell_fragment_item_list, container, false);

        recyclerView = view.findViewById(R.id.rvWallet);
        Context context = view.getContext();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        pb = view.findViewById(R.id.pbWallet);
//        TextView errorText = view.findViewById(R.id.tvBuySellError);
//        getWalletData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                DataSnapshot otc_crypto = dataSnapshot.child("otc");
                for(DataSnapshot d : otc_crypto.getChildren()){
                    list.add(new BuySellData(
                            d.getKey(),
                            d.child("buy_margin").getValue(String.class),
                            d.child("buy_max").getValue(String.class),
                            d.child("buy_min").getValue(String.class),
                            d.child("buy_positive").getValue(String.class),
                            d.child("buy_rate").getValue(String.class),
                            d.child("buy_rate_btc").getValue(String.class),
                            d.child("buy_vol").getValue(String.class),
                            d.child("min_trade").getValue(String.class),
                            d.child("name").getValue(String.class),
                            d.child("positive").getValue(String.class),
                            d.child("sell_margin").getValue(String.class),
                            d.child("sell_max").getValue(String.class),
                            d.child("sell_min").getValue(String.class),
                            d.child("sell_positive").getValue(String.class),
                            d.child("sell_rate").getValue(String.class),
                            d.child("sell_rate_btc").getValue(String.class),
                            d.child("sell_vol").getValue(String.class),
                            d.child("working_None").getValue(String.class),
                            d.child("working_buy").getValue(String.class),
                            d.child("working_sell").getValue(String.class)

                    ));
                }
                recyclerView.setAdapter(new BuySellRecyclerViewAdapter(list));
                Utilities.hideProgressBar(pb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void getWalletData(){
//        list.clear();
//        OkHttpHandler.auth_get("get_wallet", ACCESS_TOKEN, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String s = response.body().string();
//                if(Utilities.isSuccess(s)) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(s);
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        String[] arr = {"btc", "eth","ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
//                        for (String anArr : arr) {
//                            try {
//                                list.add(data.getJSONObject(anArr).put("currencyname", anArr));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                recyclerView.setAdapter(new BuySellRecyclerViewAdapter(list));
//                                Utilities.hideProgressBar(pb);
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new Dashboard().ServerErrorFragment();
//
////                            Utilities.hideProgressBar(pb);
////                            errorText.setText(Utilities.getErrorMessage(s));
////                            errorText.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }
//            }
//        });
//    }
}
