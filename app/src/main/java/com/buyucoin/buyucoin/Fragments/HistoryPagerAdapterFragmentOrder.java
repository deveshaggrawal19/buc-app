package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.pojos.History;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class HistoryPagerAdapterFragmentOrder extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String ACCESS_TOKEN = null;
    RecyclerView rv;
    ProgressBar pb;
    ArrayList<History> histories;
    BuyucoinPref buyucoinPref;
    String url;
    Bundle b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        histories = new ArrayList<>();
        buyucoinPref = new BuyucoinPref(getContext());
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        if(getArguments()!=null){
            b = getArguments();
        }
        url = b.getString("URL");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_page, container, false);
        rv = view.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        pb = view.findViewById(R.id.pbHistory);
        getList(url);
        return view;
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
                try {
                    final JSONArray array = new JSONObject(s).getJSONObject("data").getJSONArray(url.equals("order") ? "orders" : url + "_comp");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);
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

                        }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(new MyHistoryRecyclerViewAdapter(histories));
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

        public MyHistoryRecyclerViewAdapter(ArrayList<History> items) {
            mValues = items;
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

                        if(holder.mItem.getType().equals("Buy")){
                            holder.mType.setImageResource(R.drawable.history_deposite_icon);
                        }else{
                            holder.mType.setImageResource(R.drawable.history_withdraw_icon);
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
                    AccountFragment.makeViewDisable(holder.mView);
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
