package com.buyucoin.buyucoin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesArea;
import com.anychart.anychart.Chart;
import com.anychart.anychart.Crosshair;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Stroke;
import com.anychart.anychart.ValueDataEntry;
import com.buyucoin.buyucoin.Adapters.AsksAdapter;
import com.buyucoin.buyucoin.Adapters.BidsAdapter;
import com.buyucoin.buyucoin.DataClasses.Markets;
import com.buyucoin.buyucoin.pojos.Ask;
import com.buyucoin.buyucoin.pojos.Bids;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CurrencyActivity extends AppCompatActivity {

    DatabaseReference myRef;
    AnyChartView anyChartView;
    Button sell,buy;
    Intent intent;
    Bundle bundle;
    ProgressBar pb;
    RecyclerView bids_recview,ask_recview;
    DatabaseReference myref;
    List<Markets> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //Toast.makeText(getApplicationContext(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();



        pb = (ProgressBar) findViewById(R.id.pbCurrencyActivity);
        buy = findViewById(R.id.tvCurrencyBuy);
        sell = findViewById(R.id.tvCurrencySell);
        bids_recview = findViewById(R.id.rvBid);
        ask_recview = findViewById(R.id.rvAsk);


        BidsAdapter bidsAdapter = new BidsAdapter(getApplicationContext(),Bids.randomBids());
        bids_recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bids_recview.setAdapter(bidsAdapter);

        AsksAdapter asksAdapter = new AsksAdapter(getApplicationContext(),Ask.randomAsks());
        ask_recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ask_recview.setAdapter(asksAdapter);

        Log.d("ASK LIST SIZE", String.valueOf(Ask.randomAsks().size()));
        Log.d("BIDS LISR SIZE",String.valueOf(Bids.randomBids().size()));

    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = getIntent();
        bundle = intent.getExtras();

        TextView tv = findViewById(R.id.tvCurrencyCurr);
        TextView hrs24 = findViewById(R.id.tvCurrency24Hrs);
        ImageView img = findViewById(R.id.ivCurrencyImg);

        final String s = bundle.getString("currency").split("_")[0];


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String marketstring = "market_"+s;
                DataSnapshot data = dataSnapshot.child(marketstring).child("data");
                Log.d("MARKET___", data.toString());

                list = new ArrayList<>();
                list.clear();
                if(data.hasChild("buy_orders")){
                    for(DataSnapshot d : data.child("buy_orders").getChildren()){
                        Double price = d.child("price").getValue(Double.class);
                        String value = d.child("value").getValue(String.class);
                        Double vol = d.child("vol").getValue(Double.class);
                        list.add(new Markets(price+"", value, vol+""));
                    }
                }
                Log.d("LIST SIZE__", list.size()+"");


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
        tv.setText(s.toUpperCase());
        buy.setText("Buy \u20B9 "+bundle.getString("ask"));
        sell.setText("Sell \u20B9 "+bundle.getString("bid"));
        hrs24.setText("24Hrs Change "+bundle.getString("high_24"));

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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = null;
                if (response.body() != null) {
                    s = response.body().string();

               //     Log.d("currency response", s);
                    try {
                        JSONObject object = new JSONObject(s).getJSONObject("data");
                        JSONArray prices = object.getJSONArray("price");
                        JSONArray time = object.getJSONArray("time");
                        initialiseGraph(time, prices);

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void BuySellButtonFunction(String type, String price) {
            Intent buysellintent = new Intent(this,BuySellActivity.class);
            buysellintent.putExtra("price",price);
            buysellintent.putExtra("type",type);
            startActivity(buysellintent);
    }

    public void initialiseGraph(JSONArray x, JSONArray y){

        Cartesian areaChart = AnyChart.area();

        areaChart.setAnimation(true);
        Crosshair crosshair = areaChart.getCrosshair();
        crosshair.setEnabled(true);
        crosshair.setYStroke((Stroke)null, null, null, null, null)
                .setXStroke("#000", 1d, null, null, null)
                .setZIndex(39d);

        areaChart.setTitle("chart");
        areaChart.setLabel(false);

        addToGraph(x, y);
    }

    public void addToGraph(JSONArray x, JSONArray y) {
        List<DataEntry> seriesdata = new ArrayList<>();
        for(int i=0; i<x.length(); i++){
            try {
                seriesdata.add(new ValueDataEntry(x.getString(i), y.getDouble(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Cartesian areaChart = AnyChart.area();
        CartesianSeriesArea area = areaChart.area(seriesdata);

            anyChartView = findViewById(R.id.any_chart_view);
            anyChartView.setChart(areaChart);
            anyChartView.setHorizontalScrollBarEnabled(true);


        Utilities.hideProgressBar(pb);
    }




    private class CustomDataEntry extends ValueDataEntry{
        CustomDataEntry(String x, Integer value, Integer value1) {
            super(x, value);
            setValue(x, value);
            setValue(x, value1);
        }
    }
}
