package org.chl.repos;

import org.chl.models.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface ChallengeRepository extends MongoRepository<Challenge, String> {
}
