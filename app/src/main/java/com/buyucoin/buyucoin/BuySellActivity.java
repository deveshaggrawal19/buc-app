package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.buyucoin.buyucoin.Interfaces.BuyDialogFunction;
import com.buyucoin.buyucoin.customDialogs.CustomDialogs;
import com.buyucoin.buyucoin.Interfaces.SellDialogFunction;

public class BuySellActivity extends AppCompatActivity implements BuyDialogFunction,SellDialogFunction {
    String type;
    Double price;
    Button sell_button,buy_button,sell,buy;
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

        buy = findViewById(R.id.buy_layout_buy_btn);
        sell = findViewById(R.id.sell_layout_sell_btn);


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

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("quantity","453");
                bundle.putString("price","10000");
                bundle.putString("fees","0.5 - 1.0 GST");
                bundle.putString("total","10100");
                BuyDialogFunction buyDialogFunction = new BuySellActivity();

                CustomDialogs coustomDialogs = new CustomDialogs(BuySellActivity.this,bundle,buyDialogFunction,"BUY ORDER");
                coustomDialogs.confirmBuyDialog();
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("quantity","453");
                bundle.putString("price","10000");
                bundle.putString("fees","0.5 - 1.0 GST");
                bundle.putString("total","10100");
                SellDialogFunction sellDialogFunction = new BuySellActivity();

                CustomDialogs coustomDialogs = new CustomDialogs(BuySellActivity.this,bundle,sellDialogFunction,"SELL ORDER");
                coustomDialogs.confirmSellDialog();
            }
        });











    }

    public void changeButtonParemeter(Button open, Button close) {
        open.setBackground(getDrawable(R.drawable.pills));
        open.setTextColor(Color.WHITE);

        close.setBackgroundColor(Color.WHITE);
        close.setTextColor(Color.BLACK);
    }

    public void changeLayoutParameter(LinearLayout open, LinearLayout close) {
        open.setVisibility(View.VISIBLE);

        close.setVisibility(View.GONE);


    }

    @Override
    public boolean buyFunction(Context context,String amount, String price) {
        Toast.makeText(context,"Amount : "+amount+", Price : "+price,Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean sellFunction(Context context,String amount,String price) {
        Toast.makeText(context,"Amount : "+amount+", Price : "+price,Toast.LENGTH_LONG).show();
        return false;
    }
}
