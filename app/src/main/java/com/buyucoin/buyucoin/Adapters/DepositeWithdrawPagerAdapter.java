package com.buyucoin.buyucoin.Adapters;

import android.os.Bundle;

import com.buyucoin.buyucoin.Fragments.DepositePagerFragment;
import com.buyucoin.buyucoin.Fragments.P2P_Deposite_History_Fragment;
import com.buyucoin.buyucoin.Fragments.P2P_Withdraw_History_Fragment;
import com.buyucoin.buyucoin.Fragments.WithdrawPagerFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DepositeWithdrawPagerAdapter extends FragmentPagerAdapter {

    Bundle b;

    public DepositeWithdrawPagerAdapter(@NonNull FragmentManager fm, Bundle b) {
        super(fm);
        this.b = b;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DepositePagerFragment depositePagerFragment = new DepositePagerFragment();
                depositePagerFragment.setArguments(b);
                return depositePagerFragment;
            case 1:
                WithdrawPagerFragment withdrawPagerFragment = new WithdrawPagerFragment();
                withdrawPagerFragment.setArguments(b);
                return withdrawPagerFragment;
            default:
                DepositePagerFragment depositePagerFragment_ = new DepositePagerFragment();
                depositePagerFragment_.setArguments(b);
                return depositePagerFragment_;
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


