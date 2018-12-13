package com.buyucoin.buyucoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.Fragments.AccountFragment;
import com.buyucoin.buyucoin.Fragments.BuySellFragment;
import com.buyucoin.buyucoin.Fragments.HistoryFragment;
import com.buyucoin.buyucoin.Fragments.P2PFragment;
import com.buyucoin.buyucoin.Fragments.RateFragment;
import com.buyucoin.buyucoin.Fragments.ReferralFragment;
import com.buyucoin.buyucoin.Fragments.WalletFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

public class Dashboard extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener,
    AccountFragment.OnFragmentInteractionListener,
    WalletFragment.OnListFragmentInteractionListener,
    RateFragment.OnListFragmentInteractionListener,
    HistoryFragment.OnListFragmentInteractionListener,
    ReferralFragment.OnFragmentInteractionListener,
    P2PFragment.OnFragmentInteractionListener {

        String ACCESS_TOKEN = null;
        Toolbar toolbar;
        TextView navname, navemail, tv;
        View fragView;
        BottomNavigationView bm;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bm = findViewById(R.id._btnbar);
            setSupportActionBar(toolbar);


            SharedPreferences prefs = getSharedPreferences("BUYUCOIN_USER_PREFS", MODE_PRIVATE);
            ACCESS_TOKEN = prefs.getString("access_token", null);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            });

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
            tv = (TextView) findViewById(R.id.tvInternet);
            fragView = findViewById(R.id.flContent);

            if (Utilities.isOnline(getApplicationContext())) {
                loadProfile();
            } else {
                fragView.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }

            bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    Class fragmentClass = null;
                    int id = item.getItemId();

                    switch (item.getItemId()) {
                        case R.id.acc_det:
                            toolbar.setTitle("Account");
                            setTitle(item.getTitle());
                            item.setChecked(true);
                            if (Utilities.isOnline(getApplicationContext())) {} else {
                                fragView.setVisibility(View.GONE);
                                tv.setVisibility(View.VISIBLE);
                            }
                            fragmentClass = AccountFragment.class;
                            break;
                        case R.id.wll_bal:
                            toolbar.setTitle("Wallet");
                            fragmentClass = WalletFragment.class;
                            break;
                        case R.id._buysell:
                            toolbar.setTitle("Buy/Sell");
                            fragmentClass = BuySellFragment.class;
                            break;
                        case R.id._rates:
                            toolbar.setTitle("Rates");
                            fragmentClass = RateFragment.class;
                            break;
                        case R.id._p2p:
                            toolbar.setTitle("Create Deposit/Withdrawl");
                            fragmentClass = P2PFragment.class;
                            break;
                        default:
                            fragmentClass = AccountFragment.class;


                    }
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (Utilities.isOnline(getApplicationContext())) {
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        tv.setVisibility(View.GONE);
                        fragView.setVisibility(View.VISIBLE);
                    } else {
                        fragView.setVisibility(View.GONE);
                        tv.setVisibility(View.VISIBLE);
                    }
                    // Set action bar title
                    setTitle(item.getTitle());
                    item.setChecked(true);
                    // Close the navigation drawer
                    return true;
                }
            });

        }

    public void BuySellFragmentFun(View view) {
        Fragment fragment  = new BuySellFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment);
        changeTab(R.id._buysell);

    }

        public void changeTab(int i){
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
                    tv.setVisibility(View.VISIBLE);
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
                tv.setVisibility(View.GONE);
                fragView.setVisibility(View.VISIBLE);
            } else {
                fragView.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
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
                    e.printStackTrace();
                    finish();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    //      Log.d("RESPONSE_____", s);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                final JSONObject data = jsonObject1.getJSONObject(("data"));



                                navname.setText(data.getString("name"));
                                navemail.setText(data.getString("email"));

                                Fragment fragment = null;
                                fragment = new WalletFragment();


                                toolbar.setTitle("Wallet");
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.flContent, fragment);
                                fragmentTransaction.commit();


                            } catch (Exception e) {
                                e.printStackTrace();
                                showToast("Error loading Wallet");
                                // finish();
                            }
                        }
                    });
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
    }