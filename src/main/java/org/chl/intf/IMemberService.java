package org.chl.intf;

import org.chl.models.FriendList;
import org.chl.models.Like;
import org.chl.models.Member;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface IMemberService {
    void addMember(Member member);

    Member getMemberInfo(String memberId);

    Member getMemberInfoByEmail(String email);

    Iterable<Member> getMembers();

    void addFriend(FriendList friendList);

    Iterable<FriendList> getFriendList(String memberId);
}
