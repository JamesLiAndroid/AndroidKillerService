package com.shiliukeji.xc_lsy.androidkillerservice.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class GetSharePrefe {
    Context context;
    private SharedPreferences ps;

    public GetSharePrefe(Context context) {
        this.context = context;
        this.ps = context.getSharedPreferences("userset", 0);
    }

    public String getShoujiName() {
        return this.ps.getString("shoujiname", "");
    }

    public String getUserEmail() {
        return this.ps.getString("email", "");
    }

    public String getUserPassword() {
        return this.ps.getString("password", "");
    }

    public String getUserPhoneNumber() {
        return this.ps.getString("phonenumber", "");
    }

    public boolean isUserSetOk() {
        return this.ps.getBoolean("isok", false);
    }

    public boolean isTonghuajilu() {
        return this.ps.getBoolean("tonghuajilu", false);
    }

    public boolean isDuanxinjilu() {
        return this.ps.getBoolean("duanxinjilu", false);
    }

    public boolean isTonghualuyin() {
        return this.ps.getBoolean("tonghualuyin", false);
    }

    public boolean isWeizhijilu() {
        return this.ps.getBoolean("weizhijilu", false);
    }

    public boolean isAllnet() {
        return this.ps.getBoolean("allnet", false);
    }

    public boolean isWifi() {
        return this.ps.getBoolean("wifi", false);
    }

    public String getSmtp() {
        return this.ps.getString("smtp", "");
    }

    public String getPort() {
        return this.ps.getString("port", "");
    }

    public String getShoujiImei() {
        return this.ps.getString("shoujiimei", "");
    }

    public String getCardIMSI() {
        return this.ps.getString("cardimsi", "");
    }

    public String getCardNumber() {
        return this.ps.getString("cardnumber", "");
    }

}
