package com.fst.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.fst.adapters.BidReportAdapter;
import com.fst.modal.LoginVO;
import com.fst.modal.MyBidsVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 29,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class FilterBidReportFragment extends Fragment {

    private Context context;
    private LoadingView mLoadingView;

    private ArrayList<MyBidsVO> myBidsList;


    private RecyclerView rclBidReport;
    private TextView txtNoRecord;
    private LinearLayout lnrFilterBid;

    private EditText edtStartDate;
    private EditText edtEndDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.bid_report_fragment, container, false);

        context = getActivity();
        myBidsList = new ArrayList<>();

        rclBidReport = (RecyclerView) mView.findViewById(R.id.rclBidReport);
        txtNoRecord = (TextView) mView.findViewById(R.id.txtNoRecord);
        lnrFilterBid = (LinearLayout) mView.findViewById(R.id.lnrFilterBid);
        TextView txtWinPointHeader = (TextView) mView.findViewById(R.id.txtWinPointHeader);
        txtWinPointHeader.setVisibility(View.VISIBLE);

        lnrFilterBid.setVisibility(View.VISIBLE);

        LinearLayoutManager rctBidLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) rctBidLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rclBidReport.setLayoutManager(rctBidLayoutManager);

        edtStartDate = (EditText) mView.findViewById(R.id.edtBidStartDate);
        edtEndDate = (EditText) mView.findViewById(R.id.edtBidEndDate);


//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String formattedDate = dateFormat.format(cal.getTime());

        //        mcalendar = Calendar.getInstance();
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);

        edtStartDate.setText(formattedDate);
        edtEndDate.setText(formattedDate);

        // 2018-04-24
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };
                DatePickerDialog dpDialog = new DatePickerDialog(context, listener, year, month, day);
                dpDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dpDialog.show();
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };
                DatePickerDialog dpDialog = new DatePickerDialog(context, listener, year, month, day);
                dpDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dpDialog.show();
            }
        });

        initiateLoadingView(mView);
        getMyBids();

        return mView;
    }


    private void initiateLoadingView(View mView) {

//        mediaPlayer = MediaPlayer.create(context, R.raw.btn_click);

        mLoadingView = (LoadingView) mView.findViewById(R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), R.drawable.fst_app_logo, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.drawable.fst_app_logo, LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.drawable.fst_app_logo, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.fst_app_logo, LoadingView.FROM_BOTTOM);
    }

    private void getMyBids() {
        txtNoRecord.setVisibility(View.GONE);
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


                                JSONArray dataArray = jsonResult.optJSONArray("data");
                                for (int counter = 0; counter < dataArray.length(); counter++) {

                                    JSONObject jsonObject = dataArray.optJSONObject(counter);
                                    MyBidsVO myBidsVO = new MyBidsVO();

                                    myBidsVO.id = jsonObject.optString("id");
                                    myBidsVO.bidCat = jsonObject.optString("bid_cat");
                                    myBidsVO.bidName = jsonObject.optString("bid_name");
                                    myBidsVO.lucyNum = jsonObject.optString("lucy_num");
                                    myBidsVO.pointsBid = jsonObject.optString("points_bid");
                                    myBidsVO.comments = jsonObject.optString("comments");
                                    myBidsVO.eventName = jsonObject.optString("event_name");
                                    myBidsVO.eventId = jsonObject.optString("event_id");
                                    myBidsVO.seriesId = jsonObject.optString("series_id");
                                    myBidsVO.eventDtTm = jsonObject.optString("bid_date");
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


//                                    // inflate add_view.xml to create new edit text views
//                                    View newView = tblInflater.inflate(R.layout.bid_report_adapter, null);
//                                    TextView txtBidDate = (TextView) newView.findViewById(R.id.txtBidDate);
//                                    TextView txtBidId = (TextView) newView.findViewById(R.id.txtBidId);
//                                    TextView txtBidEvents = (TextView) newView.findViewById(R.id.txtBidEvents);
//                                    TextView txtBidCategory = (TextView) newView.findViewById(R.id.txtBidCategory);
//                                    TextView txtBidChoice = (TextView) newView.findViewById(R.id.txtBidChoice);
//                                    TextView txtBidPoints = (TextView) newView.findViewById(R.id.txtBidPoints);
//
//                                    txtBidDate.setText(myBidsVO.eventDtTm);
//                                    txtBidId.setText(myBidsVO.bidId);
//                                    txtBidEvents.setText(myBidsVO.eventTime);
//                                    txtBidCategory.setText(myBidsVO.bidCat);
//                                    txtBidChoice.setText(myBidsVO.lucyNum);
//                                    txtBidPoints.setText(myBidsVO.pointsBid);

                                    myBidsList.add(myBidsVO);

                                }

                                RecyclerView.Adapter chapterVideoAdapter = new BidReportAdapter(getActivity(), myBidsList, true);
                                rclBidReport.setAdapter(chapterVideoAdapter);


                            } else if (responseCode == 0) {
                                txtNoRecord.setVisibility(View.VISIBLE);
                                String message = jsonResult.optString("message");
                                txtNoRecord.setText(message);
//                                Utils.showAlertDialog(context, message);
                            } else {
                                txtNoRecord.setVisibility(View.VISIBLE);
                                txtNoRecord.setText(getString(R.string.please_try_again));
//                                Utils.showAlertDialog(context, getString(R.string.please_try_again));
                            }
                        } catch (JSONException e) {
                            txtNoRecord.setVisibility(View.VISIBLE);
                            txtNoRecord.setText(getString(R.string.please_try_again));
//                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Failure", error.toString());
                        txtNoRecord.setVisibility(View.VISIBLE);
                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();
                        if (Utils.isInternetConnected(context)) {
                            txtNoRecord.setText(getString(R.string.please_try_again));
//                            Utils.showAlertDialog(context, getString(R.string.please_try_again));
                        } else {
                            txtNoRecord.setText(getString(R.string.no_internet_connection));
//                            Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
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
//	startDate = 2018-10-22			// start date by default date is -3 day from current date
//	endDate = 2018-10-24			// end date by default date is current date
//	us_time_diff = 5.5
//	flag = LAST3DAYSBID
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.START_DATE, edtStartDate.getText().toString());
                params.put(Constants.END_DATE, edtEndDate.getText().toString());
                params.put(Constants.US_TIME_DIFF, Constants.MY_BID_FLAG);
                params.put(Constants.FLAG, Constants.LAST_THREE_BID_FLAG);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mLoadingView != null) {

        }
    }
}
