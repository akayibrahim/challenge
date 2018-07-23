package org.chl.repository;

import org.chl.model.JoinAttendance;
import org.chl.model.Proof;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 06/24/2018.
 */
public interface ProofRepository extends MongoRepository<Proof, String> {
    Page<Proof> findByChallengeId(String challengeId, Pageable pageable);
    
    Proof findByChallengeIdAndMemberId(String challengeId, String memberId);
}
