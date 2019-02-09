package com.buyucoin.buyucoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.buyucoin.buyucoin.Adapters.DepositeWithdrawPagerAdapter;
import com.crashlytics.android.Crashlytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import io.fabric.sdk.android.Fabric;

public class CoinDepositWithdraw extends AppCompatActivity {
    private static final String DEPOSITE = "DEPOSITE";
    private static final String WITHDRAW = "WITHDRAW";

    Intent i;


    ViewPager viewPager;
    RadioGroup radioGroup;
    RadioButton depoite_rb, withdraw_rb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_coin_deposite_withdraw);

        i = getIntent();

        String TYPE = i.getStringExtra("type");
        String COIN = i.getStringExtra("coin_name");
        String AVAILABEL = i.getStringExtra("available");
        String ADDRESS = i.getStringExtra("address");
        String DESCRIPTION = i.getStringExtra("description");
        String TAG = i.getStringExtra("tag");
        String COIN_FULL_NAME = i.getStringExtra("full_coin_name");

        Bundle b = new Bundle();

        b.putString("type", TYPE);
        b.putString("coin", COIN);
        b.putString("available", AVAILABEL);
        b.putString("address", ADDRESS);
        b.putString("description", DESCRIPTION);
        b.putString("tag", TAG);
        b.putString("coin_full_name", COIN_FULL_NAME);


        radioGroup = findViewById(R.id.deposit_withdraw_rg);

        depoite_rb = findViewById(R.id.deposite_layout_rb);
        withdraw_rb = findViewById(R.id.withdraw_layout_rb);

        viewPager = findViewById(R.id.deposite_withdrwa_viewpager);


        viewPager.setAdapter(new DepositeWithdrawPagerAdapter(getSupportFragmentManager(), b));

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


}
