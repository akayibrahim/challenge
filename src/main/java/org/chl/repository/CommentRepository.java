package org.chl.repository;

import org.chl.model.TextComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface CommentRepository extends MongoRepository<TextComment, String> {

    //    @Query("SELECT s FROM SeqTb s where s.analysisId = :analysisId")
    List<TextComment> findByChallengeId(String challengeId);
}
