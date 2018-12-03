package com.fst.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.fst.Utility.Utils;
import com.fst.modal.CountryVo;
import com.fst.modal.HintQuestVo;
import com.fst.skytop.R;
import com.jraska.falcon.Falcon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.saeid.fabloading.LoadingView;

/**
 * Created by Sudhir Singh on 11,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private String[] spinnerItemsArray = {"Male", "Female"};
    private ArrayList<HintQuestVo> hintQuestList;
    private ArrayList<CountryVo> countryVoArrayList;

    private Context context;
    private LoadingView mLoadingView;
    private EditText edtFName;
    private TextView txtValidUSer;
    //    private EditText edtLName;
    private EditText edtPassword;
    private EditText edtConfirmPwd;

    //    private EditText edtHintAnswer;
//    private EditText edtDob;
//    private Spinner spnGender;
//    private Spinner spnCountry;
//    private Spinner spnHintQuestion;
    private TextView txtSignUp;
    private TextView txtSignIn;
    private CheckBox chkTerms;
//    private Calendar mcalendar;
//    private int day, month, year;
//
//    //paramaters
//    private String gender;
//    private String countryID;
//    private String questionId;

    private File getScreenshotsDirectory(Context context) throws IllegalAccessException {
        String dirName = "screenshots_" + context.getPackageName();

        File rootDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            rootDir = context.getDir("screens", MODE_PRIVATE);
        }

        File directory = new File(rootDir, dirName);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalAccessException("Unable to create screenshot directory " + directory.getAbsolutePath());
            }
        }

        return directory;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration_screen);
        context = SignUpActivity.this;

        edtFName = (EditText) findViewById(R.id.edtFName);
        txtValidUSer = (TextView) findViewById(R.id.txtValidUSer);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPwd = (EditText) findViewById(R.id.edtConfirmPwd);
//        edtHintAnswer = (EditText) findViewById(R.id.edtHintAnswer);
//        edtDob = (EditText) findViewById(R.id.edtDob);
//        spnGender = (Spinner) findViewById(R.id.spnGender);
//        spnCountry = (Spinner) findViewById(R.id.spnCountry);
//        spnHintQuestion = (Spinner) findViewById(R.id.spnHintQuestion);
        chkTerms = (CheckBox) findViewById(R.id.chkTerms);
        LinearLayout lnrTnc = (LinearLayout) findViewById(R.id.lnrTnc);
        TextView txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        TextView txtSignIn = (TextView) findViewById(R.id.txtSignIn);

//        mcalendar = Calendar.getInstance();
//        day = mcalendar.get(Calendar.DAY_OF_MONTH);
//        year = mcalendar.get(Calendar.YEAR);
//        month = mcalendar.get(Calendar.MONTH);

        countryVoArrayList = new ArrayList<>();
        hintQuestList = new ArrayList<>();


//        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.adapter_spinner, spinnerItemsArray, getResources());
//        spnGender.setAdapter(adapter);

//        fetchCountries();
//        fetchHintQuestion();
        initiateLoadingView();

        txtSignUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        lnrTnc.setOnClickListener(this);

        setListener();

    }

    private void setListener() {

        edtFName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edtFName.getText().toString().trim().length() >= 4) {
//                    txtValidUSer.setVisibility(View.VISIBLE);
                    fetchCountries();
//                    txtValidUSer.setTextColor(getResources().getColor(R.color.loading_view_top_color));
                } else {
                    edtFName.setError("4 character minimum!");
//                    txtValidUSer.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edtFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

//                if (hasFocus) {
//                    if (edtFName.getText().toString().trim()
//                        .length() < 4) {
//                        edtFName.setError("4 character minimum!");
//                    } else {
//                        edtFName.setError(null);
//                    }}

//                if (!hasFocus && edtFName.getText().toString().trim().length()>0) {
//                    fetchCountries();
//                }
            }
        });

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

            case R.id.txtSignIn:

                finish();

                break;


            case R.id.lnrTnc:

                showTNCPopUp();

                break;

            case R.id.txtSignUp:

                if (Utils.isInternetConnected(context)) {
                    registerUser();
                } else {
                    Utils.showAlertDialog(context, getString(R.string.no_internet_connection));
                }

                break;
        }
    }

    private void registerUser() {

        if (edtFName.getText().toString().trim().length() <= 0) {

            Utils.showAlertDialog(context, "Please enter userId.");
        }
//        else if (edtLName.getText().toString().trim().length() <= 0) {
//
//            Utils.showAlertDialog(context, "Please enter last name.");
//        }

        else if (edtPassword.getText().toString().trim().length() <= 0) {

            Utils.showAlertDialog(context, "Please enter password.");

        } else if (!edtConfirmPwd.getText().toString().equals(edtPassword.getText().toString())) {

            Utils.showAlertDialog(context, "Mismatch password");
        } else if (!chkTerms.isChecked()) {
            Utils.showAlertDialog(context, "Please accept Terms and Conditions.");
        } else {
            performRegistration();
        }

    }

    private void fetchCountries() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.LIVE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mLoadingView.setVisibility(View.GONE);
                        mLoadingView.pauseAnimation();

                        countryVoArrayList.clear();

                        JSONObject jsonResult;
                        try {
                            jsonResult = new JSONObject(response);
                            int responseCode = jsonResult.optInt("status");

                            if (responseCode == 1) {
                                edtFName.setError(null);

                            } else if (responseCode == 0) {
                                String message = jsonResult.optString("message");
                                edtFName.setError(message);
//                                Utils.showAlertDialog(context, message);

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
                params.put(Constants.USER_ID, edtFName.getText().toString());
                params.put(Constants.FLAG, Constants.DEMO_USER_VALIDATE);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showTNCPopUp() {
        final Dialog logoutPopUp = new Dialog(SignUpActivity.this, R.style.DialogSlideAnim);
        logoutPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopUp.setContentView(R.layout.popup_logout);
        logoutPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutPopUp.getWindow().setGravity(Gravity.CENTER);
        logoutPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button logoutNo = (Button) logoutPopUp.findViewById(R.id.logoutNoBtn);
        Button logoutYes = (Button) logoutPopUp.findViewById(R.id.logoutYesBtn);

        TextView txtTitle = (TextView) logoutPopUp.findViewById(R.id.txtTitle);
        TextView txtDesc = (TextView) logoutPopUp.findViewById(R.id.txtDesc);

        txtTitle.setText(R.string.agree_tnc_text);
        txtDesc.setText(R.string.reg_terms_condition_desc);

        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chkTerms.setChecked(false);
                if (logoutPopUp.isShowing()) {
                    logoutPopUp.dismiss();
                    logoutPopUp.cancel();
                }
            }
        });
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkTerms.setChecked(true);
                if (logoutPopUp.isShowing()) {
                    logoutPopUp.dismiss();
                    logoutPopUp.cancel();
                }
            }
        });
        logoutPopUp.show();
    }


    private void performRegistration() {

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

                                JSONObject dataObj = jsonResult.getJSONObject("data");

                                String message = jsonResult.optString("message");
                                showAlertDialog(context, message + " \n Please take screenshot of the credentials for future use."
                                        + " \n \n User id : " + dataObj.getString("user_id")
                                        + " \n Password : " + edtPassword.getText().toString().trim());

                                edtFName.setText("");
//                                edtLName.setText("");
                                edtPassword.setText("");
                                edtConfirmPwd.setText("");
//                                edtDob.setText("");
//                                edtHintAnswer.setText("");


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

                params.put(Constants.IP_ADDRESS, "45.249.84.255");
                params.put(Constants.USER_AGENT, "android");
                params.put(Constants.USER_NAME, edtFName.getText().toString().trim());
                params.put(Constants.PASSWORD, edtPassword.getText().toString().trim());
                params.put(Constants.TNC, "android");
                params.put(Constants.REF_ID, "AGENTM");
                params.put(Constants.FLAG, Constants.REG_FLAG_VALUE);
                params.put(Constants.API_KEY, Urls.API_KEY_VALUE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showAlertDialog(Context context, final String description) {
        final Dialog emailDialog = new Dialog(context);
        emailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        emailDialog.setContentView(R.layout.dialog_email);
        emailDialog.setCancelable(false);

        TextView btnProceed = (TextView) emailDialog.findViewById(R.id.btnProceed);
        TextView btnCancel = (TextView) emailDialog.findViewById(R.id.btnCancel);
        TextView btnShare = (TextView) emailDialog.findViewById(R.id.btnShare);
        TextView tvMail = (TextView) emailDialog.findViewById(R.id.tvMail);

        tvMail.setText(description);

        btnProceed.setText("Take Screenshot");
        btnCancel.setText("Dismiss");

        btnShare.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AlertDialog dialog2 =AlertDialog.class.cast(dialog);
                takeScreenshot(emailDialog);

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String textToShare = "Visit <a href=\"http://www.google.com\">google</a> for more info.";
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, description);
                startActivity(Intent.createChooser(intent2, "Share via"));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailDialog.dismiss();
                finish();
            }
        });

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emailDialog.dismiss();
//            }
//        });
        emailDialog.show();
    }

    private void openDateDialog() {
//        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                edtDob.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
//            }
//        };
//        DatePickerDialog dpDialog = new DatePickerDialog(context, listener, year, month, day);
//        dpDialog.show();
    }

    public void takeScreenshot(Dialog emailDialog) {
        File screenshotFile = getScreenshotFile();

        Falcon.takeScreenshot(this, screenshotFile);

        String message = "Screenshot captured to " + screenshotFile.getAbsolutePath();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//        emailDialog.dismiss();

        Uri uri = Uri.fromFile(screenshotFile);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(scanFileIntent);

//        finish();
    }

    protected File getScreenshotFile() {
        File screenshotDirectory;
        try {
            screenshotDirectory = getScreenshotsDirectory(getApplicationContext());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS", Locale.getDefault());

        String screenshotName = dateFormat.format(new Date()) + ".png";
        return new File(screenshotDirectory, screenshotName);
    }


}
