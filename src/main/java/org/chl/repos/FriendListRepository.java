package org.chl.repos;

import org.chl.models.FriendList;
import org.chl.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface FriendListRepository extends MongoRepository<FriendList, String> {
    Iterable<FriendList> findByMemberId(String  memberId);
}
