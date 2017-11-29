package org.chl.repos;

import org.chl.models.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface LikeRepository extends MongoRepository<Like, BigInteger> {

    //    @Query("SELECT s FROM SeqTb s where s.analysisId = :analysisId")
    Iterable<Like> findByChallengeId(String  challengeId);
}
