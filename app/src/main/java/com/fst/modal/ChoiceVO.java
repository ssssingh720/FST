package com.fst.modal;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ChoiceVO implements Serializable {

    String id;
    String bid_category;
    int block_status;
    String cat_name ;
    String cat_value;
    String descp ;
    String event_name;
    int status;
    boolean isSelected;
    private String bidCatName;

    public ChoiceVO(String id, String bid_category, int block_status, String cat_name,
                    String cat_value, String descp, String event_name, int status, boolean isSelected, String bidCatName) {
        this.id = id;
        this.bid_category = bid_category;
        this.block_status = block_status;
        this.cat_name = cat_name;
        this.cat_value = cat_value;
        this.descp = descp;
        this.event_name = event_name;
        this.status = status;
        this.isSelected = isSelected;
        this.bidCatName=bidCatName;
    }

    public String getBidCatName() {
        return bidCatName;
    }

    public void setBidCatName(String bidCatName) {
        this.bidCatName = bidCatName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid_category() {
        return bid_category;
    }

    public void setBid_category(String bid_category) {
        this.bid_category = bid_category;
    }

    public int getBlock_status() {
        return block_status;
    }

    public void setBlock_status(int block_status) {
        this.block_status = block_status;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_value() {
        return cat_value;
    }

    public void setCat_value(String cat_value) {
        this.cat_value = cat_value;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


