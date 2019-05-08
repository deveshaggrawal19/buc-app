package com.buyucoinApp.buyucoin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.pojos.OTCData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.Objects;

public class OTCActivity extends AppCompatActivity {
     private String OTC_TYPE = "BUY";
     private String OTC_RATE = "0";
    private String OTC_MIN_QTY = null;
    private String OTC_MAX_QTY = null;
     private  OTCData o = null;
     private EditText otc_qty;
     private TextView otc_total_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otc);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("OTC Desk");

        final String currency = getIntent().getStringExtra("currency");
        final TextView coin_name_tv = findViewById(R.id.coin_name_tv);

        final TextView buy_price_tv = findViewById(R.id.buy_price);
        final TextView sell_price_tv = findViewById(R.id.sell_price);
        final TextView buy_vol_tv = findViewById(R.id.buy_vol);
        final TextView sell_vol_tv = findViewById(R.id.sell_vol);
        final RadioGroup otc_type_rgb= findViewById(R.id.otc_type_rgb);
        final Button otc_btn = findViewById(R.id.otc_btn);
        final ImageView otc_img = findViewById(R.id.otc_img);
        otc_qty = findViewById(R.id.otc_qty);
        otc_total_tv = findViewById(R.id.otc_total_tv);


        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
        DatabaseReference myRef = db.getReference();

        otc_type_rgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        otc_btn.setBackgroundResource(R.color.bids_color);
                        otc_btn.setText(getResources().getString(R.string.buy));
                        OTC_TYPE = "BUY";
                        if(o!=null) {
                            OTC_MIN_QTY = o.getBuy_min();
                            OTC_MAX_QTY = o.getBuy_max();
                            OTC_RATE = o.getBuy_rate();
                            totalPrice();
                        }
                        break;
                    case R.id.radioButton2:
                        otc_btn.setBackgroundResource(R.color.colorRed);
                        otc_btn.setText(getResources().getString(R.string.sell));
                        OTC_TYPE = "SELL";
                        if(o!=null) {
                            OTC_MIN_QTY = o.getSell_min();
                            OTC_MAX_QTY = o.getSell_max();
                            OTC_RATE = o.getSell_rate();
                            totalPrice();
                        }
                        break;
                        default:
                }
            }
        });

        if(currency !=null){
//            coin_name_tv.setText(currency);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot otc_crypto_val = dataSnapshot.child("otc/"+currency);

               o = new OTCData(
                        otc_crypto_val.getKey(),
                        otc_crypto_val.child("buy_margin").getValue(String.class),
                        otc_crypto_val.child("buy_max").getValue(String.class),
                        otc_crypto_val.child("buy_min").getValue(String.class),
                        otc_crypto_val.child("buy_positive").getValue(String.class),
                        otc_crypto_val.child("buy_rate").getValue(String.class),
                        otc_crypto_val.child("buy_rate_btc").getValue(String.class),
                        otc_crypto_val.child("buy_vol").getValue(String.class),
                        String.valueOf(otc_crypto_val.child("min_trade").getValue(Object.class)),
                        otc_crypto_val.child("name").getValue(String.class),
                        otc_crypto_val.child("positive").getValue(String.class),
                        otc_crypto_val.child("sell_margin").getValue(String.class),
                        otc_crypto_val.child("sell_max").getValue(String.class),
                        otc_crypto_val.child("sell_min").getValue(String.class),
                        otc_crypto_val.child("sell_positive").getValue(String.class),
                        otc_crypto_val.child("sell_rate").getValue(String.class),
                        otc_crypto_val.child("sell_rate_btc").getValue(String.class),
                        otc_crypto_val.child("sell_vol").getValue(String.class),
                        otc_crypto_val.child("working_None").getValue(String.class),
                        otc_crypto_val.child("working_buy").getValue(String.class),
                        otc_crypto_val.child("working_sell").getValue(String.class)
                );


                coin_name_tv.setText(o.getName());
                buy_price_tv.setText(o.getBuy_rate());
                sell_price_tv.setText(o.getSell_rate());
                buy_vol_tv.setText(o.getBuy_vol());
                sell_vol_tv.setText(o.getSell_vol());
                OTC_RATE = o.getBuy_rate();
                OTC_MIN_QTY= o.getBuy_min();
                OTC_MAX_QTY= o.getBuy_max();
                try {
                    otc_img.setImageResource(MyResourcesClass.COIN_ICON.getInt(currency));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }

        otc_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

//            boolean b = false;
            @Override
            public void afterTextChanged(Editable s) {
//                if (b) return;
//                b = true;
                try{

                   totalPrice();

                }catch (Exception e){
                    e.printStackTrace();
//                    Toast.makeText(BuySellActivity.this, "WRONG INPUT TYPE", Toast.LENGTH_SHORT).show();
                    String error_s = "Total : 0";
                    otc_total_tv.setText(error_s);
                }

//                b = false;
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void totalPrice(){
        if(!otc_qty.getText().toString().equals("") && !OTC_RATE.equals("0")){


            Double qty = Double.parseDouble(otc_qty.getText().toString());
            Double rate = Double.parseDouble(OTC_RATE);
            Double min = Double.valueOf(OTC_MIN_QTY);
            Double max = Double.valueOf(OTC_MAX_QTY);
            String m;
            if(qty < min){
                 m = "Minimum "+OTC_TYPE.toLowerCase()+" is "+OTC_MIN_QTY;
                otc_total_tv.setTextColor(getResources().getColor(R.color.colorRed));
            }
            else if(qty > max){
                m = "Maximum "+OTC_TYPE.toLowerCase()+" is "+OTC_MAX_QTY;
                otc_total_tv.setTextColor(getResources().getColor(R.color.colorRed));
            }else{
                double total = (qty*rate);
                m = "Total : "+total;
                otc_total_tv.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            otc_total_tv.setText(m);
        }else{
            String error_s = "Total : 0";
            otc_total_tv.setText(error_s);
        }
    }
}
