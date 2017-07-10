package com.shiliukeji.xc_lsy.androidkillerservice.location;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class LocationInfo {

    private String addr = "位置信息不详";
    private double latitude;
    private double longitude;
    private String poiStr = "位置周边信息不详";

    public String getPoiStr() {
        return this.poiStr;
    }

    public void setPoiStr(String poiStr) {
        this.poiStr = poiStr;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
