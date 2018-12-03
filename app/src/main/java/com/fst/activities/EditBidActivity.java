package com.fst.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fst.Network.Urls;
import com.fst.Utility.Constants;
import com.fst.Utility.Prefs;
import com.fst.Utility.Utils;
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
import com.fst.skytop.R;
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

import static com.fst.Utility.Constants.MY_BID_DETAIL;

/**
 * Created by Sudhir Singh on 28,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class EditBidActivity extends Activity implements View.OnClickListener {

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

    private MyBidsVO myBidsVO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_activity);

        context = EditBidActivity.this;

        myBidsVO = (MyBidsVO) getIntent().getExtras().get(MY_BID_DETAIL);

        rclEvents = (RecyclerView) findViewById(R.id.rclEvents);
        lnrChoice = (LinearLayout) findViewById(R.id.lnrChoice);
        rctBid = (RecyclerView) findViewById(R.id.rctBid);
        rclPoints = (RecyclerView) findViewById(R.id.rclPoints);
        txtChoice = (TextView) findViewById(R.id.txtChoice);

        imgEventLeftScroll = (ImageView) findViewById(R.id.imgEventLeftScroll);
        imgEventRightScroll = (ImageView) findViewById(R.id.imgEventRightScroll);
        imgBidLeftScroll = (ImageView) findViewById(R.id.imgBidLeftScroll);
        imgBidRightScroll = (ImageView) findViewById(R.id.imgBidRightScroll);

        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        TextView txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        TextView txtDeleteBid = (TextView) findViewById(R.id.txtDeleteBid);
        TextView txtBidId = (TextView) findViewById(R.id.txtBidId);

        txtPlayPoints = (TextView) findViewById(R.id.txtPlayPoints);
        txtReservePoints = (TextView) findViewById(R.id.txtReservePoints);

        imgOverlay = (ImageView) findViewById(R.id.imgOverlay);

        //for bid report
        tblBidReport = (TableLayout) findViewById(R.id.tblBidReport);
        tblHeader = (LinearLayout) findViewById(R.id.tblHeader);

        imgEventLeftScroll.setOnClickListener(this);
        imgEventRightScroll.setOnClickListener(this);

        imgEventLeftScroll.setVisibility(View.GONE);
        imgEventRightScroll.setVisibility(View.GONE);

        imgBidLeftScroll.setOnClickListener(this);
        imgBidRightScroll.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);
        txtDeleteBid.setOnClickListener(this);

        initiateLoadingView();

        mLayoutManager = new LinearLayoutManager(context);
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rclEvents.setLayoutManager(mLayoutManager);

        rctBidLayoutManager = new LinearLayoutManager(context);
        ((LinearLayoutManager) rctBidLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rctBid.setLayoutManager(rctBidLayoutManager);

        RecyclerView.LayoutManager rclPintsLayoutManager = new LinearLayoutManager(context);
        ((LinearLayoutManager) rclPintsLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rclPoints.setLayoutManager(rclPintsLayoutManager);

        getEvents();
        getMyPoints();

        txtBidId.setText("(Bid no. "+myBidsVO.bidId+")");

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

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imgBack:
                onBackPressed();
                break;


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

            case R.id.txtDeleteBid:

                if (Utils.isInternetConnected(context)) {

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(" ")
                            .setCancelText("No")
                            .setContentText("Delete the bid?")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    deleteBid();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();


                } else {
                    Utils.showAlertDialog(context, getResources().getString(R.string.no_internet_connection));
                }

                break;

        }
    }

    private void initiateLoadingView() {

//        mediaPlayer = MediaPlayer.create(context, R.raw.btn_click);

        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
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

        imgOverlay.setVisibility(View.GONE);
        rclPoints.setVisibility(View.GONE);
        lnrChoice.setVisibility(View.GONE);

        txtChoice.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

//                        int eventSelectedPoition = 0;
                        int catSelectedPosition = 0;
                        int pointSelectedPosition = 0;

                        String result = response.toString();
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("event_status");

                            if (responseCode == 1) {
//                                String selectedPoint = "";
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

                                        boolean isEventSelected = false;
                                        if (myBidsVO.eventName.equalsIgnoreCase(event_name)) {
                                            isEventSelected = true;
                                        }

                                        JSONArray pointsArray = eventDetailObj.getJSONArray("points");
                                        ArrayList<PointsVO> eventPointsList = new ArrayList<>();
                                        for (int counter = 0; counter < pointsArray.length(); counter++) {

                                            String point = pointsArray.getJSONObject(counter).getString("point");
                                            int status = pointsArray.getJSONObject(counter).getInt("status");
                                            boolean isPointSelected = false;
                                            if (isEventSelected && point.equalsIgnoreCase(myBidsVO.pointsBid)) {
                                                isPointSelected = true;

                                                pointSelectedPosition = counter;
                                            }

                                            PointsVO pointsVO = new PointsVO(point, status, isPointSelected);
                                            eventPointsList.add(pointsVO);

                                        }


                                        if (isEventSelected) {

                                            EventsVO eventsVO = new EventsVO(id, event_name, bid_init_tm, event_time,
                                                    myeventtime, series_id, event_id, eventPointsList, isEventSelected);
                                            eventsList.add(eventsVO);

                                            rclPoints.setVisibility(View.VISIBLE);
//                                            eventSelectedPoition = (eventsList.size() - 1);
//                                            pointsList = eventsList.get((eventsList.size() - 1)).getPointsList();
                                            pointsList = eventPointsList;
                                            pointAdapter = new PointAdapter(EditBidActivity.this, pointsList);
                                            rclPoints.setAdapter(pointAdapter);
                                            rclPoints.smoothScrollToPosition(pointSelectedPosition);
                                        }

                                    } catch (JSONException e) {
                                        // Something went wrong!
                                    }
                                }

                                RecyclerView.Adapter chapterVideoAdapter = new EventAdapter(context, eventsList,
                                        imgOverlay, null, EditBidActivity.this);
                                rclEvents.setAdapter(chapterVideoAdapter);

//                                rclEvents.smoothScrollToPosition(eventSelectedPoition);


                                JSONArray categoryArray = jsonObject.getJSONArray("category");

                                for (int counter = 0; counter < categoryArray.length(); counter++) {

                                    String id = categoryArray.getJSONObject(counter).getString("id");
                                    String bid_cat = categoryArray.getJSONObject(counter).getString("bid_cat");
                                    String bid_name = categoryArray.getJSONObject(counter).getString("bid_name");
                                    int status = categoryArray.getJSONObject(counter).getInt("status");

                                    boolean isCatSelected = false;
                                    if (myBidsVO.bidCat.equalsIgnoreCase(bid_cat)) {

                                        ChoiceVO choiceVO = new ChoiceVO(myBidsVO.id, myBidsVO.bidCat, 1, myBidsVO.cat_name,
                                                myBidsVO.lucyNum,
                                                myBidsVO.lucyNum, myBidsVO.eventName, 1, true, myBidsVO.bidName);
                                        choiceVOList.add(choiceVO);

                                        lnrChoice.setVisibility(View.VISIBLE);
                                        isCatSelected = true;
                                        txtChoice.setText(bid_name + " (" + myBidsVO.cat_name + ")");
                                        catSelectedPosition = counter;
                                    }

                                    CategoryVO pointsVO = new CategoryVO(id, bid_cat, bid_name, status, isCatSelected);
                                    categoryList.add(pointsVO);

                                }
                                bidAdapter = new BidAdapter(EditBidActivity.this,
                                        categoryList, eventsList, mLoadingView, choiceVOList, null);
                                rctBid.setAdapter(bidAdapter);

                                rctBid.smoothScrollToPosition(catSelectedPosition);

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

        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.startAnimation();

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
                                String message = jsonResult.optString("message");
//                                Utils.showAlertDialog(context, message);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                setResult(RESULT_OK);
                                finish();

                            } else if (responseCode == 0) {
                                String message = jsonResult.optString("message");
                                Utils.showAlertDialog(context, message);
//                                getEvents();
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


//           ip_address = 127.0.0.1
//	mac_address = 50-2B-73-C0-B3-42
//	user_agent = Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36
//	user_id = sunil1
//	bid_id = d2a319
//	bid_init_tm = 2018-10-23 16:01:15
//	series_id = GMT20181023
//	mypoints = 10
//	category_name = DDLR
//	event = lICE
//	choice = 2
//	bidcomments = System
//	flag = BIDUPD
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.IP_ADDRESS, "45.249.84.255");
                params.put(Constants.MAC_ADDRESS, "mac");
                params.put(Constants.USER_AGENT, "android");
                params.put(Constants.BID_ID, myBidsVO.bidId);
                params.put(Constants.BID_INIT_TIME, formattedDate);
                params.put(Constants.SERIES_ID, myBidsVO.seriesId);
                params.put(Constants.MYPOINTS, bidSubmitVO.getPointsVO().getPoint());
                params.put(Constants.CATEGORY_NAME, bidSubmitVO.getChoiceVO().getBid_category());
                params.put(Constants.EVENT, bidSubmitVO.getEventsVO().getEvent_name() + bidSubmitVO.getEventsVO().getEvent_time());
                params.put(Constants.CHOICE, bidSubmitVO.getChoiceVO().getCat_value());
                params.put(Constants.BID_COMMENTS, "test");
                params.put(Constants.FLAG, Constants.BID_UPDATE_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);
                params.put(Constants.USER_ID, loginDetails.userId);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void deleteBid() {

//        mLoadingView.setVisibility(View.VISIBLE);
//        mLoadingView.startAnimation();

        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Deleting Bid");
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4218"));
        pDialog.setCancelable(false);
        pDialog.show();

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
                                String message = jsonResult.optString("message");
//                                Utils.showAlertDialog(context, message);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                pDialog.setTitleText(" ").setContentText(message)
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                setResult(RESULT_OK);
                                finish();

                            } else if (responseCode == 0) {
                                String message = jsonResult.optString("message");
                                pDialog.setTitleText("Oops...").setContentText(message)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//                                getEvents();
                            } else {
                                pDialog.setTitleText("Oops...").setContentText(getString(R.string.please_try_again))
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        } catch (JSONException e) {
                            pDialog.setTitleText("Oops...").setContentText(getString(R.string.please_try_again))
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
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

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());


//  ip_address = 127.0.0.1
//	mac_address = 50-2B-73-C0-B3-42
//	user_agent = Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36
//	user_id = sunil1
//	bid_id = e35a39
//	us_time_diff = 5.5
//	bidcomments = Delete Bid
//	event_name = SONG
//	flag = BIDDEL
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.IP_ADDRESS, "45.249.84.255");
                params.put(Constants.MAC_ADDRESS, "mac");
                params.put(Constants.USER_AGENT, "android");
                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.BID_ID, myBidsVO.bidId);
                params.put(Constants.US_TIME_DIFF, loginDetails.usTimeDiff);
                params.put(Constants.BID_COMMENTS, "Delete Bid");
                params.put(Constants.EVENT_NAME, myBidsVO.eventName);
                params.put(Constants.FLAG, Constants.BID_DELETE_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

//                params.put(Constants.MYPOINTS, bidSubmitVO.getPointsVO().getPoint());
//                params.put(Constants.CATEGORY_NAME, bidSubmitVO.getChoiceVO().getBid_category());
//                params.put(Constants.EVENT, bidSubmitVO.getEventsVO().getEvent_name() + bidSubmitVO.getEventsVO().getEvent_time());
//                params.put(Constants.CHOICE, bidSubmitVO.getChoiceVO().getCat_value());

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
//            pointsList.clear();
            for (int counter = 0; counter < eventsList.size(); counter++) {

                if (eventsList.get(counter).isEventSelected()) {
                    rclPoints.setVisibility(View.VISIBLE);
                    pointsList = eventsList.get(counter).getPointsList();
                    pointAdapter = new PointAdapter(context, pointsList);
                    rclPoints.setAdapter(pointAdapter);
                    break;
                }
            }
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

        btnSubmitBid.setText("Modify Bid");

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
    public void onBackPressed() {
        super.onBackPressed();

        EditBidActivity.this.finish();
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);

    }
}
