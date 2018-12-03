package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class WinReportVO {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("event_time")
    @Expose
    public String eventTime;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("win_first_num")
    @Expose
    public String winFirstNum;
    @SerializedName("win_second_num")
    @Expose
    public String winSecondNum;
}
