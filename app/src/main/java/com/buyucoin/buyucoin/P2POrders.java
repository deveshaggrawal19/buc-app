package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.buyucoin.buyucoin.Adapters.P2P_PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class P2POrders extends AppCompatActivity {
    ViewPager p2p_viewpager;
    TabLayout p2p_tablayout;
    P2P_PagerAdapter p2P_pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_porders);

        p2p_viewpager = findViewById(R.id.p2p_order_viewpager);
        p2p_tablayout = findViewById(R.id.p2p_order_tabs);


        p2P_pagerAdapter = new P2P_PagerAdapter(getSupportFragmentManager());

        p2p_tablayout.setupWithViewPager(p2p_viewpager);
        p2p_viewpager.setAdapter(p2P_pagerAdapter);




    }
}
