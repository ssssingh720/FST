package com.fst.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.fst.Utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 11,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LoadingView mLoadingView;

    private EditText edtForgotPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_pwd);

        context = ForgotPasswordActivity.this;

        edtForgotPwd = (EditText) findViewById(R.id.edtForgotPwd);
        TextView txtForgotPwd = (TextView) findViewById(R.id.txtForgotPwd);
        TextView txtSignIn = (TextView) findViewById(R.id.txtSignIn);

        txtForgotPwd.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);

        initiateLoadingView();

    }

    private void initiateLoadingView() {

        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), R.drawable.fst_app_logo, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.drawable.fst_app_logo, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.drawable.fst_app_logo, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.fst_app_logo, LoadingView.FROM_RIGHT);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtForgotPassword:

                if (Utils.isInternetConnected(context)) {
                    registerUser();
                } else {
                    Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                }

                break;

            case R.id.txtSignIn:

                finish();

                break;

        }
    }

    private void registerUser() {

        if (edtForgotPwd.getText().toString().length() < 0) {

            Utils.showAlertDialog(context, "Please enter email id.");
        } else {
            fetchPassword();
        }
    }


    private void fetchPassword() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.FORGOT_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        String result = response;
                        JSONObject jsonResult;
                        try {
                            jsonResult = new JSONObject(result);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {

                                String message = jsonResult.optString("message");
                                Utils.showAlertDialog(context, message);

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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.USER_ID, edtForgotPwd.getText().toString());

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
