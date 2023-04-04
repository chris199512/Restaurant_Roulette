package com.smarthomebear.restaurantroulette;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailTask extends AsyncTask<Void, Void, Void> {

    private String mEmail;
    private String mPassword;
    private String mRecipient;
    private String mSubject;
    private String mBody;

    public SendEmailTask(String subject, String body) {
        mEmail = "tastespinner@web.de";
        mPassword = BuildConfig.EMAIL_KEY;
        mRecipient = "tastespinner@gmail.com";
        mSubject = subject;
        mBody = body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            Properties props=new Properties();
            props.put("mail.smtp.host", "smtp.web.de");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mEmail, mPassword);
                }
            });
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(mEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mRecipient));
            message.setSubject(mSubject);
            message.setText(mBody);
            Transport.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        return null;
    }
}
