package org.chl.repository;

import org.chl.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByEmail(String email);

    @Query(" { '$or' : [ { 'name' : {$regex: '(?i)?0' } }, { 'surname' : {$regex: '(?i)?0' } } ]  }")
    List<Member> findByKey(String searchKey);

    Member findByfacebookID(String facebookID);
}
