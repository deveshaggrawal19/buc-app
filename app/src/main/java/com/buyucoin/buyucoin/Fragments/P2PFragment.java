package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

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
    EditText amount, transaction;
    Button b;
    String ACCESS_TOKEN = null;

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

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_p2p, container, false);

        amount = (EditText) view.findViewById(R.id.etP2PAmount);
        transaction = (EditText) view.findViewById(R.id.etP2PTransaction);

        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rgp2p);
        b = (Button) view.findViewById(R.id.bP2Prequest);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radioButton:
                        b.setText("Deposit Request");
                        transaction.setEnabled(true);
                        transaction.setText("");
                        transaction.setHint("Transaction ID");
                        break;
                    case R.id.radioButton2:
                        transaction.setHint("Currency");
                        transaction.setEnabled(false);
                        transaction.setText("inr");
                        b.setText("Withdraw Request");
                        break;
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                switch(rg.getCheckedRadioButtonId()){
                    case R.id.radioButton:
                        try {
                            obj.put("amount", Integer.parseInt(amount.getText().toString())).put("tx_id", transaction.getText().toString()).put("type", "neft");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request("create_deposit", obj.toString());
                        break;
                    case R.id.radioButton2:
                        try {
                            obj.put("amount", 100).put("currency", "inr");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request("create_withdraw", obj.toString());
                        break;
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

    public void request(String url, String content){
        OkHttpHandler.auth_post(url, ACCESS_TOKEN, content, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d("RESP", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if(object.getString("status").equals("error")){
                        Utilities.showToast(getActivity(), "Error: "+object.getJSONArray("message").getJSONArray(0).getString(0));
                    }else {
                        Utilities.showToast(getActivity(), object.getJSONArray("message").getJSONArray(0).getString(0));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
