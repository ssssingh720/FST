package com.fst.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import com.google.gson.Gson;
import com.fst.Network.Urls;
import com.fst.skytop.R;
import com.fst.Utility.Constants;
import com.fst.Utility.Prefs;
import com.fst.Utility.Utils;
import com.fst.modal.LoginVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 10,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingView mLoadingView;
    private Context context;
    private Prefs prefs;
    private MediaPlayer mediaPlayer;

    private EditText edtUserId;
    private EditText edtPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);

        context = LoginActivity.this;
        prefs = new Prefs();

        TextView txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        TextView txtLoginSubmit = (TextView) findViewById(R.id.txtLoginSubmit);
        TextView txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        initiateLoadingView();

        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        txtSignUp.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtLoginSubmit.setOnClickListener(this);

    }

    private void initiateLoadingView() {

        mediaPlayer = MediaPlayer.create(context, R.raw.btn_click);

        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), R.drawable.fst_app_logo, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.drawable.fst_app_logo, LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.drawable.fst_app_logo, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.fst_app_logo, LoadingView.FROM_BOTTOM);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtLoginSubmit:

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }

                if (Utils.isInternetConnected(context)) {

                    if (edtUserId.getText().toString().length() <= 0) {

                        Utils.showAlertDialog(context, "Please enter user id.");

                    } else if (edtUserId.getText().toString().length() <= 0) {
                        Utils.showAlertDialog(context, "Please enter password.");
                    } else {

                        performLogin();
                    }

                } else {
                    Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                }


                break;

            case R.id.txtSignUp:

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }

                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);

                break;

            case R.id.txtForgotPassword:

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }

                Intent forgotPwdIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPwdIntent);

                break;

        }
    }

    void performLogin() {

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

//                            Toast.makeText(context, "Login "+status, Toast.LENGTH_SHORT).show();
                            if (responseCode == 1) {

                                JSONObject jsonObject = jsonResult.optJSONObject("data");
                                LoginVO registerPojo = new LoginVO();
                                registerPojo.id = jsonObject.optString("id");
                                registerPojo.userId = jsonObject.optString("user_id");
                                registerPojo.email = jsonObject.optString("email");
                                registerPojo.level = jsonObject.optString("level");
                                registerPojo.userFname = jsonObject.optString("user_fname");
                                registerPojo.userLname = jsonObject.optString("user_lname");
                                registerPojo.password = jsonObject.optString("password");
                                registerPojo.userType = jsonObject.optString("user_type");
                                registerPojo.usTimeDiff = jsonObject.optString("us_time_diff");
                                registerPojo.gName = jsonObject.optString("g_name");
                                registerPojo.mobileNo = jsonObject.optString("mobile_no");
                                registerPojo.status = jsonObject.optString("status");
                                registerPojo.sessionId = jsonObject.optString("session_id");
                                registerPojo.userCountry = jsonObject.optString("user_country");
                                saveDataToPref(registerPojo);

                                Intent login = new Intent(context, LandingActivity.class);
                                startActivity(login);
                                finish();
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
//                    password = 123456
//            flag = USER_LOGIN
//                    api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.USER_ID, edtUserId.getText().toString());
                params.put(Constants.PASSWORD, edtPassword.getText().toString());
                params.put(Constants.FLAG, Constants.LOGIN_FLAG);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    void saveDataToPref(LoginVO data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefs.setPreferences(context, "LOGIN_DATA", json);
    }
}
