package com.buyucoinApp.buyucoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WithdrawPagerFragment extends Fragment {
    private Bundle b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            b = getArguments();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdraw_pager_layout, container, false);
        final String ty, co, av, ad, de, tag, cfn;
        LinearLayout tag_layout;
        Button withdraw_layout_btnview;
        TextView withdraw_coin_name;
        final EditText destination_tag, coin_amonut, coin_address;

        ty = b.getString("type");
        co = b.getString("coin");
        av = b.getString("available");
        ad = b.getString("address");
        de = b.getString("description");
        tag = b.getString("tag");
        cfn = b.getString("coin_full_name");

        tag_layout = view.findViewById(R.id.tag_layout);
        withdraw_layout_btnview = view.findViewById(R.id.withdraw_layout_btnview);
        destination_tag = view.findViewById(R.id.destination_tag_edittext);
        coin_amonut = view.findViewById(R.id.withdraw_layout_amount_et);
        coin_address = view.findViewById(R.id.withdraw_layout_address_et);
        withdraw_coin_name = view.findViewById(R.id.withdraw_coin_name);
        withdraw_coin_name.setText(co);

        if (tag.equals("true")) {
            tag_layout.setVisibility(View.VISIBLE);
            destination_tag.setHint(de);

        }


        withdraw_layout_btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coin_amonut.getText().toString().equals("")) {

                    double amount = Double.valueOf(coin_amonut.getText().toString());
                    String tag = destination_tag.getText().toString();
                    String address = coin_address.getText().toString();

                    Bundle bundle = new Bundle();
                    if (amount > 0 && !address.equals("")) {

                        bundle.putString("coin_tag", tag);
                        bundle.putDouble("coin_amount", amount);
                        bundle.putString("coin_address", address);
                        bundle.putString("coin_name",co);


                        WithdrawBottomsheet withdrawBottomsheet = new WithdrawBottomsheet();
                        withdrawBottomsheet.setArguments(bundle);
                        withdrawBottomsheet.show(getChildFragmentManager(), "WITHDRAW");
                    }

                }

            }
        });


        return view;
    }


}
