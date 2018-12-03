package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 26,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AccountVO {

    @SerializedName("zid")
    @Expose
    public String zid;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("dt_tm")
    @Expose
    public String dtTm;
    @SerializedName("trans_type")
    @Expose
    public String transType;
    @SerializedName("trans_points")
    @Expose
    public String transPoints;
    @SerializedName("ref_id")
    @Expose
    public String refId;
    @SerializedName("drcr")
    @Expose
    public String drcr;
    @SerializedName("comments")
    @Expose
    public String comments;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("trans_by")
    @Expose
    public String transBy;
    @SerializedName("trans_to")
    @Expose
    public String transTo;
    @SerializedName("transby_desc")
    @Expose
    public String transbyDesc;
    @SerializedName("transto_desc")
    @Expose
    public String transtoDesc;
    @SerializedName("is_valid")
    @Expose
    public String isValid;
    @SerializedName("ref_type")
    @Expose
    public Object refType;
    @SerializedName("zdate")
    @Expose
    public String zdate;
    @SerializedName("wallet_funds")
    @Expose
    public String walletFunds;
    @SerializedName("reserve_funds")
    @Expose
    public String reserveFunds;
    @SerializedName("t_type_id")
    @Expose
    public String tTypeId;
    @SerializedName("t_type")
    @Expose
    public String tType;
    @SerializedName("t_cat")
    @Expose
    public String tCat;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("W")
    @Expose
    public String w;
    @SerializedName("R")
    @Expose
    public String r;
}
