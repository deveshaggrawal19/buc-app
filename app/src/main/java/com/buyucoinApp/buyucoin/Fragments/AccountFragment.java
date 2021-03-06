package com.buyucoinApp.buyucoin.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.buyucoinApp.buyucoin.Dashboard;
import com.buyucoinApp.buyucoin.OkHttpHandler;
import com.buyucoinApp.buyucoin.R;
import com.buyucoinApp.buyucoin.RetrofitHandler;
import com.buyucoinApp.buyucoin.Utilities;
import com.buyucoinApp.buyucoin.bottomsheets.ChatsBottomsheet;
import com.buyucoinApp.buyucoin.bottomsheets.ProfileBottomsheet;
import com.buyucoinApp.buyucoin.bottomsheets.ReferralBottomsheet;
import com.buyucoinApp.buyucoin.bottomsheets.SettingsBottomsheet;
import com.buyucoinApp.buyucoin.customDialogs.ChangePasscodeDialog;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
//import com.buyucoinApp.buyucoin.retrofitClients.RetrofitClients;
//import com.buyucoinApp.buyucoin.retrofitRepos.account.AccountResponse;
//import com.buyucoinApp.buyucoin.retrofitRepos.account.Data;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private String ACCESS_TOKEN = null;
    private TextView email, mob, name,ref_id;
    private Button kyc_button;
    private ProgressBar pb;
    private View ll;
    private AlertDialog.Builder ad;
    private BuyucoinPref buyucoinPref;
    private LinearLayout profile_sheet_handler,
            share_ref_id,
            referral_sheet_handler,
            chats_sheet_handler,
            settings_sheet_handler,
            account_dep_history,
            account_with_history,
            account_trade_history;
    private Switch app_pass_switch;
    private boolean applock_enabled;
    private ImageView imageView;
    private RelativeLayout account_about_us, account_term_policy,change_passcode_layout;
    private String referral_id = "";
    private Button generate_wallet;
    private ShimmerFrameLayout shimmerFrameLayout;

    public AccountFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buyucoinPref = new BuyucoinPref(Objects.requireNonNull(getContext()));
        ACCESS_TOKEN = buyucoinPref.getPrefString("access_token");
        applock_enabled = buyucoinPref.getPrefBoolean("DISABLE_PASS_CODE");
        String FRAGMENT_STATE = "ACCOUNT";
        ad = new AlertDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        InitializeAllViews(view);
        sheetClickHandler();
        HistoryClickHandler();
        termAndAbout();
        MakeCircularImage();
        AppPassWordHandler(view);
        changePasscodeHandler();
        shareRefid();

        name.setText(buyucoinPref.getPrefString("name"));
        email.setText(buyucoinPref.getPrefString("email"));
        mob.setText(buyucoinPref.getPrefString("mob"));
        referral_id = buyucoinPref.getPrefString("ref_id");
        ref_id.setText(referral_id);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getAccountData();
            }
        });


        return view;
    }



    private void shareRefid(){
        share_ref_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setChooserTitle("Share Referral link by")
                        .setText("https://www.buyucoin.com/referral?referral="+referral_id)
                        .startChooser();
            }
        });
    }

    private void sheetClickHandler() {
        profile_sheet_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileBottomsheet profileBottomsheet = new ProfileBottomsheet();
                profileBottomsheet.show(getChildFragmentManager(),"PROFILE BOTTOM SHEET");

                makeViewDisable(profile_sheet_handler);


            }
        });
        referral_sheet_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferralBottomsheet referralBottomsheet = new ReferralBottomsheet();
                referralBottomsheet.show(getChildFragmentManager(),"REFERRAL BOTTOM SHEET");

                makeViewDisable(referral_sheet_handler);
            }
        });
        chats_sheet_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatsBottomsheet chatsBottomsheet = new ChatsBottomsheet();
                chatsBottomsheet.show(getChildFragmentManager(),"REFERRAL BOTTOM SHEET");

                makeViewDisable(chats_sheet_handler);
            }
        });
        settings_sheet_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsBottomsheet settingsBottomsheet = new SettingsBottomsheet();
                settingsBottomsheet.show(getChildFragmentManager(),"REFERRAL BOTTOM SHEET");

                makeViewDisable(settings_sheet_handler);
            }
        });
    }

    private void HistoryClickHandler(){

        final Bundle bundle = new Bundle();

        account_dep_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment historyFragment = new HistoryFragment();
                bundle.putInt("POSITION",0);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:0");

                makeViewDisable(account_dep_history);

            }
        });

        account_with_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment historyFragment = new HistoryFragment();
                bundle.putInt("POSITION",1);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:1");

                makeViewDisable(account_with_history);

            }
        });
        account_trade_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFragment historyFragment = new HistoryFragment();
                bundle.putInt("POSITION",2);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:2");

                makeViewDisable(account_trade_history);

            }
        });
    }

    private void termAndAbout(){
        account_term_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buyucoin.com/privacy-policy"));
                startActivity(intent);
            }
        });
        account_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buyucoin.com/about"));
                startActivity(intent);
            }
        });

    }

    public static void makeViewDisable(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },1000);
    }

    private void MakeCircularImage(){
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    private void AppPassWordHandler(final View view){
        app_pass_switch.setChecked(applock_enabled);
        app_pass_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   buyucoinPref.setEditpref("DISABLE_PASS_CODE",true);
                    new CoustomToast(view.getContext(), "App Lock Disabled",CoustomToast.TYPE_SUCCESS).showToast();
                    change_passcode_layout.setEnabled(false);
                    change_passcode_layout.setAlpha(0.5f);

                }else{
                    buyucoinPref.setEditpref("DISABLE_PASS_CODE",false);
                    new CoustomToast(view.getContext(),"App Lock Enabled",CoustomToast.TYPE_SUCCESS).showToast();
                    change_passcode_layout.setEnabled(true);
                    change_passcode_layout.setAlpha(1f);
                }
            }
        });
    }

    private void changePasscodeHandler(){
        if(applock_enabled){
            change_passcode_layout.setEnabled(false);
            change_passcode_layout.setAlpha(0.5f);
        }
        change_passcode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment changePassocde = new ChangePasscodeDialog();
                changePassocde.setCancelable(true);
                changePassocde.show(getChildFragmentManager(),"CHANGE PASSCODE");
            }
        });
    }

//
//    private void getAccountDataNew(){
//        RetrofitClients retrofitClients =  RetrofitHandler.createAuthService(RetrofitClients.class,ACCESS_TOKEN);
//
//        retrofit2.Call<AccountResponse> account_call = retrofitClients.getAccount();
//
//        account_call.enqueue(new retrofit2.Callback<AccountResponse>() {
//            @Override
//            public void onResponse(retrofit2.Call<AccountResponse> call, final retrofit2.Response<AccountResponse> response) {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            AccountResponse ar = response.body();
//                            Data d = ar.getData();
//                            if (ar.getStatus().equals("redirect")) {
//                                Utilities.getOTP(getActivity(), ACCESS_TOKEN, ad);
//                                new Dashboard().ServerErrorFragment();
//                                return;
//                            }
//
//                            buyucoinPref.setEditpref("email",d.getEmail());
//                            buyucoinPref.setEditpref("name",d.getName().split(" ")[0]);
//                            buyucoinPref.setEditpref("mob",d.getMob());
//                            buyucoinPref.setEditpref("kyc_status",d.getKycStatus());
//
//                            email.setText(d.getEmail());
//                            name.setText(d.getName());
//                            mob.setText(d.getMob());
////                          kyc.getBackground().setTint(Color.green(R.color.kyc_color));
//                            ll.setVisibility(View.VISIBLE);
//                            Utilities.hideProgressBar(pb);
//
//                        }
//                    });
//                }
//                else{
//                    try {
//                        Looper.prepare();
//                        new CoustomToast(getContext(),"Server Error",CoustomToast.TYPE_DANGER).showToast();
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<AccountResponse> call, Throwable t) {
//                if(getActivity()!=null){
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new CoustomToast(getContext(),"Something Went Wrong",CoustomToast.TYPE_DANGER).showToast();
//                        }
//                    });
//                }
//            }
//        });
//    }
//

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void getAccountData() {
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if(getActivity()!=null){
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new CoustomToast(getContext(),"Something Went Wrong",CoustomToast.TYPE_DANGER).showToast();
                    }
                });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                final String s = response.body().string();
                Log.d("ACCOUNT DATA", "onResponse: "+s);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("status").equals("redirect")) {
                                    Utilities.getOTP(getActivity(), ACCESS_TOKEN, ad);
                                    new Dashboard().ServerErrorFragment();
                                    return;
                                }
                                final JSONObject data = jsonObject.getJSONObject(("data"));
                                buyucoinPref.setEditpref("email",data.get("email").toString());
                                buyucoinPref.setEditpref("name",data.get("name").toString().split(" ")[0]);
                                buyucoinPref.setEditpref("mob",data.get("mob").toString());
                                buyucoinPref.setEditpref("kyc_status",data.getBoolean("kyc_status"));

                                email.setText(data.getString("email"));
                                name.setText(data.getString("name"));
                                mob.setText(data.getString("mob"));
//                                kyc.getBackground().setTint(Color.green(R.color.kyc_color));
                                ll.setVisibility(View.VISIBLE);
                                Utilities.hideProgressBar(pb);
                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else{
                    try {
                        Looper.prepare();
                        new CoustomToast(getContext(),"Server Error",CoustomToast.TYPE_DANGER).showToast();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void InitializeAllViews(View view){
        email = view.findViewById(R.id.tvAccountEmail);
        ImageView kyc = view.findViewById(R.id.tvAccountKyc);
        mob = view.findViewById(R.id.tvAccountMobile);
        name = view.findViewById(R.id.tvAccountName);
        pb = view.findViewById(R.id.pbAccount);
        ll = view.findViewById(R.id.llAccount);
        imageView = view.findViewById(R.id.acc_pic);
        profile_sheet_handler = view.findViewById(R.id.bottom_sheet_profile_handler);
        referral_sheet_handler = view.findViewById(R.id.bottom_sheet_referral_handler);
        chats_sheet_handler = view.findViewById(R.id.bottom_sheet_chats_handler);
        settings_sheet_handler = view.findViewById(R.id.bottom_sheet_settings_handler);
        app_pass_switch = view.findViewById(R.id.disable_app_lock_switch);
        account_dep_history = view.findViewById(R.id.account_dep_history);
        account_with_history = view.findViewById(R.id.account_with_history);
        account_trade_history = view.findViewById(R.id.account_trade_history);
        account_about_us = view.findViewById(R.id.account_about_us_link);
        account_term_policy = view.findViewById(R.id.account_term_policy);
        ref_id = view.findViewById(R.id.ref_id);
        share_ref_id = view.findViewById(R.id.share_ref_id);
        change_passcode_layout = view.findViewById(R.id.change_passcode_layout);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setAlpha(0.2f);

    }


}
