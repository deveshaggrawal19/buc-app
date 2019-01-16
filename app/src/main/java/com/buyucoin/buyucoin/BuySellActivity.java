package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Interfaces.BuyDialogFunction;
import com.buyucoin.buyucoin.customDialogs.CustomDialogs;
import com.buyucoin.buyucoin.Interfaces.SellDialogFunction;
import com.buyucoin.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BuySellActivity extends AppCompatActivity {
    String type;
    Double price;
    Button sell_button,buy_button,sell,buy;
    LinearLayout sell_layout,buy_layout;
    EditText buy_amount,buy_price,sell_amount,sell_price;
    TextView wallet_inr_tv;
    String WalletInr;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        preferences = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE);

        WalletInr = preferences.getString("inr_amount","0");



        Intent i = getIntent();

        price = i.getStringExtra("price")!=null?Double.valueOf(i.getStringExtra("price")):0;
        type = i.getStringExtra("type")!=null?i.getStringExtra("type"):"buy";

        sell_button = findViewById(R.id.sell_layout_btn);
        buy_button = findViewById(R.id.buy_layout_btn);

        sell_layout = findViewById(R.id.sell_layout);
        buy_layout = findViewById(R.id.buy_layout);

        buy = findViewById(R.id.buy_layout_buy_btn);
        sell = findViewById(R.id.sell_layout_sell_btn);

        buy_amount = findViewById(R.id.buy_amount_edittext);
        sell_amount = findViewById(R.id.sell_amount_edittext);
        buy_price = findViewById(R.id.buy_price_edittext);
        sell_price = findViewById(R.id.sell_price_edittext);

        wallet_inr_tv = findViewById(R.id.wallet_inr);
        wallet_inr_tv.setText(WalletInr);


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

                String amount = buy_amount.getText().toString();
                String price  = buy_price.getText().toString();

                if(!amount.equals("") && !price.equals("")){

                Bundle bundle = new Bundle();
                bundle.putString("quantity",amount);
                bundle.putString("price",price);
                bundle.putString("fees","0.5% - 1.0% + GST");
                bundle.putString("total","10100");
                bundle.putString("type","buy");

                    DialogFragment dialogFragment = CustomDialogs.newInstance();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(),"");


                }

            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String amount = sell_amount.getText().toString();
                String price = sell_price.getText().toString();

                if (!amount.equals("") && !price.equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("quantity", amount);
                    bundle.putString("price", price);
                    bundle.putString("fees", "0.5 - 1.0 + GST");
                    bundle.putString("total", "10100");
                    bundle.putString("type","sell");

                    DialogFragment dialogFragment = CustomDialogs.newInstance();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(),"");



                }
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




}
