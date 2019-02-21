package com.buyucoinApp.buyucoin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.MyResourcesClass;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pojos.History;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CoinActiveOrderAdapter extends RecyclerView.Adapter<CoinActiveOrderAdapter.ViewHolder> {

    private final ArrayList<History> mValues;
    private boolean isCanceld = true;
    private String mainmsg = "hi";
    private Context context;

    public CoinActiveOrderAdapter(ArrayList<History> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CoinActiveOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CoinActiveOrderAdapter.ViewHolder holder, final int position) {
        try {
            holder.mItem = mValues.get(position);


            holder.mCurrency.setText(holder.mItem.getCurr().toUpperCase());
            holder.mAmount.setText(String.valueOf(holder.mItem.getAmount()));
            if (holder.mItem.getOpen() != "") {

                holder.mOpenTime.setText(holder.mItem.getOpen());
            } else {

                holder.mOpenTime.setText(holder.mItem.getOpen_time());
            }
            holder.mTxHash.setText(holder.mItem.getTx_hash());

            if (holder.mItem.getType().equals("Buy")) {
                holder.mType.setImageResource(R.drawable.history_deposite_icon);
            } else {
                holder.mType.setImageResource(R.drawable.history_withdraw_icon);
            }


            switch (holder.mItem.getStatus()) {
                case "Pending":
                    holder.mStatus.setVisibility(View.GONE);
                    holder.cancel_btn.setVisibility(View.VISIBLE);
                    break;
                case "Success":
                    holder.mStatus.setText(holder.mItem.getStatus());
                    holder.mStatus.setTextColor(context.getResources().getColor(R.color.kyc_color));

                    break;
                case "Cancelled":
                    holder.mStatus.setText(holder.mItem.getStatus());
                    holder.mStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
                    break;
                default:
                    holder.mStatus.setVisibility(View.GONE);
                    holder.cancel_btn.setVisibility(View.VISIBLE);

            }

            holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String currency = holder.mItem.getCurr();
                    int id = holder.mItem.getId();
                    final JSONObject cancel_order = new JSONObject();
                    try {
                        cancel_order.put("id", id);
                        new AlertDialog.Builder(context).setMessage("Do you want to delete this Order ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ProgressDialog p = new ProgressDialog(context);
                                        p.setMessage("processing");
                                        p.show();
                                        boolean b = cancelOrder(currency, cancel_order.toString());
                                        if(b){
                                            new CoustomToast(context,mainmsg,CoustomToast.TYPE_SUCCESS).showToast();
                                            mValues.remove(position);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();
                                            p.dismiss();
                                            dialog.dismiss();
                                        }else{
                                            new CoustomToast(context,mainmsg,CoustomToast.TYPE_DANGER).showToast();
                                            dialog.dismiss();
                                        }
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            holder.mCurrency.setText("N/A");
            holder.mAmount.setText("N/A");
            holder.mOpenTime.setText("N/A");
            holder.mTxHash.setText("N/A");
        }

        try {
            holder.mImage.setImageResource(MyResourcesClass.COIN_ICON.getInt(holder.mItem.getCurr()));
        } catch (Exception e) {
            holder.mImage.setVisibility(View.GONE);
        }


    }

    public boolean cancelOrder(String s1, String s2) {
        OkHttpHandler.auth_post("cancel_order?currency="+s1, new BuyucoinPref(context).getPrefString(BuyucoinPref.ACCESS_TOKEN), s2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isCanceld = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String s = response.body().string();
                try {
                    JSONObject j = new JSONObject(s);
                    Log.d(context.getPackageName()+"===>", "onResponse:"+j.toString());
                    final String status = j.getString("status");
                    mainmsg = j.getJSONArray("message").getJSONArray(0).getString(0);
                    switch (status){
                        case "success":
                            isCanceld=true;break;
                        case "error":isCanceld=false;break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return isCanceld;
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAmount, mCurrency, mOpenTime, mTxHash, mAddress, mFilled, mValue, mStatus;
        public final ImageView mImage, mType;
        public History mItem;
        public Button cancel_btn;

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
            cancel_btn = view.findViewById(R.id.cancel_pending_order);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
