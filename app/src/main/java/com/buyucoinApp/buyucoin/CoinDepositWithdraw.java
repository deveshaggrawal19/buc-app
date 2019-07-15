package com.buyucoinApp.buyucoin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.buyucoinApp.buyucoin.Adapters.DepositeWithdrawPagerAdapter;
import com.crashlytics.android.Crashlytics;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class CoinDepositWithdraw extends AppCompatActivity {
    private static final String DEPOSITE = "DEPOSITE";
    private static final String WITHDRAW = "WITHDRAW";

    Intent i;


    ViewPager viewPager;
    RadioGroup radioGroup;
    RadioButton depoite_rb, withdraw_rb;
    JSONObject jsonObject;
    String TYPE ;
    String COIN ;
    String AVAILABEL ;
    String ADDRESS ;
    String DESCRIPTION ;
    String BASE_ADDRESS ;
    String TAG ;
    String COIN_FULL_NAME ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_coin_deposite_withdraw);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Deposit / Withdraw");

        i = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(i.getStringExtra("object"));




             TYPE = jsonObject.getString("type");
             COIN = jsonObject.getString("coin_name");
             AVAILABEL = jsonObject.getString("available");
             ADDRESS = jsonObject.getString("address");
             DESCRIPTION = jsonObject.getString("description");
             BASE_ADDRESS = jsonObject.getString("base_address");
             TAG = jsonObject.getString("tag");
             COIN_FULL_NAME = jsonObject.getString("full_coin_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }




        JSONObject b = new JSONObject();

        try {
            b.put("type", TYPE);
            b.put("coin", COIN);
            b.put("available", AVAILABEL);
            b.put("address", ADDRESS);
            b.put("description", DESCRIPTION);
            b.put("tag", TAG);
            b.put("coin_full_name", COIN_FULL_NAME);
            b.put("base_address",BASE_ADDRESS);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        radioGroup = findViewById(R.id.deposit_withdraw_rg);

        depoite_rb = findViewById(R.id.deposite_layout_rb);
        withdraw_rb = findViewById(R.id.withdraw_layout_rb);

        viewPager = findViewById(R.id.deposite_withdrwa_viewpager);


        viewPager.setAdapter(new DepositeWithdrawPagerAdapter(getSupportFragmentManager(), b.toString()));

        if (TYPE.equals(DEPOSITE)) {
            viewPager.setCurrentItem(0);
            depoite_rb.setChecked(true);
        } else {
            viewPager.setCurrentItem(1);
            withdraw_rb.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.deposite_layout_rb:
                        viewPager.setCurrentItem(0);

                        break;
                    case R.id.withdraw_layout_rb:
                        viewPager.setCurrentItem(1);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        depoite_rb.setChecked(true);
                        break;
                    case 1:
                        withdraw_rb.setChecked(true);
                        break;
                    default:
                        depoite_rb.setChecked(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
