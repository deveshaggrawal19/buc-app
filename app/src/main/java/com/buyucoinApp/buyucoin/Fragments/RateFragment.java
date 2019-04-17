package com.buyucoinApp.buyucoin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.buyucoinApp.buyucoin.Adapters.MyrateRecyclerViewAdapter;
import com.buyucoinApp.buyucoin.DataClasses.Rates;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.Utilities;
import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class RateFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    String ACCESS_TOKEN = null;
    RecyclerView recyclerView;
    androidx.appcompat.widget.SearchView searchView;
    ProgressBar pb;

    ArrayList<Rates> list = new ArrayList<>();
    DatabaseReference myRef;
    MyrateRecyclerViewAdapter adapter;
    private String FRAGMENT_STATE = "RATES";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RateFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RateFragment newInstance(int columnCount) {
        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        BuyucoinPref buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));

        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);

        list = new ArrayList<>();
        adapter = new MyrateRecyclerViewAdapter(list, Objects.requireNonNull(getActivity()).getApplicationContext());


        FirebaseDatabase db = new Config().getProductionFirebaseDatabase();
       // Toast.makeText(getActivity(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_list, container, false);

        searchView = view.findViewById(R.id.searchview);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                searchView.requestFocus();
                searchView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
            }
        });

        recyclerView = view.findViewById(R.id.list);
        pb = view.findViewById(R.id.pbRate);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter!=null){
                    adapter.getFilter().filter(newText);
                    return true;
                }else {
                    return false;
                }
            }
        });





        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot crypto = dataSnapshot.child("rates").child("data").child("crypto");

                list.clear();
                int i=0;
                for(DataSnapshot d : crypto.getChildren()){
                    String currency = d.getKey();
                    String ask = d.child("ask").getValue(String.class);
                    String bid = d.child("bid").getValue(String.class);
                    String last_trade = d.child("last_trade").getValue(String.class);
                    String change = d.child("change").getValue(String.class);
                    list.add(new Rates(bid, ask, last_trade, change, currency));
                    i++;
                }

                adapter = new MyrateRecyclerViewAdapter(list, getActivity());
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                Utilities.hideProgressBar(pb);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value", databaseError.toException());
            }
        });
    }



}
