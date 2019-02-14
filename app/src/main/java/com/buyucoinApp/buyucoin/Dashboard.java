package com.buyucoinApp.buyucoin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyucoinApp.buyucoin.Adapters.Dashboard_PagerAdapter;
import com.buyucoinApp.buyucoin.Fragments.P2PFragment;
import com.buyucoinApp.buyucoin.Fragments.ServerError;
import com.buyucoinApp.buyucoin.Fragments.SuperSettingsBottomsheet;
import com.buyucoinApp.buyucoin.broadcast.NotNetworkBroadCastReceiver;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
import com.buyucoinApp.buyucoin.pref.BuyucoinPref;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DASHBOARD" ;
    String ACCESS_TOKEN = null, refresh_token = null;
    static final String FRAGMENT_STATE = "FRAGMENT_STATE";
//    TextView navname, navemail;
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
    Bundle b;
    static Toolbar toolbar;
    static TextView toolbar_title;
//    NavigationView navigationView;
//    DrawerLayout drawer ;
//    ActionBarDrawerToggle toggle;
    ImageView imageview_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        DialogFragment dialogFragment = new ConfirmPasscodeDialog();
//        dialogFragment.setCancelable(false);
//        dialogFragment.show(getSupportFragmentManager(),"");

        bm = findViewById(R.id._btnbar);
        toolbar = findViewById(R.id.toolbar);

        toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        drawer = findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        noInternet = findViewById(R.id.ll_no_internet);
        serverError = findViewById(R.id.ll_server_error);
        DashboardViewpager = findViewById(R.id.dashboard_viewpager);
        DashboardViewpager.setAdapter(new Dashboard_PagerAdapter(getSupportFragmentManager()));
        buyucoinPref = new BuyucoinPref(getApplicationContext());
        notNetworkBroadCastReceiver = new NotNetworkBroadCastReceiver(this);
        ACCESS_TOKEN = buyucoinPref.getPrefString(BuyucoinPref.ACCESS_TOKEN);
        FRAGENT_TYPE = buyucoinPref.getPrefString(FRAGMENT_STATE);
//        navigationView = findViewById(R.id.nav_view);
//        View header = navigationView.getHeaderView(0);
//        navname = header.findViewById(R.id.tvName);
//        navemail = header.findViewById(R.id.tvEmail);
        imageview_menu = findViewById(R.id.imageview_menu);


        refresh_token = buyucoinPref.getPrefString("refresh_token");

        ad = new AlertDialog.Builder(this);

        fragmentManager = getSupportFragmentManager();



        imageview_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperSettingsBottomsheet superSettingsBottomsheet = new SuperSettingsBottomsheet();
                superSettingsBottomsheet.show(getSupportFragmentManager(), "SUPER SETTINGS BOTTOM SHEET");
            }
        });






        fragView = findViewById(R.id.flContent);

//        navigationView.setNavigationItemSelectedListener(this);

        String name = buyucoinPref.getPrefString("name");
        String email = buyucoinPref.getPrefString("email");
//        navname.setText(name);
//        navemail.setText(email);




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
                Looper.prepare();
                new CoustomToast(Dashboard.this,Dashboard.this,"Error retreiving API",CoustomToast.TYPE_DANGER).showToast();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.d(TAG, "getNonFreshToken: "+s);


                try {
                    final JSONObject jsonObject1 = new JSONObject(s);
                    Log.d(TAG, "onResponse: "+jsonObject1.toString());
                    buyucoinPref.setEditpref(BuyucoinPref.ACCESS_TOKEN,jsonObject1.getString("access_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    new CoustomToast(Dashboard.this,Dashboard.this,e.getMessage(),CoustomToast.TYPE_NORMAL).showToast();


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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_account:
                DashboardViewpager.setCurrentItem(4);
                break;
            case R.id.nav_wallet:
                DashboardViewpager.setCurrentItem(0);
                break;
            case R.id.nav_rate:
                DashboardViewpager.setCurrentItem(1);
                break;
            case R.id.nav_p2p:
                DashboardViewpager.setCurrentItem(3);
                break;
            case R.id.nav_buysell:
                DashboardViewpager.setCurrentItem(2);
                break;

        }
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}