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

import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DepositePagerFragment extends Fragment {

    private Bundle b;
    private ClipboardManager clipboardManager;
    private Context context;
    private LinearLayout wallet_maintain_layout,deposite_layout;
    private ImageView deposite_layout_qr_img;
    private String ty,co,av,ad,bd,de,tag,cfn;
    private TextView deposite_layout_address_txt;
    private TextView deposite_layout_base_address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
         b = getArguments();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deposite_pager_layout,container,false);


        context = view.getContext();
        wallet_maintain_layout = view.findViewById(R.id.wallet_maintain_layout);
        deposite_layout = view.findViewById(R.id.deposite_layout);
        deposite_layout_qr_img = view.findViewById(R.id.coin_address_qrcode);
        deposite_layout_address_txt = view.findViewById(R.id.coin_address);
        deposite_layout_base_address = view.findViewById(R.id.coin_base_address);
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


        ty = b.getString("type");
        co = b.getString("coin");
        av = b.getString("available");
        ad = b.getString("address");
        bd = b.getString("base_address");
        de = b.getString("description");
        tag = b.getString("tag");
        cfn = b.getString("coin_full_name");

        Log.d("DEPOSIT PAGER FRAGMENT", "ADDRESS=>"+ad+"  BASE ADDRESS=>"+bd+"  TAG=>"+tag);




        deposite_layout_address_txt.setText(ad);
        deposite_layout_base_address.setText(bd);
        deposite_layout_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cd = deposite_layout_address_txt.getText().toString();
                if(!cd.equals("") && cd!=null){
                    ClipData clipData = ClipData.newPlainText("ADDRESS",cd);
                    clipboardManager.setPrimaryClip(clipData);
                    new CoustomToast(context, "ADDRESS COPIED !",CoustomToast.TYPE_NORMAL).showToast();
                }
            }
        });

        if(ad!=null && !ad.equals("null")){
            wallet_maintain_layout.setVisibility(View.GONE);
            deposite_layout.setVisibility(View.VISIBLE);
            deposite_layout_address_txt.setText(ad);
            qrCodeGenrator(ad,bd);
        }

        return view;
    }

    public void qrCodeGenrator(String address,String bass_address){
        if(address!=null && !address.equals("null")){
            String fulladdress ;
            if(bass_address!=null && !bass_address.equals("null")){
                fulladdress = "address : ["+address+"] \n tag : ["+bass_address+"]";
            }else{
                fulladdress = "address : ["+address+"]";
            }


            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(fulladdress, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                deposite_layout_qr_img.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
}
