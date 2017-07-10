package com.shiliukeji.xc_lsy.androidkillerservice.mail;

import com.elvishew.xlog.XLog;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by XC_LSY on 2017/7/6.
 */

public class SimpleMailSender {

    public boolean sendAttachMail(MailSenderInfo mailInfo) {
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        try {
            Message mailMessage = new MimeMessage(Session.getInstance(pro, authenticator));
            mailMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            Multipart multi = new MimeMultipart();
            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            multi.addBodyPart(textBodyPart);
            for (String path : mailInfo.getAttachFileNames()) {
                DataSource fds = new FileDataSource(path);
                BodyPart fileBodyPart = new MimeBodyPart();
                fileBodyPart.setDataHandler(new DataHandler(fds));
                fileBodyPart.setFileName(path.substring(path.lastIndexOf("/") + 1));
                multi.addBodyPart(fileBodyPart);
            }
            mailMessage.setContent(multi);
            mailMessage.saveChanges();
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean sendTextMail(MailSenderInfo mailInfo) {
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        try {
            Message mailMessage = new MimeMessage(Session.getInstance(pro, authenticator));
            mailMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            mailMessage.setText(mailInfo.getContent());
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean sendHtmlMail(MailSenderInfo mailInfo) {
        XLog.d("发送网页版邮件！");
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        try {
            Message mailMessage = new MimeMessage(Session.getInstance(pro, authenticator));
            mailMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            String[] receivers = mailInfo.getReceivers();
            Address[] tos = new InternetAddress[receivers.length];
            for (int i = 0; i < receivers.length; i++) {
                tos[i] = new InternetAddress(receivers[i]);
            }
            mailMessage.setRecipients(Message.RecipientType.TO, tos);
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
