package com.buyucoin.buyucoin.Adapters;

import android.os.Bundle;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Fragments.AccountFragment;
import com.buyucoin.buyucoin.Fragments.BuySellFragment;
import com.buyucoin.buyucoin.Fragments.P2PFragment;
import com.buyucoin.buyucoin.Fragments.RateFragment;
import com.buyucoin.buyucoin.Fragments.WalletFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Dashboard_PagerAdapter extends FragmentStatePagerAdapter {

    public Dashboard_PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        new Dashboard().changeTab(position);
        switch (position) {
            case 0:
                return new WalletFragment();
            case 1:
                return new RateFragment();
            case 2:
                return new BuySellFragment();
            case 3:
                return new P2PFragment();
            case 4:
                return new AccountFragment();
            default:
                return new WalletFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Deposite Orders";
            case 1:
                return "WithDraw Orders";
            default:
                return "Deposite Orders";

        }
    }
}


