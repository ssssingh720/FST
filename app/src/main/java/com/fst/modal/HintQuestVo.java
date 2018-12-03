package com.fst.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 13,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class HintQuestVo {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("q_id")
    @Expose
    public String qId;
    @SerializedName("q_name")
    @Expose
    public String qName;
}
