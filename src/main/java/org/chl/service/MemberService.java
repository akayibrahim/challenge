package org.chl.service;

import com.google.common.collect.Lists;
import org.chl.intf.IMemberService;
import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.FriendListRepository;
import org.chl.repository.MemberRepository;
import org.chl.util.Constant;
import org.chl.util.Exception;
import org.chl.util.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class MemberService implements IMemberService {
    private MemberRepository memberRepo;
    private FriendListRepository friendRepo;
    private ActivityService activityService;

    @Autowired
    public MemberService(MemberRepository memberRepo, FriendListRepository friendRepo, ActivityService activityService) {
        this.memberRepo = memberRepo;
        this.friendRepo = friendRepo;
        this.activityService = activityService;
    }

    @Override
    public String addMember(Member member) {
        Member exist = memberRepo.findByEmail(member.getEmail());
        if (exist != null)
            return exist.getId();
        member.setPrivateMember(false);
        memberRepo.save(member);
        return member.getId();
    }

    @Override
    public Member getMemberInfo(String memberId) {
        return memberRepo.findById(memberId).get();
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
    public void followingFriend(String friendMemberId, String memberId, Boolean follow) {
        FriendList exist = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        String activityTableId;
        if (exist != null) {
            exist.setFollowed(follow);
            friendRepo.save(exist);
            activityTableId = exist.getId();
        } else {
            FriendList friendList = new FriendList();
            friendList.setDeleted(false);
            friendList.setFollowed(follow);
            friendList.setFriendMemberId(friendMemberId);
            friendList.setMemberId(memberId);
            friendRepo.save(friendList);
            activityTableId = friendList.getId();
        }
        if (follow) {
            activityService.createActivity(Mappers.prepareActivity(activityTableId, null, friendMemberId, memberId, Constant.ACTIVITY.FOLLOWING));
            activityService.createActivity(Mappers.prepareActivity(activityTableId, null, memberId, friendMemberId, Constant.ACTIVITY.FOLLOWER));
        }
    }

    @Override
    public void deleteSuggestion(String friendMemberId, String memberId) {
        FriendList exist = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        if (exist != null && !exist.getFollowed()) {
            exist.setFollowed(false);
            exist.setDeleted(true);
            friendRepo.save(exist);
        } else
            Exception.throwNotFoundRecord();
    }

    @Override
    public Iterable<FriendList> getFollowingList(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, true);
        return friendLists;
    }

    @Override
    public Iterable<FriendList> getFollowerList(String memberId) {
        Iterable<FriendList> friendLists = friendRepo.findByFriendMemberIdAndFollowed(memberId, true);
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
        Member member = memberRepo.findById(memberId).get();
        return member != null ? true : false;
    }

    @Override
    public Boolean isMyFriend(String memberId, String friendMemberId) {
        FriendList friend = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        return friend != null ? friend.getFollowed() : false;
    }

    @Override
    public List<FriendList> getSuggestionsForFollowing(String memberId) {
        List<FriendList> friends = friendRepo.findByMemberIdAndFollowed(memberId, false);
        List<String> friendList = getFollowingIdList(memberId);
        Optional.ofNullable(friendList).orElseGet(Collections::emptyList).stream()
                .filter(fri -> friends.size() < 10)
            .forEach(fri -> {
                List<FriendList> list = friendRepo.findByMemberIdAndFollowed(fri, false);
                if (list.stream().noneMatch(friend -> !friend.getMemberId().equals(memberId)))
                    friends.addAll(list);
            });
        return friends;
    }

    @Override
    public List<Member> searchFriends(String searchKey) {
        Iterable<Member> memberList = memberRepo.findByKey(searchKey.toUpperCase());
        List<Member> members = Lists.newArrayList(memberList);
        return members;
    }
}
