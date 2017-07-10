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

public class OtherServiceReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        XLog.d("亮屏的检测信息！");
        GetSharePrefe paras = new GetSharePrefe(context);
        if (paras.isTonghuajilu() || paras.isTonghualuyin()) {
            context.startService(new Intent(context, PhoneService.class));
        }
    }
}
