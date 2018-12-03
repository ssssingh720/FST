package com.fst.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fst.skytop.R;
import com.fst.Utility.Prefs;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


public class SplashScreen extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ImageView imgNextScreen = (ImageView) findViewById(R.id.imgNextScreen);

        imgNextScreen.setOnClickListener(this);



//        new SplashTask().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgNextScreen:

                Prefs prefs = new Prefs();
                if (prefs.getPreferences(SplashScreen.this, "LOGIN_DATA").length() <= 0) {

                    Intent login = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(login);

                } else {

                    Intent login = new Intent(SplashScreen.this, LandingActivity.class);
                    startActivity(login);

                }

                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();

                break;

        }
    }

    private class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Delete")

                    .show();

        }
    }


}
