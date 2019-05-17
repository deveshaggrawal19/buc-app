package com.buyucoinApp.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.buyucoinApp.buyucoin.Adapters.MyHistoryWithdrawRecyclerViewAdapter;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pojos.History;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryPagerAdapterFragmentWithdraw extends DialogFragment {

    String ACCESS_TOKEN = null;
    private RecyclerView rv;
    private ProgressBar pb;
    private ArrayList<History> histories;
    private static   JSONArray MainArray;
    private String url,coin;
    private Bundle b;

    private LinearLayout empty_layout;
    private static BuyucoinPref buyucoinPref;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        histories = new ArrayList<>();
        try{
            buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getActivity()));
            ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);

        }catch (Exception e){
            e.printStackTrace();

        }
        if(getArguments()!=null){
            b = getArguments();
        }

        url = b.getString("URL");
        if(b.getString("COIN")!=null){
            coin =  b.getString("COIN");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_page, container, false);
        rv = view.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        pb = view.findViewById(R.id.pbHistory);
        empty_layout = view.findViewById(R.id.empty_orders);
        RadioGroup filter_group = view.findViewById(R.id.filter_radio_group);
        filter_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.filter_all:
                        pb.setVisibility(View.VISIBLE);
                        if(coin!=null) getListCoin(url,"all",coin);
                        else getList(url,"all");
                        break;
                    case R.id.filter_success:
                        pb.setVisibility(View.VISIBLE);
                        if(coin!=null) getListCoin(url,"Success",coin);
                        else getList(url,"Success");
                        break;
                    case R.id.filter_pending:
                        pb.setVisibility(View.VISIBLE);
                        if(coin!=null) getListCoin(url,"Pending",coin);
                        else getList(url,"Pending");
                        break;
                    case R.id.filter_canceled:
                        pb.setVisibility(View.VISIBLE);
                        if(coin!=null) getListCoin(url,"Cancelled",coin);
                        else getList(url,"Cancelled");
                        break;
                }
            }
        });
        if(coin!=null){
            getListCoin(url,"all",coin);

        }else{
            getList(url,"all");

        }
        return view;
    }



    private void getListCoin(final String url, final String filter, final String coin) {
        histories.clear();
        try {
            MainArray =  new JSONObject(buyucoinPref.getPrefString("withdraw_his")).getJSONArray(url.equals("order") ? "orders" : url + "_comp");
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getListCoin_(filter,coin);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void getList(final String url, final String filter) {
        try {
            MainArray =  new JSONObject(buyucoinPref.getPrefString("withdraw_his")).getJSONArray(url.equals("order") ? "orders" : url + "_comp");
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getList_(filter);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
           }

    private void getList_(final String filter){
        histories.clear();
        Log.d("wwwwwwwwwwwwwww",MainArray.toString());
        try {
        for (int i = 0; i < MainArray.length(); i++) {
            JSONObject j = MainArray.getJSONObject(i);
            if(j.getString("status").equals(filter) || filter.equals("all"))
                if(j.getString("status").equals(filter))
                    histories.add(new History(
                            j.getDouble("amount"),
                            j.getString("curr"),
                            j.getString("open_time"),
                            j.getString("open_time"),
                            j.getString("status"),
                            j.getString("tx_hash"),
                            j.getString("address"),
                            0.0,
                            0.0,
                            0.0,
                            "",
                            0.0,
                            0
                    ));
            if( filter.equals("all")){
                histories.add(new History(
                        j.getDouble("amount"),
                        j.getString("curr"),
                        j.getString("open_time"),
                        j.getString("open_time"),
                        j.getString("status"),
                        j.getString("tx_hash"),
                        j.getString("address"),
                        0.0,
                        0.0,
                        0.0,
                        "",
                        0.0,
                        0
                ));}
        }

        Log.d("wwwwwwwwwwwwww",histories.size()+"");

                if(histories.size()>0){

                    rv.setAdapter(new MyHistoryWithdrawRecyclerViewAdapter(histories,getActivity(),getChildFragmentManager()));
                    pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            pb.setVisibility(View.GONE);
                            pb.setAlpha(1f);
                        }
                    });
                    empty_layout.setVisibility(View.GONE);
                }else{

                    pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            pb.setVisibility(View.GONE);
                            pb.setAlpha(1f);
                        }
                    });
                    empty_layout.setVisibility(View.VISIBLE);

                }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getListCoin_(final String filter, final String coin){
        try {
            for (int i = 0; i < MainArray.length(); i++) {
                JSONObject j = MainArray.getJSONObject(i);
                if(j.getString("status").equals(filter) && j.getString("curr").equals(coin) && !j.getString("status").equals("all")) {
                    histories.add(new History(
                            j.getDouble("amount"),
                            j.getString("curr"),
                            j.getString("open_time"),
                            j.getString("open_time"),
                            j.getString("status"),
                            j.getString("tx_hash"),
                            j.getString("address"),
                            0.0,
                            0.0,
                            0.0,
                            "",
                            0.0,
                            0

                    ));
                }
                if( filter.equals("all") && j.getString("curr").equals(coin)) {
                    histories.add(new History(
                            j.getDouble("amount"),
                            j.getString("curr"),
                            j.getString("open_time"),
                            j.getString("open_time"),
                            j.getString("status"),
                            j.getString("tx_hash"),
                            j.getString("address"),
                            0.0,
                            0.0,
                            0.0,
                            "",
                            0.0,
                            0

                    ));
                }

            }

            if(histories.size()>0){
                rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                rv.setAdapter(new MyHistoryWithdrawRecyclerViewAdapter(histories,getActivity(),getChildFragmentManager()));
                pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        pb.setVisibility(View.GONE);
                        pb.setAlpha(1f);
                    }
                });
                empty_layout.setVisibility(View.GONE);
            }else{

                pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        pb.setVisibility(View.GONE);
                        pb.setAlpha(1f);
                    }
                });
                empty_layout.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
