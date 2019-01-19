package com.buyucoin.buyucoin.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.dummy.DummyContent.DummyItem;
import com.buyucoin.buyucoin.pojos.WalletCoinHorizontal;
import com.buyucoin.buyucoin.pojos.WalletCoinVertical;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArrayList> arrayList = new ArrayList<>();
    private ArrayList<WalletCoinVertical> wcv = new ArrayList<>();
    private ArrayList<WalletCoinHorizontal> wch = new ArrayList<>();
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;
    private Context context;


    public MyItemRecyclerViewAdapter(Context context, List<JSONObject> items, boolean hidezero) {
        this.context = context;
        if(items.size()>0) {
            for (JSONObject js : items) {
                try {
                    if (hidezero) {
                        if (!js.getString("available").equals("0")) {
                            WalletCoinVertical wl = new WalletCoinVertical();
                            wl.setCoinname(js.getString("currencyname"));
                            wl.setAmount(js.getString("available"));
                            wl.setAddress(js.getString("address"));
                            wl.setAvailabel(js.getString("available"));
                            wl.setBase_address(js.getString("base_address"));
                            wl.setDescription(js.getString("desciption"));
                            wl.setFees(js.getString("fees"));
                            wl.setMin_width(js.getString("min_with"));
                            wl.setPending(js.getString("pending"));
                            wl.setPortfolio(js.getString("portfolio"));
                            wl.setTag(js.getString("tag"));
                            wl.setFull_coin_name(js.getString("currencies"));
                            wcv.add(wl);
                        }

                        Log.d("JSONOBJECTS:==========>", "TRUE");
                    } else {

                        WalletCoinVertical wl = new WalletCoinVertical();
                        wl.setCoinname(js.getString("currencyname"));
                        wl.setAmount(js.getString("available"));
                        wl.setAddress(js.getString("address"));
                        wl.setAvailabel(js.getString("available"));
                        wl.setBase_address(js.getString("base_address"));
                        wl.setDescription(js.getString("desciption"));
                        wl.setFees(js.getString("fees"));
                        wl.setMin_width(js.getString("min_with"));
                        wl.setPending(js.getString("pending"));
                        wl.setPortfolio(js.getString("portfolio"));
                        wl.setTag(js.getString("tag"));
                        wl.setFull_coin_name(js.getString("currencies"));

                        if (!js.getString("available").equals("0")) {
                            if (js.getString("currencyname").equals("inr")) {
                                wcv.add(0, wl);
                            } else {
//                                wcv.add(1, wl);
                                wcv.add(wl);
                            }

                        } else {

                            wcv.add(wl);
                        }
                    }
                    WalletCoinHorizontal wh = new WalletCoinHorizontal();
                    wh.setCoinname(js.getString("currencyname"));
                    wh.setAmount(js.getString("available"));
                    wh.setAddress(js.getString("address"));
                    wh.setAvailabel(js.getString("available"));
                    wh.setBase_address(js.getString("base_address"));
                    wh.setDescription(js.getString("desciption"));
                    wh.setFees(js.getString("fees"));
                    wh.setMin_width(js.getString("min_with"));
                    wh.setPending(js.getString("pending"));
                    wh.setPortfolio(js.getString("portfolio"));
                    wh.setTag(js.getString("tag"));
                    wh.setFull_coin_name(js.getString("currencies"));

                    wch.add(wh);
                    Log.d("JSONOBJECTS:==========>", "FALSE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            arrayList.add(wcv);
            arrayList.add(wch);
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case VERTICAL:
                view = layoutInflater.inflate(R.layout.vertical, parent, false);
                holder = new VerticalViewHolder(view);
                break;
            case HORIZONTAL:
                view = layoutInflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            default:
                view = layoutInflater.inflate(R.layout.vertical, parent, false);
                holder = new VerticalViewHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VERTICAL) {
            verticalView((VerticalViewHolder) holder);
        } else if (holder.getItemViewType() == HORIZONTAL) {
            horizontalView((HorizontalViewHolder) holder);
        }


    }

    private void verticalView(VerticalViewHolder holder) {
        VerticalAdapter adapter1 = new VerticalAdapter(wcv);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter1);
    }

    private void horizontalView(HorizontalViewHolder holder) {
        HorizontalAdapter adapter2 = new HorizontalAdapter(wch);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter2);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (arrayList.get(position).get(position) instanceof WalletCoinVertical) {
            return VERTICAL;
        }
        if (arrayList.get(position).get(position) instanceof WalletCoinHorizontal) {
            return HORIZONTAL;

        }


        return -1;
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerview);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inner_recyclerview);

        }
    }


}
