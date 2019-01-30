package com.buyucoin.buyucoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.CoinHistoryAdapter;
import com.buyucoin.buyucoin.Fragments.HistoryPagerAdapterFragmentOrder;
import com.buyucoin.buyucoin.Fragments.P2P_History;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pojos.History;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DepositWithdrawActivity extends AppCompatActivity {
    LinearLayout qr_layout, buy_layout, sell_layout, deposite_layout, withdraw_layout, empty_layout;
    ImageView imageView,card_coin_img;
    RecyclerView history_recyclerview;
    TextView card_coin_full_name, card_coin_availabel, card_coin_pending, card_coin_address, card_coin_base_address;
    Intent i;
    Button address_gen_btn;
    NestedScrollView nestedScrollView;
    BuyucoinPref pref;
    ProgressBar pb;
    String coin;
    private ArrayList<History> histories;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_withdraw);
        pref = new BuyucoinPref(getApplicationContext());
        InitializeAllViews();
        i = getIntent();
        histories = new ArrayList<>();

        final String COIN = coin = i.getStringExtra("coin_name");
        final String AVAILABEL = i.getStringExtra("available");
        final String PENDING = i.getStringExtra("pendings");
        final String ADDRESS = i.getStringExtra("address");
        final String BASE_ADDRESS = i.getStringExtra("base_address");
        final String DESCRIPTION = i.getStringExtra("description");
        final String TAG = i.getStringExtra("tag");
        final String COIN_FULL_NAME = i.getStringExtra("full_coin_name");

        card_coin_full_name.setText(COIN_FULL_NAME);
        card_coin_availabel.setText(AVAILABEL);
        card_coin_address.setText(ADDRESS);
        try {
            card_coin_img.setImageResource(MyResourcesClass.COIN_ICON.getInt(coin));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(BASE_ADDRESS!=null){
        card_coin_base_address.setText(BASE_ADDRESS);
        }else{
            card_coin_base_address.setVisibility(View.GONE);
        }
        card_coin_pending.setText(PENDING);


        if (ADDRESS.equals("null")) {
            card_coin_address.setVisibility(View.GONE);
            address_gen_btn.setVisibility(View.VISIBLE);

        }

        address_gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAddress(card_coin_address);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_layout.setVisibility(View.VISIBLE);
            }
        });

        qr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_layout.setVisibility(View.GONE);
            }
        });

        history_recyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        CoinHistoryAdapter coinHistoryAdapter = new CoinHistoryAdapter(getApplicationContext());
        history_recyclerview.setAdapter(coinHistoryAdapter);

        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(0, 0);
            }
        });

        buy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuySellActivity.class);
                intent.putExtra("type", "buy");
                intent.putExtra("price", "234567");
                intent.putExtra("coin_name", COIN);
                intent.putExtra("available", AVAILABEL);
                intent.putExtra("address", ADDRESS);
                intent.putExtra("description", DESCRIPTION);
                intent.putExtra("tag", TAG);
                intent.putExtra("full_coin_name", COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        sell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuySellActivity.class);
                intent.putExtra("type", "sell");
                intent.putExtra("price", "234567");
                intent.putExtra("coin_name", COIN);
                intent.putExtra("available", AVAILABEL);
                intent.putExtra("address", ADDRESS);
                intent.putExtra("description", DESCRIPTION);
                intent.putExtra("tag", TAG);
                intent.putExtra("full_coin_name", COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        deposite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoinDepositWithdraw.class);
                intent.putExtra("type", "DEPOSITE");
                intent.putExtra("coin_name", COIN);
                intent.putExtra("available", AVAILABEL);
                intent.putExtra("address", ADDRESS);
                intent.putExtra("description", DESCRIPTION);
                intent.putExtra("tag", TAG);
                intent.putExtra("full_coin_name", COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        withdraw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoinDepositWithdraw.class);
                intent.putExtra("type", "WITHDRAW");
                intent.putExtra("coin_name", COIN);
                intent.putExtra("available", AVAILABEL);
                intent.putExtra("address", ADDRESS);
                intent.putExtra("description", DESCRIPTION);
                intent.putExtra("tag", TAG);
                intent.putExtra("full_coin_name", COIN_FULL_NAME);
                startActivity(intent);

            }
        });

        address_gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAddress(card_coin_address);
            }
        });

        getList("order");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();
                return true;
             default:finish();
             return true;
        }
    }

    private void InitializeAllViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageView = findViewById(R.id.qrcodeimg);
        qr_layout = findViewById(R.id.qrcodelayout);
        history_recyclerview = findViewById(R.id.rvActiveCoinOrdrs);

        buy_layout = findViewById(R.id.buy_layout_card);
        sell_layout = findViewById(R.id.sell_layout_card);
        deposite_layout = findViewById(R.id.deposite_layout_card);
        withdraw_layout = findViewById(R.id.withdraw_layout_card);

        card_coin_full_name = findViewById(R.id.card_coin_full_name);
        card_coin_availabel = findViewById(R.id.card_coin_availabel);
        card_coin_address = findViewById(R.id.card_coin_address);
        card_coin_base_address = findViewById(R.id.card_coin_base_address);
        card_coin_pending = findViewById(R.id.card_coin_pending);
        card_coin_img = findViewById(R.id.card_coin_image);

        address_gen_btn = findViewById(R.id.card_coin_address_gen_btn);

        nestedScrollView = findViewById(R.id.card_coin_nested_view);
        pb = findViewById(R.id.order_pb);
        empty_layout = findViewById(R.id.empty_orders);
    }

    private void generateAddress(TextView card_coin_address) {

        card_coin_address.setText("ADDRESS GENERATED");

    }

    public void getList(final String url) {
        new CoustomToast(this, this, "loading", CoustomToast.TYPE_NORMAL);
        OkHttpHandler.auth_get(url + "_history", pref.getPrefString(BuyucoinPref.ACCESS_TOKEN), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        String s = response.body().string();

                        try {
                            final JSONArray array = new JSONObject(s).getJSONObject("data").getJSONArray(url.equals("order") ? "orders" : url + "_comp");
                            Log.d("sdfghjsdfghjk", array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject j = array.getJSONObject(i);
                        if(j.getString("curr").equals(coin)){
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

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (histories.size() > 0) {
                                        history_recyclerview.setAdapter(new MyHistoryRecyclerViewAdapter(histories));
                                        history_recyclerview.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                    } else {
                                        pb.setVisibility(View.GONE);
                                        empty_layout.setVisibility(View.VISIBLE);

                                    }
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    pb.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }

            }
        });
    }


    public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<History> mValues;

        public MyHistoryRecyclerViewAdapter(ArrayList<History> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public MyHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_history_list_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MyHistoryRecyclerViewAdapter.ViewHolder holder, int position) {
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
                holder.mImage.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(holder.mItem.getCurr(), "drawable", getPackageName())));
            } catch (Exception e) {
                holder.mImage.setVisibility(View.GONE);
            }


        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mAmount, mCurrency, mOpenTime, mTxHash, mAddress, mFilled, mValue;
            public final ImageView mImage, mStatus, mType;
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

