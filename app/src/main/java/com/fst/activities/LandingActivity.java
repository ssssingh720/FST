package com.fst.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fst.Network.Urls;
import com.fst.skytop.R;
import com.fst.Utility.Constants;
import com.fst.Utility.Prefs;
import com.fst.Utility.Utils;
import com.fst.adapters.ViewPagerAdapter;
import com.fst.modal.LoginVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sudhir Singh on 12,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    //    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private LoginVO loginDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        context = LandingActivity.this;
        viewPager = (ViewPager) findViewById(R.id.view_pager);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ImageView imgNavMenu = (ImageView) findViewById(R.id.imgNavMenu);
        TextView txtUserID = (TextView) findViewById(R.id.txtUserID);
        TextClock txtTime = (TextClock) findViewById(R.id.txtTime);

        Gson gson = new Gson();
        Prefs prefs = new Prefs();
        String json = prefs.getPreferences(context, "LOGIN_DATA");
          loginDetails = gson.fromJson(json, LoginVO.class);

          txtUserID.setText(loginDetails.userId);

        txtTime.setFormat12Hour(null);
        txtTime.setFormat24Hour("dd/MM/yyyy hh:mm:ss a");
//        txtTime.setFormat24Hour("EEE MMM d hh:mm:ss a");

//        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
//        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        toggle.setDrawerIndicatorEnabled(false);
//        toolbar.setNavigationIcon(R.drawable.wallet);
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getTheme());
//        toggle.setHomeAsUpIndicator(R.drawable.wallet);

        imgNavMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 4; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab, null, false);

        LinearLayout linearLayoutOne = (LinearLayout) headerView.findViewById(R.id.ll);
        LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.ll2);
        LinearLayout linearLayout3 = (LinearLayout) headerView.findViewById(R.id.ll3);
        LinearLayout linearLayout4 = (LinearLayout) headerView.findViewById(R.id.ll4);

        tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
        tabLayout.getTabAt(1).setCustomView(linearLayout2);
        tabLayout.getTabAt(2).setCustomView(linearLayout3);
        tabLayout.getTabAt(3).setCustomView(linearLayout4);

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), LandingActivity.this);
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

        if(savedInstanceState==null){
            viewPager.setCurrentItem(1);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.funskytop) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.play_screen) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.manage_points) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.live_draw) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.win_report) {
            viewPager.setCurrentItem(4);
        } else if (id == R.id.bid_report) {
            viewPager.setCurrentItem(5);
        }else if (id == R.id.accounts) {
            viewPager.setCurrentItem(6);
        }
        else if (id == R.id.settings) {
            viewPager.setCurrentItem(7);
        } else if(id==R.id.how_to_play){
            viewPager.setCurrentItem(8);
        } else if(id==R.id.advanced){
            viewPager.setCurrentItem(9);
        }else if(id==R.id.share_app){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "FunSkyTop -World's first game of its kind!");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.fst.skytop \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }

        else if (id == R.id.logout_menu) {

            showLogoutPopUp();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutPopUp() {
        final Dialog logoutPopUp = new Dialog(LandingActivity.this, R.style.DialogSlideAnim);
        logoutPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopUp.setContentView(R.layout.popup_logout);
        logoutPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutPopUp.getWindow().setGravity(Gravity.CENTER);
        logoutPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button logoutNo = (Button) logoutPopUp.findViewById(R.id.logoutNoBtn);
        Button logoutYes = (Button) logoutPopUp.findViewById(R.id.logoutYesBtn);

        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logoutPopUp.isShowing()) {
                    logoutPopUp.dismiss();
                    logoutPopUp.cancel();
                }
            }
        });
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout(logoutPopUp);
            }
        });
        logoutPopUp.show();
    }

    private void logout(final Dialog logoutPopUp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {
                                Prefs prefs = new Prefs();
                                prefs.setPreferences(context, "LOGIN_DATA", "");

                                Intent login = new Intent(context, LoginActivity.class);
                                context.startActivity(login);
                                if (logoutPopUp.isShowing()) {
                                    logoutPopUp.dismiss();
                                    logoutPopUp.cancel();
                                }
                                finish();
                            } else if (responseCode == 0) {
                                String message = jsonResult.optString("message");
                                Utils.showAlertDialog(context, message);
                            } else {
                                Utils.showAlertDialog(context, getString(R.string.please_try_again));
                            }
                        } catch (JSONException e) {
                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Failure", error.toString());
                        if (Utils.isInternetConnected(context)) {
                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        } else {
                            Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



//             user_id = sunil1
//	user_type = 6
//	flag = USER_LOGOUT
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11
                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.USER_TYPE, loginDetails.userType);
                params.put(Constants.FLAG, Constants.LOG_OUT_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        toggle.onConfigurationChanged(newConfig);
    }

}
