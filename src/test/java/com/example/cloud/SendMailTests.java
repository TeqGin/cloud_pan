package com.example.cloud;


import com.example.cloud.service.MailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

@SpringBootTest
public class SendMailTests {


    @Test
    void sendMailTest() throws MessagingException {
        MailSendService mailSendService = new MailSendService();
        mailSendService.sendSimpleMail(
                "1102647596@qq.com",
                "这是TeqGin的云",
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<a href = 'https://www.baidu.com' style=\"color: red\">点击进入百度</a>\n" +
                        "</body>\n" +
                        "</html>");

        mailSendService.sendHtmlMail("1102647596@qq.com",
                "这是TeqGin的云",
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<a href = 'https://www.baidu.com' style=\"color: red\">点击进入百度</a>\n" +
                        "</body>\n" +
                        "</html>");

    }

}
