package org.chl.repository;

import org.chl.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface LikeRepository extends MongoRepository<Like, String> {

    //    @Query("SELECT s FROM SeqTb s where s.analysisId = :analysisId")
    List<Like> findByChallengeId(String challengeId);
}
