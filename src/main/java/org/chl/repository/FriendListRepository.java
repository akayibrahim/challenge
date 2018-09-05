package org.chl.repository;

import org.chl.model.FriendList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface FriendListRepository extends MongoRepository<FriendList, String> {
    Iterable<FriendList> findByMemberId(String  memberId);

    @Query(" { 'deleted' : false, 'memberId' : ?0, 'followed' : ?1 }")
    List<FriendList> findByMemberIdAndFollowed(String  memberId, Boolean followed);

    @Query(" { 'deleted' : false, 'friendMemberId' : ?0, 'followed' : ?1 }")
    List<FriendList> findByFriendMemberIdAndFollowed(String  friendMemberId, Boolean followed);

    @Query(" { 'deleted' : false, 'friendMemberId' : ?0, 'memberId' : ?1 }")
    FriendList findByFriendMemberIdAndMemberId(String friendMemberId, String memberId);
}
