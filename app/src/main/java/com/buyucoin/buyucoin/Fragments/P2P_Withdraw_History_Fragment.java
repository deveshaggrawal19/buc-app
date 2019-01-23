package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class P2P_Withdraw_History_Fragment extends Fragment {
    LinearLayout layout;
    ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p2p_order_withdraw_history,container,false);
        layout = view.findViewById(R.id.ll_empty);
        progressBar = view.findViewById(R.id.progress_circular_w);
        getActiveOrders();
        return view;
    }

    private void getActiveOrders() {
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
                    final JSONArray deposite = j.getJSONArray("deposits");
                    Log.d("kcjndgjfnhufngjhbfuhse",deposite.toString());
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(deposite.length()>0){
                                progressBar.setVisibility(View.GONE);
                                //                        layout.setVisibility(View.VISIBLE);
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


}
