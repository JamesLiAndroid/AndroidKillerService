package com.shiliukeji.xc_lsy.androidkillerservice.location;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.service.PhoneService;
import com.xdandroid.hellodaemon.DaemonEnv;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class Location extends Application {
    private static final String COORD_TYPE_BD09LL = "gcj02";
    public static String TAG = "LocTestDemo";
    public static LocationInfo locInfo = new LocationInfo();
    public static LocationClient mLocationClient = null;
    public static MyLocationListenner myListener = new MyLocationListenner();

    public static class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                Location.mLocationClient.requestPoi();
                return;
            }
            Location.locInfo.setLongitude(poiLocation.getLongitude());
            Location.locInfo.setLatitude(poiLocation.getLatitude());
            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                Location.locInfo.setAddr(poiLocation.getAddrStr());
                Log.i(Location.TAG, "onaddr");
            }
            if (poiLocation.hasPoi()) {
                Location.locInfo.setPoiStr(poiLocation.getPoi());
            }
        }
    }

    public class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance) {
        }
    }

    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(this);
        //mLocationClient.setAK("jsqqwX8UQZR10tEaxGBrEP7t");
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        mLocationClient.start();
        XLog.init(LogLevel.ALL);
        XLog.d(TAG, "application oncreate!");
        // 启动守护进程信息！
        DaemonEnv.initialize(this, PhoneService.class, 30000);
        try {
            XLog.d("Application Daemon!");
            startService(new Intent(this, PhoneService.class));
        } catch (Exception ignored) {
            XLog.d("Application Daemon:" +  ignored.toString());
            ignored.printStackTrace();
        }
    }

    public static LocationInfo getLocation() {
        return locInfo;
    }

    public static void updateListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestPoi();
            Log.i(TAG, "update");
        }
    }

    public static void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType(COORD_TYPE_BD09LL);
        option.setServiceName("com.baidu.location.service_v2.9");
        // option.setIsNeedLocationPoiList(true);
        option.setAddrType("all");
        option.setProdName("tylbd");
        option.setPriority(2);
        option.setPoiNumber(100);
        option.disableCache(true);
        mLocationClient.setLocOption(option);
    }
}

