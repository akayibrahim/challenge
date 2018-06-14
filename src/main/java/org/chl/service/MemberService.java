package org.chl.service;

import com.google.common.collect.Lists;
import org.chl.intf.IMemberService;
import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.FriendListRepository;
import org.chl.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class MemberService implements IMemberService {
    private MemberRepository memberRepo;
    private FriendListRepository friendRepo;

    @Autowired
    public MemberService(MemberRepository memberRepo, FriendListRepository friendRepo) {
        this.memberRepo = memberRepo;
        this.friendRepo = friendRepo;
    }

    @Override
    public void addMember(Member member) {
        memberRepo.save(member);
    }

    @Override
    public Member getMemberInfo(String memberId) {
        return memberRepo.findOne(memberId);
    }

    @Override
    public Member getMemberInfoByEmail(String email) {
        return memberRepo.findByEmail(email);
    }

    @Override
    public Iterable<Member> getMembers() {
        return memberRepo.findAll();
    }

    @Override
    public void followingFriend(FriendList friendList) {
        friendRepo.save(friendList);
    }

    @Override
    public Iterable<FriendList> getFollowingList(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, true);
        return friendLists;
    }

    public List<String> getFollowingIdList(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, true);
        List<String> listOfFriend = new ArrayList<>();
        for (FriendList friend:friendLists) {
            listOfFriend.add(friend.getFriendMemberId());
        }
        return listOfFriend;
    }


    @Override
    public Boolean checkMemberAvailable(String memberId) {
        Member member = memberRepo.findOne(memberId);
        return member != null ? true : false;
    }

    @Override
    public List<FriendList> getSuggestionsForFollowing(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, false);
        List<FriendList> friends = Lists.newArrayList(friendLists);
        return friends;
    }
}
