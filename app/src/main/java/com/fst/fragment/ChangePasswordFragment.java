package com.fst.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fst.modal.LoginVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ChangePasswordFragment extends Fragment {

    private Context context;
    private LoadingView mLoadingView;

    private EditText edtOldpwd;
    private EditText edtNewPassword;
    private EditText edtConfirmPwd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.password_fragment, container, false);

        edtOldpwd = (EditText) mView.findViewById(R.id.edtOldpwd);
        edtNewPassword = (EditText) mView.findViewById(R.id.edtNewPassword);
        edtConfirmPwd = (EditText) mView.findViewById(R.id.edtConfirmPwd);
        TextView txtSignUp = (TextView) mView.findViewById(R.id.txtSignUp);

        context = getActivity();

        initiateLoadingView(mView);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isInternetConnected(context)) {
                    registerUser();
                } else {
                    Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                }

            }
        });


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

    private void registerUser() {

        Gson gson = new Gson();
        Prefs prefs = new Prefs();
        String json = prefs.getPreferences(context, "LOGIN_DATA");
        LoginVO loginDetails = gson.fromJson(json, LoginVO.class);

        if (edtOldpwd.getText().toString().trim().length() <= 0) {

            Utils.showAlertDialog(context, "Please enter old password.");
        } else if (!loginDetails.password.equals(edtOldpwd.getText().toString())) {

            Utils.showAlertDialog(context, "Incorrect old password");
        } else if (edtNewPassword.getText().toString().trim().length() <= 0) {

            Utils.showAlertDialog(context, "Please enter new password.");

        } else if (edtConfirmPwd.getText().toString().trim().length() <= 0) {

            Utils.showAlertDialog(context, "Please confirm new password.");

        } else if (!edtNewPassword.getText().toString().equals(edtConfirmPwd.getText().toString())) {

            Utils.showAlertDialog(context, "Mismatch password");
        } else {
            performRegistration(loginDetails,prefs);
        }

    }

    private void performRegistration(final LoginVO loginDetails,final Prefs prefs) {

        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        JSONObject jsonResult;
                        try {
                            jsonResult = new JSONObject(response);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {

                                loginDetails.password=edtNewPassword.getText().toString();

                                Gson gson = new Gson();
                                String json = gson.toJson(loginDetails);
                                prefs.setPreferences(context, "LOGIN_DATA", json);

                                edtOldpwd.setText("");
                                edtNewPassword.setText("");
                                edtConfirmPwd.setText("");

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

//                user_id = sunil1
//                oldpassword = 123456
//                mynewpassword = 45612
//                flag = CHANGE_PASS
//                api_key = 2NDpkirb9xFcYHFqDkRD3Pt2GmzabLb4T4Q11

                params.put(Constants.USER_ID, loginDetails.userId);
                params.put(Constants.OLD_PASSWORD, edtOldpwd.getText().toString());
                params.put(Constants.MY_NEW_PASSWORD, edtNewPassword.getText().toString().trim());
                params.put(Constants.FLAG, Constants.CHANGE_PASSWORD_FLAG);
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
