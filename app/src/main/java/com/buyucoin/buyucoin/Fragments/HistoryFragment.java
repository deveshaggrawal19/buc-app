package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.pojos.History;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ACCESS_TOKEN = null;
    RecyclerView rv;
    ProgressBar pb;
    ArrayList<History> histories;
    private int TAB_INDEX;


    public HistoryFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        histories = new ArrayList<>();

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rv = view.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        pb = view.findViewById(R.id.pbHistory);
        TabLayout tl = view.findViewById(R.id.tlHistory);

        int tab_position = (getArguments() != null) ? getArguments().getInt("SELECTED_TAB") : 0;

        TabLayout.Tab st = tl.getTabAt(tab_position);
        if (st != null) {
            st.select();
        }


        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pb.setVisibility(View.VISIBLE);
                switch (tab.getPosition()) {
                    case 0:
                        histories.clear();
                        getList("deposit");
                        break;
                    case 1:
                        histories.clear();
                        getList("withdraw");
                        break;
                    case 2:
                        histories.clear();
                        getList("order");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getList("deposit");
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event






    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getList(final String url) {
        OkHttpHandler.auth_get(url + "_history", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                //Log.d("RESPONSE____", s);
                try {
                    final JSONArray array = new JSONObject(s).getJSONObject("data").getJSONArray(url.equals("order") ? "orders" : url + "_comp");
                    Log.d("ARRAY______", array.length() + "");
                    Log.d("_____", array.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);

                        switch (url){
                            case "deposit":
                                histories.add(new History(
                                        j.getDouble("amount"),
                                        j.getString("curr"),
                                        j.getString("open_time"),
                                        "",
                                        j.getString("status"),
                                        j.getString("tx_hash"),
                                        "",
                                        0.0,
                                        0.0,
                                        0.0,
                                        "",
                                        0.0
                                ));
                                break;
                            case "withdraw":
                                histories.add(new History(
                                        j.getDouble("amount"),
                                        j.getString("curr"),
                                        j.getString("open_time"),
                                        "",
                                        j.getString("status"),
                                        j.getString("tx_hash"),
                                        j.getString("address"),
                                        0.0,
                                        0.0,
                                        0.0,
                                        "",
                                        0.0
                                ));
                                break;
                            case "order":
                                histories.add(new History(
                                        j.getDouble("amount"),
                                        j.getString("curr"),
                                        j.getString("open"),
                                        j.getString("open"),
                                        j.getString("status"),
                                        "",
                                        "",
                                        j.getDouble("fee"),
                                        j.getDouble("filled"),
                                        j.getDouble("price"),
                                        j.getString("type"),
                                        j.getDouble("value")
                                ));
                                default:
                                    break;
                        }


                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(new MyHistoryRecyclerViewAdapter(histories,url));
                            pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animator) {
                                    pb.setVisibility(View.GONE);
                                    pb.setAlpha(1f);
                                }
                            });

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<History> mValues;
        private final String mType;

        public MyHistoryRecyclerViewAdapter(ArrayList<History> items, String type) {
            mValues = items;
            mType = type;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_history_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                holder.mItem = mValues.get(position);



                holder.mCurrency.setText(holder.mItem.getCurr().toUpperCase());
                holder.mAmount.setText(String.valueOf(holder.mItem.getAmount()));
                if(holder.mItem.getOpen()!=""){

                holder.mOpenTime.setText(holder.mItem.getOpen());
                }else{

                holder.mOpenTime.setText(holder.mItem.getOpen_time());
                }
                holder.mTxHash.setText(holder.mItem.getTx_hash());
                switch (mType){
                    case "deposte":
                        holder.mType.setImageResource(R.drawable.history_deposite_icon);
                        break;
                    case "withdraw":
                        holder.mType.setImageResource(R.drawable.history_withdraw_icon);

                        break;
                    case "order":
                        if(holder.mItem.getType().equals("Buy")){
                            holder.mType.setImageResource(R.drawable.history_deposite_icon);
                        }else{
                            holder.mType.setImageResource(R.drawable.history_withdraw_icon);
                        }
                        break;
                        default:

                }

                switch (holder.mItem.getStatus()){
                    case "Pending":
                        holder.mStatus.setImageResource(R.drawable.history_pending_icon);
                        break;
                    case "Success":
                        holder.mStatus.setImageResource(R.drawable.history_success_icon);
                        break;
                    case "Cancelled":
                        holder.mStatus.setImageResource(R.drawable.history_cancel_icon);
                        break;
                        default:
                        holder.mStatus.setImageResource(R.drawable.history_pending_icon);

                }


            } catch (Exception e) {
                e.printStackTrace();
                holder.mCurrency.setText("N/A");
                holder.mAmount.setText("N/A");
                holder.mOpenTime.setText("N/A");
                holder.mTxHash.setText("N/A");
            }

            try {
                holder.mImage.setImageDrawable(getActivity().getResources().getDrawable(getActivity().getResources().getIdentifier(holder.mItem.getCurr(), "drawable", getActivity().getPackageName())));
            } catch (Exception e) {
                holder.mImage.setVisibility(View.GONE);
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
//                        mListener.onListFragmentInteraction(holder.mItem);
                        HistoryBottomsheet historyBottomsheet = new HistoryBottomsheet();
                        Bundle b = new Bundle();
                        b.putString("history_amount",String.valueOf(holder.mItem.getAmount()));
                        b.putString("history_currency",holder.mItem.getCurr());
                        if(holder.mItem.getOpen()!=""){

                            b.putString("history_time",holder.mItem.getOpen());
                        }else{

                            b.putString("history_time",holder.mItem.getOpen_time());
                        }
                        b.putString("history_status",holder.mItem.getStatus());
                        b.putString("history_tx_hash",holder.mItem.getTx_hash());
                        b.putString("history_address",holder.mItem.getAddress());
                        b.putString("history_fees",String.valueOf(holder.mItem.getFee()));
                        b.putString("history_filled",String.valueOf(holder.mItem.getFilled()));
                        b.putString("history_price",String.valueOf(holder.mItem.getPrice()));
                        b.putString("history_type",String.valueOf(holder.mItem.getType()));
                        b.putString("history_value",String.valueOf(holder.mItem.getValue()));

                        historyBottomsheet.setArguments(b);

                        historyBottomsheet.show(getChildFragmentManager(), "HISTORY");
                }
            });
        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mAmount, mCurrency, mOpenTime, mTxHash, mAddress, mFilled, mValue;
            public final ImageView mImage,mStatus,mType;
            public History mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAmount = view.findViewById(R.id.tvHistoryAmount);
                mCurrency = view.findViewById(R.id.tvHistoryCurrency);
                mOpenTime = view.findViewById(R.id.tvHistoryOpenTime);
                mTxHash = view.findViewById(R.id.tvHistoryTxHash);
                mStatus = view.findViewById(R.id.tvHistoryStatus);
                mAddress = view.findViewById(R.id.tvHistoryAddress);
                mFilled = view.findViewById(R.id.tvHistoryFilled);
                mValue = view.findViewById(R.id.tvHistoryValue);
                mImage = view.findViewById(R.id.ivHistory);
                mType = view.findViewById(R.id.history_type_image);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }
        }
    }

}
