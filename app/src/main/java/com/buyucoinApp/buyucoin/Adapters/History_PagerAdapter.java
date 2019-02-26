package com.buyucoinApp.buyucoin.Adapters;

import android.os.Bundle;

import com.buyucoinApp.buyucoin.Fragments.HistoryPagerAdapterFragmentDeposite;
import com.buyucoinApp.buyucoin.Fragments.HistoryPagerAdapterFragmentOrder;
import com.buyucoinApp.buyucoin.Fragments.HistoryPagerAdapterFragmentWithdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class History_PagerAdapter extends FragmentStatePagerAdapter {
    String COIN;

    public History_PagerAdapter(@NonNull FragmentManager fm, String coin) {
        super(fm);
        if(coin!=null){
            COIN = coin;
        }
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
                b.putString("COIN",COIN);
                fragment.setArguments(b);
                break;
            case 1:
                fragment =  new HistoryPagerAdapterFragmentWithdraw();
                b.putString("URL","withdraw");
                b.putString("COIN",COIN);

                fragment.setArguments(b);
                break;
                case 2:
               fragment =  new HistoryPagerAdapterFragmentOrder();
                    b.putString("URL","order");
                    b.putString("COIN",COIN);

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


