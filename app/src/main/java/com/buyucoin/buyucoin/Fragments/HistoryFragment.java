package com.buyucoin.buyucoin.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.buyucoin.buyucoin.Adapters.History_PagerAdapter;
import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.pojos.History;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


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
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyFullScreenDialog);
        if(getArguments()!=null){
            b = getArguments();
        }
        position = b.getInt("POSITION");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        tabLayout = view.findViewById(R.id.tlHistory);
        viewPager = view.findViewById(R.id.history_view_pager);

        tabLayout.setupWithViewPager(viewPager);
        History_PagerAdapter history_pagerAdapter = new History_PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(history_pagerAdapter);

        viewPager.setCurrentItem(position);





        return view;
    }





}
