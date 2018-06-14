package org.chl.intf;

import org.chl.model.FriendList;
import org.chl.model.Member;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
public interface IMemberService {
    void addMember(Member member);

    Member getMemberInfo(@Valid @NotEmpty String memberId);

    Member getMemberInfoByEmail(@Valid @NotEmpty String email);

    Iterable<Member> getMembers();

    void followingFriend(FriendList friendList);

    Iterable<FriendList> getFollowingList(@Valid @NotEmpty String memberId);

    Boolean checkMemberAvailable(String memberId);

    List<FriendList> getSuggestionsForFollowing(String memberId);
}
