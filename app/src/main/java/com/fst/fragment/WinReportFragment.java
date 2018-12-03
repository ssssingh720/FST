package com.fst.fragment;

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
import com.fst.adapters.WinReportAdapter;
import com.fst.modal.LoginVO;
import com.fst.modal.WinReportVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class WinReportFragment extends Fragment {

    private Context context;
    private LoadingView mLoadingView;

    private ArrayList<WinReportVO> myBidsList;

    private RecyclerView rclWinReport;
    private TextView txtNoRecord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.win_report_fragment, container, false);

        context = getActivity();
        myBidsList = new ArrayList<>();

        rclWinReport = (RecyclerView) mView.findViewById(R.id.rclWinReport);
        txtNoRecord = (TextView) mView.findViewById(R.id.txtNoRecord);

        LinearLayoutManager rctBidLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) rctBidLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rclWinReport.setLayoutManager(rctBidLayoutManager);

        initiateLoadingView(mView);
        getWinReport();

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

    private void getWinReport() {

        txtNoRecord.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.pauseAnimation();

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
                                    WinReportVO myBidsVO = new WinReportVO();

                                    myBidsVO.id = jsonObject.optString("id");
                                    myBidsVO.eventTime = jsonObject.optString("event_time");
                                    myBidsVO.date = jsonObject.optString("date");
                                    myBidsVO.winFirstNum = jsonObject.optString("win_first_num");
                                    myBidsVO.winSecondNum = jsonObject.optString("win_second_num");

                                    myBidsList.add(myBidsVO);

                                }

                                RecyclerView.Adapter chapterVideoAdapter = new WinReportAdapter(getActivity(), myBidsList);
                                rclWinReport.setAdapter(chapterVideoAdapter);


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

//               user_id = sunil1
//	win_records = 3				// 3,7,15,30  day as you select
//	flag = WINRESULT
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.WIN_RECORD, "30");
                params.put(Constants.FLAG, Constants.WIN_REPORT_FLAG);
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

        if (isVisibleToUser) {

        }
    }

}
