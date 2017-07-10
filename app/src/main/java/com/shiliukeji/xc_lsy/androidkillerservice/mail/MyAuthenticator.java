package com.shiliukeji.xc_lsy.androidkillerservice.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class MyAuthenticator extends Authenticator {
    String password = null;
    String userName = null;

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.userName, this.password);
    }
}