package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 24,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MyBidsVO implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("bid_cat")
    @Expose
    public String bidCat;
    @SerializedName("bid_name")
    @Expose
    public String bidName;
    @SerializedName("lucy_num")
    @Expose
    public String lucyNum;
    @SerializedName("cat_name")
    @Expose
    public String cat_name;


    @SerializedName("points_bid")
    @Expose
    public String pointsBid;
    @SerializedName("comments")
    @Expose
    public String comments;
    @SerializedName("event_name")
    @Expose
    public String eventName;
    @SerializedName("event_id")
    @Expose
    public String eventId;
    @SerializedName("series_id")
    @Expose
    public String seriesId;
    @SerializedName("event_dt_tm")
    @Expose
    public String eventDtTm;
    @SerializedName("df_time")
    @Expose
    public Object dfTime;
    @SerializedName("userid")
    @Expose
    public String userid;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("bid_id")
    @Expose
    public String bidId;
    @SerializedName("event_time")
    @Expose
    public String eventTime;
    @SerializedName("perce")
    @Expose
    public String perce;
    @SerializedName("win_points")
    @Expose
    public String winPoints;
    @SerializedName("win_number")
    @Expose
    public String winNumber;
    @SerializedName("wincategory")
    @Expose
    public String wincategory;
    @SerializedName("draw_number")
    @Expose
    public String drawNumber;
 @SerializedName("event_status")
    @Expose
    public String event_status;

}
