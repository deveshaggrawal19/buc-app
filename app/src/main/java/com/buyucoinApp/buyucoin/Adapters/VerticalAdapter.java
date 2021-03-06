package com.buyucoinApp.buyucoin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buyucoinApp.buyucoin.CoinDepositWithdraw;
import com.buyucoinApp.buyucoin.Dashboard;
import com.buyucoinApp.buyucoin.DepositWithdrawActivity;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.pojos.WalletCoinHorizontal;
import com.buyucoinApp.buyucoin.pojos.WalletCoinVertical;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

    private ArrayList<WalletCoinVertical> walletCoinVerticals;


    public VerticalAdapter(Context context, ArrayList<JSONObject> list, boolean hidezero) {
        Context context1 = context;
        ArrayList<WalletCoinVertical> arrayList = new ArrayList<>();
        if(list.size()>0) {
            for (JSONObject js : list) {
                Log.d("WALLET COIN", "VerticalAdapter: "+list.toString());
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
                            if (js.getString("currencyname").equals("inr")) {
                                arrayList.add(0, wl);
                            } else {
                                arrayList.add(wl);
                            }
                        }

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
                                arrayList.add(0, wl);
                            } else {
                                arrayList.add(wl);
                            }

                        } else {
                            arrayList.add(wl);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            this.walletCoinVerticals = arrayList;
        }
    }

    @NonNull
    @Override
    public VerticalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_single_row,viewGroup,false);
        return new VerticalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VerticalAdapter.MyViewHolder myViewHolder, final int i) {

        final Context context = myViewHolder.itemView.getContext();
        BuyucoinPref pref = new BuyucoinPref(context);
        final String coin_name,availabel,pendings,address,description,tag,full_coin_name,porfolio,pending,base_address;

        coin_name = walletCoinVerticals.get(i).getCoinname();
        availabel =  walletCoinVerticals.get(i).getAvailabel();
        pendings = walletCoinVerticals.get(i).getPending();
        address =  walletCoinVerticals.get(i).getAddress();
        description =  walletCoinVerticals.get(i).getDescription();
        tag =   walletCoinVerticals.get(i).getTag();
        full_coin_name =  walletCoinVerticals.get(i).getFull_coin_name();
        porfolio = walletCoinVerticals.get(i).getPortfolio();
        pending = walletCoinVerticals.get(i).getPending();
        base_address = walletCoinVerticals.get(i).getBase_address();



        myViewHolder.coinname.setText(coin_name.toUpperCase());

        myViewHolder.portfolio.setText(porfolio);

        if(porfolio.length()>5){
            String pf = porfolio.substring(0,4).concat("....");
            myViewHolder.portfolio.setText(pf);
        }else{
            myViewHolder.portfolio.setText(porfolio);
        }

        if(pending.length()>5) {
            String new_pending = pending.substring(0, 4).concat("...");
            myViewHolder.pending.setText(new_pending);
        }
        else{
            myViewHolder.pending.setText(pending);
        }

        if(!coin_name.equals("inr")){
            if(availabel.length()>5){
                String av = availabel.substring(0,4).concat("....");
                myViewHolder.balance.setText(av);


            }else{
                myViewHolder.balance.setText(availabel);

            }
        }

//        int r = (int) (Math.random() * 6);
//
//        myViewHolder.itemView.getBackground().setLevel(r);



        if(coin_name.equals("inr")){
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,DepositWithdrawActivity.class);
                    JSONObject js = new JSONObject();
                    try {
                        js.put("coin_name",coin_name);
                        js.put("available",availabel);
                        js.put("pendings",pendings);
                        js.put("base_address",base_address);
                        js.put("address",address);
                        js.put("description",description);
                        js.put("tag",tag);
                        js.put("full_coin_name",full_coin_name);
                        js.put("portfolio",porfolio);
                        js.put("pending",pending);
                        intent.putExtra("object",js.toString());
                        myViewHolder.itemView.getContext().startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (coin_name.equals("buc")) {
            String BUC = availabel;
            pref.setEditpref("buc_amount", BUC);
        }

        if(coin_name.equals("inr")){
            DecimalFormat decimalFormat = new DecimalFormat("0.####");
            String INR = decimalFormat.format(Double.parseDouble(availabel));
            if(INR.length()>5){
                myViewHolder.balance.setText(INR.substring(0,4).concat("...."));
            }else{
                myViewHolder.balance.setText(INR);
            }
           pref.setEditpref("inr_amount",INR);
           pref.setEditpref("portfolio",porfolio);
            myViewHolder.deposite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else{
            myViewHolder.deposite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CoinDepositWithdraw.class);
                    JSONObject js = new JSONObject();

                    try {
                        js.put("type","DEPOSITE");
                        js.put("coin_name",coin_name);
                        js.put("available",availabel);
                        js.put("pendings",pendings);
                        js.put("base_address",base_address);
                        js.put("address",address);
                        js.put("description",description);
                        js.put("tag",tag);
                        js.put("full_coin_name",full_coin_name);
                        js.put("portfolio",porfolio);
                        js.put("pending",pending);
                        intent.putExtra("object",js.toString());

                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        if(coin_name.equals("inr")){
            myViewHolder.withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Dashboard().inrToP2P();
                }
            });
        }
        else{
            myViewHolder.withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,CoinDepositWithdraw.class);
                    JSONObject js = new JSONObject();

                    try {
                        js.put("type","WITHDRAW");
                        js.put("coin_name",coin_name);
                        js.put("available",availabel);
                        js.put("pendings",pendings);
                        js.put("base_address",base_address);
                        js.put("address",address);
                        js.put("description",description);
                        js.put("tag",tag);
                        js.put("full_coin_name",full_coin_name);
                        js.put("portfolio",porfolio);
                        js.put("pending",pending);
                        intent.putExtra("object",js.toString());


                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return walletCoinVerticals.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView coinname,balance,portfolio,pending;
        Button deposite,withdraw;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinname = itemView.findViewById(R.id.tvCurrency);
            balance = itemView.findViewById(R.id.balance);
            pending = itemView.findViewById(R.id.wallet_card_pending);
            portfolio = itemView.findViewById(R.id.wallet_card_portfolio);

            deposite = itemView.findViewById(R.id.vertical_deposite_btn);
            withdraw = itemView.findViewById(R.id.vertical_withdraw_btn);

        }
    }
}

