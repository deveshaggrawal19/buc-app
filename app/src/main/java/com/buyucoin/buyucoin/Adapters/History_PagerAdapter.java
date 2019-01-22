package com.buyucoin.buyucoin.Adapters;

import android.os.Bundle;

import com.buyucoin.buyucoin.Fragments.HistoryPagerAdapterFragmentDeposite;
import com.buyucoin.buyucoin.Fragments.HistoryPagerAdapterFragmentOrder;
import com.buyucoin.buyucoin.Fragments.HistoryPagerAdapterFragmentWithdraw;
import com.buyucoin.buyucoin.Fragments.P2P_Deposite_History_Fragment;
import com.buyucoin.buyucoin.Fragments.P2P_Withdraw_History_Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class History_PagerAdapter extends FragmentStatePagerAdapter {

    public History_PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle b = new Bundle();
        switch (position) {
            case 0:
                fragment = new HistoryPagerAdapterFragmentDeposite();
                b.putString("URL","deposit");
                fragment.setArguments(b);
                break;
            case 1:
                fragment =  new HistoryPagerAdapterFragmentWithdraw();
                b.putString("URL","withdraw");
                fragment.setArguments(b);
                break;
                case 2:
               fragment =  new HistoryPagerAdapterFragmentOrder();
                    b.putString("URL","order");
                    fragment.setArguments(b);
               break;
            default:
                fragment =  new HistoryPagerAdapterFragmentDeposite();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Deposit";
            case 1:
                return "WithDraw";
            case 2:
                return "Orders";
            default:
                return "Deposit";

        }
    }





}


