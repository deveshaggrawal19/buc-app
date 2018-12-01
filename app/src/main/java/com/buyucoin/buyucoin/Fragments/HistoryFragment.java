package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ACCESS_TOKEN= null;
    RecyclerView rv;
    ProgressBar pb;

    private OnListFragmentInteractionListener mListener;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rv = (RecyclerView)view.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        pb = (ProgressBar)view.findViewById(R.id.pbHistory);
        TabLayout tl = (TabLayout)view.findViewById(R.id.tlHistory);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pb.setVisibility(View.VISIBLE);
                switch(tab.getPosition()){
                    case 0:
                        getList("deposit");
                        break;
                    case 1:
                        getList("withdraw");
                        break;
                    case 2:
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(JSONObject item);
    }
    // TODO: Rename method, update argument and hook method into UI event


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getList(final String url){
        OkHttpHandler.auth_get(url+"_history", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                //Log.d("RESPONSE____", s);
                try {
                    final JSONArray array = new JSONObject(s).getJSONObject("data").getJSONArray(url.equals("order")?"orders":url+"_comp");
                    Log.d("ARRAY______", array.length()+"");
                    Log.d("_____", array.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(new MyHistoryRecyclerViewAdapter(array, mListener, url));
                            pb.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter(){
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

        private final JSONArray mValues;
        private final OnListFragmentInteractionListener mListener;
        private final String mType;

        public MyHistoryRecyclerViewAdapter(JSONArray items, OnListFragmentInteractionListener listener, String type) {
            mValues = items;
            mListener = listener;
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
            try{
                holder.mItem = mValues.getJSONObject(position);
                switch(mType){
                    case "deposit":
                        holder.mCurrency.setText(holder.mItem.getString("curr").toUpperCase());
                        holder.mAmount.setText(holder.mItem.getString("amount"));
                        holder.mOpenTime.setText("Open Time: "+ holder.mItem.getString("open_time"));
                        holder.mTxHash.setText("Tx Hash: "+holder.mItem.getString("tx_hash"));
                        holder.mStatus.setText("Status: "+holder.mItem.getString("status"));
                        holder.mAddress.setVisibility(View.GONE);
                        holder.mFilled.setVisibility(View.GONE);
                        holder.mValue.setVisibility(View.GONE);
                        break;
                    case "withdraw":
                        holder.mCurrency.setText(holder.mItem.getString("curr").toUpperCase());
                        holder.mAmount.setText(holder.mItem.getString("amount"));
                        holder.mOpenTime.setText("Open Time: "+holder.mItem.getString("open_time"));
                        holder.mTxHash.setText("Tx Hash: "+holder.mItem.getString("tx_hash"));
                        holder.mStatus.setText("Status: "+ holder.mItem.getString("status"));
                        holder.mAddress.setText("Address: "+holder.mItem.getString("address"));
                        holder.mAddress.setVisibility(View.VISIBLE);
                        holder.mFilled.setVisibility(View.GONE);
                        holder.mValue.setVisibility(View.GONE);
                        break;
                    case "order":
                        holder.mCurrency.setText(holder.mItem.getString("curr").toUpperCase());
                        holder.mAmount.setText(holder.mItem.getString("amount"));
                        holder.mOpenTime.setText("Fee: "+ holder.mItem.getString("fee"));
                        holder.mTxHash.setText("Price: "+holder.mItem.getString("price"));
                        holder.mStatus.setText("Status: "+holder.mItem.getString("status"));
                        holder.mAddress.setText(holder.mItem.getString("type").toUpperCase());
                        holder.mAddress.setVisibility(View.VISIBLE);
                        holder.mFilled.setText("Filled: "+holder.mItem.getString("filled"));
                        holder.mFilled.setVisibility(View.VISIBLE);
                        holder.mValue.setText("Value: "+holder.mItem.getString("value"));
                        holder.mValue.setVisibility(View.VISIBLE);
                        break;
                }

            }catch(Exception e){
                e.printStackTrace();
                holder.mCurrency.setText("N/A");
                holder.mAmount.setText("N/A");
                holder.mOpenTime.setText("N/A");
                holder.mTxHash.setText("N/A");
                holder.mStatus.setText("N/A");
            }

            try{
                holder.mImage.setImageDrawable(getActivity().getResources().getDrawable(getActivity().getResources().getIdentifier(holder.mItem.getString("curr"), "drawable", getActivity().getPackageName())));
            }catch(Exception e){
                holder.mImage.setVisibility(View.GONE);
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mAmount, mCurrency, mOpenTime, mTxHash, mStatus, mAddress, mFilled, mValue;
            public final ImageView mImage;
            public JSONObject mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAmount = (TextView) view.findViewById(R.id.tvHistoryAmount);
                mCurrency = (TextView) view.findViewById(R.id.tvHistoryCurrency);
                mOpenTime = (TextView) view.findViewById(R.id.tvHistoryOpenTime);
                mTxHash = (TextView) view.findViewById(R.id.tvHistoryTxHash);
                mStatus = (TextView) view.findViewById(R.id.tvHistoryStatus);
                mAddress = (TextView) view.findViewById(R.id.tvHistoryAddress);
                mFilled = (TextView)view.findViewById(R.id.tvHistoryFilled);
                mValue = (TextView)view.findViewById(R.id.tvHistoryValue);
                mImage = (ImageView) view.findViewById(R.id.ivHistory);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }
        }
    }

}
