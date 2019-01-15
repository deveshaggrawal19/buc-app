package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.P2POrders;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;
import com.buyucoin.buyucoin.textWatcher.P2P_TextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link P2PFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link P2PFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class P2PFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText amount,min_amount;
    Button b;
    String ACCESS_TOKEN = null;
    int amt,min_amt;
    String type = "deposit";
    LinearLayout min_amt_layout;


    private SharedPreferences prefs ;
    private SharedPreferences.Editor edit_pref ;
    private String FRAGMENT_STATE = "P2P";

    private OnFragmentInteractionListener mListener;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        edit_pref =  getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
        edit_pref.putString("FRAGMENT_STATE",FRAGMENT_STATE).apply();
        ACCESS_TOKEN = prefs.getString("access_token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_p2p, container, false);

        amount = (EditText) view.findViewById(R.id.etP2PAmount);
        min_amount = (EditText) view.findViewById(R.id.etP2PMinAmount);
        min_amt_layout  =view.findViewById(R.id.min_amt_layout);



        amount.addTextChangedListener(new P2P_TextWatcher(min_amount,min_amt_layout));




        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rgp2p);
        b = (Button) view.findViewById(R.id.bP2Prequest);



        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radioButton:
                        b.setText("Deposit Request");
                        type = "deposit";
                        break;
                    case R.id.radioButton2:

                        b.setText("Withdraw Request");
                        type = "withdraw";
                        break;
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amt = (!amount.getText().toString().equals(""))?Integer.parseInt(amount.getText().toString()):0;
                min_amt = (!min_amount.getText().toString().equals(""))?Integer.parseInt(min_amount.getText().toString()):0;
                P2P_PayBottomsheet p2P_payBottomsheet = new P2P_PayBottomsheet();
                Bundle bundle = new Bundle();
                if(amt>100 && min_amt < amt){
                    bundle.putInt("amount",amt);
                    bundle.putInt("min_amount", min_amt);
                    bundle.putString("type",type);
                    p2P_payBottomsheet.setArguments(bundle);
                    p2P_payBottomsheet.show(getChildFragmentManager(),"PAY");


                }
                else{
                    Toast.makeText(getContext(),"All Fields Are Mandatory",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}