package com.example.cloud.service;

import com.example.cloud.dao.MailSendDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class MailSendService {
    private final Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    MailSendDao mailSendDao = new MailSendDao();
    private JavaMailSenderImpl sender = mailSendDao.setSender(
            "smtp.sina.com",
            "xudafengabc@sina.com",
            "fee6ed81a5750132");


    public void sendSimpleMail(String to, String subject, String content){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("xudafengabc@sina.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setSentDate(new Date());// 邮件发送时间
        mail.setText(content);
        sender.send(mail);
    }

    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(mail);
        mailHelper.setFrom("xudafengabc@sina.com");
        mailHelper.setTo(to);
        mailHelper.setSubject(subject);
        mailHelper.setSentDate(new Date());// 邮件发送时间
        mailHelper.setText(content, true);
        sender.send(mail);
    }

}
