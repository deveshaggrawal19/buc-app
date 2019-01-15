package com.buyucoin.buyucoin.textWatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class P2P_TextWatcher implements TextWatcher {
    private EditText editText;
    private LinearLayout linearLayout;
    public P2P_TextWatcher(EditText editText, LinearLayout min_amt_layout){
        this.editText = editText;
        this.linearLayout = min_amt_layout;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        try{
            int amount = Integer.parseInt(s.toString());
            String min_amount = "100";
            if (amount>100){
                editText.setText(min_amount);
                linearLayout.setVisibility(View.VISIBLE);
            }else{
                linearLayout.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
