package com.buyucoinApp.buyucoin.Adapters;

import com.buyucoinApp.buyucoin.Dashboard;
import com.buyucoinApp.buyucoin.Fragments.AccountFragment;
import com.buyucoinApp.buyucoin.Fragments.BuySellFragment;
import com.buyucoinApp.buyucoin.Fragments.P2PFragment;
import com.buyucoinApp.buyucoin.Fragments.RateFragment;
import com.buyucoinApp.buyucoin.Fragments.WalletFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
                return "Deposit Orders";
            case 1:
                return "WithDraw Orders";
            default:
                return "Deposit Orders";

        }
    }
}


