package org.chl.repository;

import org.chl.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 12/9/2017.
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
