package org.chl.repository;

import org.chl.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByEmail(String email);
}
