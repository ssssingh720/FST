package com.fst.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fst.activities.ChoiceActivity;
import com.fst.customViews.MyBounceInterpolator;
import com.fst.fragment.PlayScreenFragment;
import com.fst.modal.CategoryVO;
import com.fst.modal.ChoiceVO;
import com.fst.modal.EventsVO;
import com.fst.modal.LoginVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder> implements View.OnClickListener {

    PlayScreenFragment playScreenFragment;
    private Activity context;
    private List<EventsVO> eventsVOList;
    private List<CategoryVO> categoryList;
    private List<ChoiceVO> choiceVOList;
    private LoadingView mLoadingView;


    public BidAdapter(Activity context, List<CategoryVO> categoryList, List<EventsVO> eventsVOList, LoadingView mLoadingView,
                      ArrayList<ChoiceVO> choiceVOList, PlayScreenFragment playScreenFragment) {
        this.context = context;
        this.categoryList = categoryList;
        this.eventsVOList = eventsVOList;
        this.mLoadingView = mLoadingView;
        this.choiceVOList = choiceVOList;
        this.playScreenFragment = playScreenFragment;

    }

    @Override
    public BidAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bid_adapter, parent, false);

        return new BidAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(BidAdapter.ViewHolder holder, int position) {


        holder.txtBidName.setText(categoryList.get(position).getBid_name());

        if (categoryList.get(position).isCatSelected()) {
            final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            holder.lnrBid.startAnimation(myAnim);
            holder.imgBidSelect.setBackgroundResource(R.drawable.bid_btn_choosen);
        } else {
            holder.imgBidSelect.setBackgroundResource(R.drawable.bid_btn_unchoosen);
        }

        holder.lnrBid.setTag(position);
        holder.imgBidSelect.setTag(position);
        holder.txtBidName.setTag(position);

        holder.lnrBid.setOnClickListener(this);
        holder.imgBidSelect.setOnClickListener(this);
        holder.txtBidName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lnrBid:
            case R.id.imgBidSelect:
            case R.id.txtBidName:

                boolean isEventSelected = false;
                String eventName = "";
                for (int eventCounter = 0; eventCounter < eventsVOList.size(); eventCounter++) {
                    if (eventsVOList.get(eventCounter).isEventSelected()) {
                        isEventSelected = true;
                        eventName = eventsVOList.get(eventCounter).getEvent_name();
                        break;
                    }
                }

                if (isEventSelected) {
                    int positionCliked = (Integer) view.getTag();

                    for (int counter = 0; counter < categoryList.size(); counter++) {

                        categoryList.get(counter).setCatSelected(false);

                    }

                    categoryList.get(positionCliked).setCatSelected(true);
                    notifyDataSetChanged();

                    getChoice(categoryList.get(positionCliked).getBid_cat(), eventName, categoryList.get(positionCliked).getBid_name());

                } else {
                    Utils.showAlertDialog(context, "First select an event to proceed.");
                }
                break;
        }
    }

    private void getChoice(final String bidCategory, final String eventName, final String bidCatName) {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        choiceVOList.clear();
                        String result = response;
                        JSONArray jsonArray = null;
                        try {

                            JSONObject jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {
                                choiceVOList.clear();
                                jsonArray = jsonResult.optJSONArray("data");
                                for (int counter = 0; counter < jsonArray.length(); counter++) {

                                    JSONObject pointObj = jsonArray.getJSONObject(counter);

                                    String id = pointObj.optString("id");
                                    String bid_category = pointObj.optString("bid_category");
                                    int block_status = pointObj.optInt("block_status");
                                    String cat_name = pointObj.optString("cat_name");
                                    String cat_value = pointObj.optString("cat_value");
                                    String descp = pointObj.optString("descp");
                                    String event_name = pointObj.optString("event_name");
                                    int status = pointObj.optInt("status");

                                    ChoiceVO choiceVO = new ChoiceVO(id, bid_category, block_status, cat_name, cat_value,
                                            descp, event_name, status, false, bidCatName);
                                    choiceVOList.add(choiceVO);

                                }

                                Intent choiceIntent = new Intent(context, ChoiceActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLIST", (Serializable) choiceVOList);
                                choiceIntent.putExtra(Constants.CHOICE_INTENT_DATA, args);

                                if (playScreenFragment != null) {
                                    playScreenFragment.startActivityForResult(choiceIntent, Constants.CHOICE_INTENT_RESULT);
                                } else {
                                    context.startActivityForResult(choiceIntent, Constants.CHOICE_INTENT_RESULT);
                                }

                            }
                        } catch (JSONException e) {
                            Utils.showAlertDialog(context, context.getString(R.string.please_try_again));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Failure", error.toString());
                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();
                        if (Utils.isInternetConnected(context)) {
                            Utils.showAlertDialog(context, context.getString(R.string.please_try_again));
                        } else {
                            Utils.showAlertDialog(context, context.getString(R.string.no_internet_connection));
                        }
                    }
                }) {

//            user_id = sunil1
//                    bid_cat = group    // category name : TDR, group, T20, T10
//            event_name = h   // event name : h, m, j, o
//                    flag = GBIDCHOICEWTHBLOCKED
//            api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Gson gson = new Gson();
                Prefs prefs = new Prefs();
                String json = prefs.getPreferences(context, "LOGIN_DATA");
                LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.FLAG, Constants.GET_CHOICE_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);
                params.put(Constants.BID_CAT, bidCategory);
                params.put(Constants.EVENT_NAME, eventName);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
//        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //        public TextView txtJuryName;
//        public ImageView imgJury;
//        public TextView txtJuryDesig;
        public TextView txtBidName;
        public ImageView imgBidSelect;
        LinearLayout lnrBid;
//        //        public TextView txtJuryProf;
//        LinearLayout lnrSpeakerDetail;

        public ViewHolder(View view) {
            super(view);

            lnrBid = (LinearLayout) view.findViewById(R.id.lnrBid);
//
//            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
            txtBidName = (TextView) view.findViewById(R.id.txtBidName);
            imgBidSelect = (ImageView) view.findViewById(R.id.imgBidSelect);
////            txtJuryProf = (TextView) view.findViewById(R.id.txtJuryProf);
//            imgJury = (ImageView) view.findViewById(R.id.imgJury);
//            txtJuryDesig = (TextView) view.findViewById(R.id.txtJuryDesig);
//            txtJuryCompany = (TextView) view.findViewById(R.id.txtJuryCompany);
        }
    }
}
