package org.chl.repository;

import org.chl.model.FriendList;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface FriendListRepository extends MongoRepository<FriendList, String> {
    Iterable<FriendList> findByMemberId(String  memberId);

    Iterable<FriendList> findByMemberIdAndFollowed(String  memberId, Boolean followed);
}
