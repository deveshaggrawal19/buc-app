package com.buyucoinApp.buyucoin.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.buyucoinApp.buyucoin.Interfaces.TriggerActiveOrder;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.bottomsheets.P2P_PayBottomsheet;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.customDialogs.P2pActiveOrdersDialog;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class P2PFragment extends Fragment implements TriggerActiveOrder, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText amount;
    CheckBox imps_cb, upi_cb, notime_cb;
    SeekBar timelimit_sb;
    TextView require_buc_tv, avialable_buc_tv, bonus_tv;
    LinearLayout upi_layout, buc_token_info, fee_layout;

    Button b;
    String ACCESS_TOKEN = null;
    int amt, min_amt = 100;
    String type = "deposit";
    LinearLayout p2p_active_orders_layout;
    JSONArray payment_method_arry = new JSONArray();
    String buc_amount = "0";
    ProgressDialog p;


    //    private OnFragmentInteractionListener mListener;

    public P2PFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment P2PFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static P2PFragment newInstance(String param1, String param2) {
        P2PFragment fragment = new P2PFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        BuyucoinPref prefs = new BuyucoinPref(Objects.requireNonNull(getActivity()));

        ACCESS_TOKEN = prefs.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        buc_amount = prefs.getPrefString("buc_amount");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_p2p, container, false);

        amount = view.findViewById(R.id.etP2PAmount);
        imps_cb = view.findViewById(R.id.imps_cb);
        upi_cb = view.findViewById(R.id.upi_cb);
        notime_cb = view.findViewById(R.id.notime_cb);
        timelimit_sb = view.findViewById(R.id.seekBar);
        bonus_tv = view.findViewById(R.id.bonus);
        require_buc_tv = view.findViewById(R.id.buc_needed);
        avialable_buc_tv = view.findViewById(R.id.buc_available);
        fee_layout = view.findViewById(R.id.fees_layout);
        buc_token_info = view.findViewById(R.id.buc_token_info_layout);
        upi_layout = view.findViewById(R.id.upi_layout);
        payment_method_arry.put("imps");

        avialable_buc_tv.setText(buc_amount);

        p = new ProgressDialog(getActivity());
        p.setTitle("Fetching Fees");
        p.setMessage("please wait while fetching your fees details");
        p.create();


        timelimit_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                generateFees(String.valueOf(seekBar.getProgress()));

            }
        });

        notime_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    timelimit_sb.setEnabled(false);
                else
                    timelimit_sb.setEnabled(true);
            }
        });






        p2p_active_orders_layout = view.findViewById(R.id.p2p_active_orders_ll);
        p2p_active_orders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment active_order = P2pActiveOrdersDialog.newInstance();
                active_order.show(getChildFragmentManager(),"");
            }
        });
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        int main_amt = Integer.parseInt(amount.getText().toString());
                        String min_amount = "100";
                        if (main_amt < 100) {
                            amount.setText(min_amount);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imps_cb.setOnCheckedChangeListener(this);
        upi_cb.setOnCheckedChangeListener(this);


        final RadioGroup rg = view.findViewById(R.id.rgp2p);
        b = view.findViewById(R.id.bP2Prequest);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton:
                        b.setText("Deposit Request");
                        type = "deposit";
                        fee_layout.setVisibility(View.GONE);
                        bonus_tv.setVisibility(View.VISIBLE);
                        buc_token_info.setVisibility(View.GONE);
                        upi_layout.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton2:

                        b.setText("Withdraw Request");
                        type = "withdraw";
                        fee_layout.setVisibility(View.VISIBLE);
                        bonus_tv.setVisibility(View.GONE);
                        buc_token_info.setVisibility(View.VISIBLE);
                        if (upi_cb.isChecked()) {
                            upi_layout.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amt = (!amount.getText().toString().equals("")) ? Integer.parseInt(amount.getText().toString()) : 0;
                min_amt = 100;

                if ((amt >= 100 && min_amt <= amt && min_amt != 0)) {
                    P2P_PayBottomsheet p2P_payBottomsheet = new P2P_PayBottomsheet();
                    Bundle bundle = new Bundle();
                    bundle.putInt("amount", amt);
                    bundle.putInt("min_amount", min_amt);
                    bundle.putString("type", type);
                    p2P_payBottomsheet.setArguments(bundle);
                    p2P_payBottomsheet.show(getChildFragmentManager(), "PAY");
                    AccountFragment.makeViewDisable(b);

                }

                if (amt < 100) {
                    new CoustomToast(getContext(),"Amount must be 100 or more",CoustomToast.TYPE_DANGER).showToast();
                }
                if (min_amt < 100) {
                    new CoustomToast(getContext(),"Min Amount must be 100",CoustomToast.TYPE_DANGER).showToast();
                }
                if (min_amt > amt) {
                    new CoustomToast(getContext(),"Min Amount must be low",CoustomToast.TYPE_DANGER).showToast();
                }

            }


        });


        return view;
    }

    @Override
    public void triggerOrder() {
        DialogFragment active_order = P2pActiveOrdersDialog.newInstance();
        assert getFragmentManager() != null;
        active_order.show(getFragmentManager(),"");
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {

            if (imps_cb.isChecked()) {
                payment_method_arry.put(0, "imps");
                payment_method_arry.remove(1);
            }
            if (upi_cb.isChecked()) {
                payment_method_arry.put(1, "upi");
                payment_method_arry.remove(0);
                if (type.equals("withdraw")) {
                    upi_layout.setVisibility(View.VISIBLE);
                } else {
                    upi_layout.setVisibility(View.GONE);
                }


            } else {
                upi_layout.setVisibility(View.GONE);
            }
            if (imps_cb.isChecked() && upi_cb.isChecked()) {
                payment_method_arry.put(0, "imps");
                payment_method_arry.put(1, "upi");

            }
            if (!imps_cb.isChecked() && !upi_cb.isChecked()) {
                imps_cb.setChecked(true);
            }
            new CoustomToast(getActivity(), payment_method_arry.toString(), CoustomToast.TYPE_DANGER).showToast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateFees(String s) {

        p.show();
        OkHttpHandler.auth_post("get_notifications", ACCESS_TOKEN, s, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.dismiss();
                            double d = Math.round(Math.random() * 200);
                            require_buc_tv.setText(String.valueOf(d + 1));
                        }
                    });

                }
            }
        });
    }
}