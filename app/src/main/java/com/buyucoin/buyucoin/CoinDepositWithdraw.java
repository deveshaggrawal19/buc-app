package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CoinDepositWithdraw extends AppCompatActivity {
    private static  final String DEPOSITE = "DEPOSITE";
    private static  final String WITHDRAW = "WITHDRAW";
    Intent i;
    LinearLayout deposite_layout,withdraw_layout,tag_layout;
    Button deposite_layout_btn,withdraw_layout_btn;
    EditText destination_tag;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_coin_deposite_withdraw);

        i = getIntent();

        String TYPE = i.getStringExtra("type");
        String COIN = i.getStringExtra("coin_name");
        String AVAILABEL = i.getStringExtra("available");
        String ADDRESS = i.getStringExtra("address");
        String DESCRIPTION = i.getStringExtra("description");
        String TAG = i.getStringExtra("tag");
        String COIN_FULL_NAME = i.getStringExtra("full_coin_name");

        deposite_layout = findViewById(R.id.deposite_layout);
        withdraw_layout = findViewById(R.id.withdraw_layout);
        tag_layout = findViewById(R.id.tag_layout);

        deposite_layout_btn = findViewById(R.id.deposite_layout_btn);
        withdraw_layout_btn = findViewById(R.id.withdraw_layout_btn);

        destination_tag = findViewById(R.id.destination_tag_edittext);

        if(TAG.equals("true")){
            tag_layout.setVisibility(View.VISIBLE);
            destination_tag.setHint(DESCRIPTION);

        }


        if(TYPE.equals(DEPOSITE)){
            changeLayoutParameter(deposite_layout,withdraw_layout);
            changeButtonParameter(deposite_layout_btn,withdraw_layout_btn);

        }

        if(TYPE.equals(WITHDRAW)){
            changeLayoutParameter(withdraw_layout,deposite_layout);
            changeButtonParameter(withdraw_layout_btn,deposite_layout_btn);

        }

        deposite_layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayoutParameter(deposite_layout,withdraw_layout);
                changeButtonParameter(deposite_layout_btn,withdraw_layout_btn);
            }
        });

        withdraw_layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayoutParameter(withdraw_layout,deposite_layout);
                changeButtonParameter(withdraw_layout_btn,deposite_layout_btn);
            }
        });






    }

    private void changeButtonParameter(Button show, Button hide) {
        show.getBackground().setLevel(1);
        show.setTextColor(getResources().getColor(R.color.colorPrimary));
        hide.getBackground().setLevel(0);
        hide.setTextColor(Color.WHITE);

    }

    private void changeLayoutParameter(LinearLayout show, LinearLayout hide) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);
    }

}
