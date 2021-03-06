package com.buyucoinApp.buyucoin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.Adapters.AsksAdapter;
import com.buyucoinApp.buyucoin.Adapters.BidsAdapter;
import com.buyucoinApp.buyucoin.Adapters.MarketHistoryAdapter;
import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.pojos.Ask;
import com.buyucoinApp.buyucoin.pojos.Bids;
import com.buyucoinApp.buyucoin.pojos.MarketHistory;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CurrencyActivity extends AppCompatActivity {

    DatabaseReference myRef;
    GraphView graphView;
    Button sell,buy;
    Intent intent;
    Bundle bundle;
    ProgressBar pb;
    RecyclerView bids_recview,ask_recview,market_recview;
    DatabaseReference myref;
    ArrayList<Bids> arrayListBids ;
    ArrayList<Ask> arrayListAsks ;
    ArrayList<MarketHistory> arrayListMarketHistory;
    TextView hight24,low24,change24;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        init();

    }

    public void init(){
        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
        //Toast.makeText(getApplicationContext(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Market / Order Book");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        pb = findViewById(R.id.pbCurrencyActivity);
        buy = findViewById(R.id.tvCurrencyBuy);
        sell = findViewById(R.id.tvCurrencySell);
        bids_recview = findViewById(R.id.rvBid);
        ask_recview = findViewById(R.id.rvAsk);
        market_recview = findViewById(R.id.rvMarketHIstory);
        hight24 = findViewById(R.id.high24);
        low24 = findViewById(R.id.low24);
        change24 = findViewById(R.id.high24change);


        bids_recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ask_recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        market_recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        intent = getIntent();
        bundle = intent.getExtras();
        assert bundle != null;
        s = bundle.getString("currency");
    }


    @Override
    protected void onStart() {
        super.onStart();

        init();

        TextView tv = findViewById(R.id.tvCurrencyCurr);
        ImageView img = findViewById(R.id.ivCurrencyImg);




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String marketstring = "market_"+s;
                DataSnapshot data = dataSnapshot.child(marketstring).child("data");
                DataSnapshot crypto = data.child("crypto");
//                Log.d("MARKET___", data.toString());

                hight24.setText(crypto.child("high_24").getValue(String.class));
                change24.setText(crypto.child("change").getValue(String.class));
                low24.setText(crypto.child("low_24").getValue(String.class));

                arrayListBids = new ArrayList<>();
                arrayListAsks = new ArrayList<>();
                arrayListMarketHistory = new ArrayList<>();

                arrayListBids.clear();
                arrayListAsks.clear();


                try {
                    if(data.hasChild("buy_orders")){
                        for(DataSnapshot d : data.child("buy_orders").getChildren()){
                            Double price = d.child("price").getValue(Double.class);
                            String value = d.child("value").getValue(String.class);
                            Bids b = null;
                            try{
                                String vol = d.child("vol").getValue(String.class);
                                b = new Bids(price,value,Double.valueOf(vol));
                            }catch (Exception e){
                                Double vol = d.child("vol").getValue(Double.class);
                                b = new Bids(price,value,vol);
                            }
                            arrayListBids.add(b);
                        }
                        Collections.sort(arrayListBids, new Comparator<Bids>() {
                            @Override
                            public int compare(Bids o1, Bids o2) {
                                if(o1.getBid_price() > o2.getBid_price())return 1;
                                if(o1.getBid_price().equals(o2.getBid_price()))return 0;
                                else return -1;
                            }
                        });
                        Collections.reverse(arrayListBids);
                        BidsAdapter bidsAdapter = new BidsAdapter(getApplicationContext(),arrayListBids);
                        bids_recview.setAdapter(bidsAdapter);
                    }
                    if(data.hasChild("sell_orders")){
                        for(DataSnapshot d : data.child("sell_orders").getChildren()){
                            Double price = d.child("price").getValue(Double.class);
                            String value = d.child("value").getValue(String.class);
                            Ask a = null;
                            try{
                                String vol = d.child("vol").getValue(String.class);
                                a = new Ask(price,value,Double.valueOf(vol));
                            }catch (Exception e){
                                Double vol = d.child("vol").getValue(Double.class);
                                a = new Ask(price,value,vol);
                            }
                            arrayListAsks.add(a);
                        }
                        AsksAdapter asksAdapter = new AsksAdapter(getApplicationContext(),arrayListAsks);
                        ask_recview.setAdapter(asksAdapter);
                    }
                    if (data.hasChild("market_history")){
                        for(DataSnapshot d : data.child("market_history").getChildren()){
                            Double amount = d.child("amount").getValue(Double.class);
                            Double price = d.child("price").getValue(Double.class);
                            String value = d.child("value").getValue(String.class);
                            Long time = d.child("time").getValue(Long.class);
                            String type = d.child("type").getValue(String.class);

                            MarketHistory m = new MarketHistory();
                            m.setAmount(amount);
                            m.setPrice(price);
                            m.setTime(time);
                            m.setType(type);
                            m.setValue(value);

                            arrayListMarketHistory.add(m);
                        }
                        MarketHistoryAdapter marketHistoryAdapter = new MarketHistoryAdapter(getApplicationContext(),arrayListMarketHistory);
                        market_recview.setAdapter(marketHistoryAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Log.d("LIST SIZE__", arrayListAsks.size()+""+arrayListBids.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value", databaseError.toException());
            }
        });


        try {
            img.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(s, "drawable", getPackageName())));
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            String coin_full_name = MyResourcesClass.COIN_FULL_NAME.getString(s);
            tv.setText(coin_full_name.toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
            tv.setText(s.toUpperCase());
        }

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySellButtonFunction("buy",bundle.getString("ask"));

            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySellButtonFunction("sell",bundle.getString("bid"));
            }
        });




        OkHttpHandler.get("https://www.buyucoin.com/market-graph?currency=" + s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.hideProgressBar(pb);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = null;
                if (response.body() != null) {
                    s = response.body().string();

//                    Log.d("currency response", s);
                    try {
                        JSONObject object = new JSONObject(s).getJSONObject("data");
                        JSONArray prices = object.getJSONArray("price");
                        JSONArray time = object.getJSONArray("time");

                        List<XY> list = new ArrayList<>();
                        for(int i=0; i<prices.length(); i++){
                            list.add(new XY(time.getString(i), prices.getDouble(i)));
                        }
                        addToGraph(list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public double removeExtraZero(String s){
        if(s.contains("0000")){
            return Double.parseDouble(s)/10000;
        }
        else{
            return Double.parseDouble(s);
        }
    }

    public class XY{
        public String x;
        public Double y;
        public XY(String x, Double y){
            this.x = x;
            this.y = y;
        }
    }

    private void BuySellButtonFunction(String type, String price) {
            Intent buysellintent = new Intent(this,BuySellActivity.class);
            buysellintent.putExtra("price",price);
            buysellintent.putExtra("type",type);
            buysellintent.putExtra("coin_name",s);
            startActivity(buysellintent);
    }

    public void addToGraph(List<XY> list) {
//        Log.d("ARRAY LENGTH", list.size()+ "");

        Collections.sort(list, new Comparator<XY>() {
            DateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            @Override
            public int compare(XY xy, XY t1) {
                try {
                    return f.parse(xy.x).compareTo(f.parse(t1.x));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });


        DataPoint[] dp = new DataPoint[list.size()];
        try {
            for (int i = 0; i < list.size(); i++) {
                DateFormat df = new SimpleDateFormat("yy-MM-dd hh:mm:ss", Locale.ENGLISH);
                Date date = df.parse(list.get(i).x);
                dp[i] = new DataPoint(date, list.get(i).y);
            }
        } catch(ParseException e){
            e.printStackTrace();
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
        series.setThickness(5);
        graphView = (GraphView) findViewById(R.id.graphView);
        GridLabelRenderer gl = graphView.getGridLabelRenderer();
        gl.setGridColor(Color.TRANSPARENT);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setNumVerticalLabels(10);
        graphView.getGridLabelRenderer().setTextSize(20f);
        graphView.addSeries(series);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                graphView.setVisibility(View.VISIBLE);
                Utilities.hideProgressBar(pb);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        s = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
