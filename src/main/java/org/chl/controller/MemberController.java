package org.chl.controller;

import org.chl.model.ActivityCount;
import org.chl.model.Error;
import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.ActivityCountRepository;
import org.chl.service.ErrorService;
import org.chl.service.MemberService;
import org.chl.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ErrorService errorService;
    @Autowired
    ActivityCountRepository activityCountRepository;

    @Transactional
    @RequestMapping(value = "/getMemberInfo")
    public Member getMemberInfo(String memberId) throws Exception {
        try {
            Member memberInfo = memberService.getMemberInfo(memberId);
            memberInfo.setFollowerCount(getFollowerList(memberId).size());
            memberInfo.setFollowingCount(getFollowingList(memberId).size());
            return memberInfo;
        } catch (Exception e) {
            logError(null, memberId, "getMemberInfo", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getMemberInfoByEmail")
    public Member getMemberInfoByEmail(String email) throws Exception {
        try {
            Member memberInfo = memberService.getMemberInfoByEmail(email);
            return memberInfo;
        } catch (Exception e) {
            logError(null, null, "getMemberInfoByEmail", e, "email=" + email);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/addMember")
    public String addMember(@Valid @RequestBody Member member) throws Exception {
        try {
            return memberService.addMember(member);
        } catch (Exception e) {
            logError(null, null, "addMember", e, "name=" + member.getName() + "&surname=" + member.getSurname() + "&facebookId=" + member.getSurname()
            + "&language=" + member.getLanguage() + "&phoneModel=" + member.getPhoneModel() + "&region=" + member.getRegion());
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getMembers")
    public Iterable<Member> getMemberInfoByEmail() throws Exception {
        try {
            Iterable<Member> memberInfo = memberService.getMembers();
            return memberInfo;
        } catch (Exception e) {
            logError(null, null, "getMembers", e, null);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getFollowingList")
    public List<Member> getFollowingList(String memberId) throws Exception {
        try {
            List<Member> memberList = new ArrayList<>();
            Iterable<FriendList> friendList = memberService.getFollowingList(memberId);
            for (FriendList friend: friendList) {
                memberList.add(memberService.getMemberInfo(friend.getFriendMemberId()));
            }
            return memberList;
        } catch (Exception e) {
            logError(null, memberId, "getFollowingList", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getFollowerList")
    public List<Member> getFollowerList(String memberId) throws Exception {
        try {
            List<Member> memberList = new ArrayList<>();
            Iterable<FriendList> friendList = memberService.getFollowerList(memberId);
            for (FriendList friend: friendList) {
                memberList.add(memberService.getMemberInfo(friend.getMemberId()));
            }
            return memberList;
        } catch (Exception e) {
            logError(null, memberId, "getFollowerList", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/followingFriend")
    public void followingFriend(String friendMemberId, String memberId, Boolean follow) throws Exception {
        try {
            memberService.followingFriend(friendMemberId, memberId, follow);
        } catch (Exception e) {
            logError(null, memberId, "followingFriend", e, "memberId=" + memberId + "&friendMemberId=" + friendMemberId + "&follow=" + follow);
        }
    }

    @Transactional
    @RequestMapping(value = "/deleteSuggestion")
    public void deleteSuggestion(String friendMemberId, String memberId) throws Exception {
        try {
            memberService.deleteSuggestion(friendMemberId, memberId);
        } catch (Exception e) {
            logError(null, memberId, "deleteSuggestion", e, "memberId=" + memberId + "&friendMemberId=" + friendMemberId);
        }
    }

    @Transactional
    @RequestMapping(value = "/isMyFriend")
    public Boolean isMyFriend(String memberId, String friendMemberId) throws Exception {
        try {
            return memberService.isMyFriend(memberId, friendMemberId);
        } catch (Exception e) {
            logError(null, memberId, "isMyFriend", e, "memberId=" + memberId + "&friendMemberId=" + friendMemberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getSuggestionsForFollowing")
    public List<Member> getSuggestionsForFollowing(String memberId) throws Exception {
        try {
            List<Member> memberList = new ArrayList<>();
            Iterable<FriendList> friendList = memberService.getSuggestionsForFollowing(memberId);
            for (FriendList friend: friendList) {
                memberList.add(memberService.getMemberInfo(friend.getFriendMemberId()));
            }
            return memberList;
        } catch (Exception e) {
            logError(null, memberId, "getSuggestionsForFollowing", e, "memberId=" + memberId);
        }
        return null;
    }

    private void logError(String challengeId, String memberId, String serviceURL, Exception e, String inputs) throws Exception {
        errorService.logError(challengeId,memberId,serviceURL,e,inputs);
    }

    @Transactional
    @RequestMapping(value = "/errorLog")
    public void errorLog(@Valid @RequestBody Error error) {
        error.setFe(true);
        error.setInsertTime(new Date());
        errorService.save(error);
    }

    @Transactional
    @RequestMapping(value = "/getActivityCount")
    public String getActivityCount(@Valid String memberId, Boolean delete) throws Exception {
        try {
            ActivityCount activityCount = activityCountRepository.findByMemberId(memberId);
            String count = activityCount != null ? activityCount.getCount() : Constant.ZERO;
            if (delete && activityCount != null) {
                activityCount.setCount(Constant.ZERO);
                activityCountRepository.save(activityCount);
            }
            return count;
        } catch (Exception e) {
            logError(null, memberId, "getActivityCount", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/searchFriends")
    public List<Member> searchFriends(@Valid String searchKey) throws Exception {
        try {
            List<Member> members = memberService.searchFriends(searchKey);
            return members;
        } catch (Exception e) {
            logError(null, null, "searchFriends", e, "searchKey=" + searchKey);
        }
        return null;
    }
}
