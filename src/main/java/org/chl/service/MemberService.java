package org.chl.service;

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
    public void addFriend(FriendList friendList) {
        friendRepo.save(friendList);
    }

    @Override
    public List<String> getFriendList(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByMemberId(memberId);
        List<String> listOfFriend = new ArrayList<>();
        for (FriendList friend:friendLists) {
            listOfFriend.add(friend.getFriendMemberId());
        }
        return listOfFriend;
    }

    @Override
    public Iterable<FriendList> getDetailFriendList(String memberId) {
        return friendRepo.findByMemberId(memberId);
    }

    @Override
    public Boolean checkMemberAvailable(String memberId) {
        Member member = memberRepo.findOne(memberId);
        return member != null ? true : false;
    }
}
