package org.chl.service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.chl.intf.INotificationService;
import org.chl.model.ActivityCount;
import org.chl.model.Notification;
import org.chl.model.PushNotification;
import org.chl.repository.ActivityCountRepository;
import org.chl.repository.NotificationRepository;
import org.chl.util.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;

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
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ActivityCountRepository activityCountRepository;
    @Autowired
    private ErrorService errorService;

    @Value("${jms.queue.destination:CHL-QUEUE}")
    private String destinationQueue;

    public void send(@RequestBody Notification notification) {
        if(notification instanceof PushNotification) {
            PushNotification pushNotification = (PushNotification) notification;
            /*
            LocalDate today = LocalDate.now();
            LocalDate untilDate = pushNotification.getUntilDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long diff = Duration.between(untilDate.atStartOfDay(), today.atStartOfDay()).toDays();
            // jmsTemplate.setDeliveryDelay(diff * oneDay);
            notification.setMessageTitle(pushNotification.getNotification().getMessageTitle());
            notification.setMessage(pushNotification.getNotification().getMessage());
            */
            try {
                callApns(pushNotification.getDeviceToken(), pushNotification.getMessageTitle(), pushNotification.getMessage(),
                        pushNotification.getMemberId());
            } catch (Throwable e) {
                //Exception.throwCannotSendNotify();
            }
            notificationRepository.save(pushNotification);
        }
        // jmsTemplate.convertAndSend(destinationQueue, notification);
    }

    public void callApns(String deviceToken, String title, String body, String memberId) throws Throwable {
        if (deviceToken == null) {
            errorService.logError(null, memberId, "callApns",null, "deviceToken=null, title=" + title
            + ", body=" + body + ", memberid=" + memberId);
            return;
        }
        File file = new File("Certificates.p12");
        ApnsService service = APNS.newService()
                .withCert(file.getAbsolutePath(), "iAkay2712")
                .withAppleDestination(true)
                .build();
        ActivityCount count = activityCountRepository.findByMemberId(memberId);
        String payload = APNS.newPayload()
                .badge(count != null ? Integer.valueOf(count.getCount()) : 1)
                .alertBody(body).build();
                // .alertTitle(title).build();

        service.push(deviceToken, payload);
    }
}
