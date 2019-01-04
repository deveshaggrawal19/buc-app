package com.buyucoin.buyucoin.Fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;



public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private String ACCESS_TOKEN = null;
    private TextView email,mob, name;
    private ProgressBar pb;
    private View ll;
    private AlertDialog.Builder ad;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs ;
    private SharedPreferences.Editor edit_pref ;
    private String FRAGMENT_STATE = "ACCOUNT";

    public AccountFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        edit_pref =  getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
        ACCESS_TOKEN = prefs.getString("access_token", null);
        edit_pref.putString("FRAGMENT_STATE",FRAGMENT_STATE).apply();
        ad = new AlertDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        email =  view.findViewById(R.id.tvAccountEmail);
        ImageView kyc = view.findViewById(R.id.tvAccountKyc);
        mob =  view.findViewById(R.id.tvAccountMobile);
        name =  view.findViewById(R.id.tvAccountName);
        pb =  view.findViewById(R.id.pbAccount);
        ll =  view.findViewById(R.id.llAccount);
        ImageView imageView = view.findViewById(R.id.acc_pic);

        Drawable drawable = imageView.getDrawable();

        Bitmap bitmap =  ((BitmapDrawable) drawable).getBitmap() ;
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);


        name.setText(prefs.getString("name", ""));
        email.setText(prefs.getString("email", ""));
        mob.setText(prefs.getString("mob", ""));

        getAccountData();
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

                    Log.d("/account RESPONSE",name+"\t"+email+"\t"+kyc+"\t"+mob);


                    edit_pref.putString("name",name)
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

    public void getAccountData(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Utilities.showToast(getActivity(), "Error retrieving API");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("RESP___", s);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if(jsonObject.getString("status").equals("redirect")){
                                    Utilities.getOTP(getActivity(), ACCESS_TOKEN, ad);
                                    new Dashboard().ServerErrorFragment();
                                    return;
                                }
                                final JSONObject data = jsonObject.getJSONObject(("data"));
                                email.setText(data.getString("email"));
                                name.setText(data.getString("name"));
                                mob.setText(data.getString("mob"));
//                                kyc.getBackground().setTint(Color.green(R.color.kyc_color));
                                ll.setVisibility(View.VISIBLE);
                                Utilities.hideProgressBar(pb);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

            }
        });
    }
}
