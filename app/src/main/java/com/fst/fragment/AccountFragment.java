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
import com.fst.adapters.AccountAdapter;
import com.fst.modal.AccountVO;
import com.fst.modal.LoginVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 26,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AccountFragment extends Fragment {

    private Context context;
    private LoadingView mLoadingView;

    private ArrayList<AccountVO> myBidsList;


    private RecyclerView rclAccountReport;

    private TextView txtNoRecord;

    private EditText edtStartDate;
    private EditText edtEndDate;

    private Calendar mcalendar;
    private int day, month, year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.account_fragment, container, false);

        context = getActivity();
        myBidsList = new ArrayList<>();

        rclAccountReport = (RecyclerView) mView.findViewById(R.id.rclAccountReport);
        TextView txtShowReport = (TextView) mView.findViewById(R.id.txtShowReport);
         txtNoRecord = (TextView) mView.findViewById(R.id.txtNoRecord);

        LinearLayoutManager rctBidLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) rctBidLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rclAccountReport.setLayoutManager(rctBidLayoutManager);

        edtStartDate = (EditText) mView.findViewById(R.id.edtStartDate);
        edtEndDate = (EditText) mView.findViewById(R.id.edtEndDate);

        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

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
                dpDialog.show();
            }
        });

        txtShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyBids();
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

        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.pauseAnimation();
        myBidsList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        txtNoRecord.setVisibility(View.GONE);
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
                                    AccountVO myBidsVO = new AccountVO();

                                    myBidsVO.comments = jsonObject.optString("comments");
                                    myBidsVO.displayName = jsonObject.optString("display_name");
                                    myBidsVO.drcr = jsonObject.optString("drcr");
                                    myBidsVO.dtTm = jsonObject.optString("dt_tm");
                                    myBidsVO.id = jsonObject.optString("id");
                                    myBidsVO.isValid = jsonObject.optString("is_valid");
                                    myBidsVO.r = jsonObject.optString("R");
                                    myBidsVO.refId = jsonObject.optString("ref_id");
                                    myBidsVO.refType = jsonObject.optString("ref_type");
                                    myBidsVO.reserveFunds = jsonObject.optString("reserve_funds");
                                    myBidsVO.status = jsonObject.optString("status");
                                    myBidsVO.transbyDesc = jsonObject.optString("transby_desc");
                                    myBidsVO.transtoDesc = jsonObject.optString("transto_desc");
                                    myBidsVO.transBy = jsonObject.optString("trans_by");
                                    myBidsVO.transPoints = jsonObject.optString("trans_points");
                                    myBidsVO.transTo = jsonObject.optString("trans_to");
                                    myBidsVO.transType = jsonObject.optString("trans_type");
                                    myBidsVO.tCat = jsonObject.optString("t_cat");
                                    myBidsVO.tType = jsonObject.optString("t_type");
                                    myBidsVO.tTypeId = jsonObject.optString("t_type_id");
                                    myBidsVO.w = jsonObject.optString("W");
                                    myBidsVO.walletFunds = jsonObject.optString("wallet_funds");
                                    myBidsVO.zdate = jsonObject.optString("zdate");
                                    myBidsVO.zid = jsonObject.optString("zid");

                                    myBidsList.add(myBidsVO);
                                }

                                RecyclerView.Adapter chapterVideoAdapter = new AccountAdapter(getActivity(), myBidsList);
                                rclAccountReport.setAdapter(chapterVideoAdapter);

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

//              user_id = sunil1
//	startDate = 2018-04-24			// start date by default date is -180 days of current date
//	endDate = 2018-10-24			// end date by default date is current date
//	user_type = 6
//	us_time_diff = 5.5
//	report = report
//	flag = OLDTRANS
//	api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.USER_TYPE, loginDetails.userType);
                params.put(Constants.US_TIME_DIFF, loginDetails.usTimeDiff);
                params.put(Constants.START_DATE, edtStartDate.getText().toString());
                params.put(Constants.END_DATE, edtEndDate.getText().toString());
                params.put(Constants.REPORT, Constants.REPORT);
                params.put(Constants.FLAG, Constants.ACCOUNT_REPORT_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
