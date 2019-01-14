package com.buyucoin.buyucoin.Adapters;

import com.buyucoin.buyucoin.Fragments.P2P_Deposite_Fragment;
import com.buyucoin.buyucoin.Fragments.P2P_Withdraw_Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class P2P_PagerAdapter extends FragmentPagerAdapter {

    public P2P_PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new P2P_Deposite_Fragment();
            case 1:
                return new P2P_Withdraw_Fragment();
            default:
                return new P2P_Deposite_Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
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


