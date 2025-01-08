package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.Busers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class SendBookingRemindService {

    @Autowired
    private final SendEmailService sendEmailService;
    private final BusersService busersService;


    //@EnableScheduling
//    @Scheduled(cron="0 30 9 * Jan Mon")
//    @Scheduled(cron="0 0 */8 * * *")
    public void sendReminder(String timePeriod, String content) {

        String subject = "";
        String body = "";

        List<Busers> allUsers = busersService.allUsers();

        List<String> userEmails = allUsers.stream().map(Busers::getEmail).collect(Collectors.toList());
        subject = content;

        if (timePeriod.equals("Εξεταστική")) {

        //    subject = "Υπενθύμιση Εξεταστικής Περιόδου - Κράτηση εργαστηρίων προς εξέταση";

            body = "<p>Κύριοι, παρακαλώ προς υπενθύμιση κράτησης εργαστηρίων για την ερχόμενη Εξεταστική Περίοδο.\n";
            body = body + "Παρακαλούμε κάνετε τις κρατήσεις των εργαστηρίων και με συνεννόηση αμφοτέρων για τις ημερομηνίες των κρατήσεων </p>";
            body = body + "<p>Με εκτίμηση \n Κέντρο Υπολογιστή </p>";
        }
        else {

        //     subject = "Υπενθύμιση Εξαμήνου - Κράτηση εργαστηρίων μέχρι το τέλος του Εξαμήνου";


            body = "<p>Κύριοι, παρακαλώ προς υπενθύμιση κράτησης εργαστηρίων για την ερχόμενο Εξάμηνο.\n";
            body = body + "Παρακαλούμε αν επιθυμείτε να γίνει κράτηση των εργαστηρίων όπως και την προηγούμενη χρονιά τότε επιβεβαίωστε με ένα ";
            body = body + "μήνυμα στην ηλεκτρονική διεύθυνση adm@central.ntua.gr. Σε κάθε άλλη περίπτωση παρακαλούμε να προχωρήσετε στην κράτηση</p>";
            body = body + " των εργαστηρίων χρησιμοποιώντας τους κωδικούς σας.\n</p>";
            body = body + "<p>Με εκτίμηση \n Κέντρο Υπολογιστή </p>";
        }

        //sendEmailService.sendMailToMany(userEmails, subject, body);
        sendEmailService.sendNewMail("dekadimi@mail.ntua.gr", subject, body);
        sendEmailService.sendNewMail("mkyrieri@central.ntua.gr", subject, body);

    }

    @Scheduled(cron="0 0 */2 * * *")
    public void sendingReminders() {

        String contentExams = "Υπενθύμιση Εξεταστικής Περιόδου - Κράτηση εργαστηρίων προς εξέταση";
        String contentSemester = "Υπενθύμιση Εξαμήνου - Κράτηση εργαστηρίων μέχρι το τέλος του Εξαμήνου";

        sendReminder("Εξεταστική",contentExams);
        sendReminder("Εξάμηνο",contentSemester);

    }


}
