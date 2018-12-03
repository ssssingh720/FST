package com.fst.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fst.fragment.BidReportFragment;
import com.fst.fragment.FilterBidReportFragment;
import com.fst.fragment.TodayBidReportFragment;

/**
 * Created by Sudhir Singh on 29,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class BidTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public BidTabsAdapter(FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodayBidReportFragment home = new TodayBidReportFragment();
                return home;
            case 1:
                FilterBidReportFragment about = new FilterBidReportFragment();
                return about;

            default:
                return null;
        }
    }
}
