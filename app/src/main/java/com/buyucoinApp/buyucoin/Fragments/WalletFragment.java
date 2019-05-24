package com.buyucoinApp.buyucoin.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buyucoinApp.buyucoin.Adapters.VerticalAdapter;
import com.buyucoinApp.buyucoin.Dashboard;
import com.buyucoinApp.buyucoin.LoginActivity;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.Utilities;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.customDialogs.P2pActiveOrdersDialog;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WalletFragment extends Fragment {

    String ACCESS_TOKEN = null;
    private ArrayList<JSONObject> list;
    private RecyclerView recyclerView;
    private ProgressBar pb;
    private TextView err,wallet_inr,welcome,total_crypto_portfolio;
    private CheckBox hidezero_checkbox;
    private ImageView wallet_process_img;
    private BuyucoinPref buyucoinPref;
    private String WALLET_INR_BALANCE = "0";
    private String WALLET_CRYTPO_PORTFOLIO = "0";
    private LinearLayout account_dep_history;
    private LinearLayout account_with_history;
    private LinearLayout account_trade_history;
    private LinearLayout p2p_history_layout;
    private LinearLayout p2p_active_orders_layout;
    private NestedScrollView nsView;
    private ShimmerFrameLayout shimmerFrameLayout;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        list = new ArrayList<>();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wallet, container, false);


        initView(view);
        HistoryClickHandler();
        if(buyucoinPref.getPrefBoolean("kyc_status") && buyucoinPref.getPrefBoolean("mob_verified") && buyucoinPref.getPrefBoolean("wallet")){
                getWalletData();
                getAccountData();
        }

        WALLET_INR_BALANCE = buyucoinPref.getPrefString("inr_amount");
        String WALLET_STRING = getResources().getText(R.string.rupees)+" "+WALLET_INR_BALANCE;
        wallet_inr.setText(WALLET_STRING);
        String name = "Weclome ";
        name += buyucoinPref.getPrefString("name");
        welcome.setText(name);



        p2p_history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2P_History p2P_history = new P2P_History();
                assert getFragmentManager() != null;
                p2P_history.show(getFragmentManager(), "P2P HISTORY");
            }
        });

        p2p_active_orders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment active_order = P2pActiveOrdersDialog.newInstance();
                active_order.show(getChildFragmentManager(),"");
            }
        });
        hidezero_checkbox.setChecked(false);
        hidezero_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    buyucoinPref.setEditpref("hide_zero",hidezero_checkbox.isChecked());
                    recyclerView.setAdapter(new VerticalAdapter(getContext(),list,hidezero_checkbox.isChecked()));
            }
        });
        


        return view;
    }



    private void   initView(View view){
        recyclerView = view.findViewById(R.id.rvWallet);
        wallet_inr = view.findViewById(R.id.wallet_inr);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        account_dep_history = view.findViewById(R.id.account_dep_history);
        account_with_history = view.findViewById(R.id.account_with_history);
        account_trade_history = view.findViewById(R.id.account_trade_history);
        p2p_history_layout = view.findViewById(R.id.p2p_history_ll);
        wallet_process_img = view.findViewById(R.id.wallet_process_img);
        p2p_active_orders_layout = view.findViewById(R.id.p2p_active_orders_ll);
        pb = view.findViewById(R.id.pbWallet);
        err = view.findViewById(R.id.tvWalletError);
        nsView = view.findViewById(R.id.nsView);
        hidezero_checkbox = view.findViewById(R.id.wallet_checkbox);
        welcome = view.findViewById(R.id.welcome);
        total_crypto_portfolio = view.findViewById(R.id.wallet_crypto_port);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);



    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void HistoryClickHandler(){


        account_dep_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewHistoryFragment(0,account_dep_history);

            }
        });

        account_with_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewHistoryFragment(1,account_with_history);
            }
        });
        account_trade_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewHistoryFragment(2,account_trade_history);

            }
        });
    }

    private void createNewHistoryFragment(int position, View view){
        final DialogFragment historyFragment = new HistoryFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        historyFragment.setArguments(bundle);
        historyFragment.show(getChildFragmentManager(),"HISTORY FRAGMENT "+position);

        makeViewDisable(view);
    }

    private static void makeViewDisable(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },1000);
    }


    private void getWalletData(){

        list.clear();
        hidezero_checkbox.setChecked(false);
        OkHttpHandler.auth_get("get_wallet", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body() != null ? response.body().string() : null;
                if(s!=null)
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("status").equals("error") || jsonObject.getString("status").equals("redirect")){
                        if(jsonObject.has("msg") && jsonObject.getString("msg").equals("The token has expired")) {
                            buyucoinPref.removePref("access_token").apply();
                            buyucoinPref.removePref("refresh_token").apply();
                            Utilities.showToast(getActivity(), "Login again to access wallet");
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            if(getActivity()!=null)getActivity().finish();
                        }else{
                            if(jsonObject.has("message")){
                                final String e = jsonObject.getJSONArray("message").getJSONArray(0).getString(0);
                               if(getActivity()!=null){
                                   err.setText(e);
                                   err.setVisibility(View.VISIBLE);
                                   nsView.setVisibility(View.GONE);
                               }

                            }
                        }

                        Utilities.hideProgressBar(pb, getActivity());
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    String refid = data.getString("referral_id");
                    String remark_id = data.getString("remark");
                    buyucoinPref.setEditpref("ref_id",refid);
                    buyucoinPref.setEditpref("remark_id",remark_id);
                    Log.d("WALLET_FRAGMENT", "onResponse: "+data.toString());
                    String[] arr = {"btc", "eth", "inr", "ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
                    for (String anArr : arr)
                        try {
                            JSONObject cj = data.getJSONObject(anArr)
                                    .put("currencyname", anArr)
                                    .put("currencies", data.getJSONObject("currencies").get(anArr));
                            list.add(cj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nsView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(new VerticalAdapter(getContext(),list,false));
                                Utilities.hideProgressBar(pb);
                                wallet_process_img.setVisibility(View.GONE);
                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    if(getActivity()!=null){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Utilities.hideProgressBar(pb);
//                            err.setVisibility(View.VISIBLE);
                            new Dashboard().ServerErrorFragment();

                        }
                    });
                    }
                }
            }
        });
    }

    private void getAccountData() {
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            new CoustomToast(getActivity(),"Error retreiving API",CoustomToast.TYPE_DANGER).showToast();
                            }
                        });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                final String s = response.body().string();
                Log.d("ACCOUNT DATA", "onResponse: "+s);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("status").equals("redirect")) {
                                    Utilities.getOTP(getActivity(), ACCESS_TOKEN,new AlertDialog.Builder(getActivity()));
                                    new Dashboard().ServerErrorFragment();
                                    return;
                                }
                                final JSONObject data = jsonObject.getJSONObject(("data"));
                                buyucoinPref.setEditpref("email",data.get("email").toString());
                                buyucoinPref.setEditpref("name",data.get("name").toString().split(" ")[0]);
                                buyucoinPref.setEditpref("mob",data.get("mob").toString());
                                buyucoinPref.setEditpref("kyc_status",data.getBoolean("kyc_status"));

                                WalletBalance();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else{
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Dashboard().ServerErrorFragment();
                            }
                        });
                    }
                }

            }
        });
    }

    private void WalletBalance(){
        try {
            WALLET_INR_BALANCE = buyucoinPref.getPrefString("inr_amount");
            WALLET_CRYTPO_PORTFOLIO = buyucoinPref.getPrefString("portfolio");
            String rupess = getResources().getText(R.string.rupees).toString();
            String wallet_balance = rupess+" "+WALLET_INR_BALANCE;
            String crypto_portfolio = rupess+" "+WALLET_CRYTPO_PORTFOLIO;
            wallet_inr.setText(wallet_balance);
            String name = "";
            name += buyucoinPref.getPrefString("name");
            welcome.setText(name);
            total_crypto_portfolio.setText(crypto_portfolio);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        hidezero_checkbox = null;
    }
}

