package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 11,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LoginVO {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("level")
    @Expose
    public String level;

    @SerializedName("user_fname")
    @Expose
    public String userFname;
    @SerializedName("user_lname")
    @Expose
    public String userLname;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("us_time_diff")
    @Expose
    public String usTimeDiff;
    @SerializedName("g_name")
    @Expose
    public String gName;
    @SerializedName("mobile_no")
    @Expose
    public Object mobileNo;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("session_id")
    @Expose
    public String sessionId;
    @SerializedName("user_country")
    @Expose
    public String userCountry;


}
