package com.buyucoinApp.buyucoin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.buyucoinApp.buyucoin.Dashboard;
import com.buyucoinApp.buyucoin.Utilities;

public class NotNetworkBroadCastReceiver extends BroadcastReceiver {
    Dashboard dcontext;

    public NotNetworkBroadCastReceiver() {
    }

    public  NotNetworkBroadCastReceiver(Dashboard dcontext){
        this.dcontext = dcontext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean iscon = Utilities.isOnline(context);
        if (iscon){
//            Toast.makeText(context, "CONNECTED", Toast.LENGTH_SHORT).show();
            dcontext.reloadPageer();
            dcontext.noInternet(iscon);
        }
        else {
//            Toast.makeText(context, "LOST CONNECTION", Toast.LENGTH_SHORT).show();
            dcontext.noInternet(iscon);
        }
    }
}
