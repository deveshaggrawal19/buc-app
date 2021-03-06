package com.buyucoinApp.buyucoin.Adapters;

import com.buyucoinApp.buyucoin.Fragments.P2P_Deposite_History_Fragment;
import com.buyucoinApp.buyucoin.Fragments.P2P_Withdraw_History_Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class P2P_PagerAdapter extends FragmentStatePagerAdapter{

    public P2P_PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new P2P_Deposite_History_Fragment();
            case 1:
                return new P2P_Withdraw_History_Fragment();
            default:
                return new P2P_Deposite_History_Fragment();
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
                return "Deposit Orders";
            case 1:
                return "WithDraw Orders";
            default:
                return "Deposit Orders";

        }
    }





}


