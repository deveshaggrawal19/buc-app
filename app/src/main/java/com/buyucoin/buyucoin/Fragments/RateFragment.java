package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buyucoin.buyucoin.Adapters.MyrateRecyclerViewAdapter;
import com.buyucoin.buyucoin.DataClasses.Rates;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RateFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    String ACCESS_TOKEN = null;
    RecyclerView recyclerView;
    ProgressBar pb;

    ArrayList<Rates> list = new ArrayList<>();
    DatabaseReference myRef;
    MyrateRecyclerViewAdapter adapter;

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

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);

        list = new ArrayList<>();
        adapter = new MyrateRecyclerViewAdapter(list, mListener, getActivity().getApplicationContext());


        FirebaseDatabase db = FirebaseDatabase.getInstance();
       // Toast.makeText(getActivity(), ""+db.getReference().toString(), Toast.LENGTH_LONG).show();
        myRef = db.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_list, container, false);

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
            //recyclerView.setAdapter(new MyrateRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }

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
                Log.d("logging", crypto.toString());
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

                adapter = new MyrateRecyclerViewAdapter(list, mListener, getActivity());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                Utilities.hideProgressBar(pb);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value", databaseError.toException());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(JSONObject item);
    }

    public void getRates(){
        OkHttpHandler.get_rates(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject object = new JSONObject(s).getJSONObject("data");
                    JSONArray array = object.toJSONArray(object.names());
                    for(int i=0; i<array.length(); i++){
                        array.getJSONObject(i).put("currency", object.names().get(i));
                    }
                    final JSONArray farray = array;
                    //Log.d("RESPONSE____",array.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new MyrateRecyclerViewAdapter(list, mListener, getActivity().getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            Utilities.hideProgressBar(pb);
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
