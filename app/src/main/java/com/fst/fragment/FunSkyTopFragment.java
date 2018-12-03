package com.fst.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fst.skytop.R;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class FunSkyTopFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fun_sky_top_fragment, container, false);

        return mView;
    }
}
