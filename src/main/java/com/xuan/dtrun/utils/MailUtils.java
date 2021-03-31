package com.xuan.dtrun.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailUtils {

    public static void sendMail(String message, String contact) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.exmail.qq.com");
        javaMailSender.setUsername("mail@dtrun.cn");
        javaMailSender.setPassword("Xuan.199951");
        javaMailSender.setProtocol("smtps");
        javaMailSender.setPort(465);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("mail@dtrun.cn");
        simpleMailMessage.setTo(contact);
        simpleMailMessage.setSubject("来自DTRUN迁移系统的信件");
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
