package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.UniversalTimeScale;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.BuySellRecyclerViewAdapter;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class BuySellFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private WalletFragment.OnListFragmentInteractionListener mListener;
    String ACCESS_TOKEN = null;
    ArrayList<JSONObject> list;
    RecyclerView recyclerView;
    ProgressBar pb;
    TextView errorText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BuySellFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BuySellFragment newInstance(int columnCount) {
        BuySellFragment buySellFragment = new BuySellFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        buySellFragment.setArguments(args);
        return buySellFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);
        list = new ArrayList<>();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buysell_fragment_item_list, container, false);

        // Set the adapter
        recyclerView = view.findViewById(R.id.rvWallet);
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        pb = view.findViewById(R.id.pbWallet);

        errorText = view.findViewById(R.id.tvBuySellError);

        getWalletData();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletFragment.OnListFragmentInteractionListener) {
            mListener = (WalletFragment.OnListFragmentInteractionListener) context;
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
        OkHttpHandler.auth_get("get_wallet", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("/get_wallet RESPONSE", s);
                if(Utilities.isSuccess(s)) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String[] arr = {"btc", "eth", "inr", "ltc", "bcc", "xmr", "qtum", "etc", "zec", "xem", "gnt", "neo", "xrp", "dash", "strat", "steem", "rep", "lsk", "fct", "omg", "cvc", "sc", "pay", "ark", "doge", "dgb", "nxt", "bat", "bts", "cloak", "pivx", "dcn", "buc", "pac"};
                        for (int i = 0; i < arr.length; i++) {
                            try {
                                list.add(data.getJSONObject(arr[i]).put("currencyname", arr[i]));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(new BuySellRecyclerViewAdapter(list, mListener));
                                Utilities.hideProgressBar(pb);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utilities.hideProgressBar(pb);
                            errorText.setText(Utilities.getErrorMessage(s));
                            errorText.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
