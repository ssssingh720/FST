package com.fst.modal;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CategoryVO {

    String id ;
    String bid_cat;
    String bid_name;
    int status;
    boolean isCatSelected;

    public CategoryVO(String id, String bid_cat, String bid_name, int status,boolean isCatSelected) {
        this.id = id;
        this.bid_cat = bid_cat;
        this.bid_name = bid_name;
        this.status = status;
        this.isCatSelected=isCatSelected;
    }

    public boolean isCatSelected() {
        return isCatSelected;
    }

    public void setCatSelected(boolean catSelected) {
        isCatSelected = catSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid_cat() {
        return bid_cat;
    }

    public void setBid_cat(String bid_cat) {
        this.bid_cat = bid_cat;
    }

    public String getBid_name() {
        return bid_name;
    }

    public void setBid_name(String bid_name) {
        this.bid_name = bid_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
