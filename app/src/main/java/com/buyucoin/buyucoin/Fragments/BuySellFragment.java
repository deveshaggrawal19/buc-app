package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.BuySellRecyclerViewAdapter;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BuySellFragment extends Fragment {

    String ACCESS_TOKEN = null;
    ArrayList<JSONObject> list;
    RecyclerView recyclerView;
    ProgressBar pb;
    TextView errorText;
    TextView trade;
    private BuyucoinPref buyucoinPref;
    private String FRAGMENT_STATE = "BUYSELL";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        list = new ArrayList<>();


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
        errorText = view.findViewById(R.id.tvBuySellError);
        getWalletData();
        return view;
    }


    public void getWalletData(){
        list.clear();
        OkHttpHandler.auth_get("get_wallet", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                if(Utilities.isSuccess(s)) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String[] arr = {"btc", "eth","ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
                        for (String anArr : arr) {
                            try {
                                list.add(data.getJSONObject(anArr).put("currencyname", anArr));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(new BuySellRecyclerViewAdapter(list));
                                Utilities.hideProgressBar(pb);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Dashboard().ServerErrorFragment();

//                            Utilities.hideProgressBar(pb);
//                            errorText.setText(Utilities.getErrorMessage(s));
//                            errorText.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
