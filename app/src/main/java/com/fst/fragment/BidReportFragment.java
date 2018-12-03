package com.fst.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fst.skytop.R;
import com.fst.adapters.BidTabsAdapter;

/**
 * Created by Sudhir Singh on 29,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class BidReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.bid_rep_frag, container, false);

        TabLayout tabLayout = (TabLayout)mView. findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today's Bid"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Bid"));
//        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)mView.findViewById(R.id.view_pager);
        BidTabsAdapter tabsAdapter = new BidTabsAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        return mView;
    }
}
