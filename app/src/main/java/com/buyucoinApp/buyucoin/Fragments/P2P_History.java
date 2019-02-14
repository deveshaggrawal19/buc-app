package com.buyucoinApp.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buyucoinApp.buyucoin.Adapters.P2P_PagerAdapter;
import com.buyucoinApp.buyucoin.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class P2P_History extends DialogFragment {

    private ViewPager p2p_history_viewpager;
    private TabLayout tabLayout;
    private ImageView goback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.MyFullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p2p_history_fragment,container,false);

        goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        p2p_history_viewpager = view.findViewById(R.id.p2p_history_viewpager);
        tabLayout = view.findViewById(R.id.p2p_history_tab);

        tabLayout.setupWithViewPager(p2p_history_viewpager);

        P2P_PagerAdapter p2P_pagerAdapter = new P2P_PagerAdapter(getChildFragmentManager());
        p2p_history_viewpager.setAdapter(p2P_pagerAdapter);

        return  view;
    }
}
