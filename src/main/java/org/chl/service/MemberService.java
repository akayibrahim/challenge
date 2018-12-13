package org.chl.service;

import com.google.common.collect.Lists;
import org.chl.intf.IMemberService;
import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.FriendListRepository;
import org.chl.repository.MemberRepository;
import org.chl.util.Constant;
import org.chl.util.DateUtil;
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
        if (exist != null) {
            exist.setBuildVersion(member.getBuildVersion());
            exist.setReleaseVersion(member.getReleaseVersion());
            exist.setOsVersion(member.getOsVersion());
            exist.setPhoneModel(member.getPhoneModel());
            exist.setGender(member.getGender());
            exist.setAge_range(member.getAge_range());
            exist.setDeviceNotifyToken(member.getDeviceNotifyToken());
            memberRepo.save(exist);
            return exist.getId();
        }
        member.setPrivateMember(false);
        member.setInsertDate(DateUtil.getCurrentDatePlusThreeHour());
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
    public List<Member> getMembers() {
        return memberRepo.findAll();
    }

    @Override
    public void followingFriend(String friendMemberId, String memberId, Boolean follow) {
        Member member = memberRepo.findById(friendMemberId).get();
        FriendList exist = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        Boolean existFollow = !Objects.isNull(exist) ? exist.getFollowed() : false;
        boolean isRequested = !Objects.isNull(exist) ? exist.getRequested() : false;
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
        if (follow && !existFollow && !isRequested) {
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

    public List<String> getFollowerIdList(String memberId) {
        List<FriendList> friendLists = friendRepo.findByFriendMemberIdAndFollowed(memberId, true);
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
    public List<Member> getSuggestionsForFollowing(String memberId) {
        List<Member> memberList = new ArrayList<>();
        List<FriendList> friendList = getSuggestions(memberId);
        friendList.stream().filter(fri -> !fri.getRequested()).forEach(fri -> {
            memberList.add(getMemberInfo(fri.getFriendMemberId()));
        });
        return memberList;
    }

    private List<FriendList> getSuggestions(String memberId) {
        List<FriendList> friends = friendRepo.findByMemberIdAndFollowed(memberId, false);
        if (friends.size() < 10) {
            List<Member> members = getMembers();
            /*
            List<String> friendList = getFollowingIdList(memberId);
            List<String> followerList = getFollowerIdList(memberId);
            friendList.addAll(followerList);
            Optional.ofNullable(friendList).orElseGet(Collections::emptyList).stream().forEach(fri -> {
                List<FriendList> list = friendRepo.findByMemberIdAndFollowed(fri, true);
                list.stream()
                        .filter(friend -> !friend.getFriendMemberId().equals(memberId) && !isMyFriend(memberId, friend.getFriendMemberId()) &&
                                friends.stream().noneMatch(myFriends -> myFriends.getFriendMemberId().equals(friend.getFriendMemberId())))
                        .forEach(friend -> {
                            friends.add(friend);
                        });
            });
            */
            members.stream()
                    .filter(member -> !member.getId().equals(memberId) &&
                            !isMyFriend(memberId, member.getId()) &&
                                    !isRequestedFriend(memberId, member.getId()))
                    .forEach(member -> {
                        FriendList friendList = new FriendList();
                        friendList.setMemberId(memberId);
                        friendList.setFriendMemberId(member.getId());
                        friendList.setFollowed(isMyFriend(memberId, member.getId()));
                        friendList.setRequested(isRequestedFriend(memberId, member.getId()));
                        friendList.setDeleted(false);
                        friendList.setFriendMemberInfo(member);
                        friends.add(friendList);
                    });
        }
        return friends;
    }

    @Override
    public List<Member> searchFriends(String searchKey, String memberId) {
        Iterable<Member> memberList = null;
        String[] keys = searchKey.split(Constant.SPACE);
        if (keys.length > 1) {
            memberList = memberRepo.findByNameAndSurname(keys[0].toUpperCase(), keys[keys.length-1].toUpperCase());
        } else {
            memberList = memberRepo.findByKey(searchKey.toUpperCase());
        }
        List<Member> members = Lists.newArrayList(memberList);
        return members.stream()
                .filter(member -> !isMyFriend(memberId, member.getId()))
                .filter(member -> !isRequestedFriend(memberId, member.getId()))
                .filter(member -> !member.getId().equals(memberId)).collect(Collectors.toList());
    }

    @Override
    public Boolean isRequestedFriend(String memberId, String friendMemberId) {
        FriendList friendList = friendRepo.findByFriendMemberIdAndMemberId(friendMemberId, memberId);
        return friendList != null && friendList.getRequested() ? true : false;
    }

    @Override
    public void updateWithDeviceToken(String memberId, String deviceToken) {
        Member member = memberRepo.findById(memberId).get();
        member.setDeviceNotifyToken(deviceToken);
        memberRepo.save(member);
    }

    @Override
    public List<Member> getFollowerRequests(String memberId) throws java.lang.Exception {
        List<Member> memberList = new ArrayList<>();
        List<FriendList> friendList = getFollowerList(memberId, false);
        friendList.stream().filter(fri -> fri.getRequested()).forEach(fri -> {
            memberList.add(getMemberInfo(fri.getMemberId()));
        });
        return memberList;
    }

    @Override
    public List<Member> getBots(Boolean botFlag) throws java.lang.Exception {
        return memberRepo.findByBotFlag(botFlag);
    }

    @Override
    public void changeAccountPrivacy(String memberId, Boolean toPrivate) {
        Member member = memberRepo.findById(memberId).get();
        member.setPrivateMember(toPrivate);
        memberRepo.save(member);
    }
}
