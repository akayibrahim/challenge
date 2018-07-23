package org.chl.repository;

import org.chl.model.TrendChallenge;
import org.chl.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by ibrahim on 06/08/2018.
 */
public interface TrendChallengeRepository extends MongoRepository<TrendChallenge, String> {
    Page<TrendChallenge> findTrendChallengesByType(Constant.TYPE type, Pageable pageable);

    TrendChallenge findTrendByChallengeId(String challengeId);

    @Query(" { 'subject' : {$regex: ?0 } , 'type': ?1 }")
    Page<TrendChallenge> findTrendChallengesBySearch(String subject, Constant.TYPE type, Pageable pageable);
}
