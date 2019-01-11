package com.buyucoin.buyucoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoin.buyucoin.Fragments.WithdrawBottomsheet;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CoinDepositWithdraw extends AppCompatActivity {
    private static  final String DEPOSITE = "DEPOSITE";
    private static  final String WITHDRAW = "WITHDRAW";
    Intent i;
    LinearLayout deposite_layout,withdraw_layout,tag_layout;
    Button deposite_layout_btn,withdraw_layout_btn,withdraw_layout_btnview;
    EditText destination_tag,coin_amonut,coin_address;
    ImageView deposite_layout_qr_img;
    TextView deposite_layout_address_txt;




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
        withdraw_layout_btnview = findViewById(R.id.withdraw_layout_btnview);

        destination_tag = findViewById(R.id.destination_tag_edittext);
        coin_amonut = findViewById(R.id.withdraw_layout_amount_et);
        coin_address = findViewById(R.id.withdraw_layout_address_et);

        deposite_layout_qr_img = findViewById(R.id.coin_address_qrcode);
        deposite_layout_address_txt = findViewById(R.id.coin_address);

        if(TAG.equals("true")){
            tag_layout.setVisibility(View.VISIBLE);
            destination_tag.setHint(DESCRIPTION);

        }



        if(ADDRESS!=null){
            deposite_layout_address_txt.setText(ADDRESS);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(ADDRESS,BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                deposite_layout_qr_img.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
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



        withdraw_layout_btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coin_amonut.getText().toString().equals("")){

                double amount = Double.valueOf(coin_amonut.getText().toString());
                String tag = destination_tag.getText().toString();
                String address = coin_address.getText().toString();

                Bundle bundle = new Bundle();
                if(amount > 0 && !address.equals("")){

                    bundle.putString("coin_tag",tag);
                    bundle.putDouble("coin_amount",amount);
                    bundle.putString("coin_address",address);


                    WithdrawBottomsheet withdrawBottomsheet = new WithdrawBottomsheet();
                    withdrawBottomsheet.setArguments(bundle);
                    withdrawBottomsheet.show(getSupportFragmentManager(),"WITHDRAW");
                }

                }

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
