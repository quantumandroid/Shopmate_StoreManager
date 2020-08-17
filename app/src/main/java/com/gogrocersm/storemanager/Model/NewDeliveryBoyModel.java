package com.gogrocersm.storemanager.Model;

import java.io.Serializable;

public class NewDeliveryBoyModel implements Serializable {

    private String boy_name;
    private String dboy_id;
    private String lat;
    private String lng;
    private String boy_city;
    private String count;
    private String distance;

    public String getBoy_name() {
        return boy_name;
    }

    public void setBoy_name(String boy_name) {
        this.boy_name = boy_name;
    }

    public String getDboy_id() {
        return dboy_id;
    }

    public void setDboy_id(String dboy_id) {
        this.dboy_id = dboy_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getBoy_city() {
        return boy_city;
    }

    public void setBoy_city(String boy_city) {
        this.boy_city = boy_city;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
