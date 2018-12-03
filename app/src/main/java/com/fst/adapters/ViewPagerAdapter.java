package com.fst.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fst.Network.Urls;
import com.fst.Utility.Constants;
import com.fst.Utility.Prefs;
import com.fst.fragment.AccountFragment;
import com.fst.fragment.BidReportFragment;
import com.fst.fragment.ChangePasswordFragment;
import com.fst.fragment.OtherFragment;
import com.fst.fragment.PlayScreenFragment;
import com.fst.fragment.WebViewFragment;
import com.fst.fragment.WinReportFragment;
import com.fst.modal.LoginVO;
import com.google.gson.Gson;

/**
 * Created by Sudhir Singh on 13,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    private Activity context;

    public ViewPagerAdapter(FragmentManager fm, Activity context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        if (position == 0) {
            Bundle args = new Bundle();
            args.putString(Constants.WEB_VIEW_URL, Urls.ABOUT_US_URL);
            fragment = new WebViewFragment();
            fragment.setArguments(args);
        } else if (position == 1) {
            fragment = new PlayScreenFragment();
        } else if (position == 2) {

            Gson gson = new Gson();
            Prefs prefs = new Prefs();
            String json = prefs.getPreferences(context, "LOGIN_DATA");
            LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

            Bundle args = new Bundle();
            args.putString(Constants.WEB_VIEW_URL, Urls.MANAGE_POINTS_URL + loginDetails.userId);
            fragment = new WebViewFragment();
            fragment.setArguments(args);

        } else if (position == 3) {

            Bundle args = new Bundle();
            args.putString(Constants.WEB_VIEW_URL, Urls.LIVE_DRAW_URL);
            fragment = new WebViewFragment();
            fragment.setArguments(args);

        } else if (position == 4) {
            fragment = new WinReportFragment();
        } else if (position == 5) {
            fragment = new BidReportFragment();
        } else if (position == 6) {
            fragment = new AccountFragment();
        } else if (position == 7) {
            fragment = new ChangePasswordFragment();
        } else if (position == 8) {
            Bundle args = new Bundle();
            args.putString(Constants.WEB_VIEW_URL, Urls.HOW_TO_PLAY);
            fragment = new WebViewFragment();
            fragment.setArguments(args);
        } else if (position == 9) {

            Gson gson = new Gson();
            Prefs prefs = new Prefs();
            String json = prefs.getPreferences(context, "LOGIN_DATA");
            LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

            Bundle args = new Bundle();
            args.putString(Constants.WEB_VIEW_URL, Urls.ADVANCED + loginDetails.userId);
            fragment = new WebViewFragment();
            fragment.setArguments(args);
        }  else {
            fragment = new OtherFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 11;
    }
}
