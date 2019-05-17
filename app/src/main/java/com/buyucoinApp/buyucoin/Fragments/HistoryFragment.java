package com.buyucoinApp.buyucoin.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buyucoinApp.buyucoin.Adapters.History_PagerAdapter;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.buyucoinApp.buyucoin.pref.BuyucoinPref.ACCESS_TOKEN;


public class HistoryFragment extends DialogFragment {

    Bundle b;
    TabLayout tabLayout;
    ViewPager viewPager;
    int position = 0;
    String coin = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        if(getArguments()!=null){
            b = getArguments();
         position = b.getInt("POSITION");
         coin = b.getString("coin");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        try {
            BuyucoinPref buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
            ACCESS_TOKEN = buyucoinPref.getPrefString(ACCESS_TOKEN);
        }catch (Exception e){
            e.printStackTrace();
        }

        ImageView goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        tabLayout = view.findViewById(R.id.tlHistory);
        viewPager = view.findViewById(R.id.history_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        getHistory();

        History_PagerAdapter history_pagerAdapter = new History_PagerAdapter(getChildFragmentManager(),coin);
        viewPager.setAdapter(history_pagerAdapter);

        viewPager.setCurrentItem(position);







        return view;
    }

    private void getHistory(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OkHttpHandler.auth_get("deposit_history", ACCESS_TOKEN, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        assert response.body() != null;
                        String s = response.body().string();
                        //Log.d("RESPONSE____", s);
                        try {
                            JSONObject jsonArray = new JSONObject(s).getJSONObject("data");
                            new BuyucoinPref(Objects.requireNonNull(getContext())).setEditpref("deposit_his",jsonArray.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OkHttpHandler.auth_get("withdraw_history", ACCESS_TOKEN, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        assert response.body() != null;
                        String s = response.body().string();
                        //Log.d("RESPONSE____", s);
                        try {
                            JSONObject jsonArray = new JSONObject(s).getJSONObject("data");
                            new BuyucoinPref(Objects.requireNonNull(getContext())).setEditpref("withdraw_his",jsonArray.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },110);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OkHttpHandler.auth_get("order_history", ACCESS_TOKEN, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        assert response.body() != null;
                        String s = response.body().string();
                        Log.d("OOOOOOOOOOORESPONSE____", s);
                        try {
                            JSONObject jsonArray = new JSONObject(s).getJSONObject("data");
                            new BuyucoinPref(Objects.requireNonNull(getContext())).setEditpref("order_his",jsonArray.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },120);

    }





}
