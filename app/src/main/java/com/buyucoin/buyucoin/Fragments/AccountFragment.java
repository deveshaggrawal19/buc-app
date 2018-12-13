package com.buyucoin.buyucoin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;

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
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    String ACCESS_TOKEN = null;
    TextView email, kyc, mob, name;

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2, mParam3, mParam4;
    static JSONObject mRefs;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        ACCESS_TOKEN = prefs.getString("access_token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        email = (TextView) view.findViewById(R.id.tvAccountEmail);
        kyc = (TextView) view.findViewById(R.id.tvAccountKyc);
        mob = (TextView) view.findViewById(R.id.tvAccountMobile);
        name = (TextView) view.findViewById(R.id.tvAccountName);

        loadProfile();

        SharedPreferences prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);


            email.setText(prefs.getString("email","test"));
            String _kyc = prefs.getString("kyc","test");
            String kyc_test = (_kyc.equals("true"))?"KYC VERIFIED":"KYC NOT VERIFIED";
            int color = R.color.kyc_color ;
            Toast.makeText(this.getContext(),kyc_test, Toast.LENGTH_SHORT).show();
            if(_kyc.equals("true")){
                kyc.setText(kyc_test);
                kyc.setBackgroundColor(getResources().getColor(color));
            }else{
                kyc.setText(kyc_test);

            }
            mob.setText(prefs.getString("mob","test"));
            name.setText(prefs.getString("name","test"));


        return view;
    }


    public void loadProfile(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try{
                    JSONObject jsonObject1 = new JSONObject(s);
                    String name = jsonObject1.getJSONObject("data").getString("name");
                    String email = jsonObject1.getJSONObject("data").getString("email");
                    String kyc = jsonObject1.getJSONObject("data").getString("kyc_status");
                    String mob = jsonObject1.getJSONObject("data").getString("mob");

                    Log.d("RESPONSE_____",name+"\t"+email+"\t"+kyc+"\t"+mob);


                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
                    editor.putString("name",name)
                            .putString("email",email)
                            .putString("kyc",kyc)
                            .putString("mob",mob)
                            .apply();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

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
        void onFragmentInteraction(JSONObject item);
    }

}
