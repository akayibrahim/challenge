package org.chl.repos;

import org.chl.models.Like;
import org.chl.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByEmail(String email);
}
