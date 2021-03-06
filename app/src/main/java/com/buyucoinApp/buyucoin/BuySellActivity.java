package com.buyucoinApp.buyucoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.bottomsheets.BuySellConfirm;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import io.fabric.sdk.android.Fabric;


public class BuySellActivity extends AppCompatActivity{
    String type,coin;
    Double price;
    Button order_btn;
    RadioButton sell_radio_btn,buy_radio_btn;
    RadioGroup radioGroup;
    EditText order_quantity,order_price;
    TextView wallet_inr_tv,order_coin_tv,latest_price_tv,order_fees,order_total;
    String WalletInr;
    DatabaseReference myRef;
    BuyucoinPref buyucoinPref;
    final String fees = "0.5% to 1.0% including GST";
    public static String coin_buy_price;
    public static String coin_sell_price;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Buy / Sell");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching latest Price...");
        progressDialog.show();





        buyucoinPref = new BuyucoinPref(this);

        final DecimalFormat decimalFormat = new DecimalFormat("0.####");

        WalletInr = decimalFormat.format(Double.valueOf(buyucoinPref.getPrefString("inr_amount")));


        Intent i = getIntent();

        price = i.getStringExtra("price")!=null?Double.valueOf(i.getStringExtra("price")):0;
        type = i.getStringExtra("type")!=null?i.getStringExtra("type"):"buy";
        coin = i.getStringExtra("type")!=null?i.getStringExtra("coin_name"):"";

        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
        //Toast.makeText(getApplicationContext(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();





        order_btn = findViewById(R.id.order_btn);

        radioGroup = findViewById(R.id.buy_sell_radio_group);

        buy_radio_btn = findViewById(R.id.buy_radio_btn);
        sell_radio_btn = findViewById(R.id.sell_radio_btn);



        order_quantity = findViewById(R.id.order_quantity_edittext);
        order_price = findViewById(R.id.order_price_edittext);



        wallet_inr_tv = findViewById(R.id.wallet_inr);
        wallet_inr_tv.setText(WalletInr);

        order_coin_tv = findViewById(R.id.order_coin_textview);
        latest_price_tv = findViewById(R.id.coin_price_latest);

        order_fees = findViewById(R.id.order_fees);
        order_total = findViewById(R.id.order_total);

        order_coin_tv.setText(coin.toUpperCase());


        if(type.equals("buy")){
            buy_radio_btn.setChecked(true);
            type = "buy";
        }
        if(type.equals("sell")) {
            sell_radio_btn.setChecked(true);
            type = "sell";

        }

        order_btn.setText(type);
        order_price.setEnabled(false);
        order_quantity.setEnabled(false);

        order_fees.setText(String.valueOf(fees));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sell_radio_btn:
                        updateLayout("sell", coin_sell_price);
                        break;
                    case R.id.buy_radio_btn:
                        updateLayout("buy", coin_buy_price);
                        break;
                }
            }
        });



        if (coin!=null){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String marketstring = "market_"+coin;
                    DataSnapshot data = dataSnapshot.child(marketstring).child("data");
                    Double bs  = data.child("ask").getValue(Double.class);
                    Double ss = data.child("bid").getValue(Double.class);

                        coin_buy_price = decimalFormat.format(bs);
                        coin_sell_price = decimalFormat.format(ss);
                        String coin_price;
                        if(type.equals("buy")){
                            coin_price = String.valueOf(coin_buy_price);
                        }else{
                            coin_price = String.valueOf(coin_sell_price);

                        }
                        order_price.setText(String.valueOf(coin_price));
                        String ls = "1 "+coin.toUpperCase()+" = "+coin_price;
                        latest_price_tv.setText(ls);

                        order_price.setEnabled(true);
                        order_quantity.setEnabled(true);


//                    new CoustomToast(getApplicationContext(),BuySellActivity.this,coin+" price :"+coin_price,CoustomToast.TYPE_NORMAL).showToast();

                    Log.d("MARKET___", data.toString());
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            boolean b = false;
            @Override
            public void afterTextChanged(Editable s) {
                if (b) return;
                b = true;
                try{

                    if(!order_price.getText().toString().equals("") && !order_quantity.getText().toString().equals("")){


                Double op = Double.parseDouble(order_price.getText().toString());
                Double oq = Double.parseDouble(order_quantity.getText().toString());

                Double total = op * oq;

                order_total.setText(String.valueOf(total));
                    }

                }catch (Exception e){
                    e.printStackTrace();
//                    Toast.makeText(BuySellActivity.this, "WRONG INPUT TYPE", Toast.LENGTH_SHORT).show();
                    order_total.setText("");
                }

                b = false;
            }
        };

        order_price.addTextChangedListener(watcher);
        order_quantity.addTextChangedListener(watcher);









        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quanitiy = order_quantity.getText().toString();
                String price  = order_price.getText().toString();

                if(!quanitiy.equals("") && !price.equals("")) {
                    String total = String.valueOf(Double.valueOf(price) * Double.valueOf(quanitiy));
                    if (!total.equals("") && !quanitiy.equals("") && !price.equals("")) {

                        Bundle bundle = new Bundle();
                        bundle.putString("quantity", quanitiy);
                        bundle.putString("price", price);
                        bundle.putString("fees", fees);
                        bundle.putString("total", total);
                        bundle.putString("type", type);
                        bundle.putString("coin", coin);

                        DialogFragment dialogFragment = BuySellConfirm.newInstance();
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getSupportFragmentManager(), "");


                    }
                    makeViewDisable(order_btn);
                }
                else{
                    String msg = (quanitiy.equals(""))?"Amount":(price.equals(""))?"Price":"Amount and Price";
                    new CoustomToast(getApplicationContext(),"Enter "+msg,CoustomToast.TYPE_DANGER).showToast();
                }

            }
        });

    }

    private void updateLayout(String type, String coin_price) {
        this.type = type;
        order_btn.setText(type);
//        order_fees.setText(String.valueOf(fees)+"%");
        String ls = "1 "+coin.toUpperCase()+" = "+coin_price;
        latest_price_tv.setText(ls);
        order_price.setText(String.valueOf(coin_price));
    }

    public  void makeViewDisable(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },1000);
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
