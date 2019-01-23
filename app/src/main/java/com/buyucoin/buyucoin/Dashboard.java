package com.buyucoin.buyucoin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Adapters.Dashboard_PagerAdapter;
import com.buyucoin.buyucoin.Fragments.AccountFragment;
import com.buyucoin.buyucoin.Fragments.BuySellFragment;
import com.buyucoin.buyucoin.Fragments.HistoryFragment;
import com.buyucoin.buyucoin.Fragments.P2PFragment;
import com.buyucoin.buyucoin.Fragments.RateFragment;
import com.buyucoin.buyucoin.Fragments.ReferralFragment;
import com.buyucoin.buyucoin.Fragments.ServerError;
import com.buyucoin.buyucoin.Fragments.SuperSettingsBottomsheet;
import com.buyucoin.buyucoin.Fragments.WalletFragment;
import com.buyucoin.buyucoin.broadcast.NotNetworkBroadCastReceiver;
import com.buyucoin.buyucoin.customDialogs.CoustomToast;
import com.buyucoin.buyucoin.pref.BuyucoinPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Dashboard extends AppCompatActivity {

    String ACCESS_TOKEN = null, refresh_token = null;
    static final String FRAGMENT_STATE = "FRAGMENT_STATE";
    static Toolbar toolbar;
    TextView navname, navemail;
    View fragView;
    static BottomNavigationView bm;
    AlertDialog.Builder ad;
    static FragmentManager fragmentManager;
    LinearLayout noInternet, serverError, bottom_sheet_layout;

    String FRAGENT_TYPE;
    ViewPager DashboardViewpager;
    MenuItem prev = null;
    BuyucoinPref buyucoinPref;
    NotNetworkBroadCastReceiver notNetworkBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bm = findViewById(R.id._btnbar);
        setSupportActionBar(toolbar);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        View header = navigationView.getHeaderView(0);
        navname = (TextView) header.findViewById(R.id.tvName);
        navemail = (TextView) header.findViewById(R.id.tvEmail);

        fragView = findViewById(R.id.flContent);


        DashboardViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    bm.getMenu().getItem(position).setChecked(true);
                    switch (position){
                        case 0: toolbar.setTitle(R.string.wallet);break;
                        case 1: toolbar.setTitle(R.string.rates);break;
                        case 2: toolbar.setTitle(R.string.buy_sell);break;
                        case 3: toolbar.setTitle(R.string.p2p);break;
                        case 4: toolbar.setTitle(R.string.account);break;
                        default: toolbar.setTitle(R.string.wallet);break;
                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.acc_det: toolbar.setTitle(R.string.account);DashboardViewpager.setCurrentItem(4);break;
                    case R.id.wll_bal: toolbar.setTitle(R.string.wallet);DashboardViewpager.setCurrentItem(0);break;
                    case R.id._rates: toolbar.setTitle(R.string.rates);DashboardViewpager.setCurrentItem(1);break;
                    case R.id._buysell: toolbar.setTitle(R.string.buy_sell);DashboardViewpager.setCurrentItem(2);break;
                    case R.id._p2p: toolbar.setTitle(R.string.p2p);DashboardViewpager.setCurrentItem(3);break;
                    default:toolbar.setTitle(R.string.wallet); DashboardViewpager.setCurrentItem(0);


                }
                return true;
            }


        });

        getNonFreshToken(refresh_token);


    }

    public void updateFrammentState(String STATE) {
        buyucoinPref.setEditpref(FRAGMENT_STATE,STATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    public void BuySellFragmentFun() {
        if (!isFinishing() && !isDestroyed()) {
            Fragment fragment = new P2PFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
        }
//        changeTab(R.id._buysell);

    }


    public void changeTab(int i) {
        bm.setSelectedItemId(i);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
            editor.remove("access_token");
            editor.remove("refresh_token");
            editor.apply();
            new CoustomToast(getApplicationContext(),this,"Logging out....",CoustomToast.TYPE_SUCCESS).showToast();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_settings) {
            SuperSettingsBottomsheet superSettingsBottomsheet = new SuperSettingsBottomsheet();
            superSettingsBottomsheet.show(getSupportFragmentManager(), "SUPER SETTINGS BOTTOM SHEET");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")



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
                Utilities.showToast(Dashboard.this, "Error retreiving API");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject jsonObject1 = new JSONObject(s);
                    buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN,jsonObject1.getJSONObject("data").getString("access_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage());


                }
            }
        });
    }

    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CoustomToast(getApplicationContext(),Dashboard.this,s,CoustomToast.TYPE_DANGER).showToast();
            }
        });
    }

    public void reload() {
        finish();
        startActivity(getIntent());
    }






    public void inrToP2P() {
        if (!isFinishing() && !isDestroyed()) {
            P2PFragment fragment = new P2PFragment();
            toolbar.setTitle("P2P");
            changeTab(R.id._p2p);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commitAllowingStateLoss();
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

    public void HistoryFragment(int tab) {
        if (!isFinishing() && !isDestroyed()) {

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



}