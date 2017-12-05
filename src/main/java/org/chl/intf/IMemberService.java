package org.chl.intf;

import org.chl.models.FriendList;
import org.chl.models.Like;
import org.chl.models.Member;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
public interface IMemberService {
    void addMember(Member member);

    Member getMemberInfo(@Valid @NotEmpty String memberId);

    Member getMemberInfoByEmail(@Valid @NotEmpty String email);

    Iterable<Member> getMembers();

    void addFriend(FriendList friendList);

    Iterable<FriendList> getFriendList(@Valid @NotEmpty String memberId);
}
