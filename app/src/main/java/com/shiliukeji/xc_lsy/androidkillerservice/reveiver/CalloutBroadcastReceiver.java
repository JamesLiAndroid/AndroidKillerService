package com.shiliukeji.xc_lsy.androidkillerservice.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.service.GetSharePrefe;
import com.shiliukeji.xc_lsy.androidkillerservice.service.PhoneService;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class CalloutBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        XLog.d("打电话的信息！");
        GetSharePrefe paras = new GetSharePrefe(context);
        if ((paras.isTonghuajilu() || paras.isTonghualuyin()) && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            String outphoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            Intent service = new Intent(context, PhoneService.class);
            service.putExtra("outNumber", outphoneNumber);
            context.startService(service);
        }
    }
}
