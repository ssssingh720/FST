package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 12,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CountryVo {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("us_time_diff")
    @Expose
    public String usTimeDiff;
    @SerializedName("monaco_time_diff")
    @Expose
    public String monacoTimeDiff;

    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
