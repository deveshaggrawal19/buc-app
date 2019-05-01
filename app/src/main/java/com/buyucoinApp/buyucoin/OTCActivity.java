package com.buyucoinApp.buyucoin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.pojos.OTCData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otc);

        final String currency = getIntent().getStringExtra("currency");
        final TextView coin_name_tv = findViewById(R.id.coin_name_tv);

        final TextView buy_price_tv = findViewById(R.id.buy_price);
        final TextView sell_price_tv = findViewById(R.id.sell_price);
        final TextView buy_vol_tv = findViewById(R.id.buy_vol);
        final TextView sell_vol_tv = findViewById(R.id.sell_vol);
        final RadioGroup otc_type_rgb= findViewById(R.id.otc_type_rgb);
        final Button otc_btn = findViewById(R.id.otc_btn);

        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
        DatabaseReference myRef = db.getReference();

        otc_type_rgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        otc_btn.setBackgroundResource(R.color.bids_color);
                        break;
                    case R.id.radioButton2:
                        otc_btn.setBackgroundResource(R.color.colorRed);
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

                OTCData o = new OTCData(
                        otc_crypto_val.getKey(),
                        otc_crypto_val.child("buy_margin").getValue(String.class),
                        otc_crypto_val.child("buy_max").getValue(String.class),
                        otc_crypto_val.child("buy_min").getValue(String.class),
                        otc_crypto_val.child("buy_positive").getValue(String.class),
                        otc_crypto_val.child("buy_rate").getValue(String.class),
                        otc_crypto_val.child("buy_rate_btc").getValue(String.class),
                        otc_crypto_val.child("buy_vol").getValue(String.class),
                        otc_crypto_val.child("min_trade").getValue(String.class),
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }



    }
}
