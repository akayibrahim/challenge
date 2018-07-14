package org.chl.intf;

import org.chl.model.FriendList;
import org.chl.model.Member;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
public interface IMemberService {
    String addMember(Member member);

    Member getMemberInfo(@Valid @NotEmpty String memberId);

    Member getMemberInfoByEmail(@Valid @NotEmpty String email);

    Iterable<Member> getMembers();

    void followingFriend(@Valid @NotEmpty String friendMemberId, String memberId, Boolean follow);

    void deleteSuggestion(@Valid @NotEmpty String friendMemberId, String memberId);

    Iterable<FriendList> getFollowingList(@Valid @NotEmpty String memberId);

    Iterable<FriendList> getFollowerList(String memberId);

    Boolean checkMemberAvailable(String memberId);

    List<FriendList> getSuggestionsForFollowing(String memberId);

    Boolean isMyFriend(String memberId, String friendMemberId);

    List<Member> searchFriends(String searchKey);
}

