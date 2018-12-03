package com.fst.modal;

import java.util.ArrayList;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class EventsVO {

    String id;
    String event_name;
    String bid_init_tm;
    String event_time;
    String myeventtime;
    String series_id;
    String event_id;
    ArrayList<PointsVO> pointsList;
    boolean isEventSelected;

    public EventsVO(String id, String event_name, String bid_init_tm, String event_time, String myeventtime, String series_id, String event_id, ArrayList<PointsVO> pointsList,boolean isEventSelected) {
        this.id = id;
        this.event_name = event_name;
        this.bid_init_tm = bid_init_tm;
        this.event_time = event_time;
        this.myeventtime = myeventtime;
        this.series_id = series_id;
        this.event_id = event_id;
        this.pointsList = pointsList;
        this.isEventSelected=isEventSelected;
    }

    public boolean isEventSelected() {
        return isEventSelected;
    }

    public void setEventSelected(boolean eventSelected) {
        isEventSelected = eventSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getBid_init_tm() {
        return bid_init_tm;
    }

    public void setBid_init_tm(String bid_init_tm) {
        this.bid_init_tm = bid_init_tm;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getMyeventtime() {
        return myeventtime;
    }

    public void setMyeventtime(String myeventtime) {
        this.myeventtime = myeventtime;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public ArrayList<PointsVO> getPointsList() {
        return pointsList;
    }

    public void setPointsList(ArrayList<PointsVO> pointsList) {
        this.pointsList = pointsList;
    }
}
