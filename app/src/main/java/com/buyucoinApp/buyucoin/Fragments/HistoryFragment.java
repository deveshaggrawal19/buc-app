package com.buyucoinApp.buyucoin.Fragments;

import android.os.Bundle;

import com.buyucoinApp.buyucoin.Adapters.History_PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buyucoinApp.buyucoin.R;

import java.util.Objects;

import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends DialogFragment {

    Bundle b;
    TabLayout tabLayout;
    ViewPager viewPager;
    int position = 0;
    private ImageView goback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        if(getArguments()!=null){
            b = getArguments();
        position = b.getInt("POSITION");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        tabLayout = view.findViewById(R.id.tlHistory);
        viewPager = view.findViewById(R.id.history_view_pager);

        tabLayout.setupWithViewPager(viewPager);
        History_PagerAdapter history_pagerAdapter = new History_PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(history_pagerAdapter);

        viewPager.setCurrentItem(position);





        return view;
    }





}
