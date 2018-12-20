package com.buyucoin.buyucoin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Fragments.WalletFragment.OnListFragmentInteractionListener;
import com.buyucoin.buyucoin.dummy.DummyContent.DummyItem;
import com.buyucoin.buyucoin.pojos.WalletCoinHorizontal;
import com.buyucoin.buyucoin.pojos.WalletCoinVertical;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArrayList> arrayList = new ArrayList<>();
    private ArrayList<WalletCoinVertical> wcv = new ArrayList<>();
    private ArrayList<WalletCoinHorizontal> wch = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;
    private  final int VERTICAL =1;
    private  final int HORIZONTAL =2;
     private Context context ;


    public MyItemRecyclerViewAdapter(Context context,List<JSONObject> items, OnListFragmentInteractionListener listener)  {
        mListener = listener;
        this.context = context;
        for (JSONObject js : items) {
            try {
                if(!js.getString("available").equals("0")) {
                    WalletCoinVertical wl = new WalletCoinVertical();
                    wl.setCoinname(js.getString("currencyname"));
                    wl.setAmount(js.getString("available"));
                    wcv.add(wl);

                    Log.d("JSONOBJECTS:==========>","TRUE");
                }else{
                    WalletCoinHorizontal wh = new WalletCoinHorizontal();
                    wh.setCoinname(js.getString("currencyname"));
                    wh.setAmount(js.getString("available"));
                    wch.add(wh);
                    Log.d("JSONOBJECTS:==========>","FALSE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        arrayList.add(wcv);
        arrayList.add(wch);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType){
            case VERTICAL:
                view = layoutInflater.inflate(R.layout.vertical,parent,false);
                holder = new VerticalViewHolder(view);
                break;
            case  HORIZONTAL:
                view = layoutInflater.inflate(R.layout.horizontal,parent,false);
                holder = new HorizontalViewHolder(view);
                break;
             default:
                 view = layoutInflater.inflate(R.layout.vertical,parent,false);
                 holder = new VerticalViewHolder(view);
                 break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(holder.getItemViewType()==VERTICAL){
            verticalView((VerticalViewHolder) holder);
        }
        else if(holder.getItemViewType()==HORIZONTAL){
            horizontalView((HorizontalViewHolder) holder);
        }


    }

    private void verticalView(VerticalViewHolder holder){
        VerticalAdapter adapter1  = new VerticalAdapter(wcv);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter1);
    }

    private void horizontalView(HorizontalViewHolder holder){
        HorizontalAdapter adapter2 = new HorizontalAdapter(wch);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter2);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(arrayList.get(position).get(position) instanceof WalletCoinVertical){
            return VERTICAL;
        }
        if(arrayList.get(position).get(position) instanceof WalletCoinHorizontal){
            return HORIZONTAL;

        }


        return -1;
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerview);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerview);

        }
    }



}
