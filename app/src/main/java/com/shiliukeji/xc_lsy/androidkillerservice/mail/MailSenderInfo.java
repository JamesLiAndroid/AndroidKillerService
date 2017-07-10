package com.shiliukeji.xc_lsy.androidkillerservice.mail;

import java.util.List;
import java.util.Properties;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class MailSenderInfo {
    private static final String PROTOCOL = "smtp";
    private List<String> attachFileNames;
    private String content;
    private String fromAddress;
    private String mailServerHost;
    private String mailServerPort;
    private String password;
    private String[] receivers;
    private String subject;
    private String toAddress;
    private String userName;
    private boolean validate;

    public MailSenderInfo(String mailServerHost, String mailServerPort, String userName, String password, boolean validate, String from, String[] tos, String subject, String content) {
        this.mailServerHost = mailServerHost;
        this.mailServerPort = mailServerPort;
        this.userName = userName;
        this.password = password;
        this.validate = validate;
        this.subject = subject;
        this.content = content;
        this.fromAddress = from;
        this.receivers = tos;
    }

    public MailSenderInfo(String mailServerHost, String mailServerPort, String userName, String password, boolean validate) {
        this.mailServerHost = mailServerHost;
        this.mailServerPort = mailServerPort;
        this.userName = userName;
        this.password = password;
        this.validate = validate;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        p.setProperty("mail.transport.protocol", PROTOCOL);
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", this.validate ? "true" : "false");
        return p;
    }

    public String getMailServerHost() {
        return this.mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return this.mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return this.validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public List<String> getAttachFileNames() {
        return this.attachFileNames;
    }

    public void setAttachFileNames(List<String> fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

    public String[] getReceivers() {
        return this.receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }

}
