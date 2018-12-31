package com.buyucoin.buyucoin.coustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyucoin.buyucoin.BuySellActivity;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.buyinterfaces.BuyDialogFunction;
import com.buyucoin.buyucoin.sellinterface.SellDialogFuncion;

public class CoustomDialogs {

    private Context acitivity ;
    private Bundle bundle;
    private BuyDialogFunction buyDialogFunction;
    private SellDialogFuncion sellDialogFuncion;
    private Dialog dialog;
    private String dialog_title;

    public CoustomDialogs(Context acitivity, Bundle bundle, BuyDialogFunction buyDialogFunction,String dialog_title) {
        this.acitivity = acitivity;
        this.bundle = bundle;
        this.buyDialogFunction = buyDialogFunction;
        dialog = new Dialog(acitivity);
        this.dialog_title = dialog_title;
    }

    public CoustomDialogs(Context acitivity, Bundle bundle, SellDialogFuncion sellDialogFuncion, String dialog_title) {
        this.acitivity = acitivity;
        this.bundle = bundle;
        this.sellDialogFuncion = sellDialogFuncion;
        dialog = new Dialog(acitivity);
        this.dialog_title = dialog_title;
    }

    public  void confirmBuyDialog(){
        TextView title,quantity,price,fees,total;
        Button agree,cancel;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.buy_dialog_layout);

        title = dialog.findViewById(R.id.dialog_title);
        quantity = dialog.findViewById(R.id.dialog_quantity);
        price = dialog.findViewById(R.id.dialog_price);
        fees = dialog.findViewById(R.id.dialog_fees);
        total = dialog.findViewById(R.id.dialog_total);

        agree = dialog.findViewById(R.id.agree_action_btn);
        cancel = dialog.findViewById(R.id.cancel_action_btn);

        title.setText(dialog_title);
        quantity.setText(bundle.getString("quantity"));
        price.setText(bundle.getString("price"));
        fees.setText(bundle.getString("fees"));
        total.setText(bundle.getString("total"));


        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        buyDialogFunction.buyFunction(acitivity,bundle.getString("quantity"),bundle.getString("price"));
                        dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();








    }

    public  void confirmSellDialog(){
        TextView title,quantity,price,fees,total;
        Button agree,cancel;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.buy_dialog_layout);

        title = dialog.findViewById(R.id.dialog_title);
        quantity = dialog.findViewById(R.id.dialog_quantity);
        price = dialog.findViewById(R.id.dialog_price);
        fees = dialog.findViewById(R.id.dialog_fees);
        total = dialog.findViewById(R.id.dialog_total);

        agree = dialog.findViewById(R.id.agree_action_btn);
        cancel = dialog.findViewById(R.id.cancel_action_btn);

        title.setText(dialog_title);
        quantity.setText(bundle.getString("quantity"));
        price.setText(bundle.getString("price"));
        fees.setText(bundle.getString("fees"));
        total.setText(bundle.getString("total"));


        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        sellDialogFuncion.sellFunction(acitivity,bundle.getString("quantity"),bundle.getString("price"));
                        dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();








    }



}
