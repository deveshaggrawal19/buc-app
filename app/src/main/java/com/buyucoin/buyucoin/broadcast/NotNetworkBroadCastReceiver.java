package com.buyucoin.buyucoin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.Utilities;

import androidx.work.impl.utils.ForceStopRunnable;

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
