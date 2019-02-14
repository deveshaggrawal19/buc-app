package com.buyucoinApp.buyucoin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;



public class ReferralFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rv;
    String ACCESS_TOKEN;



    public ReferralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferralFragment newInstance(String param1, String param2) {
        ReferralFragment fragment = new ReferralFragment();
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
        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        rv = view.findViewById(R.id.rvReferral);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));


        JSONArray array = null;
        try {

        }catch(Exception e){
            e.printStackTrace();
        }
        getReferrals();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event



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


    public void getReferrals(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject refs = new JSONObject(s).getJSONObject("data").getJSONObject("referrals");

                    JSONArray array = refs.toJSONArray(refs.names());
                    for (int i = 0; i < array.length(); i++) {
                        array.getJSONObject(i).put("currency", refs.names().get(i));
                    }
                    final JSONArray farray = array;
                    //Log.d("RESPONSE____",array.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(new MyRecyclerViewAdapter(farray,getContext()));
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private final JSONArray mValues;
        private Context context;

        public MyRecyclerViewAdapter(JSONArray items, Context context) {
            mValues = items;
            this.context =context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragement_referral_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            String s1, s2;
            try{
                holder.mItem = mValues.getJSONObject(position);

                s1 = mValues.getJSONObject(position).getString("currency").toUpperCase();
                s2 = "Pending " + mValues.getJSONObject(position).getString("pend") + " / "+ mValues.getJSONObject(position).getString("total")+" Total";
            }catch(Exception e){
                s1 = s2 = "N/A";
            }
            holder.mCurrency.setText(s1);
            holder.mPending.setText(s2);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mCurrency, mPending;
            public JSONObject mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mCurrency = view.findViewById(R.id.tvAccountCurr);
                mPending = view.findViewById(R.id.tvAccountPending);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }
        }
    }

}
