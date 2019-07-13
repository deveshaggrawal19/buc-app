package com.buyucoinApp.buyucoin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.buyucoinApp.buyucoin.Adapters.Dashboard_PagerAdapter;
import com.buyucoinApp.buyucoin.Fragments.ServerError;
import com.buyucoinApp.buyucoin.bottomsheets.SuperSettingsBottomsheet;
import com.buyucoinApp.buyucoin.broadcast.NotNetworkBroadCastReceiver;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.buyucoinApp.buyucoin.update.ForceUpdateChecker;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Dashboard extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener{

    private static final String TAG = "DASHBOARD" ;
    String ACCESS_TOKEN = null, refresh_token = null;
    static final String FRAGMENT_STATE = "FRAGMENT_STATE";
    View fragView;
    static BottomNavigationView bm;
    AlertDialog.Builder ad;
    static FragmentManager fragmentManager;
    LinearLayout noInternet, serverError;
    String FRAGENT_TYPE;
    ViewPager DashboardViewpager;
    BuyucoinPref buyucoinPref;
    NotNetworkBroadCastReceiver notNetworkBroadCastReceiver;
    Toolbar toolbar;
    static TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ForceUpdateChecker.with(Dashboard.this).onUpdateNeeded(Dashboard.this).check();
        initView();
        setUpDashboardPageChangeListner();
        setUpBottomNavigationListner();
        getNonFreshToken(refresh_token);


    }

    private void setUpBottomNavigationListner() {
        bm.inflateMenu(R.menu.bottom_navigation_menu_old);
        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.acc_det: toolbar_title.setText(R.string.account);DashboardViewpager.setCurrentItem(4);break;
                    case R.id.wll_bal: toolbar_title.setText(R.string.wallet);DashboardViewpager.setCurrentItem(0);break;
                    case R.id._rates: toolbar_title.setText(R.string.rates);DashboardViewpager.setCurrentItem(1);break;
                    case R.id._buysell: toolbar_title.setText(R.string.buy_sell);DashboardViewpager.setCurrentItem(2);break;
                    case R.id._p2p: toolbar_title.setText(R.string.p2p);DashboardViewpager.setCurrentItem(3);break;
                    default:toolbar_title.setText(R.string.wallet); DashboardViewpager.setCurrentItem(0);


                }
                return true;
            }


        });
    }

    private void setUpDashboardPageChangeListner() {
        DashboardViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                bm.getMenu().getItem(position).setChecked(true);
                switch (position){
                    case 0: toolbar_title.setText(R.string.wallet);break;
                    case 1: toolbar_title.setText(R.string.rates);break;
                    case 2: toolbar_title.setText(R.string.buy_sell);break;
                    case 3: toolbar_title.setText(R.string.p2p);break;
                    case 4: toolbar_title.setText(R.string.account);break;
                    default: toolbar_title.setText(R.string.wallet);break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        setUpBroadCastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setUpBroadCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(notNetworkBroadCastReceiver,filter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        OkHttpHandler.cancelAllRequests();
        unregisterReceiver(notNetworkBroadCastReceiver);
    }


    public void changeTab(int i) {
        bm.setSelectedItemId(i);
    }


    @Override
    public void onBackPressed() {
        setUpExitAlert();
    }

    private void setUpExitAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to exit")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }


    public void getNonFreshToken(String refresh_token) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("refresh_token", refresh_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpHandler.refresh_post("refresh", refresh_token, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                new CoustomToast(Dashboard.this,"Error retreiving API",CoustomToast.TYPE_DANGER).showToast();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d(TAG, "getNonFreshToken: "+s);


                try {

                    final JSONObject data = new JSONObject(s);
                    Log.d(TAG, "onResponse: "+data.toString());
                    buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN, data.getString("access_token"));
                    buyucoinPref.setEditpref("bank_upload",data.getBoolean("bank_upload"));
                    buyucoinPref.setEditpref("email_verified", data.getBoolean("email_verified"));
                    buyucoinPref.setEditpref("kyc_upload",data.getBoolean("kyc_upload"));
                    buyucoinPref.setEditpref("kyc_status", data.getBoolean("kyc_verified"));
                    buyucoinPref.setEditpref("mob_verified", data.getBoolean("mob_verified"));
                    buyucoinPref.setEditpref("user_status",data.getString("user_status"));
                    buyucoinPref.setEditpref("wallet",data.getBoolean("wallet"));
                } catch (final Exception e) {
                    e.printStackTrace();
//                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CoustomToast(Dashboard.this, e.getMessage(), CoustomToast.TYPE_NORMAL).showToast();
                        }
                    });


                }
            }
        });
    }

    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CoustomToast(getApplicationContext(),s,CoustomToast.TYPE_DANGER).showToast();
            }
        });
    }



    public void inrToP2P() {
        if (!isFinishing() && !isDestroyed()) {
            toolbar_title.setText("P2P");
            changeTab(R.id._p2p);
        }

    }

    public void ServerErrorFragment() {
        if (!isFinishing() && !isDestroyed()) {
            ServerError serverError = new ServerError();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, serverError);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public  void reloadPageer(){
        if(DashboardViewpager.getAdapter()!=null)
        DashboardViewpager.getAdapter().notifyDataSetChanged();
    }

    public void noInternet(boolean iscon){
       if(!iscon) noInternet.setVisibility(View.VISIBLE);
       else noInternet.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.action_super_setting){
                SuperSettingsBottomsheet superSettingsBottomsheet = new SuperSettingsBottomsheet();
                superSettingsBottomsheet.show(getSupportFragmentManager(), "SUPER SETTINGS BOTTOM SHEET");

        }
        return super.onOptionsItemSelected(item);
    }


    public void initView(){
        bm = findViewById(R.id._btnbar);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        noInternet = findViewById(R.id.ll_no_internet);
        serverError = findViewById(R.id.ll_server_error);
        DashboardViewpager = findViewById(R.id.dashboard_viewpager);
        DashboardViewpager.setAdapter(new Dashboard_PagerAdapter(getSupportFragmentManager()));
        buyucoinPref = new BuyucoinPref(getApplicationContext());
        notNetworkBroadCastReceiver = new NotNetworkBroadCastReceiver(this);
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        FRAGENT_TYPE = buyucoinPref.getPrefString(FRAGMENT_STATE);
        refresh_token = buyucoinPref.getPrefString("refresh_token");
        ad = new AlertDialog.Builder(this);
        fragmentManager = getSupportFragmentManager();
        fragView = findViewById(R.id.flContent);

    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        try {
        AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue Trading.")
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}