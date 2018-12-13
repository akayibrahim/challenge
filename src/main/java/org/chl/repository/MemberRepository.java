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

    @Query(" { '$and' : [ { 'name' : {$regex: '(?i)?0' } }, { 'surname' : {$regex: '(?i)?1' } } ]  }")
    List<Member> findByNameAndSurname(String name, String surname);

    Member findByfacebookID(String facebookID);

    @Query(" { 'botFlag': ?0 } " )
    List<Member> findByBotFlag(Boolean botFlag);
}
