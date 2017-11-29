package org.chl.services;

import org.chl.intf.IMemberService;
import org.chl.models.FriendList;
import org.chl.models.Member;
import org.chl.repos.FriendListRepository;
import org.chl.repos.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Iterable<FriendList> getFriendList(String memberId) {
        return friendRepo.findByMemberId(memberId);
    }
}
