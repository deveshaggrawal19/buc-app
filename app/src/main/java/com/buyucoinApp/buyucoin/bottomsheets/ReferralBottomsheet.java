package com.buyucoinApp.buyucoin.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.Fragments.ReferralFragment;
import com.buyucoinApp.buyucoin.MyResourcesClass;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReferralBottomsheet extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView rv;
    String ACCESS_TOKEN;
    BuyucoinPref buyucoinPref;
    LinearLayout empty_ref;
    ProgressBar ref_progress;



    public ReferralBottomsheet() {
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        empty_ref = view.findViewById(R.id.empty_ref);
        ref_progress = view.findViewById(R.id.ref_progress);
        rv = view.findViewById(R.id.rvReferral);
        rv.setLayoutManager(new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false));


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

//                    Log.d("REFERRALS ",refs.toString());

                    JSONArray array = refs.toJSONArray(refs.names());
                    final JSONArray farray = new JSONArray();
                    for (int i = 0; i < array.length(); i++) {
                        String coin = refs.names().get(i).toString();
                        String total = refs.getJSONObject(coin).getString("total");
                        String pend = refs.getJSONObject(coin).getString("pend");

                        if(!total.equals("0") || !pend.equals("0")){
                            farray.put(i,new JSONObject()
                                    .put("currency",coin)
                                    .put("pend",pend)
                                    .put("total",total));
                        }
                    }


                    //Log.d("RESPONSE____",array.toString());
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(farray.length()>0){
                                ref_progress.setVisibility(View.GONE);
                                rv.setAdapter(new ReferralBottomsheet.MyRecyclerViewAdapter(farray,getContext()));
                            }else{
                                ref_progress.setVisibility(View.GONE);
                                empty_ref.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ReferralBottomsheet.MyRecyclerViewAdapter.ViewHolder> {

        private final JSONArray mValues;
        private Context context;

        public MyRecyclerViewAdapter(JSONArray items, Context context) {
            mValues = items;
            this.context =context;
        }

        @Override
        public ReferralBottomsheet.MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragement_referral_list_item, parent, false);
            return new ReferralBottomsheet.MyRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReferralBottomsheet.MyRecyclerViewAdapter.ViewHolder holder, int position) {

            String currname,total,pending;
            try{
                holder.mItem = mValues.getJSONObject(position);

                currname = mValues.getJSONObject(position).getString("currency");
                total =  "Total : "+mValues.getJSONObject(position).getString("total");
                pending = "Pending : "+mValues.getJSONObject(position).getString("pend");

            }catch(Exception e){
                currname = total = pending = "";
            }

            try {
                holder.mIcon.setImageResource(MyResourcesClass.COIN_ICON.getInt(currname));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.mCurrency.setText(currname.toUpperCase());
            holder.mPending.setText(pending);
            holder.mTotal.setText(total);
        }

        @Override
        public int getItemCount() {
            return mValues.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mCurrency, mPending,mTotal;
            public JSONObject mItem;
            public ImageView mIcon;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mCurrency = view.findViewById(R.id.tvAccountCurr);
                mPending = view.findViewById(R.id.tvAccountPending);
                mTotal = view.findViewById(R.id.tvAccountTotal);
                mIcon = view.findViewById(R.id.ivicon);


            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }
        }
    }
}
