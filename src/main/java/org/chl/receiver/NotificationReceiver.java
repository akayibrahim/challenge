package org.chl.receiver;

import org.chl.model.Notification;
import org.chl.model.PushNotification;
import org.chl.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by ibrahim on 12/9/2017.
 */
@Component
public class NotificationReceiver {
    private static final Logger logger = LoggerFactory.getLogger(NotificationReceiver.class);

    @Autowired
    NotificationRepository notificationRepository;

    @JmsListener(destination = "${jms.queue.destination:CHL-QUEUE}")
    public void receiveNotification(Notification notification) {
        if(notification instanceof PushNotification) {
            // TODO call notificationServiceOfIOS
            logger.info("call notificationServiceOfIOS");
        }
        notificationRepository.save(notification);
    }
}
