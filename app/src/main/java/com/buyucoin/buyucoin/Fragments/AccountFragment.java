package com.buyucoin.buyucoin.Fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Dashboard;
import com.buyucoin.buyucoin.OkHttpHandler;
import com.buyucoin.buyucoin.R;
import com.buyucoin.buyucoin.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private String ACCESS_TOKEN = null;
    private TextView email, mob, name;
    private ProgressBar pb;
    private View ll;
    private AlertDialog.Builder ad;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit_pref;
    private LinearLayout profile_sheet_handler,
            referral_sheet_handler,
            chats_sheet_handler,
            settings_sheet_handler,
            account_dep_history,
            account_with_history,
            account_trade_history;
    private Switch app_pass_switch;
    private boolean applock_enabled;
    private ImageView kyc;
    private ImageView imageView;

    public AccountFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
        edit_pref = getActivity().getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
        ACCESS_TOKEN = prefs.getString("access_token", null);
        applock_enabled = prefs.getBoolean("DISABLE_PASS_CODE",false);
        String FRAGMENT_STATE = "ACCOUNT";
        edit_pref.putString("FRAGMENT_STATE", FRAGMENT_STATE).apply();
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
        MakeCircularImage();
        AppPassWordHandler(view);

        name.setText(prefs.getString("name", ""));
        email.setText(prefs.getString("email", ""));
        mob.setText(prefs.getString("mob", ""));

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getAccountData();
            }
        });
        return view;
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
        final DialogFragment historyFragment = new HistoryFragment();
        final Bundle bundle = new Bundle();

        account_dep_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("SELECTED_TAB",0);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:0");

                makeViewDisable(account_dep_history);

            }
        });

        account_with_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("SELECTED_TAB",1);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:1");

                makeViewDisable(account_with_history);

            }
        });
        account_trade_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("SELECTED_TAB",2);
                historyFragment.setArguments(bundle);
                historyFragment.show(getChildFragmentManager(),"TAB:2");

                makeViewDisable(account_trade_history);

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
                    edit_pref.putBoolean("DISABLE_PASS_CODE",true).apply();
                    Toast.makeText(view.getContext(),"App Lock Disabled",Toast.LENGTH_SHORT).show();

                }else{
                    edit_pref.putBoolean("DISABLE_PASS_CODE",false).apply();
                    Toast.makeText(view.getContext(),"App Lock Enabled",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }









    private void getAccountData() {
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                    Looper.prepare();
                    Toast.makeText(getActivity(), "Error retreiving API", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                final String s = response.body().string();

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
                                edit_pref.putString("email",data.get("email").toString()).apply();
                                edit_pref.putString("name",data.get("name").toString()).apply();
                                edit_pref.putString("mob",data.get("mob").toString()).apply();
                                edit_pref.putBoolean("kyc_status",data.getBoolean("kyc_status")).apply();

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
                else{
                    Looper.prepare();
                    new Dashboard().ServerErrorFragment();
                }

            }
        });
    }

    private void InitializeAllViews(View view){
        email = view.findViewById(R.id.tvAccountEmail);
        kyc = view.findViewById(R.id.tvAccountKyc);
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
    }


}
