package com.fst.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fst.skytop.R;
import com.fst.Utility.Constants;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class WebViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.web_view_fragment, container, false);

        Bundle b = getArguments();
        if (b!=null && b.containsKey(Constants.WEB_VIEW_URL)) {
            String url = b.getString(Constants.WEB_VIEW_URL);
            WebView myWebView = (WebView) mView.findViewById(R.id.webview);

//            WebSettings webSettings = myWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);

            myWebView.getSettings().setSaveFormData(true);
            myWebView.getSettings().setAllowContentAccess(true);
            myWebView.getSettings().setAllowFileAccess(true);
            myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            myWebView.getSettings().setLoadsImagesAutomatically(true);
            myWebView.getSettings().setDomStorageEnabled(true);
            myWebView.getSettings().setJavaScriptEnabled(true);

            myWebView.setWebViewClient(new WebViewClient());

            myWebView.loadUrl(url);
        }
        return mView;
    }
}
