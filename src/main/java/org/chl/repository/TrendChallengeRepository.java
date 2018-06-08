package org.chl.repository;

import org.chl.model.TrendChallenge;
import org.chl.util.Constant;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 06/08/2018.
 */
public interface TrendChallengeRepository extends MongoRepository<TrendChallenge, String> {
    Iterable<TrendChallenge> findTrendChallengesByType(Constant.TYPE type, Sort sort);

    TrendChallenge findTrendByChallengeId(String challengeId);
}
