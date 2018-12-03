package com.fst.Utility;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fst.skytop.R;


/**
 * Created by Sudhir Singh on 08-Jun-16.
 */
public class Utils {

    public static boolean isInternetConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void showAlertDialog(Context context, String description) {
        final Dialog emailDialog = new Dialog(context);
        emailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        emailDialog.setContentView(R.layout.dialog_email);
        emailDialog.setCancelable(false);

        TextView btnProceed = (TextView) emailDialog.findViewById(R.id.btnProceed);
        TextView btnCancel = (TextView) emailDialog.findViewById(R.id.btnCancel);
        TextView tvMail = (TextView) emailDialog.findViewById(R.id.tvMail);

        tvMail.setText(description);

        btnProceed.setText("OK");
        btnCancel.setVisibility(View.GONE);


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


    public static int fetchColor(Context context, int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }


}
