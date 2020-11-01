package com.example.cloud.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class MailSendDao {
    public JavaMailSenderImpl setSender(String host, String userName,String password){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setUsername(userName);
        sender.setPassword(password);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth","true");
        sender.setJavaMailProperties(p);
        return sender;
    }
}
