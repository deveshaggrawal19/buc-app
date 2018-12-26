package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BuySellActivity extends AppCompatActivity {
    String type;
    Double price;
    Button sell_button,buy_button;
    LinearLayout sell_layout,buy_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        Intent i = getIntent();

        price = Double.valueOf(i.getStringExtra("price"));
        type = i.getStringExtra("type");

        sell_button = findViewById(R.id.sell_layout_btn);
        buy_button = findViewById(R.id.buy_layout_btn);

        sell_layout = findViewById(R.id.sell_layout);
        buy_layout = findViewById(R.id.buy_layout);

        if(type.equals("buy")){
            changeButtonParemeter(buy_button,sell_button);
            changeLayoutParameter(buy_layout,sell_layout);

        }
        if(type.equals("sell")) {
            changeButtonParemeter(sell_button,buy_button);
            changeLayoutParameter(sell_layout,buy_layout);
        }

        sell_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonParemeter(sell_button,buy_button);
                changeLayoutParameter(sell_layout,buy_layout);
            }
        });

        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonParemeter(buy_button,sell_button);
                changeLayoutParameter(buy_layout,sell_layout);

            }
        });











    }

    private void changeButtonParemeter(Button open, Button close) {
        open.setBackground(getDrawable(R.drawable.pills));
        open.setTextColor(Color.WHITE);

        close.setBackgroundColor(Color.WHITE);
        close.setTextColor(Color.BLACK);
    }

    private void changeLayoutParameter(LinearLayout open, LinearLayout close) {
        open.setVisibility(View.VISIBLE);

        close.setVisibility(View.GONE);


    }
}
