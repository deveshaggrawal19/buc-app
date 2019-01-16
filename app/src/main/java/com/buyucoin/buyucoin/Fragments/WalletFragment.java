package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.MyItemRecyclerViewAdapter;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.LoginActivity;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WalletFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    String ACCESS_TOKEN = null;
    ArrayList<JSONObject> list;
    ArrayList<JSONObject> j = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar pb;
    TextView err,wallet_inr;
    View nsView;
    CheckBox hidezero_checkbox;
    private SharedPreferences prefs ;
    private SharedPreferences.Editor edit_pref;
    private String FRAGMENT_STATE = "WALLET";
    private String WALLET_INR_BALANCE = "0";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WalletFragment newInstance(int columnCount) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rvWallet);
        wallet_inr = view.findViewById(R.id.wallet_inr);
        Context context = view.getContext();
        GridLayoutManager  linearLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(linearLayoutManager);

        pb = (ProgressBar) view.findViewById(R.id.pbWallet);
        err = (TextView) view.findViewById(R.id.tvWalletError);
        nsView = view.findViewById(R.id.nsView);

        hidezero_checkbox = view.findViewById(R.id.wallet_checkbox);

        wallet_inr.setText(getResources().getText(R.string.rupees)+" "+WALLET_INR_BALANCE);

        hidezero_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<JSONObject> clist = list;
                if(hidezero_checkbox.isChecked()){
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),clist, mListener,true));

                }
                else {
                recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),clist, mListener,false));

                }
            }
        });



//        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),list, mListener));


//        Utilities.hideProgressBar(pb);
        getWalletData();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(JSONObject item);
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
                Log.d("/get_wallet RESPONSE", s);
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
                    String[] arr = {"btc", "eth", "inr", "ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
                    for(int i=0; i<arr.length; i++){
                        try {
                            list.add(data.getJSONObject(arr[i]).put("currencyname", arr[i]).put("currencies",data.getJSONObject("currencies").get(arr[i])));


                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nsView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(),list, mListener,false));
                                Utilities.hideProgressBar(pb);
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
}

