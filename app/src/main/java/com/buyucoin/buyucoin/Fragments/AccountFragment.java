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
    TextView email,mob, name;
    ProgressBar pb;
    View ll;
    AlertDialog.Builder ad;
    ImageView imageView,kyc;

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
        ad = new AlertDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        email =  view.findViewById(R.id.tvAccountEmail);
        kyc =  view.findViewById(R.id.tvAccountKyc);
        mob =  view.findViewById(R.id.tvAccountMobile);
        name =  view.findViewById(R.id.tvAccountName);
        pb =  view.findViewById(R.id.pbAccount);
        ll =  view.findViewById(R.id.llAccount);
        imageView = view.findViewById(R.id.acc_pic);

        Drawable drawable = imageView.getDrawable();

        Bitmap bitmap =  ((BitmapDrawable) drawable).getBitmap() ;
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);


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

    public void getAccountData(){
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error retreiving API", Toast.LENGTH_LONG).show();
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
