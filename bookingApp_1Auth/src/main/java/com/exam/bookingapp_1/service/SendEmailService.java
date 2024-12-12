package com.exam.bookingapp_1.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailService {

//    @Value("${spring.mail.username}")
//    private String sender;


//    @Autowired
//    private  Properties mailProperties;
////
////    @Autowired
////    private String sender;
//
//    //private final Session session = Session.getDefaultInstance(properties);
//
//
//    public void emailParams(HashMap<String, String> params) {
//
//
//        Session session = Session.getDefaultInstance(mailProperties,null);
//
//        sendEmail(session,
//                params.get("To"),
//                params.get("Subject"),
//                params.get("Body"));
//    }
//
//
//    public void sendEmail(Session session, String toEmail, String subject, String body){
//
//        try
//        {
//
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String user = auth.getName();
//
//            MimeMessage msg = new MimeMessage(session);
//            //set message headers
//            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//            msg.addHeader("format", "flowed");
//            msg.addHeader("Content-Transfer-Encoding", "8bit");
//
//            //msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
//            msg.setFrom(new InternetAddress("no-reply@central.ntua.gr", "NoReply-JD"));
//
//            //msg.setReplyTo(InternetAddress.parse(user + "@mail.ntua.gr", false));
//
//            msg.setSubject(subject, "UTF-8");
//
//            msg.setText(body, "UTF-8");
//
//            msg.setSentDate(new Date());
//
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
//            System.out.println("Message is ready");
//            Transport.send(msg);
//
//            System.out.println("EMail Sent Successfully!!");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
