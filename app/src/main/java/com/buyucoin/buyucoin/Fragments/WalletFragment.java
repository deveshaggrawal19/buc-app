package com.buyucoin.buyucoin.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.MyItemRecyclerViewAdapter;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.LoginActivity;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.customDialogs.P2pActiveOrdersDialog;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class WalletFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    String ACCESS_TOKEN = null;
    ArrayList<JSONObject> list;
    ArrayList<JSONObject> j = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar pb;
    TextView err,wallet_inr,welcome;
    View nsView;
    CheckBox hidezero_checkbox;
    private ImageView wallet_process_img;
    private BuyucoinPref buyucoinPref;
    private SharedPreferences prefs ;
    private SharedPreferences.Editor edit_pref;
    private String FRAGMENT_STATE = "WALLET";
    private String WALLET_INR_BALANCE = "0";
    private LinearLayout account_dep_history;
    private LinearLayout account_with_history;
    private LinearLayout account_trade_history;
    private LinearLayout p2p_history_layout;
    private LinearLayout p2p_active_orders_layout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        edit_pref =  getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
        ACCESS_TOKEN = prefs.getString("access_token", null);
        WALLET_INR_BALANCE = prefs.getString("inr_amount","0");
        edit_pref.putString("FRAGMENT_STATE",FRAGMENT_STATE).apply();
        list = new ArrayList<>();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rvWallet);
        wallet_inr = view.findViewById(R.id.wallet_inr);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        account_dep_history = view.findViewById(R.id.account_dep_history);
        account_with_history = view.findViewById(R.id.account_with_history);
        account_trade_history = view.findViewById(R.id.account_trade_history);
        p2p_history_layout = view.findViewById(R.id.p2p_history_ll);
        wallet_process_img = view.findViewById(R.id.wallet_process_img);
        p2p_history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2P_History p2P_history = new P2P_History();
                p2P_history.show(getFragmentManager(), "P2P HISTORY");
            }
        });
        p2p_active_orders_layout = view.findViewById(R.id.p2p_active_orders_ll);
        p2p_active_orders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment active_order = P2pActiveOrdersDialog.newInstance();
                active_order.show(getChildFragmentManager(),"");
            }
        });

        pb = (ProgressBar) view.findViewById(R.id.pbWallet);
        err = (TextView) view.findViewById(R.id.tvWalletError);
        nsView = view.findViewById(R.id.nsView);

        hidezero_checkbox = view.findViewById(R.id.wallet_checkbox);

        wallet_inr.setText(getResources().getText(R.string.rupees)+" "+WALLET_INR_BALANCE);

        welcome = view.findViewById(R.id.welcome);
        String name = "Weclome ";
        name += prefs.getString("name","Back");
        welcome.setText(name);

        HistoryClickHandler();
        getWalletData();
        getAccountData();



        hidezero_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hidezero_checkbox.isChecked()){
                    buyucoinPref.setEditpref("hide_zero",true);
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),list,true));

                }
                else {
                    buyucoinPref.setEditpref("hide_zero",false);
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),list,false));

                }
            }
        });



        return view;
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

    public void createNewHistoryFragment(int position,View view){
        final DialogFragment historyFragment = new HistoryFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        historyFragment.setArguments(bundle);
        historyFragment.show(getChildFragmentManager(),"HISTORY FRAGMENT "+position);

        makeViewDisable(view);
    }

    public static void makeViewDisable(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },1000);
    }


    public void getWalletData(){
        list.clear();
        hidezero_checkbox.setChecked(false);
        OkHttpHandler.auth_get("get_wallet", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("status").equals("error") || jsonObject.getString("status").equals("redirect")){
                        if(jsonObject.has("msg") && jsonObject.getString("msg").equals("The token has expired")) {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
                            editor.remove("access_token");
                            editor.remove("refresh_token");
                            editor.apply();
                            Utilities.showToast(getActivity(), "Login again to access wallet");
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }else{
                            if(jsonObject.has("message")){
                                final String e = jsonObject.getJSONArray("message").getJSONArray(0).getString(0);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        err.setText(e);
                                        err.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }

                        Utilities.hideProgressBar(pb, getActivity());
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    String refid = data.getString("referral_id");
                    String remark_id = data.getString("remark");
                    edit_pref.putString("ref_id",refid).apply();
                    edit_pref.putString("remark_id",remark_id).apply();
                    Log.d("WALLET_FRAGMENT", "onResponse: "+data.toString());
                    String[] arr = {"btc", "eth", "inr", "ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
                    for(int i=0; i<arr.length; i++){
                        try {
                            JSONObject cj = data.getJSONObject(arr[i])
                                    .put("currencyname", arr[i])
                                    .put("currencies",data.getJSONObject("currencies").get(arr[i]));
                            list.add(cj);
//                            if(arr[i].equals("inr")){
//                                String inr_amt = getResources().getText(R.string.rupees)+"";
//                                inr_amt += data.getJSONObject("inr").getString("available");
//                                wallet_inr.setText(inr_amt);
//                            }


                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nsView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),list,false));
                                Utilities.hideProgressBar(pb);
                                wallet_process_img.setVisibility(View.GONE);
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
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        new CoustomToast(getContext(), Objects.requireNonNull(getActivity()),"Error retreiving API",CoustomToast.TYPE_DANGER).showToast();

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
                                edit_pref.putString("email",data.get("email").toString()).apply();
                                edit_pref.putString("name",data.get("name").toString()).apply();
                                edit_pref.putString("mob",data.get("mob").toString()).apply();
                                edit_pref.putBoolean("kyc_status",data.getBoolean("kyc_status")).apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else{
                    Looper.prepare();
                    new Dashboard().ServerErrorFragment();
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        hidezero_checkbox.setChecked(false);
    }
}

