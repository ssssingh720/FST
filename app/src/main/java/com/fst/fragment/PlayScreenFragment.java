package com.fst.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import com.fst.activities.EditBidActivity;
import com.fst.adapters.BidAdapter;
import com.fst.adapters.EventAdapter;
import com.fst.adapters.PointAdapter;
import com.fst.modal.BidSubmitVO;
import com.fst.modal.CategoryVO;
import com.fst.modal.ChoiceVO;
import com.fst.modal.EventsVO;
import com.fst.modal.LoginVO;
import com.fst.modal.MyBidsVO;
import com.fst.modal.PointsVO;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

import static com.fst.Utility.Constants.EDIT_BID_REQUEST;
import static com.fst.Utility.Constants.MY_BID_DETAIL;

/**
 * Created by Sudhir Singh on 13,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class PlayScreenFragment extends Fragment implements View.OnClickListener {

    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager rctBidLayoutManager;
    private int mFirst = 0, mLast = 0;

    private Context context;
    private RecyclerView rclEvents;
    private RecyclerView rctBid;
    private RecyclerView rclPoints;
    private LinearLayout lnrChoice;
    private TextView txtChoice;
    private ImageView imgEventLeftScroll;
    private ImageView imgEventRightScroll;
    private ImageView imgBidLeftScroll;
    private ImageView imgBidRightScroll;
    private LoadingView mLoadingView;

    private TextView txtPlayPoints;
    private TextView txtReservePoints;

    private ArrayList<EventsVO> eventsList;
    private ArrayList<PointsVO> pointsList;
    private ArrayList<ChoiceVO> choiceVOList;
    private ArrayList<CategoryVO> categoryList;

    private RecyclerView.Adapter bidAdapter;
    private RecyclerView.Adapter pointAdapter;

    private ImageView imgOverlay;

    //for bid report
    private TableLayout tblBidReport;
    private LinearLayout tblHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.play_screen, container, false);

        context = getActivity();

        rclEvents = (RecyclerView) mView.findViewById(R.id.rclEvents);
        lnrChoice = (LinearLayout) mView.findViewById(R.id.lnrChoice);
        rctBid = (RecyclerView) mView.findViewById(R.id.rctBid);
        rclPoints = (RecyclerView) mView.findViewById(R.id.rclPoints);
        txtChoice = (TextView) mView.findViewById(R.id.txtChoice);

        imgEventLeftScroll = (ImageView) mView.findViewById(R.id.imgEventLeftScroll);
        imgEventRightScroll = (ImageView) mView.findViewById(R.id.imgEventRightScroll);
        imgBidLeftScroll = (ImageView) mView.findViewById(R.id.imgBidLeftScroll);
        imgBidRightScroll = (ImageView) mView.findViewById(R.id.imgBidRightScroll);
        TextView txtSubmit = (TextView) mView.findViewById(R.id.txtSubmit);

        txtPlayPoints = (TextView) mView.findViewById(R.id.txtPlayPoints);
        txtReservePoints = (TextView) mView.findViewById(R.id.txtReservePoints);

        imgOverlay = (ImageView) mView.findViewById(R.id.imgOverlay);

        //for bid report
        tblBidReport = (TableLayout) mView.findViewById(R.id.tblBidReport);
        tblHeader = (LinearLayout) mView.findViewById(R.id.tblHeader);

        imgEventLeftScroll.setOnClickListener(this);
        imgEventRightScroll.setOnClickListener(this);
        imgBidLeftScroll.setOnClickListener(this);
        imgBidRightScroll.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

        initiateLoadingView(mView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rclEvents.setLayoutManager(mLayoutManager);

        rctBidLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) rctBidLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rctBid.setLayoutManager(rctBidLayoutManager);

        RecyclerView.LayoutManager rclPintsLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) rclPintsLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rclPoints.setLayoutManager(rclPintsLayoutManager);

        getEvents();
        getMyBids();
        getMyPoints();

        rclEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) rclEvents.getLayoutManager();
                mLast = llm.findLastCompletelyVisibleItemPosition();
                mFirst = llm.findFirstCompletelyVisibleItemPosition();
            }
        });

        return mView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imgEventLeftScroll:
                try {
//                LinearLayoutManager llm = (LinearLayoutManager) rclEvents.getLayoutManager();
                    rclEvents.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition() - 1);
//                llm.scrollToPositionWithOffset(mFirst - 1, eventsList.size());
                } catch (Exception e) {

                }
                break;

            case R.id.imgEventRightScroll:
                try {
//                LinearLayoutManager lilm = (LinearLayoutManager) rclEvents.getLayoutManager();
                    rclEvents.smoothScrollToPosition(mLayoutManager.findLastVisibleItemPosition() + 1);
//                lilm.scrollToPositionWithOffset(mLast + 1,eventsList.size());
                } catch (Exception e) {

                }
                break;

            case R.id.imgBidLeftScroll:
                try {
//                LinearLayoutManager llm = (LinearLayoutManager) rclEvents.getLayoutManager();
                    rctBid.smoothScrollToPosition(rctBidLayoutManager.findFirstVisibleItemPosition() - 1);
//                llm.scrollToPositionWithOffset(mFirst - 1, eventsList.size());
                } catch (Exception e) {

                }
                break;

            case R.id.imgBidRightScroll:
                try {
//                LinearLayoutManager lilm = (LinearLayoutManager) rclEvents.getLayoutManager();
                    rctBid.smoothScrollToPosition(rctBidLayoutManager.findLastVisibleItemPosition() + 1);
//                lilm.scrollToPositionWithOffset(mLast + 1,eventsList.size());
                } catch (Exception e) {

                }
                break;

            case R.id.txtSubmit:

                if (Utils.isInternetConnected(context)) {
                    checkAndSubmitBid();
                } else {
                    Utils.showAlertDialog(context, getResources().getString(R.string.no_internet_connection));
                }

                break;

        }
    }

    private void initiateLoadingView(View mView) {

//        mediaPlayer = MediaPlayer.create(context, R.raw.btn_click);

        mLoadingView = (LoadingView) mView.findViewById(R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), R.drawable.fst_app_logo, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.drawable.fst_app_logo, LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.drawable.fst_app_logo, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.fst_app_logo, LoadingView.FROM_BOTTOM);
    }

    private void checkAndSubmitBid() {

        BidSubmitVO bidSubmitVO = new BidSubmitVO();

        boolean isValid = false;

        //event selection check
        if (eventsList != null && eventsList.size() > 0) {
            for (int eventCounter = 0; eventCounter < eventsList.size(); eventCounter++) {
                if (eventsList.get(eventCounter).isEventSelected()) {
                    bidSubmitVO.setEventsVO(eventsList.get(eventCounter));
                    isValid = true;
                    break;
                }
            }
        }

        if (isValid) {
            isValid = false;
            //bid category selection check
            if (categoryList != null && categoryList.size() > 0) {
                for (int categoryCounter = 0; categoryCounter < categoryList.size(); categoryCounter++) {
                    if (categoryList.get(categoryCounter).isCatSelected()) {
                        isValid = true;
                        bidSubmitVO.setCategoryVO(categoryList.get(categoryCounter));
                        break;
                    }
                }
            }
        } else {
            Utils.showAlertDialog(context, "Please select an event to proceed.");
            return;
        }

        if (isValid) {
            isValid = false;
            //category points selection check
            if (choiceVOList != null && choiceVOList.size() > 0) {
                for (int choiceCounter = 0; choiceCounter < choiceVOList.size(); choiceCounter++) {
                    if (choiceVOList.get(choiceCounter).isSelected()) {
                        bidSubmitVO.setChoiceVO(choiceVOList.get(choiceCounter));
                        isValid = true;
                        break;
                    }
                }
            }
        } else {
            Utils.showAlertDialog(context, "Please select a category to proceed.");
            return;
        }

        if (isValid) {
            isValid = false;
            // points selection check
            if (pointsList != null && pointsList.size() > 0) {
                for (int pointCounter = 0; pointCounter < pointsList.size(); pointCounter++) {
                    if (pointsList.get(pointCounter).isPointSelected()) {
                        bidSubmitVO.setPointsVO(pointsList.get(pointCounter));
                        isValid = true;
                        break;
                    }
                }
            }
        } else {
            Utils.showAlertDialog(context, "Please select choice to proceed.");
            return;
        }

        if (isValid) {
            openConfirmationPopUP(bidSubmitVO);
//            submitBid(bidSubmitVO);
        } else {
            Utils.showAlertDialog(context, "Please select points to proceed.");

        }
    }

    private void getEvents() {

        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.startAnimation();

        eventsList = new ArrayList<>();
        choiceVOList = new ArrayList<>();
        categoryList = new ArrayList<>();
        pointsList = new ArrayList<>();

        imgOverlay.setVisibility(View.VISIBLE);
        rclPoints.setVisibility(View.GONE);
        lnrChoice.setVisibility(View.GONE);

        txtChoice.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("event_status");

                            if (responseCode == 1) {

                                JSONObject jsonObject = jsonResult.optJSONObject("data");
                                JSONObject eventObj = jsonObject.getJSONObject("event");
                                JSONObject eventsArray = eventObj.getJSONObject("Events");

                                Iterator<String> iter = eventsArray.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    try {
                                        JSONObject eventDetailObj = eventsArray.getJSONObject(key);

                                        String id = eventDetailObj.optString("id");
                                        String event_name = eventDetailObj.optString("event_name");
                                        String bid_init_tm = eventDetailObj.optString("bid_init_tm");
                                        String event_time = eventDetailObj.optString("event_time");
                                        String myeventtime = eventDetailObj.optString("myeventtime");
                                        String series_id = eventDetailObj.optString("series_id");
                                        String event_id = eventDetailObj.optString("event_id");

                                        JSONArray pointsArray = eventDetailObj.getJSONArray("points");
                                        ArrayList<PointsVO> pointsList = new ArrayList<>();
                                        for (int counter = 0; counter < pointsArray.length(); counter++) {

                                            String point = pointsArray.getJSONObject(counter).getString("point");
                                            int status = pointsArray.getJSONObject(counter).getInt("status");

                                            PointsVO pointsVO = new PointsVO(point, status, false);
                                            pointsList.add(pointsVO);

                                        }

                                        EventsVO eventsVO = new EventsVO(id, event_name, bid_init_tm, event_time,
                                                myeventtime, series_id, event_id, pointsList, false);
                                        eventsList.add(eventsVO);

                                    } catch (JSONException e) {
                                        // Something went wrong!
                                    }
                                }


                                RecyclerView.Adapter chapterVideoAdapter = new EventAdapter(getActivity(), eventsList,
                                        imgOverlay, PlayScreenFragment.this, null);
                                rclEvents.setAdapter(chapterVideoAdapter);


                                JSONArray categoryArray = jsonObject.getJSONArray("category");

                                for (int counter = 0; counter < categoryArray.length(); counter++) {

                                    String id = categoryArray.getJSONObject(counter).getString("id");
                                    String bid_cat = categoryArray.getJSONObject(counter).getString("bid_cat");
                                    String bid_name = categoryArray.getJSONObject(counter).getString("bid_name");
                                    int status = categoryArray.getJSONObject(counter).getInt("status");

                                    CategoryVO pointsVO = new CategoryVO(id, bid_cat, bid_name, status, false);
                                    categoryList.add(pointsVO);

                                }
                                bidAdapter = new BidAdapter(getActivity(),
                                        categoryList, eventsList, mLoadingView, choiceVOList, PlayScreenFragment.this);
                                rctBid.setAdapter(bidAdapter);

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
                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();
                        if (Utils.isInternetConnected(context)) {
                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        } else {
                            Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                        }
                    }
                }) {


//            user_id = sunil1
//                    flag = GETEVENT
//            api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Gson gson = new Gson();
                Prefs prefs = new Prefs();
                String json = prefs.getPreferences(context, "LOGIN_DATA");
                LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.FLAG, Constants.GETEVENT_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    public void refreshScreenData() {

        if (categoryList != null && categoryList.size() > 0) {
            for (int counter = 0; counter < categoryList.size(); counter++) {
                categoryList.get(counter).setCatSelected(false);
            }
            if (bidAdapter != null) {
                bidAdapter.notifyDataSetChanged();
            }
        }


        if (choiceVOList != null && choiceVOList.size() > 0) {
            for (int choiceCounter = 0; choiceCounter < categoryList.size(); choiceCounter++) {
                choiceVOList.get(choiceCounter).setSelected(false);
            }

        }

        lnrChoice.setVisibility(View.GONE);

        if (pointsList != null && pointsList.size() > 0) {
            for (int pointCounter = 0; pointCounter < pointsList.size(); pointCounter++) {
                pointsList.get(pointCounter).setPointSelected(false);
            }
            if (pointAdapter != null) {
                pointAdapter.notifyDataSetChanged();
            }
        }


    }

    private void getMyBids() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {

                                tblHeader.setVisibility(View.VISIBLE);
                                tblBidReport.setVisibility(View.VISIBLE);
                                tblBidReport.removeAllViews();

                                JSONArray dataArray = jsonResult.optJSONArray("data");
                                for (int counter = 0; counter < dataArray.length(); counter++) {

                                    JSONObject jsonObject = dataArray.optJSONObject(counter);
                                    final MyBidsVO myBidsVO = new MyBidsVO();

                                    myBidsVO.id = jsonObject.optString("id");
                                    myBidsVO.bidCat = jsonObject.optString("bid_cat");
                                    myBidsVO.bidName = jsonObject.optString("bid_name");
                                    myBidsVO.lucyNum = jsonObject.optString("lucy_num");
                                    myBidsVO.cat_name = jsonObject.optString("cat_name");
                                    myBidsVO.pointsBid = jsonObject.optString("points_bid");
                                    myBidsVO.comments = jsonObject.optString("comments");
                                    myBidsVO.eventName = jsonObject.optString("event_name");
                                    myBidsVO.eventId = jsonObject.optString("event_id");
                                    myBidsVO.seriesId = jsonObject.optString("series_id");
                                    myBidsVO.eventDtTm = jsonObject.optString("event_dt_tm");
                                    myBidsVO.dfTime = jsonObject.optString("df_time");
                                    myBidsVO.userid = jsonObject.optString("userid");
                                    myBidsVO.status = jsonObject.optString("status");
                                    myBidsVO.bidId = jsonObject.optString("bid_id");
                                    myBidsVO.eventTime = jsonObject.optString("event_time");
                                    myBidsVO.perce = jsonObject.optString("perce");
                                    myBidsVO.winPoints = jsonObject.optString("win_points");
                                    myBidsVO.winNumber = jsonObject.optString("win_number");
                                    myBidsVO.wincategory = jsonObject.optString("wincategory");
                                    myBidsVO.drawNumber = jsonObject.optString("draw_number");
                                    myBidsVO.event_status = jsonObject.optString("event_status");

                                    LayoutInflater tblInflater = (LayoutInflater)
                                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                    // inflate add_view.xml to create new edit text views
                                    View newView = tblInflater.inflate(R.layout.bid_report_adapter, null);
                                    TextView txtBidDate = (TextView) newView.findViewById(R.id.txtBidDate);
                                    TextView txtBidId = (TextView) newView.findViewById(R.id.txtBidId);
                                    TextView txtBidEvents = (TextView) newView.findViewById(R.id.txtBidEvents);
//                                    TextView txtBidCategory = (TextView) newView.findViewById(R.id.txtBidCategory);
                                    TextView txtBidChoice = (TextView) newView.findViewById(R.id.txtBidChoice);
                                    TextView txtBidPoints = (TextView) newView.findViewById(R.id.txtBidPoints);
//                                    ImageView imgEditBid = (ImageView) newView.findViewById(R.id.imgEditBid);

                                    txtBidId.setTag(myBidsVO);
                                    if (myBidsVO.event_status.equalsIgnoreCase("0")) {
                                        txtBidId.setTextColor(getResources().getColor(R.color.white));
                                    } else {
                                        txtBidId.setTextColor(getResources().getColor(R.color.active_points));
                                        txtBidId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                MyBidsVO positionClick = (MyBidsVO) view.getTag();
                                                Intent editIntent = new Intent(context, EditBidActivity.class);
                                                editIntent.putExtra(MY_BID_DETAIL, positionClick);
                                                startActivityForResult(editIntent, EDIT_BID_REQUEST);
                                                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                            }
                                        });
//                                        imgEditBid.setVisibility(View.VISIBLE);
                                    }

                                    txtBidDate.setText(myBidsVO.eventDtTm);
                                    txtBidId.setText(myBidsVO.bidId);
                                    txtBidEvents.setText(myBidsVO.eventTime);
//                                    txtBidCategory.setText(myBidsVO.bidCat);
                                    txtBidChoice.setText(myBidsVO.bidName + " (" + myBidsVO.cat_name + ")");
                                    txtBidPoints.setText(myBidsVO.pointsBid);

                                    tblBidReport.addView(newView, counter);

                                }


                            } else if (responseCode == 0) {
                                tblHeader.setVisibility(View.GONE);
                                tblBidReport.setVisibility(View.GONE);

//                                String message = jsonResult.optString("message");
//                                Utils.showAlertDialog(context, message);
                            } else {
//                                Utils.showAlertDialog(context, getString(R.string.please_try_again));
                            }
                        } catch (JSONException e) {
//                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
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
                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        } else {
                            Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Gson gson = new Gson();
                Prefs prefs = new Prefs();
                String json = prefs.getPreferences(context, "LOGIN_DATA");
                LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

//                user_id = sunil1
//                flag = GBDATA
//                api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.FLAG, Constants.MY_BID_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getMyPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

//                        mLoadingView.setVisibility(View.GONE);
//                        mLoadingView.pauseAnimation();

                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {
//"data":{"id":"7","user_id":"sunil1","wallet_fund":"500","reserve_fund":"250","sec_dep":"0"}

                                JSONObject jsonObject = jsonResult.getJSONObject("data");
                                int playing_point = jsonObject.optInt("wallet_fund");
                                int reserve_point = jsonObject.optInt("reserve_fund");

                                txtPlayPoints.setText("" + playing_point);
                                txtReservePoints.setText("" + reserve_point);


                            } else if (responseCode == 0) {
                                tblHeader.setVisibility(View.GONE);
                                tblBidReport.setVisibility(View.GONE);

//                                String message = jsonResult.optString("message");
//                                Utils.showAlertDialog(context, message);
                            } else {
//                                Utils.showAlertDialog(context, getString(R.string.please_try_again));
                            }
                        } catch (JSONException e) {
//                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Failure", error.toString());
//                        mLoadingView.setVisibility(View.GONE);
//                        mLoadingView.pauseAnimation();
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

                Gson gson = new Gson();
                Prefs prefs = new Prefs();
                String json = prefs.getPreferences(context, "LOGIN_DATA");
                LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

//                user_id = sunil1
//                flag = WR_POINT
//                api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11
                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.FLAG, Constants.MY_POINTS_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void submitBid(final BidSubmitVO bidSubmitVO) {

//        mLoadingView.setVisibility(View.VISIBLE);
//        mLoadingView.startAnimation();
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Submitting Bid");
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4218"));
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

//                        mLoadingView.setVisibility(View.GONE);
//                        mLoadingView.pauseAnimation();
//{"status":"1","message":"Your bid has been submitted  Successfully ",
// "data":[{"id":"337642","bid_id":"ff919b","userid":"test","bid_cat":"group","lucy_num":"3","points_bid":"10",
// "comments":"","event_name":"h","event_id":"h20181017","series_id":"GMT20181017","event_dt_tm":"2018-10-17 16:38:17",
// "df_time":null,"status":"1","event_time":"DIAMOND","perce":"0","ip_address":"45.249.84.255","mac_address":"mac",
// "user_agent":"android","other_details":"","draw_number":null}]}2840  2808
                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {
                                String message = jsonResult.optString("message");
//                                Utils.showAlertDialog(context, message);

                                pDialog.setTitleText(" ").setContentText(message)
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                getEvents();
                                getMyBids();
                                getMyPoints();

                            } else if (responseCode == 0) {
                                String message = jsonResult.optString("message");

                                pDialog.setTitleText("Oops...").setContentText(message)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            } else {
//                                Utils.showAlertDialog(context, getString(R.string.please_try_again));
                                pDialog.setTitleText("Oops...").setContentText(getString(R.string.please_try_again))
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        } catch (JSONException e) {
                            pDialog.setTitleText("Oops...").setContentText(getString(R.string.please_try_again))
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
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
                            pDialog.setTitleText("Oops...").setContentText(getString(R.string.please_try_again))
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        } else {
                            pDialog.setTitleText("Oops...").setContentText(getString(R.string.no_internet_connection))
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Gson gson = new Gson();
                Prefs prefs = new Prefs();
                String json = prefs.getPreferences(context, "LOGIN_DATA");
                LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());


//            mylucky_comment = System
//                    flag = BIDREG
//            api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.FLAG, Constants.BIDREG_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);
                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.BID_INIT_TIME, formattedDate);
                params.put(Constants.SERIES_ID, bidSubmitVO.getEventsVO().getSeries_id());
                params.put(Constants.MYPOINTS, bidSubmitVO.getPointsVO().getPoint());
                params.put(Constants.CATEGORY_NAME, bidSubmitVO.getChoiceVO().getBid_category());
                params.put(Constants.CHOICE, bidSubmitVO.getChoiceVO().getCat_value());
                params.put(Constants.EVENT, bidSubmitVO.getEventsVO().getEvent_name() + bidSubmitVO.getEventsVO().getEvent_time());
                params.put(Constants.BID_COMMENTS, "test");
                params.put(Constants.IP_ADDRESS, "45.249.84.255");
                params.put(Constants.MAC_ADDRESS, "mac");
                params.put(Constants.USER_AGENT, "android");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOICE_INTENT_RESULT && resultCode == Activity.RESULT_OK) {

            lnrChoice.setVisibility(View.VISIBLE);

            ChoiceVO choiceVO = (ChoiceVO) data.getExtras().get(Constants.CHOICE_INTENT_DATA);

            for (int counter = 0; counter < choiceVOList.size(); counter++) {
                if (choiceVOList.get(counter).getId().equals(choiceVO.getId())) {
                    choiceVOList.get(counter).setSelected(true);
                } else {
                    choiceVOList.get(counter).setSelected(false);
                }
            }

            txtChoice.setText(choiceVO.getBidCatName() + " (" + choiceVO.getCat_name() + ")");

            for (int counter = 0; counter < eventsList.size(); counter++) {

                if (eventsList.get(counter).isEventSelected()) {
                    rclPoints.setVisibility(View.VISIBLE);
                    pointsList = eventsList.get(counter).getPointsList();
                    pointAdapter = new PointAdapter(getActivity(), pointsList);
                    rclPoints.setAdapter(pointAdapter);
                    break;
                }
            }
        } else if (requestCode == Constants.EDIT_BID_REQUEST && resultCode == Activity.RESULT_OK) {

            getMyBids();
            getMyPoints();

        }
    }

    private void openConfirmationPopUP(final BidSubmitVO bidSubmitVO) {
        final Dialog emailDialog = new Dialog(context);
        emailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        emailDialog.setContentView(R.layout.confirmation_popup);
        emailDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        emailDialog.setCancelable(false);

        TextView txtEventSelected = (TextView) emailDialog.findViewById(R.id.txtEventSelected);
        TextView txtPopBidCat = (TextView) emailDialog.findViewById(R.id.txtPopBidCat);
        TextView txtPopChoice = (TextView) emailDialog.findViewById(R.id.txtPopChoice);
        TextView txtPopPoints = (TextView) emailDialog.findViewById(R.id.txtPopPoints);
        TextView btnSubmitBid = (TextView) emailDialog.findViewById(R.id.btnSubmitBid);
        TextView btnCancel = (TextView) emailDialog.findViewById(R.id.btnCancel);

        txtEventSelected.setText(bidSubmitVO.getEventsVO().getEvent_time());
        txtPopBidCat.setText(bidSubmitVO.getCategoryVO().getBid_name());
        txtPopChoice.setText(txtChoice.getText().toString());
        txtPopPoints.setText(bidSubmitVO.getPointsVO().getPoint());

        btnSubmitBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBid(bidSubmitVO);
                emailDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
            }
        });
        emailDialog.show();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && context != null) {
            getMyPoints();
        }
    }

}
