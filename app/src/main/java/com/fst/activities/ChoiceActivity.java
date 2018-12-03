package com.fst.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import com.fst.Utility.Constants;
import com.fst.adapters.ChoiceAdapter;
import com.fst.modal.ChoiceVO;
import com.fst.skytop.R;

import java.util.ArrayList;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ChoiceActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choice_activity);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFinishOnTouchOutside(false);

        context = ChoiceActivity.this;

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra(Constants.CHOICE_INTENT_DATA);

        ArrayList<ChoiceVO> choiceVOList = (ArrayList<ChoiceVO>) args.getSerializable("ARRAYLIST");

        // Create the recyclerview.
        TextView txtBidCateName=(TextView)findViewById(R.id.txtBidCateName);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclChoice);
        // Create the grid layout manager with 2 columns.

        txtBidCateName.setText(choiceVOList.get(0).getBidCatName());

        GridLayoutManager layoutManager=  new GridLayoutManager(this, 4);

        String bidCat=choiceVOList.get(0).getBid_category();
        if(bidCat.equalsIgnoreCase("T10")||bidCat.equalsIgnoreCase("T20")||
                bidCat.equalsIgnoreCase("group")) {
             layoutManager = new GridLayoutManager(this, 1);
        }else{
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
             layoutManager = new GridLayoutManager(this, 4);
        }
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set layout manager.
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter chapterVideoAdapter = new ChoiceAdapter(ChoiceActivity.this, choiceVOList);
        recyclerView.setAdapter(chapterVideoAdapter);

    }
}
