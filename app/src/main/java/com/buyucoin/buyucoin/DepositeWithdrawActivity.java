package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.Adapters.CoinHistoryAdapter;

import java.util.ArrayList;

public class DepositeWithdrawActivity extends AppCompatActivity {
    LinearLayout qr_layout,buy_layout,sell_layout,deposite_layout,withdraw_layout;
    ImageView imageView;
    RecyclerView history_recyclerview;
    TextView card_coin_full_name,card_coin_availabel,card_coin_address;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_withdraw);

        imageView = findViewById(R.id.qrcodeimg);
        qr_layout = findViewById(R.id.qrcodelayout);
        history_recyclerview = findViewById(R.id.rvCoinHistory);

        buy_layout = findViewById(R.id.buy_layout_card);
        sell_layout = findViewById(R.id.sell_layout_card);
        deposite_layout = findViewById(R.id.deposite_layout_card);
        withdraw_layout = findViewById(R.id.withdraw_layout_card);

        card_coin_full_name = findViewById(R.id.card_coin_full_name);
        card_coin_availabel = findViewById(R.id.card_coin_availabel);
        card_coin_address = findViewById(R.id.card_coin_address);



        i = getIntent();

        final String COIN = i.getStringExtra("coin_name");
        final String AVAILABEL = i.getStringExtra("available");
        final String ADDRESS = i.getStringExtra("address");
        final String DESCRIPTION = i.getStringExtra("description");
        final String TAG = i.getStringExtra("tag");
        final String COIN_FULL_NAME = i.getStringExtra("full_coin_name");

        card_coin_full_name.setText(COIN_FULL_NAME);
        card_coin_availabel.setText(AVAILABEL);
        card_coin_address.setText(ADDRESS);
        

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

        buy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BuySellActivity.class);
                intent.putExtra("type","buy");
                intent.putExtra("price","234567");
                intent.putExtra("coin_name",COIN);
                intent.putExtra("available",AVAILABEL);
                intent.putExtra("address",ADDRESS);
                intent.putExtra("description",DESCRIPTION);
                intent.putExtra("tag",TAG);
                intent.putExtra("full_coin_name",COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        sell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BuySellActivity.class);
                intent.putExtra("type","sell");
                intent.putExtra("price","234567");
                intent.putExtra("coin_name",COIN);
                intent.putExtra("available",AVAILABEL);
                intent.putExtra("address",ADDRESS);
                intent.putExtra("description",DESCRIPTION);
                intent.putExtra("tag",TAG);
                intent.putExtra("full_coin_name",COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        deposite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CoinDepositeWithdraw.class);
                intent.putExtra("type","DEPOSITE");
                intent.putExtra("coin_name",COIN);
                intent.putExtra("available",AVAILABEL);
                intent.putExtra("address",ADDRESS);
                intent.putExtra("description",DESCRIPTION);
                intent.putExtra("tag",TAG);
                intent.putExtra("full_coin_name",COIN_FULL_NAME);
                startActivity(intent);

            }
        });
        withdraw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CoinDepositeWithdraw.class);
                intent.putExtra("type","WITHDRAW");
                intent.putExtra("coin_name",COIN);
                intent.putExtra("available",AVAILABEL);
                intent.putExtra("address",ADDRESS);
                intent.putExtra("description",DESCRIPTION);
                intent.putExtra("tag",TAG);
                intent.putExtra("full_coin_name",COIN_FULL_NAME);
                startActivity(intent);

            }
        });
    }


}

