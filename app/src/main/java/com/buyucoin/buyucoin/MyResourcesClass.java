package com.buyucoin.buyucoin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.DrawableWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MyResourcesClass {



    public static JSONObject COIN_ICON = new JSONObject();

    static {
        try {
            COIN_ICON = new JSONObject()
                .put("btc",R.drawable.btc)
                .put("eth",R.drawable.eth)
                .put("ltc",R.drawable.ltc)
                .put("bcc",R.drawable.bcc)
                .put("xmr",R.drawable.xmr)
                .put("qtum",R.drawable.qtum)
                .put("etc",R.drawable.etc)
                .put("zec",R.drawable.zec)
                .put("xem",R.drawable.xem)
                .put("gnt",R.drawable.gnt)
                .put("neo",R.drawable.neo)
                .put("xrp",R.drawable.xrp)
                .put("dash",R.drawable.dash)
                .put("strat",R.drawable.strat)
                .put("steem",R.drawable.steem)
                .put("rep",R.drawable.rep)
                .put("lsk",R.drawable.lsk)
                .put("fct",R.drawable.fct)
                .put("omg",R.drawable.omg)
                .put("cvc",R.drawable.cvc)
                .put("sc",R.drawable.sc)
                .put("pay",R.drawable.pay)
                .put("ark",R.drawable.ark)
                .put("doge",R.drawable.doge)
                .put("dgb",R.drawable.dgb)
                .put("nxt",R.drawable.nxt)
                .put("bat",R.drawable.bat)
                .put("bts",R.drawable.bts)
                .put("cloak",R.drawable.cloak)
                .put("pivx",R.drawable.pivx)
                .put("dcn",R.drawable.dcn)
                .put("buc",R.drawable.buc)
                .put("pac",R.drawable.pac)
                .put("inr",R.mipmap.ic_inr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
