package com.fst.modal;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class PointsVO {

    String point;
    int status;
    boolean isPointSelected;

    public PointsVO(String point, int status,boolean isPointSelected) {
        this.point = point;
        this.status = status;
        this.isPointSelected=isPointSelected;
    }

    public boolean isPointSelected() {
        return isPointSelected;
    }

    public void setPointSelected(boolean pointSelected) {
        isPointSelected = pointSelected;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
