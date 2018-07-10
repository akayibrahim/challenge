package org.chl.controller;

import org.chl.model.Error;
import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.ErrorRepository;
import org.chl.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ErrorRepository errorRepository;

    @RequestMapping(value = "/getMemberInfo")
    public Member getMemberInfo(String memberId) {
        Member memberInfo = memberService.getMemberInfo(memberId);
        memberInfo.setFollowerCount(getFollowerList(memberId).size());
        memberInfo.setFollowingCount(getFollowingList(memberId).size());
        return memberInfo;
    }

    @RequestMapping(value = "/getMemberInfoByEmail")
    public Member getMemberInfoByEmail(String email) {
        Member memberInfo = memberService.getMemberInfoByEmail(email);
        return memberInfo;
    }

    @RequestMapping(value = "/addMember")
    public String addMember(@Valid @RequestBody Member member) {
        return memberService.addMember(member);
    }

    @RequestMapping(value = "/getMembers")
    public Iterable<Member> getMemberInfoByEmail() {
        Iterable<Member> memberInfo = memberService.getMembers();
        return memberInfo;
    }

    @RequestMapping(value = "/getFollowingList")
    public List<Member> getFollowingList(String memberId) {
        List<Member> memberList = new ArrayList<>();
        Iterable<FriendList> friendList = memberService.getFollowingList(memberId);
        for (FriendList friend: friendList) {
            memberList.add(memberService.getMemberInfo(friend.getFriendMemberId()));
        }
        return memberList;
    }

    @RequestMapping(value = "/getFollowerList")
    public List<Member> getFollowerList(String memberId) {
        List<Member> memberList = new ArrayList<>();
        Iterable<FriendList> friendList = memberService.getFollowerList(memberId);
        for (FriendList friend: friendList) {
            memberList.add(memberService.getMemberInfo(friend.getMemberId()));
        }
        return memberList;
    }

    @RequestMapping(value = "/followingFriend")
    public void followingFriend(String friendMemberId, String memberId, Boolean follow) {
        memberService.followingFriend(friendMemberId, memberId, follow);
    }

    @RequestMapping(value = "/deleteSuggestion")
    public void deleteSuggestion(String friendMemberId, String memberId) {
        memberService.deleteSuggestion(friendMemberId, memberId);
    }

    @RequestMapping(value = "/isMyFriend")
    public Boolean isMyFriend(String friendMemberId, String memberId) {
        return memberService.isMyFriend(friendMemberId, memberId);
    }

    @RequestMapping(value = "/getSuggestionsForFollowing")
    public List<Member> getSuggestionsForFollowing(String memberId) {
        List<Member> memberList = new ArrayList<>();
        Iterable<FriendList> friendList = memberService.getSuggestionsForFollowing(memberId);
        for (FriendList friend: friendList) {
            memberList.add(memberService.getMemberInfo(friend.getFriendMemberId()));
        }
        return memberList;
    }

    @RequestMapping(value = "/errorLog")
    public void errorLog(@Valid @RequestBody Error error) {
        errorRepository.save(error);
    }
}
