package com.buyucoin.buyucoin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class Dashboard extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AccountFragment.OnFragmentInteractionListener,
        WalletFragment.OnListFragmentInteractionListener,
        RateFragment.OnListFragmentInteractionListener,
        HistoryFragment.OnListFragmentInteractionListener,
        ReferralFragment.OnFragmentInteractionListener,
        P2PFragment.OnFragmentInteractionListener {

    String ACCESS_TOKEN = null, refresh_token = null;
    static final String FRAGMENT_STATE = "FRAGMENT_STATE";
    static Toolbar toolbar;
    TextView navname, navemail;
    View fragView;
    static BottomNavigationView bm;
    AlertDialog.Builder ad;
    static FragmentManager fragmentManager;
    LinearLayout noInternet, serverError, bottom_sheet_layout;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String FRAGENT_TYPE;
    ViewPager DashboardViewpager;

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


        prefs = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE);

        editor = this.getSharedPreferences("BUYUCOIN_USER_PREFS", Context.MODE_PRIVATE).edit();

        ACCESS_TOKEN = prefs.getString("access_token", null);
        FRAGENT_TYPE = prefs.getString(FRAGMENT_STATE, "WALLET");


        refresh_token = prefs.getString("refresh_token", null);

        ad = new AlertDialog.Builder(this);

        fragmentManager = getSupportFragmentManager();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        navname = (TextView) header.findViewById(R.id.tvName);
        navemail = (TextView) header.findViewById(R.id.tvEmail);

        fragView = findViewById(R.id.flContent);


        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.acc_det:
                        toolbar.setTitle("Account");
                        DashboardViewpager.setCurrentItem(4);

                        break;
                    case R.id.wll_bal:
                        toolbar.setTitle("Wallet");
                        DashboardViewpager.setCurrentItem(0);

                        break;
                    case R.id._rates:
                        toolbar.setTitle("Rates");
                        DashboardViewpager.setCurrentItem(1);

                        break;
                    case R.id._buysell:
                        toolbar.setTitle("Buy\\Sell");
                        DashboardViewpager.setCurrentItem(2);

                        break;
                    case R.id._p2p:
                        toolbar.setTitle("Create Deposit/Withdrawl");
                        DashboardViewpager.setCurrentItem(3);

                        break;
                    default:
                        DashboardViewpager.setCurrentItem(0);


                }
                return true;
            }


        });

    }

    public void updateFrammentState(String STATE) {
        editor.putString(FRAGMENT_STATE, STATE).apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Utilities.isOnline(getApplicationContext())) {
            getNonFreshToken(refresh_token);
//            loadProfile();
        } else {
            fragView.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        OkHttpHandler.cancelAllRequests();
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
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass;
        int id = item.getItemId();


        if (id == R.id.nav_account) {
            // Handle the camera action
            toolbar.setTitle("Account");
            setTitle(item.getTitle());
            item.setChecked(true);
            if (Utilities.isOnline(getApplicationContext())) {
                loadProfile();
            } else {
                fragView.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_wallet) {
            toolbar.setTitle("Wallet");
            fragmentClass = WalletFragment.class;
        } else if (id == R.id.nav_rate) {
            toolbar.setTitle("Rates");
            fragmentClass = RateFragment.class;
        } else if (id == R.id.nav_p2p) {
            toolbar.setTitle("Create Deposit/Withdrawl");
            fragmentClass = P2PFragment.class;
        } else if (id == R.id.nav_history) {
            toolbar.setTitle("History");
            fragmentClass = HistoryFragment.class;
        } else if (id == R.id.nav_referrals) {
            toolbar.setTitle("Referrals");
            fragmentClass = ReferralFragment.class;
        } else
            fragmentClass = AccountFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (Utilities.isOnline(getApplicationContext())) {
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            noInternet.setVisibility(View.GONE);
            fragView.setVisibility(View.VISIBLE);
        } else {
            fragView.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }
        // Set action bar title
        setTitle(item.getTitle());
        item.setChecked(true);
        // Close the navigation drawer


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadProfile() {
        OkHttpHandler.auth_get("account", ACCESS_TOKEN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("Error retrieving profile.");

                Fragment fragment = null;
                fragment = new WalletFragment();
                final Fragment finalFragment = fragment;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle("Wallet");
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContent, finalFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                });

                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("RESPONSE_____", s);

                try {
                    JSONObject jsonObject1 = new JSONObject(s);
                    if (jsonObject1.getString("status").equals("redirect")) {
                        Utilities.showToast(Dashboard.this, jsonObject1.getJSONArray("message").getJSONArray(0).getString(0));
                        if (jsonObject1.getString("sub_status").equals("6")) {
                            Log.d("STATUS", "mobile_number_required");
                            Utilities.addMobile(Dashboard.this, ACCESS_TOKEN, ad);
                        } else
                            Utilities.getOTP(Dashboard.this, ACCESS_TOKEN, ad);
                        return;
                    }
                    if (jsonObject1.getString("status").equals("error")) {
                        Utilities.showToast(Dashboard.this, jsonObject1.getString("msg"));
                        Utilities.clearPrefs(Dashboard.this);
                        startActivity(new Intent(Dashboard.this, LoginActivity.class));
                        finish();
                        return;
                    }
                    final JSONObject data = jsonObject1.getJSONObject(("data"));
                    final String email = data.getString("email");
                    final String name = data.getString("name");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navname.setText(name);
                            navemail.setText(email);

                            Fragment fragment = null;
                            switch (FRAGENT_TYPE) {
                                case "ACCOUNT":
                                    fragment = new AccountFragment();
                                    toolbar.setTitle("Account");
                                    changeTab(R.id.acc_det);

                                    break;
                                case "BUYSELL":
                                    fragment = new BuySellFragment();
                                    toolbar.setTitle("Buy Sell");
                                    changeTab(R.id._buysell);

                                    break;
                                case "P2P":
                                    fragment = new P2PFragment();
                                    toolbar.setTitle("P2P");
                                    changeTab(R.id._p2p);

                                    break;
                                case "WALLET":
                                    fragment = new WalletFragment();
                                    toolbar.setTitle("Wallet");
                                    changeTab(R.id.wll_bal);

                                    break;
                                case "RATES":
                                    fragment = new RateFragment();
                                    toolbar.setTitle("Rates");
                                    changeTab(R.id._rates);

                                    break;
                                default:
                                    fragment = new WalletFragment();
                                    toolbar.setTitle("Wallet");
                                    changeTab(R.id.wll_bal);


                            }

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment);
                            fragmentTransaction.commitAllowingStateLoss();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Error loading Wallet");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragment = null;
                            fragment = new WalletFragment();

                            toolbar.setTitle("Wallet");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    });
                    // finish();
                }

            }
        });
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
                Utilities.showToast(Dashboard.this, "Error retreiving API");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
//                Log.d("/refresh RESPONSE", s);
                try {
                    JSONObject jsonObject1 = new JSONObject(s);
                    SharedPreferences.Editor editor = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE).edit();
                    editor.putString("access_token", jsonObject1.getJSONObject("data").getString("access_token"));
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showToast(Dashboard.this, Utilities.getErrorMessage(s));
                }
            }
        });
    }

    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void reload() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onListFragmentInteraction(JSONObject item) {
        Log.d("Click", item.toString());

        try {
            if (item.getString("ask") != null) {
                Intent i = new Intent(this, CurrencyActivity.class);
                i.putExtras(Utilities.toBundle(item));
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(JSONObject item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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


}