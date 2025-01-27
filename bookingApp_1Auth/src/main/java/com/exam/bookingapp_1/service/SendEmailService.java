package com.exam.bookingapp_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNewMail(String to, String subject, String body) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendMailToMany(List<String> recipients, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(body);
        recipients.forEach(recipient -> {
            message.setTo(recipient);
            mailSender.send(message);
        });
    }

}
