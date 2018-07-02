package org.chl.service;

import org.chl.intf.INotificationService;
import org.chl.model.Notification;
import org.chl.model.PushNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by ibrahim on 12/9/2017.
 */
@Service
public class NotificationService implements INotificationService {
    private static final long oneDay = 24 * 60 * 60 * 1000;
    private static final long oneHour = 60 * 60 * 1000;
    private static final long oneMinute = 60 * 1000;

    // @Autowired
    // private JmsTemplate jmsTemplate;

    @Value("${jms.queue.destination:CHL-QUEUE}")
    private String destinationQueue;

    public void send(@RequestBody Notification notification) {
        if(notification instanceof PushNotification) {
            PushNotification pushNotification = (PushNotification) notification;
            LocalDate today = LocalDate.now();
            LocalDate untilDate = pushNotification.getUntilDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long diff = Duration.between(untilDate.atStartOfDay(), today.atStartOfDay()).toDays();
            // jmsTemplate.setDeliveryDelay(diff * oneDay);
            notification.setMessageTitle(pushNotification.getNotification().getMessageTitle());
            notification.setMessage(pushNotification.getNotification().getMessage());
        }
        // jmsTemplate.convertAndSend(destinationQueue, notification);
    }
}
