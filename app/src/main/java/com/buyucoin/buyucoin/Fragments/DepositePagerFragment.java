package com.buyucoin.buyucoin.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.R;
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
        String ty,co,av,ad,de,tag,cfn;
        ImageView deposite_layout_qr_img;
        final TextView deposite_layout_address_txt;
        context = view.getContext();

        deposite_layout_qr_img = view.findViewById(R.id.coin_address_qrcode);
        deposite_layout_address_txt = view.findViewById(R.id.coin_address);
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


        ty = b.getString("type");
        co = b.getString("coin");
        av = b.getString("available");
        ad = b.getString("address");
        de = b.getString("description");
        tag = b.getString("tag");
        cfn = b.getString("coin_full_name");

        deposite_layout_address_txt.setText(ad);
        deposite_layout_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cd = deposite_layout_address_txt.getText().toString();
                if(!cd.equals("") && cd!=null){
                    ClipData clipData = ClipData.newPlainText("ADDRESS",cd);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, "ADDRESS COPIED !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(ad!=null){
            deposite_layout_address_txt.setText(ad);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(ad, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                deposite_layout_qr_img.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
