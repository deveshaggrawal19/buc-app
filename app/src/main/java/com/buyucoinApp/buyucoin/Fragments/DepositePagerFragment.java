package com.buyucoinApp.buyucoin.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class DepositePagerFragment extends Fragment {

    private ClipboardManager clipboardManager;
    private Context context;
    private ImageView deposite_layout_qr_img,deposite_layout_tag_qr_img;
    private String de;
    private String tag;
    private TextView deposite_layout_address_txt;
    private TextView deposite_layout_base_address;
    private JSONObject j;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle b = getArguments();
            try {
                j = new JSONObject(b.getString("object"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deposite_pager_layout,container,false);


        context = view.getContext();
        LinearLayout wallet_maintain_layout = view.findViewById(R.id.wallet_maintain_layout);
        LinearLayout deposite_layout = view.findViewById(R.id.deposite_layout);
        deposite_layout_qr_img = view.findViewById(R.id.coin_address_qrcode);
        deposite_layout_address_txt = view.findViewById(R.id.coin_address);
        deposite_layout_base_address = view.findViewById(R.id.coin_base_address);
        TextView deposite_layout_tag_textview = view.findViewById(R.id.coin_address_tag_tv);
        deposite_layout_tag_qr_img = view.findViewById(R.id.coin_address_tag_qrcode);
//        TextView address_label = view.findViewById(R.id.coin_address_label);
        TextView base_address_label = view.findViewById(R.id.coin_base_address_label);
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


        try {
            String ty = j.getString("type");

        String co = j.getString("coin");
        String av = j.getString("available");
        String ad = j.getString("address");
        String bd = j.getString("base_address");
        de = j.getString("description");
        tag = j.getString("tag");
        String cfn = j.getString("coin_full_name");

        Log.d("DEPOSIT PAGER FRAGMENT", "ADDRESS=>"+ ad +"  BASE ADDRESS=>"+ bd +"  TAG=>"+tag);

        if(ad !=null && !ad.equals("null") && tag.equals("true")) {
            deposite_layout_address_txt.setText(ad);
            deposite_layout_base_address.setText(bd);
            String ADDRESS_TEXT = "Scan this QR Code to get ";
            deposite_layout_tag_textview.setText(ADDRESS_TEXT + " " + de);
            base_address_label.setText("Your "+de);
            qrCodeGenrator(ad, bd);

        }else{
            deposite_layout_tag_qr_img.setVisibility(View.GONE);
            deposite_layout_tag_textview.setVisibility(View.GONE);
            base_address_label.setVisibility(View.GONE);
            deposite_layout_base_address.setVisibility(View.GONE);
            wallet_maintain_layout.setVisibility(View.VISIBLE);
            deposite_layout.setVisibility(View.GONE);

            if(ad !=null && !ad.equals("null") && tag.equals("false")){
                deposite_layout_address_txt.setText(ad);
                qrCodeGenrator(ad, bd);
                wallet_maintain_layout.setVisibility(View.GONE);
                deposite_layout.setVisibility(View.VISIBLE);

            }else{
                wallet_maintain_layout.setVisibility(View.VISIBLE);
                deposite_layout.setVisibility(View.GONE);
            }
        }



        deposite_layout_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cd = deposite_layout_address_txt.getText().toString();
                if(!cd.equals("")){
                    ClipData clipData = ClipData.newPlainText("ADDRESS",cd);
                    clipboardManager.setPrimaryClip(clipData);
                    new CoustomToast(context, "Address copied !",CoustomToast.TYPE_NORMAL).showToast();
                }
            }
        });

        deposite_layout_base_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cd = deposite_layout_base_address.getText().toString();
                if(!cd.equals("")){
                    ClipData clipData = ClipData.newPlainText("BASE ADDRESS",cd);
                    clipboardManager.setPrimaryClip(clipData);
                    new CoustomToast(context, de+" copied",CoustomToast.TYPE_NORMAL).showToast();
                }
            }
        });

//        if(ad!=null && !ad.equals("null")){
//            wallet_maintain_layout.setVisibility(View.GONE);
//            deposite_layout.setVisibility(View.VISIBLE);
//            deposite_layout_address_txt.setText(ad);
//            qrCodeGenrator(ad,bd);
//        }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void qrCodeGenrator(String address, String bass_address){
        if(address!=null && !address.equals("null")){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix address_Matrix = multiFormatWriter.encode(address, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmapAddress = barcodeEncoder.createBitmap(address_Matrix);
                deposite_layout_qr_img.setImageBitmap(bitmapAddress);

                if(tag.equals("true")) {
                    BitMatrix base_Matrix = multiFormatWriter.encode(bass_address, BarcodeFormat.QR_CODE, 500, 500);
                    Bitmap bitmapBase = barcodeEncoder.createBitmap(base_Matrix);
                    deposite_layout_tag_qr_img.setImageBitmap(bitmapBase);
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
}
