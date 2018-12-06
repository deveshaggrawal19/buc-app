package com.buyucoin.buyucoin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.DrawableWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




public class MyCoustomDialogBoxClass {

    final static int[]  arr = new int[]{
            R.drawable.btc,
            R.drawable.eth,
            R.drawable.btc,
            R.drawable.ltc,
            R.drawable.bcc,
            R.drawable.xmr,
            R.drawable.qtum,
            R.drawable.etc,
            R.drawable.zec,
            R.drawable.xem,
            R.drawable.gnt,
            R.drawable.neo,
            R.drawable.xrp,
            R.drawable.dash,
            R.drawable.strat,
            R.drawable.steem,
            R.drawable.rep,
            R.drawable.lsk,
            R.drawable.fct,
            R.drawable.omg,
            R.drawable.cvc,
            R.drawable.sc,
            R.drawable.pay,
            R.drawable.ark,
            R.drawable.doge,
            R.drawable.dgb,
            R.drawable.nxt,
            R.drawable.bat,
            R.drawable.bts,
            R.drawable.cloak,
            R.drawable.pivx,
            R.drawable.dcn,
            R.drawable.buc,
            R.drawable.pac
    };


    public static void BuyDialog(Context activity, String coin, int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.buy_dialog);

        TextView text = dialog.findViewById(R.id.buy_coin_text);
        ImageView imageView = dialog.findViewById(R.id.buy_coin_img);
//        text.setText(msg);
        text.setText(coin.toUpperCase());
        imageView.setImageDrawable(dialog.getContext().getResources().getDrawable(arr[position]));
//
//        Button okButton = (Button) dialog.findViewById(R.id.btn_dialog_feedback);
        Button cancleButton = (Button) dialog.findViewById(R.id.close_buy_dialog);
//        final EditText edittext_tv = (EditText) dialog.findViewById(R.id.dialoge_alert_text_feedback);
//
//        okButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //Perfome Action
//            }
//        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
