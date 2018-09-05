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

import java.util.*;
import java.util.stream.Collectors;

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
    public Member getMemberInfoByFaceBookId(String facebookID) {
        return memberRepo.findByfacebookID(facebookID);
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
        Member member = memberRepo.findById(friendMemberId).get();
        FriendList exist = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        Boolean existFollow = !Objects.isNull(exist) ? exist.getFollowed() : false;
        String activityTableId;
        if (exist != null) {
            if (exist.getRequested()) {
                exist.setRequested(false);
                exist.setFollowed(follow);
                activityService.createActivity(Mappers.prepareActivity(null, null, friendMemberId, memberId, Constant.ACTIVITY.ACCEPT_FRIEND_REQUEST));
            } else {
                if (member.getPrivateMember() && follow) {
                    exist.setRequested(true);
                    exist.setFollowed(false);
                    follow = false;
                    activityService.createActivity(Mappers.prepareActivity(null, null, memberId, friendMemberId, Constant.ACTIVITY.FRIEND_REQUEST));
                } else {
                    exist.setFollowed(follow);
                }
            }
            friendRepo.save(exist);
            activityTableId = exist.getId();
        } else {
            FriendList friendList = new FriendList();
            friendList.setDeleted(false);
            if (member.getPrivateMember() && follow) {
                friendList.setRequested(true);
                friendList.setFollowed(false);
                follow = false;
                activityService.createActivity(Mappers.prepareActivity(null, null, memberId, friendMemberId, Constant.ACTIVITY.FRIEND_REQUEST));
            } else {
                friendList.setFollowed(follow);
            }
            friendList.setFriendMemberId(friendMemberId);
            friendList.setMemberId(memberId);
            friendRepo.save(friendList);
            activityTableId = friendList.getId();
            if (!follow)
                activityService.increaseActivityCount(memberId);
        }
        if (follow && !existFollow) {
            // activityService.createActivity(Mappers.prepareActivity(activityTableId, null, friendMemberId, memberId, Constant.ACTIVITY.FOLLOWING));
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
    public List<FriendList> getFollowingList(String memberId) {
        List<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, true);
        return friendLists;
    }

    @Override
    public List<FriendList> getFollowerList(String memberId, Boolean followed) {
        List<FriendList> friendLists = friendRepo.findByFriendMemberIdAndFollowed(memberId, followed);
        return friendLists;
    }

    public List<String> getFollowingIdList(String memberId) {
        List<FriendList> friendLists = friendRepo.findByMemberIdAndFollowed(memberId, true);
        List<String> listOfFriend = new ArrayList<>();
        friendLists.stream().filter(fri -> !fri.getRequested()).forEach(friend -> {
            listOfFriend.add(friend.getFriendMemberId());
        });
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
    public List<Member> searchFriends(String searchKey, String memberId) {
        Iterable<Member> memberList = memberRepo.findByKey(searchKey.toUpperCase());
        List<Member> members = Lists.newArrayList(memberList);
        return members.stream().filter(member -> !isMyFriend(memberId, member.getId()) && !isRequestedFriend(memberId, member.getId())).collect(Collectors.toList());
    }

    @Override
    public Boolean isRequestedFriend(String memberId, String friendMemberId) {
        FriendList friendList = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        return friendList != null && friendList.getRequested() ? true : false;
    }

    @Override
    public void changeAccountPrivacy(String memberId, Boolean toPrivate) {
        Member member = memberRepo.findById(memberId).get();
        member.setPrivateMember(toPrivate);
        memberRepo.save(member);
    }
}
