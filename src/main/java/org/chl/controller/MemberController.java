package org.chl.controller;

import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "/getMemberInfo")
    public Member getMemberInfo(String memberId) {
        Member memberInfo = memberService.getMemberInfo(memberId);
        return memberInfo;
    }

    @RequestMapping(value = "/getMemberInfoByEmail")
    public Member getMemberInfoByEmail(String email) {
        Member memberInfo = memberService.getMemberInfoByEmail(email);
        return memberInfo;
    }

    @RequestMapping(value = "/addMember")
    public void addMember(@Valid @RequestBody Member member) {
        memberService.addMember(member);
    }

    @RequestMapping(value = "/getMembers")
    public Iterable<Member> getMemberInfoByEmail() {
        Iterable<Member> memberInfo = memberService.getMembers();
        return memberInfo;
    }

    @RequestMapping(value = "/getDetailFriendList")
    public Iterable<FriendList> getDetailFriendList(String memberId) {
        Iterable<FriendList> friendList = memberService.getDetailFriendList(memberId);
        for (FriendList friend: friendList
             ) {
            friend.setFriendMemberInfo(memberService.getMemberInfo(friend.getFriendMemberId()));
        }
        return friendList;
    }

    @RequestMapping(value = "/addFriend")
    public void addFriend(@RequestBody FriendList friendList) {
        memberService.addFriend(friendList);
    }
}
