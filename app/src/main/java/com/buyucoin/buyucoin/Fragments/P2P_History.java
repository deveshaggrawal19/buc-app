package com.buyucoin.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyucoin.buyucoin.Adapters.P2P_PagerAdapter;
import com.buyucoin.buyucoin.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class P2P_History extends DialogFragment {

    private ViewPager p2p_history_viewpager;
    private TabLayout tabLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.MyFullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p2p_history_fragment,container,false);

        p2p_history_viewpager = view.findViewById(R.id.p2p_history_viewpager);
        tabLayout = view.findViewById(R.id.p2p_history_tab);

        tabLayout.setupWithViewPager(p2p_history_viewpager);

        P2P_PagerAdapter p2P_pagerAdapter = new P2P_PagerAdapter(getChildFragmentManager());
        p2p_history_viewpager.setAdapter(p2P_pagerAdapter);

        return  view;
    }
}
